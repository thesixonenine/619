package io.github.thesixonenine.coupon.controller.impl;

import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.coupon.controller.CouponSpuRelationController;
import io.github.thesixonenine.coupon.entity.CouponSpuRelationEntity;
import io.github.thesixonenine.coupon.service.CouponSpuRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;


/**
 * 优惠券与产品关联
 *
 * @author thesixonenine
 * @date 2020-06-06 01:15:48
 */
@RestController
public class CouponSpuRelationControllerImpl implements CouponSpuRelationController {
    @Autowired
    private CouponSpuRelationService couponSpuRelationService;

    /**
     * 列表
     */
    public R list(Map<String, Object> params) {
        PageUtils page = couponSpuRelationService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    public R info(Long id) {
        CouponSpuRelationEntity couponSpuRelation = couponSpuRelationService.getById(id);
        return R.ok().put("couponSpuRelation", couponSpuRelation);
    }

    /**
     * 保存
     */
    public R save(CouponSpuRelationEntity couponSpuRelation) {
        couponSpuRelationService.save(couponSpuRelation);
        return R.ok();
    }

    /**
     * 修改
     */
    public R update(CouponSpuRelationEntity couponSpuRelation) {
        couponSpuRelationService.updateById(couponSpuRelation);
        return R.ok();
    }

    /**
     * 删除
     */
    public R delete(Long[] ids) {
        couponSpuRelationService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
