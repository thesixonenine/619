package io.github.thesixonenine.member.controller.impl;

import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.coupon.controller.CouponController;
import io.github.thesixonenine.member.controller.MemberController;
import io.github.thesixonenine.member.entity.MemberEntity;
import io.github.thesixonenine.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/06/26 1:05
 * @since 1.0
 */
@RestController
public class MemberControllerImpl implements MemberController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private CouponController couponController;

    /**
     * 列表
     */
    @Override
    public R list(Map<String, Object> params) {
        R r = couponController.list(params);
        System.out.println(r);
        Object o = r.get("page");
        System.out.println(o);
        PageUtils page = memberService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @Override
    public R info(Long id) {
        MemberEntity member = memberService.getById(id);
        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @Override
    public R save(MemberEntity member) {
        memberService.save(member);
        return R.ok();
    }

    /**
     * 修改
     */
    @Override
    public R update(MemberEntity member) {
        memberService.updateById(member);
        return R.ok();
    }

    /**
     * 删除
     */
    @Override
    public R delete(Long[] ids) {
        memberService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

    @Override
    public R register(String username, String password, String phone) {
        try {
            memberService.register(username, password, phone);
        } catch (Exception e) {
            R.error(10001, e.getMessage());
        }
        return R.ok();
    }

    @Override
    public R login(String username, String password) {
        MemberEntity member = memberService.login(username, password);
        if (Objects.isNull(member)) {
            return R.error(10001, "登录失败, 用户名或密码错误");
        }
        return R.ok().setData(member);
    }

    @Override
    public MemberEntity getById(Long id) {
        return memberService.getById(id);
    }
}
