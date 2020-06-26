package io.github.thesixonenine.coupon.controller;

import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.coupon.entity.SeckillPromotionEntity;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * 秒杀活动
 *
 * @author thesixonenine
 * @date 2020-06-06 01:15:48
 */
@Api(value = "秒杀活动")
@FeignClient(name = "coupon")
public interface SeckillPromotionController {

    /**
     * 列表
     */
    @GetMapping("/coupon/seckillpromotion/list")
    R list(@RequestParam Map<String, Object> params);

    /**
     * 信息
     */
    @GetMapping("/coupon/seckillpromotion/info/{id}")
    R info(@PathVariable("id") Long id);

    /**
     * 保存
     */
    @PostMapping("/coupon/seckillpromotion/save")
    R save(@RequestBody SeckillPromotionEntity seckillPromotion);

    /**
     * 修改
     */
    @PostMapping("/coupon/seckillpromotion/update")
    R update(@RequestBody SeckillPromotionEntity seckillPromotion);

    /**
     * 删除
     */
    @PostMapping("/coupon/seckillpromotion/delete")
    R delete(@RequestBody Long[] ids);

}
