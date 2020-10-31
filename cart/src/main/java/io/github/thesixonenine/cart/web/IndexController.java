package io.github.thesixonenine.cart.web;

import io.github.thesixonenine.cart.interceptor.CartInterceptor;
import io.github.thesixonenine.cart.service.ICartService;
import io.github.thesixonenine.cart.vo.CartItem;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/10/27 22:29
 * @since 1.0
 */
@Controller
public class IndexController {
    @Autowired
    private ICartService cartService;

    /**
     * cookie中保存user-key来标识用户身份, 一个月过期
     * 第一次也会给一个user-key
     *
     * @return page
     */
    @GetMapping(value = "cart.html")
    public String cartListPage() {
        Pair<Long, String> pair = CartInterceptor.threadLocal.get();
        return "cartList";
    }

    /**
     * 添加商品到购物车
     *
     * @return 添加成功页面
     */
    @GetMapping(value = "addToCart")
    public String addToCart(@RequestParam(value = "skuId") Long skuId,
                            @RequestParam(value = "num") Integer num,
                            Model model) {
        CartItem cartItem = cartService.addToCart(skuId, num);
        model.addAttribute("item", cartItem);
        return "success";
    }
}
