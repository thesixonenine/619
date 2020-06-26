package io.github.thesixonenine.ware.controller;

import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.ware.entity.PurchaseDetailEntity;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * @author thesixonenine
 * @date 2020-06-06 01:52:05
 */
@Api(value = "采购详细信息")
@FeignClient(name = "ware")
public interface PurchaseDetailController {
    /**
     * 列表
     */
    @GetMapping("/ware/purchasedetail/list")
    R list(@RequestParam Map<String, Object> params);

    /**
     * 信息
     */
    @GetMapping("/ware/purchasedetail/info/{id}")
    R info(@PathVariable("id") Long id);

    /**
     * 保存
     */
    @PostMapping("/ware/purchasedetail/save")
    R save(@RequestBody PurchaseDetailEntity purchaseDetail);

    /**
     * 修改
     */
    @PostMapping("/ware/purchasedetail/update")
    R update(@RequestBody PurchaseDetailEntity purchaseDetail);

    /**
     * 删除
     */
    @PostMapping("/delete")
    R delete(@RequestBody Long[] ids);

}
