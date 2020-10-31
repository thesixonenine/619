package io.github.thesixonenine.product.controller;

import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.product.entity.SkuInfoEntity;
import io.github.thesixonenine.product.service.SkuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * sku信息
 *
 * @author thesixonenine
 * @date 2020-06-06 00:59:35
 */
@RestController
public class SkuInfoController implements ISkuInfoController{
    @Autowired
    private SkuInfoService skuInfoService;

    /**
     * 列表
     */
    @Override
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = skuInfoService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @Override
    public R info(@PathVariable("skuId") Long skuId) {
        SkuInfoEntity skuInfo = skuInfoService.getById(skuId);
        return R.ok().put("skuInfo", skuInfo);
    }

    /**
     * 保存
     */
    @Override
    public R save(@RequestBody SkuInfoEntity skuInfo) {
        skuInfoService.save(skuInfo);
        return R.ok();
    }

    /**
     * 修改
     */
    @Override
    public R update(@RequestBody SkuInfoEntity skuInfo) {
        skuInfoService.updateById(skuInfo);
        return R.ok();
    }

    /**
     * 删除
     */
    @Override
    public R delete(@RequestBody Long[] skuIds) {
        skuInfoService.removeByIds(Arrays.asList(skuIds));
        return R.ok();
    }

}
