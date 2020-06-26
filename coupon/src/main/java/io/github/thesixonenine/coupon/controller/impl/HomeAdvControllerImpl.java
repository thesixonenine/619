package io.github.thesixonenine.coupon.controller.impl;

import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.coupon.controller.HomeAdvController;
import io.github.thesixonenine.coupon.entity.HomeAdvEntity;
import io.github.thesixonenine.coupon.service.HomeAdvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;


/**
 * 首页轮播广告
 *
 * @author thesixonenine
 * @date 2020-06-06 01:15:48
 */
@RestController
public class HomeAdvControllerImpl implements HomeAdvController {
    @Autowired
    private HomeAdvService homeAdvService;

    /**
     * 列表
     */
    public R list(Map<String, Object> params) {
        PageUtils page = homeAdvService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    public R info(Long id) {
        HomeAdvEntity homeAdv = homeAdvService.getById(id);
        return R.ok().put("homeAdv", homeAdv);
    }

    /**
     * 保存
     */
    public R save(HomeAdvEntity homeAdv) {
        homeAdvService.save(homeAdv);
        return R.ok();
    }

    /**
     * 修改
     */
    public R update(HomeAdvEntity homeAdv) {
        homeAdvService.updateById(homeAdv);
        return R.ok();
    }

    /**
     * 删除
     */
    public R delete(Long[] ids) {
        homeAdvService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
