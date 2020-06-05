package io.github.thesixonenine.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.member.entity.MemberLevelEntity;

import java.util.Map;

/**
 * 会员等级
 *
 * @author thesixonenine
 * @date 2020-06-06 01:37:14
 */
public interface MemberLevelService extends IService<MemberLevelEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

