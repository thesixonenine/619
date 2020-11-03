package io.github.thesixonenine.cart.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.thesixonenine.cart.interceptor.CartInterceptor;
import io.github.thesixonenine.cart.service.ICartService;
import io.github.thesixonenine.cart.vo.Cart;
import io.github.thesixonenine.cart.vo.CartItem;
import io.github.thesixonenine.common.utils.Constant;
import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.product.controller.ISkuInfoController;
import io.github.thesixonenine.product.controller.ISkuSaleAttrValueController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/09/10 22:04
 * @since 1.0
 */
@Slf4j
@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ISkuInfoController skuInfoController;
    @Autowired
    private ISkuSaleAttrValueController skuSaleAttrValueController;
    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    private final String CART_PREFIX = "jdmall:cart:";

    @Override
    public CartItem addToCart(Long skuId, Integer num) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();

        // 添加新商品
        Object redisProduct = cartOps.get(skuId.toString());
        if (Objects.nonNull(redisProduct)) {
            //购物车有此商品, 只需要改数量
            String o = (String) redisProduct;
            CartItem item = getCartItem(o);
            item.setCount(item.getCount() + num);
            return putCartItemIntoRedis(skuId, cartOps, item);
        } else {
            CartItem item = new CartItem();
            //购物车无此商品
            CompletableFuture<Void> getSkuInfoTsk = CompletableFuture.runAsync(() -> {
                // 查询商品信息
                R r = skuInfoController.info(skuId);
                LinkedHashMap<String, Object> skuInfo = (LinkedHashMap<String, Object>) r.get("skuInfo");

                item.setCheck(true);
                item.setCount(num);
                item.setImage((String) skuInfo.get("skuDefaultImg"));
                item.setTitle((String) skuInfo.get("skuTitle"));
                item.setSkuId(skuId);
                item.setPrice(new BigDecimal(String.valueOf(skuInfo.get("price"))));
            }, threadPoolExecutor);

            CompletableFuture<Void> getSkuSaleAttrValues = CompletableFuture.runAsync(() -> {
                Map<String, Object> map = new HashMap<>();
                map.put(Constant.LIMIT, Integer.MAX_VALUE);
                map.put("sku_id", skuId);
                R r = skuSaleAttrValueController.list(map);
                LinkedHashMap<String, Object> page = (LinkedHashMap<String, Object>) r.get("page");
                ArrayList<LinkedHashMap<String, Object>> list = (ArrayList<LinkedHashMap<String, Object>>) page.get("list");
                List<String> skuAttr = new ArrayList<>();
                for (LinkedHashMap<String, Object> o : list) {
                    skuAttr.add(o.get("attrName") + ": " + o.get("attrValue"));
                }
                // 查询sku的组合信息
                item.setSkuAttr(skuAttr);
            }, threadPoolExecutor);

            try {
                CompletableFuture.allOf(getSkuInfoTsk, getSkuSaleAttrValues).get();
            } catch (InterruptedException | ExecutionException e) {
                log.error("请求远程服务异常", e);
                throw new RuntimeException("请求远程服务异常");
            }

            return putCartItemIntoRedis(skuId, cartOps, item);
        }
    }

    @Override
    public CartItem getCartItemBySkuId(Long skuId) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        String o = (String) cartOps.get(skuId.toString());
        return getCartItem(o);
    }

    @Override
    public Cart getCart() {
        List<CartItem> tempCartItemList = getTempCartItemList();
        Pair<Long, String> pair = getUserKey();
        Cart cart = new Cart();
        if (pair.getLeft() != null) {
            // 已登录
            log.debug("用户已登录");
            if (CollectionUtils.isNotEmpty(tempCartItemList)) {
                // 添加到购物车中
                for (CartItem cartItem : tempCartItemList) {
                    addToCart(cartItem.getSkuId(), cartItem.getCount());
                }
                // 清空临时购物车
                clearCartItemList(CART_PREFIX + pair.getRight());
            }
            List<CartItem> cartItemList = getCartItemList();
            cart.setItemList(cartItemList);
        } else {
            cart.setItemList(tempCartItemList);
        }
        return cart;
    }

    private CartItem getCartItem(String o) {
        ObjectMapper objectMapper = new ObjectMapper();
        CartItem item;
        try {
            item = objectMapper.readValue(o, CartItem.class);
        } catch (JsonProcessingException e) {
            log.error("[{}]读取redis数据失败", o, e);
            throw new RuntimeException("查询redis商品数据异常");
        }
        return item;
    }

    private CartItem putCartItemIntoRedis(Long skuId, BoundHashOperations<String, Object, Object> cartOps, CartItem item) {
        String value;
        try {
            value = new ObjectMapper().writeValueAsString(item);
        } catch (JsonProcessingException e) {
            log.error("序列化skuId[{}]错误", skuId, e);
            throw new RuntimeException("序列化商品错误");
        }
        cartOps.put(skuId.toString(), value);
        return item;
    }

    private BoundHashOperations<String, Object, Object> getCartOps() {
        Pair<Long, String> pair = getUserKey();
        String cartKey;
        if (pair.getLeft() != null) {
            // 已登录
            cartKey = CART_PREFIX + pair.getLeft();
        } else {
            cartKey = CART_PREFIX + pair.getRight();
        }
        // 绑定hash操作
        return redisTemplate.boundHashOps(cartKey);
    }

    private Pair<Long, String> getUserKey() {
        Pair<Long, String> pair = CartInterceptor.threadLocal.get();
        if (pair.getLeft() != null) {
            // 已登录
            log.debug("用户已登录");
        } else {
            log.debug("用户未登录");
        }
        return pair;
    }

    private List<CartItem> getCartItemList() {
        Pair<Long, String> pair = CartInterceptor.threadLocal.get();
        String key = CART_PREFIX + pair.getLeft();
        return getCartItemList(key);
    }

    private List<CartItem> getTempCartItemList() {
        Pair<Long, String> pair = CartInterceptor.threadLocal.get();
        String key = CART_PREFIX + pair.getRight();
        return getCartItemList(key);
    }

    private List<CartItem> getCartItemList(String key) {
        List<Object> values = redisTemplate.boundHashOps(key).values();
        if (CollectionUtils.isNotEmpty(values)) {
            return values.stream().map(t -> {
                String s = (String) t;
                return getCartItem(s);
            }).collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    private void clearCartItemList(String key) {
        redisTemplate.delete(key);
    }
}
