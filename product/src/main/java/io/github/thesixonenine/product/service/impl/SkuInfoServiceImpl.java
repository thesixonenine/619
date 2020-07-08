package io.github.thesixonenine.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.Query;
import io.github.thesixonenine.product.dao.SkuInfoDao;
import io.github.thesixonenine.product.entity.*;
import io.github.thesixonenine.product.service.*;
import io.github.thesixonenine.product.vo.ItemVO;
import io.github.thesixonenine.product.vo.ItemVO.BaseAttrVO;
import io.github.thesixonenine.product.vo.ItemVO.ItemAttrGroupVO;
import io.github.thesixonenine.product.vo.ItemVO.ItemSaleAttrsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Autowired
    private SkuImagesService skuImagesService;
    @Autowired
    private SpuInfoDescService spuInfoDescService;
    @Autowired
    private AttrGroupService attrGroupService;
    @Autowired
    private ProductAttrValueService productAttrValueService;
    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;
    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public ItemVO item(Long skuId) {
        ItemVO vo = new ItemVO();
        // 1. sku基本信息 pms_sku_info
        SkuInfoEntity skuInfoEntity = getById(skuId);
        vo.setSkuInfoEntity(skuInfoEntity);
        // 2. sku图片信息 pms_sku_images
        List<SkuImagesEntity> skuImagesEntityList = skuImagesService.list(Wrappers.<SkuImagesEntity>lambdaQuery()
                .eq(SkuImagesEntity::getSkuId, skuId)
        );
        vo.setImages(skuImagesEntityList);
        Long spuId = skuInfoEntity.getSpuId();
        Long catalogId = skuInfoEntity.getCatalogId();
        // 3. spu销售属性
        // 查出该spuId下的所有skuId
        List<Long> skuIdList = list(Wrappers.<SkuInfoEntity>lambdaQuery().eq(SkuInfoEntity::getSpuId, spuId)).stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());
        // 查出该spuId下涉及到的所有销售属性
        List<SkuSaleAttrValueEntity> skuSaleAttrValueEntityList = skuSaleAttrValueService.list(Wrappers.<SkuSaleAttrValueEntity>lambdaQuery().in(SkuSaleAttrValueEntity::getSkuId, skuIdList));
        // 按attrId分组后包装返回
        Map<Long, List<SkuSaleAttrValueEntity>> listMap = skuSaleAttrValueEntityList.stream().collect(Collectors.groupingBy(SkuSaleAttrValueEntity::getAttrId));
        List<ItemSaleAttrsVO> saleAttrsList = new ArrayList<>(listMap.size());
        listMap.forEach((k, v) -> {
            ItemSaleAttrsVO itemSaleAttrsVO = new ItemSaleAttrsVO();
            itemSaleAttrsVO.setAttrId(k);
            itemSaleAttrsVO.setAttrName(v.get(0).getAttrName());
            itemSaleAttrsVO.setAttrValues(v.stream().map(SkuSaleAttrValueEntity::getAttrValue).filter(Objects::nonNull).distinct().collect(Collectors.toList()));
            saleAttrsList.add(itemSaleAttrsVO);
        });
        vo.setSaleAttrsList(saleAttrsList);
        // 4. spu介绍信息
        SpuInfoDescEntity spuInfoDescEntity = spuInfoDescService.getById(spuId);
        vo.setDesc(spuInfoDescEntity);
        // 5. spu规格参数
        List<ProductAttrValueEntity> productAttrValueEntityList = productAttrValueService.list(Wrappers.<ProductAttrValueEntity>lambdaQuery().eq(ProductAttrValueEntity::getSpuId, spuId));
        Map<Long, ProductAttrValueEntity> map = productAttrValueEntityList.stream().collect(Collectors.toMap(ProductAttrValueEntity::getAttrId, v -> v));
        List<Long> attrIdList = productAttrValueEntityList.stream().map(ProductAttrValueEntity::getAttrId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<AttrAttrgroupRelationEntity> relationList = attrAttrgroupRelationService.list(Wrappers.<AttrAttrgroupRelationEntity>lambdaQuery().in(AttrAttrgroupRelationEntity::getAttrId, attrIdList));
        List<Long> list = relationList.stream().map(AttrAttrgroupRelationEntity::getAttrGroupId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<AttrGroupEntity> attrGroupEntityList = attrGroupService.listByIds(list);
        List<ItemAttrGroupVO> attrGroups = new ArrayList<>();
        attrGroupEntityList.forEach(attrGroup -> {
            ItemAttrGroupVO attrGroupVO = new ItemAttrGroupVO();
            attrGroupVO.setGroupName(attrGroup.getAttrGroupName());
            List<Long> collect = relationList.stream().filter(t -> t.getAttrGroupId().equals(attrGroup.getAttrGroupId())).map(AttrAttrgroupRelationEntity::getAttrId).distinct().collect(Collectors.toList());
            List<BaseAttrVO> baseAttrVOList = new ArrayList<>();
            collect.forEach(t -> {
                ProductAttrValueEntity valueEntity = map.get(t);
                BaseAttrVO baseAttrVO = new BaseAttrVO();
                BeanUtils.copyProperties(valueEntity, baseAttrVO);
                baseAttrVOList.add(baseAttrVO);
            });
            attrGroupVO.setAttrs(baseAttrVOList);
            attrGroups.add(attrGroupVO);
        });
        vo.setAttrGroups(attrGroups);
        return vo;
    }

}