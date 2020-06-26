package io.github.thesixonenine.ware.controller.impl;

import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.ware.controller.PurchaseController;
import io.github.thesixonenine.ware.entity.PurchaseEntity;
import io.github.thesixonenine.ware.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;


/**
 * 采购信息
 *
 * @author thesixonenine
 * @date 2020-06-06 01:52:05
 */
@RestController
public class PurchaseControllerImpl implements PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    /**
     * 列表
     */
    public R list(Map<String, Object> params) {
        PageUtils page = purchaseService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    public R info(Long id) {
        PurchaseEntity purchase = purchaseService.getById(id);
        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    public R save(PurchaseEntity purchase) {
        purchaseService.save(purchase);
        return R.ok();
    }

    /**
     * 修改
     */
    public R update(PurchaseEntity purchase) {
        purchaseService.updateById(purchase);
        return R.ok();
    }

    /**
     * 删除
     */
    public R delete(Long[] ids) {
        purchaseService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
