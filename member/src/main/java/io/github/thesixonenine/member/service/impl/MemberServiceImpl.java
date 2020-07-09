package io.github.thesixonenine.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.Query;
import io.github.thesixonenine.member.dao.MemberDao;
import io.github.thesixonenine.member.entity.MemberEntity;
import io.github.thesixonenine.member.entity.MemberLevelEntity;
import io.github.thesixonenine.member.service.MemberLevelService;
import io.github.thesixonenine.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Autowired
    private MemberLevelService memberLevelService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void register(String username, String password, String phone) {
        if (count(Wrappers.<MemberEntity>lambdaQuery().eq(MemberEntity::getUsername, username)) > 0) {
            throw new RuntimeException("用户名已存在");
        }
        if (count(Wrappers.<MemberEntity>lambdaQuery().eq(MemberEntity::getMobile, phone)) > 0) {
            throw new RuntimeException("手机号已存在");
        }
        MemberEntity member = new MemberEntity();
        member.setUsername(username);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        passwordEncoder.encode("");
        passwordEncoder.matches("", "");

        member.setPassword(password);
        member.setMobile(phone);
        member.setCreateTime(LocalDateTime.now());
        member.setLevelId(memberLevelService.getOne(Wrappers.<MemberLevelEntity>lambdaQuery().eq(MemberLevelEntity::getDefaultStatus, MemberLevelEntity.defaultStatus.IS_DEFAULT.getCode())).getId());
        save(member);
    }

}