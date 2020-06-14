package io.github.thesixonenine.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.product.dto.CategoryDTO;
import io.github.thesixonenine.product.entity.CategoryEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author thesixonenine
 * @date 2020-06-06 00:59:35
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询所有节点树
     * @return 节点树信息
     */
    List<CategoryDTO> listTree();

    /**
     * 查询节点的所有父节点id
     * @param catelogId 当前节点
     * @param allCidList 所有节点的集合
     * @return 所有节点的Id集合
     */
    List<Long> findParentCid(Long catelogId, List<Long> allCidList);

    /**
     * 更新节点信息的同时, 把其他表中的冗余信息也一并更新
     * @param category 当前节点
     */
    @Transactional
    void updateWithBrand(CategoryEntity category);
}

