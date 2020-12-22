package io.github.thesixonenine.order.controller;

import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.order.entity.OrderEntity;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/12/22 23:37
 * @since 1.0
 */
@Api(value = "订单")
@FeignClient(name = "order")
public interface OrderController {

    @GetMapping("/order/order/list")
    R list(@RequestParam Map<String, Object> params);

    @GetMapping("/order/order/info/{id}")
    R info(@PathVariable("id") Long id);

    @PostMapping("/order/order/save")
    R save(@RequestBody OrderEntity order);

    @PostMapping("/order/order/update")
    R update(@RequestBody OrderEntity order);

    @PostMapping("/order/order/delete")
    R delete(@RequestBody Long[] ids);

    @GetMapping("/order/order/info/ordersn/{orderSn}")
    OrderEntity getByOrderSn(@PathVariable("orderSn") String orderSn);
}
