package io.github.thesixonenine.coupon.controller.impl;

import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.coupon.controller.SkuLadderController;
import io.github.thesixonenine.coupon.entity.SkuLadderEntity;
import io.github.thesixonenine.coupon.service.SkuLadderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;


/**
 * 商品阶梯价格
 *
 * @author thesixonenine
 * @date 2020-06-06 01:15:48
 */
@RestController
public class SkuLadderControllerImpl implements SkuLadderController {
    @Autowired
    private SkuLadderService skuLadderService;

    /**
     * 列表
     */
    public R list(Map<String, Object> params) {
        PageUtils page = skuLadderService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    public R info(Long id) {
        SkuLadderEntity skuLadder = skuLadderService.getById(id);
        return R.ok().put("skuLadder", skuLadder);
    }

    /**
     * 保存
     */
    public R save(SkuLadderEntity skuLadder) {
        skuLadderService.save(skuLadder);
        return R.ok();
    }

    /**
     * 修改
     */
    public R update(SkuLadderEntity skuLadder) {
        skuLadderService.updateById(skuLadder);
        return R.ok();
    }

    /**
     * 删除
     */
    public R delete(Long[] ids) {
        skuLadderService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
