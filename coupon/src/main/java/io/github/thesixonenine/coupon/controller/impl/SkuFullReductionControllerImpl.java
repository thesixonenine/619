package io.github.thesixonenine.coupon.controller.impl;

import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.coupon.controller.SkuFullReductionController;
import io.github.thesixonenine.coupon.entity.SkuFullReductionEntity;
import io.github.thesixonenine.coupon.service.SkuFullReductionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;


/**
 * 商品满减信息
 *
 * @author thesixonenine
 * @date 2020-06-06 01:15:48
 */
@RestController
public class SkuFullReductionControllerImpl implements SkuFullReductionController {
    @Autowired
    private SkuFullReductionService skuFullReductionService;

    /**
     * 列表
     */
    public R list(Map<String, Object> params) {
        PageUtils page = skuFullReductionService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    public R info(Long id) {
        SkuFullReductionEntity skuFullReduction = skuFullReductionService.getById(id);
        return R.ok().put("skuFullReduction", skuFullReduction);
    }

    /**
     * 保存
     */
    public R save(SkuFullReductionEntity skuFullReduction) {
        skuFullReductionService.save(skuFullReduction);
        return R.ok();
    }

    /**
     * 修改
     */
    public R update(SkuFullReductionEntity skuFullReduction) {
        skuFullReductionService.updateById(skuFullReduction);
        return R.ok();
    }

    /**
     * 删除
     */
    public R delete(Long[] ids) {
        skuFullReductionService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
