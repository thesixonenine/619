package io.github.thesixonenine.coupon.controller;

import java.util.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import io.github.thesixonenine.coupon.entity.CouponHistoryEntity;
import io.github.thesixonenine.common.utils.R;



/**
 * 优惠券领取历史记录
 *
 * @author thesixonenine
 * @date 2020-06-06 01:15:48
 */
@Api(value = "优惠券领取历史记录")
@FeignClient(name = "coupon")
public interface CouponHistoryController {
    /**
     * 列表
     */
    @ApiOperation(value = "列表")
    @GetMapping("/coupon/couponhistory/list")
    R list(@RequestParam Map<String, Object> params);

    /**
     * 信息
     */
    @GetMapping("/coupon/couponhistory/info/{id}")
    R info(@PathVariable("id") Long id);

    /**
     * 保存
     */
    @PostMapping("/coupon/couponhistory/save")
    R save(@RequestBody CouponHistoryEntity couponHistory);

    /**
     * 修改
     */
    @PostMapping("/coupon/couponhistory/update")
    R update(@RequestBody CouponHistoryEntity couponHistory);

    /**
     * 删除
     */
    @PostMapping("/coupon/couponhistory/delete")
    R delete(@RequestBody Long[] ids);
}
