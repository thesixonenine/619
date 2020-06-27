package io.github.thesixonenine.ware.controller;

import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.ware.entity.WareSkuEntity;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 商品库存
 *
 * @author thesixonenine
 * @date 2020-06-06 01:52:04
 */
@Api(value = "商品库存")
@FeignClient(name = "ware")
public interface WareSkuController {
    /**
     * 列表
     */
    @GetMapping("/ware/waresku/list")
    R list(@RequestParam Map<String, Object> params);

    /**
     * 信息
     */
    @GetMapping("/ware/waresku/info/{id}")
    R info(@PathVariable("id") Long id);

    /**
     * 根据Id列表查询
     */
    @PostMapping("/ware/waresku/listByIds")
    R listByIds(@RequestBody List<Long> idList);

    /**
     * 保存
     */
    @PostMapping("/ware/waresku/save")
    R save(@RequestBody WareSkuEntity wareSku);

    /**
     * 修改
     */
    @PostMapping("/ware/waresku/update")
    R update(@RequestBody WareSkuEntity wareSku);

    /**
     * 删除
     */
    @PostMapping("/ware/waresku/delete")
    R delete(@RequestBody Long[] ids);

}
