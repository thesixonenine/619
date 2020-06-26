package io.github.thesixonenine.coupon.controller;

import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.coupon.entity.HomeAdvEntity;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * 首页轮播广告
 *
 * @author thesixonenine
 * @date 2020-06-06 01:15:48
 */
@Api(value = "首页轮播广告")
@FeignClient(name = "coupon")
public interface HomeAdvController {

    /**
     * 列表
     */
    @GetMapping("/coupon/homeadv/list")
    R list(@RequestParam Map<String, Object> params);


    /**
     * 信息
     */
    @GetMapping("/coupon/homeadv/info/{id}")
    R info(@PathVariable("id") Long id);

    /**
     * 保存
     */
    @PostMapping("/coupon/homeadv/save")
    R save(@RequestBody HomeAdvEntity homeAdv);

    /**
     * 修改
     */
    @PostMapping("/coupon/homeadv/update")
    R update(@RequestBody HomeAdvEntity homeAdv);

    /**
     * 删除
     */
    @PostMapping("/coupon/homeadv/delete")
    R delete(@RequestBody Long[] ids);

}
