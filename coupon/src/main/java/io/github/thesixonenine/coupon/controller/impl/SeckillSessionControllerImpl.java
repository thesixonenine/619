package io.github.thesixonenine.coupon.controller.impl;

import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.coupon.controller.SeckillSessionController;
import io.github.thesixonenine.coupon.entity.SeckillSessionEntity;
import io.github.thesixonenine.coupon.service.SeckillSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;


/**
 * 秒杀活动场次
 *
 * @author thesixonenine
 * @date 2020-06-06 01:15:48
 */
@RestController
public class SeckillSessionControllerImpl implements SeckillSessionController {
    @Autowired
    private SeckillSessionService seckillSessionService;

    /**
     * 列表
     */
    public R list(Map<String, Object> params) {
        PageUtils page = seckillSessionService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    public R info(Long id) {
        SeckillSessionEntity seckillSession = seckillSessionService.getById(id);
        return R.ok().put("seckillSession", seckillSession);
    }

    /**
     * 保存
     */
    public R save(SeckillSessionEntity seckillSession) {
        seckillSessionService.save(seckillSession);
        return R.ok();
    }

    /**
     * 修改
     */
    public R update(SeckillSessionEntity seckillSession) {
        seckillSessionService.updateById(seckillSession);
        return R.ok();
    }

    /**
     * 删除
     */
    public R delete(Long[] ids) {
        seckillSessionService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
