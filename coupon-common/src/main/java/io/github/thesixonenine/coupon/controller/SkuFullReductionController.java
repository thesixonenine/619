package io.github.thesixonenine.coupon.controller;

import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.coupon.entity.SkuFullReductionEntity;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * 商品满减信息
 *
 * @author thesixonenine
 * @date 2020-06-06 01:15:48
 */
@Api(value = "商品满减信息")
@FeignClient(name = "coupon")
public interface SkuFullReductionController {

    /**
     * 列表
     */
    @GetMapping("/coupon/skufullreduction/list")
    R list(@RequestParam Map<String, Object> params);

    /**
     * 信息
     */
    @GetMapping("/coupon/skufullreduction/info/{id}")
    R info(@PathVariable("id") Long id);

    /**
     * 保存
     */
    @PostMapping("/coupon/skufullreduction/save")
    R save(@RequestBody SkuFullReductionEntity skuFullReduction);

    /**
     * 修改
     */
    @PostMapping("/coupon/skufullreduction/update")
    R update(@RequestBody SkuFullReductionEntity skuFullReduction);

    /**
     * 删除
     */
    @PostMapping("/coupon/skufullreduction/delete")
    R delete(@RequestBody Long[] ids);

}
