package io.github.thesixonenine.ware.controller;

import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.ware.entity.WareOrderTaskDetailEntity;
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
public interface WareOrderTaskDetailController {
    /**
     * 列表
     */
    @GetMapping("/ware/wareordertaskdetail/list")
    R list(@RequestParam Map<String, Object> params);

    /**
     * 信息
     */
    @GetMapping("/ware/wareordertaskdetail/info/{id}")
    R info(@PathVariable("id") Long id);

    /**
     * 保存
     */
    @PostMapping("/ware/wareordertaskdetail/save")
    R save(@RequestBody WareOrderTaskDetailEntity wareOrderTaskDetail);

    /**
     * 修改
     */
    @PostMapping("/ware/wareordertaskdetail/update")
    R update(@RequestBody WareOrderTaskDetailEntity wareOrderTaskDetail);

    /**
     * 删除
     */
    @PostMapping("/ware/wareordertaskdetail/delete")
    R delete(@RequestBody Long[] ids);

}
