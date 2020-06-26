package io.github.thesixonenine.coupon.controller;

import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.coupon.entity.HomeSubjectEntity;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


/**
 * 首页专题表【jd首页下面很多专题，每个专题链接新的页面，展示专题商品信息】
 *
 * @author thesixonenine
 * @date 2020-06-06 01:15:48
 */
@Api(value = "首页专题表")
@FeignClient(name = "coupon")
public interface HomeSubjectController {
    /**
     * 列表
     */
    @RequestMapping("/coupon/homesubject/list")
    R list(@RequestParam Map<String, Object> params);


    /**
     * 信息
     */
    @RequestMapping("/coupon/homesubject/info/{id}")
    R info(@PathVariable("id") Long id);

    /**
     * 保存
     */
    @RequestMapping("/coupon/homesubject/save")
    R save(@RequestBody HomeSubjectEntity homeSubject);

    /**
     * 修改
     */
    @RequestMapping("/coupon/homesubject/update")
    R update(@RequestBody HomeSubjectEntity homeSubject);

    /**
     * 删除
     */
    @RequestMapping("/coupon/homesubject/delete")
    R delete(@RequestBody Long[] ids);

}
