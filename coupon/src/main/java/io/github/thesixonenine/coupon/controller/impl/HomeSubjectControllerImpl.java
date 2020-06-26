package io.github.thesixonenine.coupon.controller.impl;

import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.coupon.controller.HomeSubjectController;
import io.github.thesixonenine.coupon.entity.HomeSubjectEntity;
import io.github.thesixonenine.coupon.service.HomeSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;


/**
 * 首页专题表【jd首页下面很多专题，每个专题链接新的页面，展示专题商品信息】
 *
 * @author thesixonenine
 * @date 2020-06-06 01:15:48
 */
@RestController
public class HomeSubjectControllerImpl implements HomeSubjectController {
    @Autowired
    private HomeSubjectService homeSubjectService;

    /**
     * 列表
     */
    public R list(Map<String, Object> params) {
        PageUtils page = homeSubjectService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    public R info(Long id) {
        HomeSubjectEntity homeSubject = homeSubjectService.getById(id);
        return R.ok().put("homeSubject", homeSubject);
    }

    /**
     * 保存
     */
    public R save(HomeSubjectEntity homeSubject) {
        homeSubjectService.save(homeSubject);
        return R.ok();
    }

    /**
     * 修改
     */
    public R update(HomeSubjectEntity homeSubject) {
        homeSubjectService.updateById(homeSubject);
        return R.ok();
    }

    /**
     * 删除
     */
    public R delete(Long[] ids) {
        homeSubjectService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
