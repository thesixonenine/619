package io.github.thesixonenine.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.product.entity.SpuCommentEntity;

import java.util.Map;

/**
 * 商品评价
 *
 * @author thesixonenine
 * @date 2020-06-06 00:59:35
 */
public interface SpuCommentService extends IService<SpuCommentEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

