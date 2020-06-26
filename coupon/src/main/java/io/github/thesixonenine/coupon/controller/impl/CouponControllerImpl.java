package io.github.thesixonenine.coupon.controller.impl;

import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.coupon.controller.CouponController;
import io.github.thesixonenine.coupon.entity.CouponEntity;
import io.github.thesixonenine.coupon.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/06/26 0:34
 * @since 1.0
 */
@RestController
public class CouponControllerImpl implements CouponController {
    @Autowired
    private CouponService couponService;

    /**
     * 列表
     */
    public R list(Map<String, Object> params) {
        PageUtils page = couponService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    public R info(Long id) {
        CouponEntity coupon = couponService.getById(id);
        return R.ok().put("coupon", coupon);
    }

    /**
     * 保存
     */
    public R save(CouponEntity coupon) {
        couponService.save(coupon);
        return R.ok();
    }

    /**
     * 修改
     */
    public R update(CouponEntity coupon) {
        couponService.updateById(coupon);
        return R.ok();
    }

    /**
     * 删除
     */
    public R delete(Long[] ids) {
        couponService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }
}
