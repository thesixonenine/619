package io.github.thesixonenine.coupon.controller;

import java.util.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import io.github.thesixonenine.coupon.entity.CouponEntity;
import io.github.thesixonenine.common.utils.R;



/**
 * 优惠券信息
 *
 * @author thesixonenine
 * @date 2020-06-06 01:15:48
 */
@Api(value = "优惠券信息")
@FeignClient(name = "coupon")
public interface CouponController {
    /**
     * 列表
     */
    @ApiOperation(value = "列表")
    @GetMapping("/coupon/coupon/list")
    R list(@RequestParam Map<String, Object> params);

    /**
     * 信息
     */
    @GetMapping("/coupon/coupon/info/{id}")
    R info(@PathVariable("id") Long id);

    /**
     * 保存
     */
    @PostMapping("/coupon/coupon/save")
    R save(@RequestBody CouponEntity coupon);

    /**
     * 修改
     */
    @PostMapping("/coupon/coupon/update")
    R update(@RequestBody CouponEntity coupon);

    /**
     * 删除
     */
    @PostMapping("/coupon/coupon/delete")
    R delete(@RequestBody Long[] ids);

}
