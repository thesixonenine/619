package io.github.thesixonenine.coupon.controller;

import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.coupon.entity.MemberPriceEntity;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 商品会员价格
 *
 * @author thesixonenine
 * @date 2020-06-06 01:15:48
 */
@Api(value = "商品会员价格")
@FeignClient(name = "coupon")
public interface MemberPriceController {
    /**
     * 列表
     */
    @GetMapping("/coupon/memberprice/list")
    R list(@RequestParam Map<String, Object> params);

    /**
     * 信息
     */
    @GetMapping("/coupon/memberprice/info/{id}")
    R info(@PathVariable("id") Long id);

    /**
     * 保存
     */
    @PostMapping("/coupon/memberprice/save")
    R save(@RequestBody MemberPriceEntity memberPrice);

    /**
     * 批量保存
     */
    @PostMapping("/coupon/memberprice/saveBatch")
    R saveBatch(@RequestBody List<MemberPriceEntity> memberPriceList);

    /**
     * 修改
     */
    @PostMapping("/coupon/memberprice/update")
    R update(@RequestBody MemberPriceEntity memberPrice);

    /**
     * 删除
     */
    @PostMapping("/coupon/memberprice/delete")
    R delete(@RequestBody Long[] ids);

}
