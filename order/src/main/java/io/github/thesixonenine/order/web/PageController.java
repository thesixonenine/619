package io.github.thesixonenine.order.web;

import io.github.thesixonenine.order.service.OrderService;
import io.github.thesixonenine.order.vo.CreateOrderReq;
import io.github.thesixonenine.order.vo.CreateOrderResp;
import io.github.thesixonenine.order.vo.OrderConfirmVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/11/08 22:29
 * @since 1.0
 */
@Slf4j
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
    public String createOrder(CreateOrderReq req, Model model, RedirectAttributes redirectAttributes) {
        CreateOrderResp createOrderResp = new CreateOrderResp();
        try {
            createOrderResp = orderService.createOrder(req);
        } catch (Exception e) {
            // 下单失败
            log.error("下单失败[{}]", e.getMessage());
            redirectAttributes.addFlashAttribute("msg", e.getMessage());
            return "redirect:http://order.jdmall.com/toTrade";
        }
        if (createOrderResp.getCode() == 0) {
            // 下单成功, 去支付选择页
            model.addAttribute("result", createOrderResp);
            return "pay";
        }
        return "redirect:http://order.jdmall.com/toTrade";
    }
}
