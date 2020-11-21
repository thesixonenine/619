package io.github.thesixonenine.product.controller;

import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.product.dto.SpuInfoDTO;
import io.github.thesixonenine.product.entity.SpuInfoEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/11/21 20:00
 * @since 1.0
 */
@Api(value = "商品")
@FeignClient(name = "product")
public interface ISpuInfoController {

    /**
     * 列表
     */
    @GetMapping("/product/spuinfo/list")
    R list(@RequestParam Map<String, Object> params);

    /**
     * 信息
     */
    @GetMapping("/product/spuinfo/info/{id}")
    R info(@PathVariable("id") Long id);

    /**
     * 保存
     */
    @PostMapping("/product/spuinfo/save")
    R save(@RequestBody SpuInfoEntity spuInfo);

    /**
     * 保存
     */
    @PostMapping("/product/spuinfo/saveSpuInfo")
    R saveSpuInfo(@RequestBody SpuInfoDTO spuInfoDTO);

    /**
     * 修改
     */
    @PostMapping("/product/spuinfo/update")
    R update(@RequestBody SpuInfoEntity spuInfo);

    /**
     * 删除
     */
    @PostMapping("/product/spuinfo/delete")
    R delete(@RequestBody Long[] ids);

    /**
     * 上架
     */
    @ApiOperation(value = "上架")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "spuId", value = "spuId")
    })
    @GetMapping(value = "/product/spuinfo/{spuId}/up")
    R up(@PathVariable("spuId") Long spuId);

    @GetMapping(value = "/product/spuinfo/skuId/{skuId}/")
    SpuInfoEntity getBySkuId(@PathVariable("skuId") Long skuId);

    @PostMapping(value = "/product/spuinfo/listBySkuId")
    Map<Long, SpuInfoEntity> listBySkuId(@RequestBody List<Long> skuIdList);
}
