package io.github.thesixonenine.coupon.controller;

import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.coupon.entity.SkuFullReductionEntity;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    @RequestMapping("/coupon/skufullreduction/list")
    R list(@RequestParam Map<String, Object> params);

    /**
     * 信息
     */
    @RequestMapping("/coupon/skufullreduction/info/{id}")
    R info(@PathVariable("id") Long id);

    /**
     * 保存
     */
    @RequestMapping("/coupon/skufullreduction/save")
    R save(@RequestBody SkuFullReductionEntity skuFullReduction);

    /**
     * 修改
     */
    @RequestMapping("/coupon/skufullreduction/update")
    R update(@RequestBody SkuFullReductionEntity skuFullReduction);

    /**
     * 删除
     */
    @RequestMapping("/coupon/skufullreduction/delete")
    R delete(@RequestBody Long[] ids);

}
