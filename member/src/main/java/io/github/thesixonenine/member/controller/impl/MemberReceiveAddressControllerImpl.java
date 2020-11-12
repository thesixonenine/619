package io.github.thesixonenine.member.controller.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

// import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.github.thesixonenine.member.controller.MemberReceiveAddressController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.thesixonenine.member.entity.MemberReceiveAddressEntity;
import io.github.thesixonenine.member.service.MemberReceiveAddressService;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.R;



/**
 * 会员收货地址
 *
 * @author thesixonenine
 * @date 2020-06-06 01:37:14
 */
@RestController
public class MemberReceiveAddressControllerImpl implements MemberReceiveAddressController {
    @Autowired
    private MemberReceiveAddressService memberReceiveAddressService;

    @Override
    public List<MemberReceiveAddressEntity> getByMemberId(Long memberId) {
        return memberReceiveAddressService.list(Wrappers.<MemberReceiveAddressEntity>lambdaQuery().eq(MemberReceiveAddressEntity::getMemberId, memberId));
    }

    @Override
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberReceiveAddressService.queryPage(params);
        return R.ok().put("page", page);
    }

    @Override
    public R info(@PathVariable("id") Long id){
		MemberReceiveAddressEntity memberReceiveAddress = memberReceiveAddressService.getById(id);
        return R.ok().put("memberReceiveAddress", memberReceiveAddress);
    }

    @Override
    public R save(@RequestBody MemberReceiveAddressEntity memberReceiveAddress){
		memberReceiveAddressService.save(memberReceiveAddress);
        return R.ok();
    }

    @Override
    public R update(@RequestBody MemberReceiveAddressEntity memberReceiveAddress){
		memberReceiveAddressService.updateById(memberReceiveAddress);
        return R.ok();
    }

    @Override
    public R delete(@RequestBody Long[] ids){
		memberReceiveAddressService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }


}
