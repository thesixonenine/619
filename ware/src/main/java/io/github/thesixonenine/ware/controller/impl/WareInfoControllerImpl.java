package io.github.thesixonenine.ware.controller.impl;

import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.ware.controller.WareInfoController;
import io.github.thesixonenine.ware.entity.WareInfoEntity;
import io.github.thesixonenine.ware.service.WareInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;


/**
 * 仓库信息
 *
 * @author thesixonenine
 * @date 2020-06-06 01:52:04
 */
@RestController
public class WareInfoControllerImpl implements WareInfoController {
    @Autowired
    private WareInfoService wareInfoService;

    /**
     * 列表
     */
    @Override
    public R list(Map<String, Object> params) {
        PageUtils page = wareInfoService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @Override
    public R info(Long id) {
        WareInfoEntity wareInfo = wareInfoService.getById(id);
        return R.ok().put("wareInfo", wareInfo);
    }

    /**
     * 保存
     */
    @Override
    public R save(WareInfoEntity wareInfo) {
        wareInfoService.save(wareInfo);
        return R.ok();
    }

    /**
     * 修改
     */
    @Override
    public R update(WareInfoEntity wareInfo) {
        wareInfoService.updateById(wareInfo);
        return R.ok();
    }

    /**
     * 删除
     */
    @Override
    public R delete(Long[] ids) {
        wareInfoService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

    @Override
    public BigDecimal getFare(Long addrId) {
        return wareInfoService.getFare(addrId);
    }

}
