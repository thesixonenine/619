package io.github.thesixonenine.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.product.entity.BrandEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 品牌
 *
 * @author thesixonenine
 * @date 2020-06-06 00:59:35
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 更新品牌的同时更新冗余信息
     *
     * @param brand 当前品牌
     */
    @Transactional
    void updateWithCategory(BrandEntity brand);
}

