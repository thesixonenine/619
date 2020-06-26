package io.github.thesixonenine.coupon.controller.impl;

import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.coupon.controller.CouponHistoryController;
import io.github.thesixonenine.coupon.entity.CouponHistoryEntity;
import io.github.thesixonenine.coupon.service.CouponHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/06/26 12:39
 * @since 1.0
 */
@RestController
public class CouponHistoryControllerImpl implements CouponHistoryController {
    @Autowired
    private CouponHistoryService couponHistoryService;

    /**
     * 列表
     */
    public R list(Map<String, Object> params) {
        PageUtils page = couponHistoryService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    public R info(Long id) {
        CouponHistoryEntity couponHistory = couponHistoryService.getById(id);
        return R.ok().put("couponHistory", couponHistory);
    }

    /**
     * 保存
     */
    public R save(CouponHistoryEntity couponHistory) {
        couponHistoryService.save(couponHistory);
        return R.ok();
    }

    /**
     * 修改
     */
    public R update(CouponHistoryEntity couponHistory) {
        couponHistoryService.updateById(couponHistory);
        return R.ok();
    }

    /**
     * 删除
     */
    public R delete(Long[] ids) {
        couponHistoryService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }
}
