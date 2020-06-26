package io.github.thesixonenine.coupon.controller.impl;

import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.coupon.controller.SeckillSkuNoticeController;
import io.github.thesixonenine.coupon.entity.SeckillSkuNoticeEntity;
import io.github.thesixonenine.coupon.service.SeckillSkuNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;


/**
 * 秒杀商品通知订阅
 *
 * @author thesixonenine
 * @date 2020-06-06 01:15:48
 */
@RestController
public class SeckillSkuNoticeControllerImpl implements SeckillSkuNoticeController {
    @Autowired
    private SeckillSkuNoticeService seckillSkuNoticeService;

    /**
     * 列表
     */
    public R list(Map<String, Object> params) {
        PageUtils page = seckillSkuNoticeService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    public R info(Long id) {
        SeckillSkuNoticeEntity seckillSkuNotice = seckillSkuNoticeService.getById(id);
        return R.ok().put("seckillSkuNotice", seckillSkuNotice);
    }

    /**
     * 保存
     */
    public R save(SeckillSkuNoticeEntity seckillSkuNotice) {
        seckillSkuNoticeService.save(seckillSkuNotice);
        return R.ok();
    }

    /**
     * 修改
     */
    public R update(SeckillSkuNoticeEntity seckillSkuNotice) {
        seckillSkuNoticeService.updateById(seckillSkuNotice);
        return R.ok();
    }

    /**
     * 删除
     */
    public R delete(Long[] ids) {
        seckillSkuNoticeService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
