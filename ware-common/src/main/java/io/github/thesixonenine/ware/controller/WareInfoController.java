package io.github.thesixonenine.ware.controller;

import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.ware.entity.WareInfoEntity;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * 仓库信息
 *
 * @author thesixonenine
 * @date 2020-06-06 01:52:04
 */
@Api(value = "仓库信息")
@FeignClient(name = "ware")
public interface WareInfoController {
    /**
     * 列表
     */
    @GetMapping("/ware/wareinfo/list")
    R list(@RequestParam Map<String, Object> params);

    /**
     * 信息
     */
    @GetMapping("/ware/wareinfo/info/{id}")
    R info(@PathVariable("id") Long id);

    /**
     * 保存
     */
    @PostMapping("/ware/wareinfo/save")
    R save(@RequestBody WareInfoEntity wareInfo);

    /**
     * 修改
     */
    @PostMapping("/ware/wareinfo/update")
    R update(@RequestBody WareInfoEntity wareInfo);

    /**
     * 删除
     */
    @PostMapping("/ware/wareinfo/delete")
    R delete(@RequestBody Long[] ids);

}
