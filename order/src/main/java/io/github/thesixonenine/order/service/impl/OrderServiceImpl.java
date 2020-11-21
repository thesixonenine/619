package io.github.thesixonenine.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.thesixonenine.cart.controller.ICartController;
import io.github.thesixonenine.cart.vo.CartItem;
import io.github.thesixonenine.common.utils.Constant;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.Query;
import io.github.thesixonenine.member.controller.MemberController;
import io.github.thesixonenine.member.controller.MemberReceiveAddressController;
import io.github.thesixonenine.member.entity.MemberReceiveAddressEntity;
import io.github.thesixonenine.order.dao.OrderDao;
import io.github.thesixonenine.order.entity.OrderEntity;
import io.github.thesixonenine.order.entity.OrderItemEntity;
import io.github.thesixonenine.order.interceptor.LoginInterceptor;
import io.github.thesixonenine.order.service.OrderService;
import io.github.thesixonenine.order.vo.*;
import io.github.thesixonenine.product.controller.ISpuInfoController;
import io.github.thesixonenine.product.entity.SpuInfoEntity;
import io.github.thesixonenine.ware.controller.WareSkuController;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {
    @Autowired
    private MemberReceiveAddressController memberReceiveAddressController;
    @Autowired
    private ICartController cartController;
    @Autowired
    private MemberController memberController;
    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;
    @Autowired
    private WareSkuController wareSkuController;
    @Autowired
    private ISpuInfoController spuInfoController;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public OrderConfirmVO confirmOrder() {
        return confirmOrderV2();
    }

    private OrderConfirmVO confirmOrderV2() {
        Long memberId = LoginInterceptor.threadLocal.get().getLeft();
        OrderConfirmVO orderConfirmVO = new OrderConfirmVO();

        // 改成异步后的问题:
        // feign在调用远程接口之前会应用我们自定义的Interceptor(io.github.thesixonenine.order.config.FeignConfig.requestInterceptor)
        // 以此来带上request中的Cookie中的会员信息, 因为主线程中有
        // 但是改成异步后, 异步线程中是没有HttpServletRequest的
        // 所以((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()是get不到HttpServletRequest的
        // 解决办法就是将主线程的copy过去

        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();

        CompletableFuture<Void> getAddr = CompletableFuture.runAsync(() -> {
            List<MemberReceiveAddressEntity> addressEntityList = memberReceiveAddressController.getByMemberId(memberId);

            List<MemberReceiveAddressVO> addressList = new ArrayList<>(addressEntityList.size());
            for (MemberReceiveAddressEntity memberReceiveAddressEntity : addressEntityList) {
                MemberReceiveAddressVO vo = new MemberReceiveAddressVO();
                BeanUtils.copyProperties(memberReceiveAddressEntity, vo);
                addressList.add(vo);
            }
            orderConfirmVO.setAddressList(addressList);
        }, threadPoolExecutor);
        CompletableFuture<Void> getCartItemList = CompletableFuture.runAsync(() -> {

            RequestContextHolder.setRequestAttributes(attributes);

            // 调用远程服务获取当前的购物车
            // 注意这里涉及到了用户登录状态的传递
            // 也就是说不能直接使用feign提供的restTemplate来进行调用, 这样会丢失请求头header
            // 需要添加feign请求拦截器, 加上请求头
            // feign.SynchronousMethodHandler.executeAndDecode
            List<CartItem> cartItem = cartController.getCurrentCartItem();

            List<CartItemVO> cartItemList = new ArrayList<>(cartItem.size());
            for (CartItem item : cartItem) {
                CartItemVO vo = new CartItemVO();
                BeanUtils.copyProperties(item, vo);
                cartItemList.add(vo);
            }
            orderConfirmVO.setCartItemList(cartItemList);
        }, threadPoolExecutor).thenRunAsync(() -> {
            // 继续查询库存信息
            List<CartItemVO> cartItemList = orderConfirmVO.getCartItemList();
            List<Long> skuIdList = cartItemList.stream().map(CartItemVO::getSkuId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(skuIdList)) {
                Map<Long, Integer> hasStock = wareSkuController.getSkuHasStock(skuIdList);
                for (CartItemVO cartItemVO : cartItemList) {
                    Integer stock = hasStock.get(cartItemVO.getSkuId());
                    if (Objects.nonNull(stock)) {
                        cartItemVO.setHasStock(stock > 0);
                    }
                }
            }

        }, threadPoolExecutor);


        CompletableFuture<Void> getMember = CompletableFuture.runAsync(() -> {
            orderConfirmVO.setIntegration(memberController.getById(memberId).getIntegration());
        }, threadPoolExecutor);

        try {
            CompletableFuture.allOf(getAddr, getCartItemList, getMember).get();
        } catch (InterruptedException | ExecutionException ignored) {

        }
        List<CartItemVO> cartItemList = orderConfirmVO.getCartItemList();
        for (CartItemVO cartItemVO : cartItemList) {
            Boolean stock = cartItemVO.getHasStock();
            if (Objects.isNull(stock)) {
                cartItemVO.setHasStock(false);
            }
        }
        String orderToken = UUID.randomUUID().toString().replace("-", "");
        orderConfirmVO.setOrderToken(orderToken);
        redisTemplate.opsForValue().set(Constant.ORDER_TOKEN_PREFIX + memberId, orderToken, 30, TimeUnit.MINUTES);
        return orderConfirmVO;
    }

    private OrderConfirmVO confirmOrderV1() {
        Long memberId = LoginInterceptor.threadLocal.get().getLeft();
        OrderConfirmVO orderConfirmVO = new OrderConfirmVO();
        List<MemberReceiveAddressEntity> addressEntityList = memberReceiveAddressController.getByMemberId(memberId);

        List<MemberReceiveAddressVO> addressList = new ArrayList<>(addressEntityList.size());
        for (MemberReceiveAddressEntity memberReceiveAddressEntity : addressEntityList) {
            MemberReceiveAddressVO vo = new MemberReceiveAddressVO();
            BeanUtils.copyProperties(memberReceiveAddressEntity, vo);
            addressList.add(vo);
        }
        orderConfirmVO.setAddressList(addressList);
        // 调用远程服务获取当前的购物车
        // 注意这里涉及到了用户登录状态的传递
        // 也就是说不能直接使用feign提供的restTemplate来进行调用, 这样会丢失请求头header
        // 需要添加feign请求拦截器, 加上请求头
        // feign.SynchronousMethodHandler.executeAndDecode
        List<CartItem> cartItem = cartController.getCurrentCartItem();

        List<CartItemVO> cartItemList = new ArrayList<>(cartItem.size());
        for (CartItem item : cartItem) {
            CartItemVO vo = new CartItemVO();
            BeanUtils.copyProperties(item, vo);
            cartItemList.add(vo);
        }
        orderConfirmVO.setCartItemList(cartItemList);

        orderConfirmVO.setIntegration(memberController.getById(memberId).getIntegration());
        return orderConfirmVO;
    }

    @Override
    public CreateOrderResp createOrder(CreateOrderReq req) {
        Long memberId = LoginInterceptor.threadLocal.get().getLeft();
        CreateOrderResp resp = new CreateOrderResp();
        // 1. 验证令牌
        // 对比和删除必须保证原子性(使用lua脚本)
        String orderToken = req.getOrderToken();
        if (StringUtils.isEmpty(orderToken)) {
            resp.setCode(1);
            return resp;
        }
        String script = "if redis.call('get', KEY[1]) == ARGV[1] then return redis.call('del', KEY[1]) else return 0 end";
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
        // 0-失败 1-相同且删除成功
        Long result = redisTemplate.execute(redisScript, Collections.singletonList(Constant.ORDER_TOKEN_PREFIX + memberId), orderToken);
        if (Objects.isNull(result) || result != 1L) {
            resp.setCode(2);
            return resp;
        }

        // 2. 生成订单号
        String orderSn = IdWorker.getTimeId();

        OrderEntity order = new OrderEntity();
        // 设置订单号
        order.setOrderSn(orderSn);
        // 设置运费信息
        order.setFreightAmount(req.getFare());
        // 设置收货人信息
        MemberReceiveAddressEntity memberReceiveAddress = memberReceiveAddressController.getById(req.getAddrId());
        order.setReceiverCity(memberReceiveAddress.getCity());
        order.setReceiverDetailAddress(memberReceiveAddress.getDetailAddress());
        order.setReceiverName(memberReceiveAddress.getName());
        order.setReceiverPhone(memberReceiveAddress.getPhone());
        order.setReceiverPostCode(memberReceiveAddress.getPostCode());
        order.setReceiverProvince(memberReceiveAddress.getProvince());
        order.setReceiverRegion(memberReceiveAddress.getRegion());

        // 3. 设置订单项
        List<CartItem> cartItem = cartController.getCurrentCartItem();
        if (CollectionUtils.isNotEmpty(cartItem)) {
            List<Long> skuIdList = cartItem.stream().map(CartItem::getSkuId).distinct().collect(Collectors.toList());
            Map<Long/* skuId */, SpuInfoEntity> spuInfoEntityMap = spuInfoController.listBySkuId(skuIdList);
            List<OrderItemEntity> orderItemList = cartItem.stream().map(item -> {
                OrderItemEntity orderItemEntity = convertCartItemToOrderItem(item);
                orderItemEntity.setOrderSn(orderSn);
                SpuInfoEntity spuInfoEntity = spuInfoEntityMap.get(item.getSkuId());
                if (Objects.nonNull(spuInfoEntity)) {
                    orderItemEntity.setSpuId(spuInfoEntity.getId());
                    orderItemEntity.setSpuBrand(spuInfoEntity.getBrandId().toString());
                    orderItemEntity.setSpuName(spuInfoEntity.getSpuName());
                    orderItemEntity.setCategoryId(spuInfoEntity.getCatalogId());
                }
                return orderItemEntity;
            }).collect(Collectors.toList());
        }

        return resp;
    }

    private OrderItemEntity convertCartItemToOrderItem(CartItem cartItem) {
        OrderItemEntity orderItem = new OrderItemEntity();
        cartItem.getSkuId();
        orderItem.setSkuId(cartItem.getSkuId());
        orderItem.setSkuName(cartItem.getTitle());
        orderItem.setSkuPic(cartItem.getImage());
        orderItem.setSkuPrice(cartItem.getPrice());
        orderItem.setSkuQuantity(cartItem.getCount());
        orderItem.setSkuAttrsVals(String.join(",", cartItem.getSkuAttr()));
        orderItem.setGiftGrowth(cartItem.getPrice().intValue());
        orderItem.setGiftIntegration(cartItem.getPrice().intValue());
        return orderItem;
    }
}