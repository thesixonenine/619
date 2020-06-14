package io.github.thesixonenine.product.dao;

import io.github.thesixonenine.product.entity.AttrAttrgroupRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性&属性分组关联
 * 
 * @author thesixonenine
 * @date 2020-06-06 00:59:35
 */
public interface AttrAttrgroupRelationDao extends BaseMapper<AttrAttrgroupRelationEntity> {
    void deleteAttrRelation(@Param(value = "relation") List<AttrAttrgroupRelationEntity> relation);
}
