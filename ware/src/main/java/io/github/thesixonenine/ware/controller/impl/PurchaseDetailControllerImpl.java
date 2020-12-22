package io.github.thesixonenine.ware.controller.impl;

import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.ware.controller.PurchaseDetailController;
import io.github.thesixonenine.ware.entity.PurchaseDetailEntity;
import io.github.thesixonenine.ware.service.PurchaseDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;


/**
 * @author thesixonenine
 * @date 2020-06-06 01:52:05
 */
@RestController
public class PurchaseDetailControllerImpl implements PurchaseDetailController {
    @Autowired
    private PurchaseDetailService purchaseDetailService;

    /**
     * 列表
     */
    @Override
    public R list(Map<String, Object> params) {
        PageUtils page = purchaseDetailService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @Override
    public R info(Long id) {
        PurchaseDetailEntity purchaseDetail = purchaseDetailService.getById(id);
        return R.ok().put("purchaseDetail", purchaseDetail);
    }

    /**
     * 保存
     */
    @Override
    public R save(PurchaseDetailEntity purchaseDetail) {
        purchaseDetailService.save(purchaseDetail);
        return R.ok();
    }

    /**
     * 修改
     */
    @Override
    public R update(PurchaseDetailEntity purchaseDetail) {
        purchaseDetailService.updateById(purchaseDetail);
        return R.ok();
    }

    /**
     * 删除
     */
    @Override
    public R delete(Long[] ids) {
        purchaseDetailService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
