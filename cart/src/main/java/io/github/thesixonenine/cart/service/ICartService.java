package io.github.thesixonenine.cart.service;

import io.github.thesixonenine.cart.vo.CartItem;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/09/10 22:03
 * @since 1.0
 */
public interface ICartService {
    /**
     * 添加到购物车
     *
     * @param skuId
     * @param num
     * @return
     */
    CartItem addToCart(Long skuId, Integer num);

    /**
     * 获取购物车中的某个购物项
     *
     * @param skuId
     * @return
     */
    CartItem getCartItemBySkuId(Long skuId);
}
