package io.github.thesixonenine.coupon.controller;

import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.coupon.entity.CouponSpuCategoryRelationEntity;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * 优惠券分类关联
 *
 * @author thesixonenine
 * @date 2020-06-06 01:15:48
 */
@Api(value = "优惠券分类关联")
@FeignClient(name = "coupon")
public interface CouponSpuCategoryRelationController {
    /**
     * 列表
     */
    @GetMapping("/coupon/couponspucategoryrelation/list")
    R list(@RequestParam Map<String, Object> params);

    /**
     * 信息
     */
    @GetMapping("/coupon/couponspucategoryrelation/info/{id}")
    R info(@PathVariable("id") Long id);

    /**
     * 保存
     */
    @PostMapping("/coupon/couponspucategoryrelation/save")
    R save(@RequestBody CouponSpuCategoryRelationEntity couponSpuCategoryRelation);

    /**
     * 修改
     */
    @PostMapping("/coupon/couponspucategoryrelation/update")
    R update(@RequestBody CouponSpuCategoryRelationEntity couponSpuCategoryRelation);

    /**
     * 删除
     */
    @PostMapping("/coupon/couponspucategoryrelation/delete")
    R delete(@RequestBody Long[] ids);

}
