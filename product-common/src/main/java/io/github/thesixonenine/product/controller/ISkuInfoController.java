package io.github.thesixonenine.product.controller;

import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.product.entity.SkuInfoEntity;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/10/31 23:41
 * @since 1.0
 */
@Api(value = "商品")
@FeignClient(name = "product")
public interface ISkuInfoController {

    @GetMapping("/product/skuinfo/list")
    R list(@RequestParam Map<String, Object> params);

    @GetMapping("/product/skuinfo/info/{skuId}")
    R info(@PathVariable("skuId") Long skuId);

    @PostMapping("/product/skuinfo/save")
    R save(@RequestBody SkuInfoEntity skuInfo);

    @PostMapping("/product/skuinfo/update")
    R update(@RequestBody SkuInfoEntity skuInfo);

    @PostMapping("/product/skuinfo/delete")
    R delete(@RequestBody Long[] skuIds);
}
