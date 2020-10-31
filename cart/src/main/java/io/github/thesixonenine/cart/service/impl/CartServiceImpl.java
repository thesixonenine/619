package io.github.thesixonenine.cart.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.thesixonenine.cart.interceptor.CartInterceptor;
import io.github.thesixonenine.cart.service.ICartService;
import io.github.thesixonenine.cart.vo.CartItem;
import io.github.thesixonenine.common.utils.Constant;
import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.product.controller.ISkuInfoController;
import io.github.thesixonenine.product.controller.ISkuSaleAttrValueController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
        CartItem item = new CartItem();
        CompletableFuture<Void> getSkuInfoTsk = CompletableFuture.runAsync(() -> {
            // 查询商品信息
            R r = skuInfoController.info(skuId);
            LinkedHashMap<String, Object> skuInfo = (LinkedHashMap<String, Object>) r.get("skuInfo");

            item.setCheck(true);
            item.setCount(num);
            item.setImage((String) skuInfo.get("skuDefaultImg"));
            item.setTitle((String) skuInfo.get("skuTitle"));
            item.setSkuId(skuId);
            item.setPrice(new BigDecimal(String.valueOf((Double) skuInfo.get("price"))));
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
            // List<SkuSaleAttrValueEntity> list = (List<SkuSaleAttrValueEntity>) pageUtils.getList();
            // 查询sku的组合信息
            item.setSkuAttr(skuAttr);
        }, threadPoolExecutor);

        try {
            CompletableFuture.allOf(getSkuInfoTsk, getSkuSaleAttrValues).get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("请求远程服务异常", e);
            throw new RuntimeException("请求远程服务异常");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String value;
        try {
            value = objectMapper.writeValueAsString(item);
        } catch (JsonProcessingException e) {
            log.error("序列化skuId[{}]错误", skuId, e);
            throw new RuntimeException("序列化商品错误");
        }
        cartOps.put(skuId.toString(), value);
        return item;
    }

    private BoundHashOperations<String, Object, Object> getCartOps() {
        Pair<Long, String> pair = CartInterceptor.threadLocal.get();
        String cartKey = StringUtils.EMPTY;
        if (pair.getLeft() != null) {
            // 已登录
            log.debug("用户已登录");
            cartKey = CART_PREFIX + pair.getLeft();
        } else {
            log.debug("用户未登录");
            cartKey = CART_PREFIX + pair.getRight();
        }
        // 绑定hash操作
        return redisTemplate.boundHashOps(cartKey);
    }
}
