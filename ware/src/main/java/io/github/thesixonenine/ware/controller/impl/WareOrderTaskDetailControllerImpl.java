package io.github.thesixonenine.ware.controller.impl;

import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.ware.controller.WareOrderTaskDetailController;
import io.github.thesixonenine.ware.entity.WareOrderTaskDetailEntity;
import io.github.thesixonenine.ware.service.WareOrderTaskDetailService;
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
public class WareOrderTaskDetailControllerImpl implements WareOrderTaskDetailController {
    @Autowired
    private WareOrderTaskDetailService wareOrderTaskDetailService;

    /**
     * 列表
     */
    public R list(Map<String, Object> params) {
        PageUtils page = wareOrderTaskDetailService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    public R info(Long id) {
        WareOrderTaskDetailEntity wareOrderTaskDetail = wareOrderTaskDetailService.getById(id);
        return R.ok().put("wareOrderTaskDetail", wareOrderTaskDetail);
    }

    /**
     * 保存
     */
    public R save(WareOrderTaskDetailEntity wareOrderTaskDetail) {
        wareOrderTaskDetailService.save(wareOrderTaskDetail);
        return R.ok();
    }

    /**
     * 修改
     */
    public R update(WareOrderTaskDetailEntity wareOrderTaskDetail) {
        wareOrderTaskDetailService.updateById(wareOrderTaskDetail);
        return R.ok();
    }

    /**
     * 删除
     */
    public R delete(Long[] ids) {
        wareOrderTaskDetailService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
