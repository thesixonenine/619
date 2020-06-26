package io.github.thesixonenine.coupon.controller;

import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.coupon.entity.HomeSubjectSpuEntity;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


/**
 * 专题商品
 *
 * @author thesixonenine
 * @date 2020-06-06 01:15:48
 */
@Api(value = "专题商品")
@FeignClient(name = "coupon")
public interface HomeSubjectSpuController {
    /**
     * 列表
     */
    @RequestMapping("/coupon/homesubjectspu/list")
    R list(@RequestParam Map<String, Object> params);

    /**
     * 信息
     */
    @RequestMapping("/coupon/homesubjectspu/info/{id}")
    R info(@PathVariable("id") Long id);

    /**
     * 保存
     */
    @RequestMapping("/coupon/homesubjectspu/save")
    R save(@RequestBody HomeSubjectSpuEntity homeSubjectSpu);

    /**
     * 修改
     */
    @RequestMapping("/coupon/homesubjectspu/update")
    R update(@RequestBody HomeSubjectSpuEntity homeSubjectSpu);

    /**
     * 删除
     */
    @RequestMapping("/coupon/homesubjectspu/delete")
    R delete(@RequestBody Long[] ids);

}
