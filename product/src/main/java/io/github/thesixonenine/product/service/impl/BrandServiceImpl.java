package io.github.thesixonenine.product.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.github.thesixonenine.product.entity.CategoryBrandRelationEntity;
import io.github.thesixonenine.product.service.CategoryBrandRelationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.Query;

import io.github.thesixonenine.product.dao.BrandDao;
import io.github.thesixonenine.product.entity.BrandEntity;
import io.github.thesixonenine.product.service.BrandService;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                new QueryWrapper<BrandEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void updateWithCategory(BrandEntity brand) {
        updateById(brand);
        // 更新冗余字段
        if (StringUtils.isNotBlank(brand.getName())) {
            categoryBrandRelationService.update(CategoryBrandRelationEntity.builder().brandName(brand.getName()).build(),
                    Wrappers.<CategoryBrandRelationEntity>lambdaUpdate().eq(CategoryBrandRelationEntity::getBrandId, brand.getBrandId()));
        }
    }

}