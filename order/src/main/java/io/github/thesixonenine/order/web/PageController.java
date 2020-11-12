package io.github.thesixonenine.order.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/11/08 22:29
 * @since 1.0
 */
@Controller
public class PageController {

    @GetMapping(value = "/{page}.html")
    public String page(@PathVariable(value = "page") String page) {
        return page;
    }

    @GetMapping(value = "/toTrade")
    public String toTrade() {
        return "confirm";
    }
}
