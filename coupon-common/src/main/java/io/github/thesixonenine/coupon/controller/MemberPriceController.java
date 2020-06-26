package io.github.thesixonenine.coupon.controller;

import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.coupon.entity.MemberPriceEntity;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    @RequestMapping("/coupon/memberprice/list")
    R list(@RequestParam Map<String, Object> params);

    /**
     * 信息
     */
    @RequestMapping("/coupon/memberprice/info/{id}")
    R info(@PathVariable("id") Long id);

    /**
     * 保存
     */
    @RequestMapping("/coupon/memberprice/save")
    R save(@RequestBody MemberPriceEntity memberPrice);

    /**
     * 修改
     */
    @RequestMapping("/coupon/memberprice/update")
    R update(@RequestBody MemberPriceEntity memberPrice);

    /**
     * 删除
     */
    @RequestMapping("/coupon/memberprice/delete")
    R delete(@RequestBody Long[] ids);

}
