package io.github.thesixonenine.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.product.entity.SkuImagesEntity;

import java.util.Map;

/**
 * sku图片
 *
 * @author thesixonenine
 * @date 2020-06-06 00:59:35
 */
public interface SkuImagesService extends IService<SkuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

