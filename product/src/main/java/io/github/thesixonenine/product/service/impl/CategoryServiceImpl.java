package io.github.thesixonenine.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.Query;
import io.github.thesixonenine.product.dao.CategoryDao;
import io.github.thesixonenine.product.dto.CategoryDTO;
import io.github.thesixonenine.product.entity.CategoryEntity;
import io.github.thesixonenine.product.service.CategoryService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryDTO> listTree() {
        // 查询所有的节点
        List<CategoryEntity> all = list();
        // 将对象转换成DTO
        List<CategoryDTO> allDTO = new ArrayList<>(all.size());
        all.forEach(t -> {
            CategoryDTO dto = new CategoryDTO();
            BeanUtils.copyProperties(t, dto);
            allDTO.add(dto);
        });
        // 对DTO筛选根节点数据
        List<CategoryDTO> rootDTO = allDTO.stream().filter(t -> t.getParentCid() == 0).collect(Collectors.toList());
        // 根节点数据排序, 循环设置子节点并返回
        return rootDTO.stream().sorted(Comparator.comparingInt(CategoryEntity::getSort))
                .peek(dto-> setChildren(dto, allDTO))
                .collect(Collectors.toList());
    }

    /**
     * 设置菜单的子菜单
     *
     * @param categoryDTO 当前菜单
     * @param allDTO      所有菜单
     */
    private void setChildren(CategoryDTO categoryDTO, List<CategoryDTO> allDTO) {
        Long catId = categoryDTO.getCatId();
        List<CategoryDTO> list = allDTO.stream()
                .filter(t -> t.getParentCid().equals(catId))
                .sorted(Comparator.comparingInt(CategoryEntity::getSort))
                .peek(dto-> setChildren(dto, allDTO))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(list)) {
            categoryDTO.setChildren(list);
        }
    }

}