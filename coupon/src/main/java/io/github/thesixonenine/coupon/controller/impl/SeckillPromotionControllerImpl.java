package io.github.thesixonenine.coupon.controller.impl;

import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.coupon.controller.SeckillPromotionController;
import io.github.thesixonenine.coupon.entity.SeckillPromotionEntity;
import io.github.thesixonenine.coupon.service.SeckillPromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;


/**
 * 秒杀活动
 *
 * @author thesixonenine
 * @date 2020-06-06 01:15:48
 */
@RestController
public class SeckillPromotionControllerImpl implements SeckillPromotionController {
    @Autowired
    private SeckillPromotionService seckillPromotionService;

    /**
     * 列表
     */
    public R list(Map<String, Object> params) {
        PageUtils page = seckillPromotionService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    public R info(Long id) {
        SeckillPromotionEntity seckillPromotion = seckillPromotionService.getById(id);
        return R.ok().put("seckillPromotion", seckillPromotion);
    }

    /**
     * 保存
     */
    public R save(SeckillPromotionEntity seckillPromotion) {
        seckillPromotionService.save(seckillPromotion);
        return R.ok();
    }

    /**
     * 修改
     */
    public R update(SeckillPromotionEntity seckillPromotion) {
        seckillPromotionService.updateById(seckillPromotion);
        return R.ok();
    }

    /**
     * 删除
     */
    public R delete(Long[] ids) {
        seckillPromotionService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
