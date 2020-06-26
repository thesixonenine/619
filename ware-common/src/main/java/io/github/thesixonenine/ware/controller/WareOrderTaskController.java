package io.github.thesixonenine.ware.controller;

import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.ware.entity.WareOrderTaskEntity;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * 库存工作单
 *
 * @author thesixonenine
 * @date 2020-06-06 01:52:04
 */
@Api(value = "库存工作单")
@FeignClient(name = "ware")
public interface WareOrderTaskController {
    /**
     * 列表
     */
    @GetMapping("/ware/wareordertask/list")
    R list(@RequestParam Map<String, Object> params);

    /**
     * 信息
     */
    @GetMapping("/ware/wareordertask/info/{id}")
    R info(@PathVariable("id") Long id);

    /**
     * 保存
     */
    @PostMapping("/ware/wareordertask/save")
    R save(@RequestBody WareOrderTaskEntity wareOrderTask);

    /**
     * 修改
     */
    @PostMapping("/ware/wareordertask/update")
    R update(@RequestBody WareOrderTaskEntity wareOrderTask);

    /**
     * 删除
     */
    @PostMapping("/ware/wareordertask/delete")
    R delete(@RequestBody Long[] ids);

}
