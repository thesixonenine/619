package io.github.thesixonenine.coupon.controller;

import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.coupon.entity.CouponSpuCategoryRelationEntity;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    @RequestMapping("/coupon/couponspucategoryrelation/list")
    R list(@RequestParam Map<String, Object> params);


    /**
     * 信息
     */
    @RequestMapping("/coupon/couponspucategoryrelation/info/{id}")
    R info(@PathVariable("id") Long id);

    /**
     * 保存
     */
    @RequestMapping("/coupon/couponspucategoryrelation/save")
    R save(@RequestBody CouponSpuCategoryRelationEntity couponSpuCategoryRelation);

    /**
     * 修改
     */
    @RequestMapping("/coupon/couponspucategoryrelation/update")
    R update(@RequestBody CouponSpuCategoryRelationEntity couponSpuCategoryRelation);

    /**
     * 删除
     */
    @RequestMapping("/coupon/couponspucategoryrelation/delete")
    R delete(@RequestBody Long[] ids);

}
