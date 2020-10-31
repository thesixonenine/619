package io.github.thesixonenine.cart.web;

import io.github.thesixonenine.cart.interceptor.CartInterceptor;
import io.github.thesixonenine.common.utils.Constant;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/10/27 22:29
 * @since 1.0
 */
@Controller
public class IndexController {

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
     * @return 添加成功页面
     */
    @GetMapping(value = "addToCart")
    public String addToCart() {
        return "success";
    }
}
