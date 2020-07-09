package io.github.thesixonenine.member.controller;

import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.member.entity.MemberEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * 会员
 *
 * @author thesixonenine
 * @date 2020-06-06 01:37:14
 */
@Api(value = "会员")
@FeignClient(name = "member")
public interface MemberController {
    /**
     * 列表
     */
    @ApiOperation(value = "列表")
    @RequestMapping("/member/member/list")
    R list(@RequestParam Map<String, Object> params);

    /**
     * 信息
     */
    @RequestMapping("/member/member/info/{id}")
    R info(@PathVariable("id") Long id);

    /**
     * 保存
     */
    @RequestMapping("/member/member/save")
    R save(@RequestBody MemberEntity member);

    /**
     * 修改
     */
    @RequestMapping("/member/member/update")
    R update(@RequestBody MemberEntity member);

    /**
     * 删除
     */
    @RequestMapping("/member/member/delete")
    R delete(@RequestBody Long[] ids);

    /**
     * 用户注册
     *
     * @return 通用返回
     */
    @GetMapping(value = "/member/member/register")
    R register(@RequestParam("username") String username,
               @RequestParam("password") String password,
               @RequestParam("phone") String phone);
}
