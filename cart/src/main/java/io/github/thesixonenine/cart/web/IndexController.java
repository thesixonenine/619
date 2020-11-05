package io.github.thesixonenine.cart.web;

import io.github.thesixonenine.cart.interceptor.CartInterceptor;
import io.github.thesixonenine.cart.service.ICartService;
import io.github.thesixonenine.cart.vo.Cart;
import io.github.thesixonenine.cart.vo.CartItem;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping(value = "checkItem")
    public String checkItem(@RequestParam(value = "skuId") Long skuId,
                            @RequestParam(value = "check") Integer check) {
        cartService.checkItem(skuId, check!=0);
        return "redirect:http://cart.jdmall.com/cart.html";
    }

    /**
     * cookie中保存user-key来标识用户身份, 一个月过期
     * 第一次也会给一个user-key
     *
     * @return page
     */
    @GetMapping(value = "cart.html")
    public String cartListPage(Model model) {
        Pair<Long, String> pair = CartInterceptor.threadLocal.get();
        Cart cart = cartService.getCart();
        model.addAttribute("cart", cart);
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
                            RedirectAttributes redirectAttributes) {
        cartService.addToCart(skuId, num);
        redirectAttributes.addAttribute("skuId", skuId);
        return "redirect:http://cart.jdmall.com/addToCartSuccess.html";
    }

    @GetMapping(value = "addToCartSuccess.html")
    public String addToCartSuccessPage(@RequestParam(value = "skuId") Long skuId,
                                       Model model) {
        CartItem cartItem = cartService.getCartItemBySkuId(skuId);
        model.addAttribute("item", cartItem);
        return "success";
    }
}
