package io.github.thesixonenine.coupon.controller;

import java.util.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    @RequestMapping("/coupon/coupon/list")
    R list(@RequestParam Map<String, Object> params);


    /**
     * 信息
     */
    @RequestMapping("/coupon/coupon/info/{id}")
    R info(@PathVariable("id") Long id);

    /**
     * 保存
     */
    @RequestMapping("/coupon/coupon/save")
    R save(@RequestBody CouponEntity coupon);

    /**
     * 修改
     */
    @RequestMapping("/coupon/coupon/update")
    R update(@RequestBody CouponEntity coupon);

    /**
     * 删除
     */
    @RequestMapping("/coupon/coupon/delete")
    R delete(@RequestBody Long[] ids);

}
