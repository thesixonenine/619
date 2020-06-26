package io.github.thesixonenine.coupon.controller;

import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.coupon.entity.SkuLadderEntity;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


/**
 * 商品阶梯价格
 *
 * @author thesixonenine
 * @date 2020-06-06 01:15:48
 */
@Api(value = "商品阶梯价格")
@FeignClient(name = "coupon")
public interface SkuLadderController {
    /**
     * 列表
     */
    @RequestMapping("/coupon/skuladder/list")
    R list(@RequestParam Map<String, Object> params);

    /**
     * 信息
     */
    @RequestMapping("/coupon/skuladder/info/{id}")
    R info(@PathVariable("id") Long id);

    /**
     * 保存
     */
    @RequestMapping("/coupon/skuladder/save")
    R save(@RequestBody SkuLadderEntity skuLadder);

    /**
     * 修改
     */
    @RequestMapping("/coupon/skuladder/update")
    R update(@RequestBody SkuLadderEntity skuLadder);

    /**
     * 删除
     */
    @RequestMapping("/coupon/skuladder/delete")
    R delete(@RequestBody Long[] ids);

}
