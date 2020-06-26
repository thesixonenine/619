package io.github.thesixonenine.coupon.controller.impl;

import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.coupon.controller.HomeSubjectSpuController;
import io.github.thesixonenine.coupon.entity.HomeSubjectSpuEntity;
import io.github.thesixonenine.coupon.service.HomeSubjectSpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;


/**
 * 专题商品
 *
 * @author thesixonenine
 * @date 2020-06-06 01:15:48
 */
@RestController
public class HomeSubjectSpuControllerImpl implements HomeSubjectSpuController {
    @Autowired
    private HomeSubjectSpuService homeSubjectSpuService;

    /**
     * 列表
     */
    public R list(Map<String, Object> params) {
        PageUtils page = homeSubjectSpuService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    public R info(Long id) {
        HomeSubjectSpuEntity homeSubjectSpu = homeSubjectSpuService.getById(id);
        return R.ok().put("homeSubjectSpu", homeSubjectSpu);
    }

    /**
     * 保存
     */
    public R save(HomeSubjectSpuEntity homeSubjectSpu) {
        homeSubjectSpuService.save(homeSubjectSpu);
        return R.ok();
    }

    /**
     * 修改
     */
    public R update(HomeSubjectSpuEntity homeSubjectSpu) {
        homeSubjectSpuService.updateById(homeSubjectSpu);
        return R.ok();
    }

    /**
     * 删除
     */
    public R delete(Long[] ids) {
        homeSubjectSpuService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
