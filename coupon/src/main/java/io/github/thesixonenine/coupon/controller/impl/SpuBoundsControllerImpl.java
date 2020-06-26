package io.github.thesixonenine.coupon.controller.impl;

import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.coupon.controller.SpuBoundsController;
import io.github.thesixonenine.coupon.entity.SpuBoundsEntity;
import io.github.thesixonenine.coupon.service.SpuBoundsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;


/**
 * 商品spu积分设置
 *
 * @author thesixonenine
 * @date 2020-06-06 01:15:48
 */
@RestController
public class SpuBoundsControllerImpl implements SpuBoundsController {
    @Autowired
    private SpuBoundsService spuBoundsService;

    /**
     * 列表
     */
    public R list(Map<String, Object> params) {
        PageUtils page = spuBoundsService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    public R info(Long id) {
        SpuBoundsEntity spuBounds = spuBoundsService.getById(id);
        return R.ok().put("spuBounds", spuBounds);
    }

    /**
     * 保存
     */
    public R save(SpuBoundsEntity spuBounds) {
        spuBoundsService.save(spuBounds);
        return R.ok();
    }

    /**
     * 修改
     */
    public R update(SpuBoundsEntity spuBounds) {
        spuBoundsService.updateById(spuBounds);
        return R.ok();
    }

    /**
     * 删除
     */
    public R delete(Long[] ids) {
        spuBoundsService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
