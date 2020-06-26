package io.github.thesixonenine.coupon.controller.impl;

import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.coupon.controller.CouponSpuCategoryRelationController;
import io.github.thesixonenine.coupon.entity.CouponSpuCategoryRelationEntity;
import io.github.thesixonenine.coupon.service.CouponSpuCategoryRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/06/26 12:44
 * @since 1.0
 */
@RestController
public class CouponSpuCategoryRelationControllerImpl implements CouponSpuCategoryRelationController {
    @Autowired
    private CouponSpuCategoryRelationService couponSpuCategoryRelationService;

    /**
     * 列表
     */
    public R list(Map<String, Object> params) {
        PageUtils page = couponSpuCategoryRelationService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    public R info(Long id) {
        CouponSpuCategoryRelationEntity couponSpuCategoryRelation = couponSpuCategoryRelationService.getById(id);
        return R.ok().put("couponSpuCategoryRelation", couponSpuCategoryRelation);
    }

    /**
     * 保存
     */
    public R save(CouponSpuCategoryRelationEntity couponSpuCategoryRelation) {
        couponSpuCategoryRelationService.save(couponSpuCategoryRelation);
        return R.ok();
    }

    /**
     * 修改
     */
    public R update(CouponSpuCategoryRelationEntity couponSpuCategoryRelation) {
        couponSpuCategoryRelationService.updateById(couponSpuCategoryRelation);
        return R.ok();
    }

    /**
     * 删除
     */
    public R delete(Long[] ids) {
        couponSpuCategoryRelationService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }
}
