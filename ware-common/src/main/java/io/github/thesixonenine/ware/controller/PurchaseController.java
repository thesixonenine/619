package io.github.thesixonenine.ware.controller;

import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.ware.entity.PurchaseEntity;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * 采购信息
 *
 * @author thesixonenine
 * @date 2020-06-06 01:52:05
 */
@Api(value = "采购信息")
@FeignClient(name = "ware")
public interface PurchaseController {
    /**
     * 列表
     */
    @GetMapping("/ware/purchase/list")
    R list(@RequestParam Map<String, Object> params);

    /**
     * 信息
     */
    @GetMapping("/ware/purchase/info/{id}")
    R info(@PathVariable("id") Long id);

    /**
     * 保存
     */
    @PostMapping("/ware/purchase/save")
    R save(@RequestBody PurchaseEntity purchase);

    /**
     * 修改
     */
    @PostMapping("/ware/purchase/update")
    R update(@RequestBody PurchaseEntity purchase);

    /**
     * 删除
     */
    @PostMapping("/ware/purchase/delete")
    R delete(@RequestBody Long[] ids);

}
