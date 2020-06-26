package io.github.thesixonenine.coupon.controller;

import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.coupon.entity.CouponSpuRelationEntity;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * 优惠券与产品关联
 *
 * @author thesixonenine
 * @date 2020-06-06 01:15:48
 */
@Api(value = "优惠券与产品关联")
@FeignClient(name = "coupon")
public interface CouponSpuRelationController {

    /**
     * 列表
     */
    @GetMapping("/coupon/couponspurelation/list")
    R list(@RequestParam Map<String, Object> params);

    /**
     * 信息
     */
    @GetMapping("/coupon/couponspurelation/info/{id}")
    R info(@PathVariable("id") Long id);

    /**
     * 保存
     */
    @PostMapping("/coupon/couponspurelation/save")
    R save(@RequestBody CouponSpuRelationEntity couponSpuRelation);

    /**
     * 修改
     */
    @PostMapping("/coupon/couponspurelation/update")
    R update(@RequestBody CouponSpuRelationEntity couponSpuRelation);

    /**
     * 删除
     */
    @PostMapping("/coupon/couponspurelation/delete")
    R delete(@RequestBody Long[] ids);

}
