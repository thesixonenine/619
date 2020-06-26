package io.github.thesixonenine.product.controller;

import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.product.dto.SpuInfoDTO;
import io.github.thesixonenine.product.entity.SpuInfoEntity;
import io.github.thesixonenine.product.service.SpuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * spu信息
 *
 * @author thesixonenine
 * @date 2020-06-06 00:59:35
 */
@RestController
@RequestMapping("product/spuinfo")
public class SpuInfoController {
    @Autowired
    private SpuInfoService spuInfoService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = spuInfoService.queryPage(params);
        return R.ok().put("page", page);
    }
    
    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        SpuInfoEntity spuInfo = spuInfoService.getById(id);
        return R.ok().put("spuInfo", spuInfo);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody SpuInfoEntity spuInfo) {
        spuInfoService.save(spuInfo);
        return R.ok();
    }

    /**
     * 保存
     */
    @PostMapping("/saveSpuInfo")
    public R saveSpuInfo(@RequestBody SpuInfoDTO spuInfoDTO) {
        spuInfoService.saveSpuInfo(spuInfoDTO);
        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R update(@RequestBody SpuInfoEntity spuInfo) {
        spuInfoService.updateById(spuInfo);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        spuInfoService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
