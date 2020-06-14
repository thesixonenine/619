package io.github.thesixonenine.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.product.entity.AttrEntity;
import io.github.thesixonenine.product.entity.AttrEntity.AttrType;
import io.github.thesixonenine.product.vo.AttrVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author thesixonenine
 * @date 2020-06-06 00:59:35
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);


    /**
     * 查询分页
     *
     * @param params    分页参数
     * @param catelogId 节点id
     * @param attrType  属性类型
     * @return 分页数据
     */
    PageUtils queryPageWithType(Map<String, Object> params, Long catelogId, AttrType attrType);

    /**
     * 保存attr及与分组的关联关系
     *
     * @param attr 规格参数
     */
    @Transactional
    void save(AttrVO attr);

    /**
     * 查询详细信息
     *
     * @param attrId 属性id
     * @return 详细信息
     */
    AttrVO info(Long attrId);

    /**
     * 更新attr及与分组的关联关系
     *
     * @param attr 规格参数
     */
    @Transactional
    void update(AttrVO attr);

    /**
     * 获取分组id下的所有attr
     *
     * @param attrGroupId 分组id
     * @return 该分组下的所有attr
     */
    List<AttrEntity> getRelationAttr(Long attrGroupId);

    /**
     * 获取分组id下的所有attr
     *
     * @param attrGroupIds 分组id列表
     * @return 该分组下的所有attr
     */
    Map<Long, List<AttrEntity>> getRelationAttrBatch(List<Long> attrGroupIds);

    /**
     * 获取分组下没有关联的属性
     *
     * @param params      分页参数
     * @param attrGroupId 分组id
     * @return 分页数据
     */
    PageUtils listNoAttrRelation(Map<String, Object> params, Long attrGroupId);
}

