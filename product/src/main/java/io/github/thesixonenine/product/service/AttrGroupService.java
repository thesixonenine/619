package io.github.thesixonenine.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.product.entity.AttrGroupEntity;

import java.util.Map;

/**
 * 属性分组
 *
 * @author thesixonenine
 * @date 2020-06-06 00:59:35
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

