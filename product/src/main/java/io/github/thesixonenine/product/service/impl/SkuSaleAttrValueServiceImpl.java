package io.github.thesixonenine.product.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Objects;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.Query;

import io.github.thesixonenine.product.dao.SkuSaleAttrValueDao;
import io.github.thesixonenine.product.entity.SkuSaleAttrValueEntity;
import io.github.thesixonenine.product.service.SkuSaleAttrValueService;


@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<SkuSaleAttrValueEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Objects.nonNull(params.get("skuId")), "skuId", params.get("skuId"));
        IPage<SkuSaleAttrValueEntity> page = this.page(
                new Query<SkuSaleAttrValueEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

}