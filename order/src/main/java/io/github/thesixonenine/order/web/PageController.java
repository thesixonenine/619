package io.github.thesixonenine.order.web;

import io.github.thesixonenine.order.service.OrderService;
import io.github.thesixonenine.order.vo.CreateOrderReq;
import io.github.thesixonenine.order.vo.OrderConfirmVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/11/08 22:29
 * @since 1.0
 */
@Controller
public class PageController {

    @Autowired
    private OrderService orderService;

    @GetMapping(value = "/{page}.html")
    public String page(@PathVariable(value = "page") String page) {
        return page;
    }

    @GetMapping(value = "/toTrade")
    public String toTrade(Model model) {
        OrderConfirmVO vo = orderService.confirmOrder();
        model.addAttribute("data", vo);
        return "confirm";
    }

    @PostMapping(value = "/createOrder")
    public void createOrder(CreateOrderReq req) {
        // TODO 创建订单
        System.out.println(req);
    }
}
