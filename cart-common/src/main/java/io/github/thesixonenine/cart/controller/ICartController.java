package io.github.thesixonenine.cart.controller;

import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/10/27 22:25
 * @since 1.0
 */
@Api(value = "购物车")
@FeignClient(name = "cart")
public interface ICartController {
}
