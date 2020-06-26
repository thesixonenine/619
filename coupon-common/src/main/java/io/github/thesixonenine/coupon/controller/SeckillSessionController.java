package io.github.thesixonenine.coupon.controller;

import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.coupon.entity.SeckillSessionEntity;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    @RequestMapping("/coupon/seckillsession/list")
    R list(@RequestParam Map<String, Object> params);


    /**
     * 信息
     */
    @RequestMapping("/coupon/seckillsession/info/{id}")
    public R info(@PathVariable("id") Long id);

    /**
     * 保存
     */
    @RequestMapping("/coupon/seckillsession/save")
    R save(@RequestBody SeckillSessionEntity seckillSession);

    /**
     * 修改
     */
    @RequestMapping("/coupon/seckillsession/update")
    R update(@RequestBody SeckillSessionEntity seckillSession);

    /**
     * 删除
     */
    @RequestMapping("/coupon/seckillsession/delete")
    R delete(@RequestBody Long[] ids);

}
