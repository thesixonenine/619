package io.github.thesixonenine.product.controller;

import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.product.entity.SkuSaleAttrValueEntity;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/10/31 23:59
 * @since 1.0
 */
@Api(value = "商品")
@FeignClient(name = "product")
public interface ISkuSaleAttrValueController {

    @GetMapping("/product/skusaleattrvalue/list")
    R list(@RequestParam Map<String, Object> params);

    @GetMapping("/product/skusaleattrvalue/info/{id}")
    R info(@PathVariable("id") Long id);

    @PostMapping("/product/skusaleattrvalue/save")
    R save(@RequestBody SkuSaleAttrValueEntity skuSaleAttrValue);

    @PostMapping("/product/skusaleattrvalue/update")
    R update(@RequestBody SkuSaleAttrValueEntity skuSaleAttrValue);

    @PostMapping("/product/skusaleattrvalue/delete")
    R delete(@RequestBody Collection<Long> ids);
}
