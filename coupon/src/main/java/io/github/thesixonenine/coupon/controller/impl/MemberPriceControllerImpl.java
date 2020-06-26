package io.github.thesixonenine.coupon.controller.impl;

import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.coupon.controller.MemberPriceController;
import io.github.thesixonenine.coupon.entity.MemberPriceEntity;
import io.github.thesixonenine.coupon.service.MemberPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 商品会员价格
 *
 * @author thesixonenine
 * @date 2020-06-06 01:15:48
 */
@RestController
public class MemberPriceControllerImpl implements MemberPriceController {
    @Autowired
    private MemberPriceService memberPriceService;

    /**
     * 列表
     */
    public R list(Map<String, Object> params) {
        PageUtils page = memberPriceService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    public R info(Long id) {
        MemberPriceEntity memberPrice = memberPriceService.getById(id);
        return R.ok().put("memberPrice", memberPrice);
    }

    /**
     * 保存
     */
    public R save(MemberPriceEntity memberPrice) {
        memberPriceService.save(memberPrice);
        return R.ok();
    }

    /**
     * 批量保存
     */
    @Override
    public R saveBatch(List<MemberPriceEntity> memberPriceList) {
        memberPriceService.saveBatch(memberPriceList);
        return R.ok();
    }

    /**
     * 修改
     */
    public R update(MemberPriceEntity memberPrice) {
        memberPriceService.updateById(memberPrice);
        return R.ok();
    }

    /**
     * 删除
     */
    public R delete(Long[] ids) {
        memberPriceService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
