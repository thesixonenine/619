package io.github.thesixonenine.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.thesixonenine.cart.controller.ICartController;
import io.github.thesixonenine.cart.vo.CartItem;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.Query;
import io.github.thesixonenine.member.controller.MemberController;
import io.github.thesixonenine.member.controller.MemberReceiveAddressController;
import io.github.thesixonenine.member.entity.MemberReceiveAddressEntity;
import io.github.thesixonenine.order.dao.OrderDao;
import io.github.thesixonenine.order.entity.OrderEntity;
import io.github.thesixonenine.order.interceptor.LoginInterceptor;
import io.github.thesixonenine.order.service.OrderService;
import io.github.thesixonenine.order.vo.CartItemVO;
import io.github.thesixonenine.order.vo.MemberReceiveAddressVO;
import io.github.thesixonenine.order.vo.OrderConfirmVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;


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
        }, threadPoolExecutor);
        CompletableFuture<Void> getMember = CompletableFuture.runAsync(() -> {
            orderConfirmVO.setIntegration(memberController.getById(memberId).getIntegration());
        }, threadPoolExecutor);

        try {
            CompletableFuture.allOf(getAddr, getCartItemList, getMember).get();
        } catch (InterruptedException | ExecutionException ignored) {

        }
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

}