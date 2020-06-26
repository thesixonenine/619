package io.github.thesixonenine.coupon.controller.impl;

import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.coupon.controller.SeckillSkuRelationController;
import io.github.thesixonenine.coupon.entity.SeckillSkuRelationEntity;
import io.github.thesixonenine.coupon.service.SeckillSkuRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;


/**
 * 秒杀活动商品关联
 *
 * @author thesixonenine
 * @date 2020-06-06 01:15:48
 */
@RestController
public class SeckillSkuRelationControllerImpl implements SeckillSkuRelationController {
    @Autowired
    private SeckillSkuRelationService seckillSkuRelationService;

    /**
     * 列表
     */
    public R list(Map<String, Object> params) {
        PageUtils page = seckillSkuRelationService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    public R info(Long id) {
        SeckillSkuRelationEntity seckillSkuRelation = seckillSkuRelationService.getById(id);
        return R.ok().put("seckillSkuRelation", seckillSkuRelation);
    }

    /**
     * 保存
     */
    public R save(SeckillSkuRelationEntity seckillSkuRelation) {
        seckillSkuRelationService.save(seckillSkuRelation);
        return R.ok();
    }

    /**
     * 修改
     */
    public R update(SeckillSkuRelationEntity seckillSkuRelation) {
        seckillSkuRelationService.updateById(seckillSkuRelation);
        return R.ok();
    }

    /**
     * 删除
     */
    public R delete(Long[] ids) {
        seckillSkuRelationService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
