package io.github.thesixonenine.member.controller;

import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.member.entity.MemberReceiveAddressEntity;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/11/12 22:53
 * @since 1.0
 */
@Api(value = "会员收货地址")
@FeignClient(name = "member")
public interface MemberReceiveAddressController {

    @RequestMapping(value = "member/memberreceiveaddress/getByMemberId")
    List<MemberReceiveAddressEntity> getByMemberId(@RequestParam(value = "memberId")Long memberId);

    /**
     * 列表
     */
    @RequestMapping(value = "member/memberreceiveaddress/list")
    R list(@RequestParam Map<String, Object> params);

    /**
     * 信息
     */
    @RequestMapping(value = "member/memberreceiveaddress/info/{id}")
    R info(@PathVariable("id") Long id);

    /**
     * 保存
     */
    @RequestMapping(value = "member/memberreceiveaddress/save")
    R save(@RequestBody MemberReceiveAddressEntity memberReceiveAddress);


    /**
     * 修改
     */
    @RequestMapping(value = "member/memberreceiveaddress/update")
    R update(@RequestBody MemberReceiveAddressEntity memberReceiveAddress);

    /**
     * 删除
     */
    @RequestMapping(value = "member/memberreceiveaddress/delete")
    R delete(@RequestBody Long[] ids);

    @GetMapping(value = "member/memberreceiveaddress/getById/{id}")
    MemberReceiveAddressEntity getById(@PathVariable("id") Long id);
}
