package io.github.thesixonenine.coupon.controller;

import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.coupon.entity.SeckillSkuNoticeEntity;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


/**
 * 秒杀商品通知订阅
 *
 * @author thesixonenine
 * @date 2020-06-06 01:15:48
 */
@Api(value = "秒杀商品通知订阅")
@FeignClient(name = "coupon")
public interface SeckillSkuNoticeController {

    /**
     * 列表
     */
    @RequestMapping("/coupon/seckillskunotice/list")
    R list(@RequestParam Map<String, Object> params);


    /**
     * 信息
     */
    @RequestMapping("/coupon/seckillskunotice/info/{id}")
    R info(@PathVariable("id") Long id);

    /**
     * 保存
     */
    @RequestMapping("/coupon/seckillskunotice/save")
    R save(@RequestBody SeckillSkuNoticeEntity seckillSkuNotice);

    /**
     * 修改
     */
    @RequestMapping("/coupon/seckillskunotice/update")
    R update(@RequestBody SeckillSkuNoticeEntity seckillSkuNotice);

    /**
     * 删除
     */
    @RequestMapping("/coupon/seckillskunotice/delete")
    R delete(@RequestBody Long[] ids);

}
