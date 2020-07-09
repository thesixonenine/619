package io.github.thesixonenine.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.member.entity.MemberEntity;

import java.util.Map;

/**
 * 会员
 *
 * @author thesixonenine
 * @date 2020-06-06 01:37:14
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void register(String username, String password, String phone);

    MemberEntity login(String username, String password);
}

