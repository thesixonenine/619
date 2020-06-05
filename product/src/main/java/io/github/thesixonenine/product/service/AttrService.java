package io.github.thesixonenine.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.product.entity.AttrEntity;

import java.util.Map;

/**
 * 商品属性
 *
 * @author thesixonenine
 * @date 2020-06-06 00:59:35
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

