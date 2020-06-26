package io.github.thesixonenine.ware.controller.impl;

import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.ware.controller.WareOrderTaskController;
import io.github.thesixonenine.ware.entity.WareOrderTaskEntity;
import io.github.thesixonenine.ware.service.WareOrderTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;


/**
 * 库存工作单
 *
 * @author thesixonenine
 * @date 2020-06-06 01:52:04
 */
@RestController
public class WareOrderTaskControllerImpl implements WareOrderTaskController {
    @Autowired
    private WareOrderTaskService wareOrderTaskService;

    /**
     * 列表
     */
    public R list(Map<String, Object> params) {
        PageUtils page = wareOrderTaskService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    public R info(Long id) {
        WareOrderTaskEntity wareOrderTask = wareOrderTaskService.getById(id);
        return R.ok().put("wareOrderTask", wareOrderTask);
    }

    /**
     * 保存
     */
    public R save(WareOrderTaskEntity wareOrderTask) {
        wareOrderTaskService.save(wareOrderTask);
        return R.ok();
    }

    /**
     * 修改
     */
    public R update(WareOrderTaskEntity wareOrderTask) {
        wareOrderTaskService.updateById(wareOrderTask);
        return R.ok();
    }

    /**
     * 删除
     */
    public R delete(Long[] ids) {
        wareOrderTaskService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
