package io.github.thesixonenine.coupon.controller;

import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.coupon.entity.SeckillSkuRelationEntity;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


/**
 * 秒杀活动商品关联
 *
 * @author thesixonenine
 * @date 2020-06-06 01:15:48
 */
@Api(value = "秒杀活动商品关联")
@FeignClient(name = "coupon")
public interface SeckillSkuRelationController {
    /**
     * 列表
     */
    @RequestMapping("/coupon/seckillskurelation/list")
    R list(@RequestParam Map<String, Object> params);

    /**
     * 信息
     */
    @RequestMapping("/coupon/seckillskurelation/info/{id}")
    R info(@PathVariable("id") Long id);

    /**
     * 保存
     */
    @RequestMapping("/coupon/seckillskurelation/save")
    R save(@RequestBody SeckillSkuRelationEntity seckillSkuRelation);

    /**
     * 修改
     */
    @RequestMapping("/coupon/seckillskurelation/update")
    R update(@RequestBody SeckillSkuRelationEntity seckillSkuRelation);

    /**
     * 删除
     */
    @RequestMapping("/coupon/seckillskurelation/delete")
    R delete(@RequestBody Long[] ids);

}
