package io.github.thesixonenine.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.product.entity.AttrAttrgroupRelationEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author thesixonenine
 * @date 2020-06-06 00:59:35
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 删除关联关系
     *
     * @param relation relation List
     */
    @Transactional
    void deleteAttrRelation(List<AttrAttrgroupRelationEntity> relation);
}

