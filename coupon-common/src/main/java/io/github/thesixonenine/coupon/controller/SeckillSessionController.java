package io.github.thesixonenine.coupon.controller;

import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.coupon.entity.SeckillSessionEntity;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * 秒杀活动场次
 *
 * @author thesixonenine
 * @date 2020-06-06 01:15:48
 */
@Api(value = "秒杀活动场次")
@FeignClient(name = "coupon")
public interface SeckillSessionController {

    /**
     * 列表
     */
    @GetMapping("/coupon/seckillsession/list")
    R list(@RequestParam Map<String, Object> params);

    /**
     * 信息
     */
    @GetMapping("/coupon/seckillsession/info/{id}")
    R info(@PathVariable("id") Long id);

    /**
     * 保存
     */
    @PostMapping("/coupon/seckillsession/save")
    R save(@RequestBody SeckillSessionEntity seckillSession);

    /**
     * 修改
     */
    @PostMapping("/coupon/seckillsession/update")
    R update(@RequestBody SeckillSessionEntity seckillSession);

    /**
     * 删除
     */
    @PostMapping("/coupon/seckillsession/delete")
    R delete(@RequestBody Long[] ids);

}
