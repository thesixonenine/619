package io.github.thesixonenine.cart.controller.impl;

import io.github.thesixonenine.cart.controller.ICartController;
import io.github.thesixonenine.cart.service.ICartService;
import io.github.thesixonenine.cart.vo.CartItem;
import io.github.thesixonenine.product.controller.ISkuInfoController;
import io.github.thesixonenine.product.entity.SkuInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/10/27 22:27
 * @since 1.0
 */
@Controller
public class CartControllerImpl implements ICartController {

    @Autowired
    private ICartService cartService;

    @Autowired
    private ISkuInfoController skuInfoController;

    @Override
    public List<CartItem> getCurrentCartItem() {
        List<CartItem> cartItemList = cartService.getCartItemList().stream().filter(t->(Objects.nonNull(t.getCheck()) && t.getCheck())).collect(Collectors.toList());
        List<Long> skuIdList = cartItemList.stream().map(CartItem::getSkuId).distinct().collect(Collectors.toList());
        Map<Long, BigDecimal> priceMap = skuInfoController.getPriceBatch(skuIdList).stream().collect(Collectors.toMap(SkuInfoEntity::getSkuId, SkuInfoEntity::getPrice));
        for (CartItem cartItem : cartItemList) {
            Long skuId = cartItem.getSkuId();
            BigDecimal price = priceMap.get(skuId);
            cartItem.setPrice(price);
        }
        return cartItemList;
    }
}
