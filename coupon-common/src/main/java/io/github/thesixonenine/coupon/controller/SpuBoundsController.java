package io.github.thesixonenine.coupon.controller;

import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.coupon.entity.SpuBoundsEntity;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


/**
 * 商品spu积分设置
 *
 * @author thesixonenine
 * @date 2020-06-06 01:15:48
 */
@Api(value = "商品spu积分设置")
@FeignClient(name = "coupon")
public interface SpuBoundsController {
    /**
     * 列表
     */
    @RequestMapping("/coupon/spubounds/list")
    R list(@RequestParam Map<String, Object> params);


    /**
     * 信息
     */
    @RequestMapping("/coupon/spubounds/info/{id}")
    R info(@PathVariable("id") Long id);

    /**
     * 保存
     */
    @RequestMapping("/coupon/spubounds/save")
    R save(@RequestBody SpuBoundsEntity spuBounds);

    /**
     * 修改
     */
    @RequestMapping("/coupon/spubounds/update")
    R update(@RequestBody SpuBoundsEntity spuBounds);

    /**
     * 删除
     */
    @RequestMapping("/coupon/spubounds/delete")
    R delete(@RequestBody Long[] ids);

}
