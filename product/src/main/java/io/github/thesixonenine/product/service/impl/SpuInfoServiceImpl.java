package io.github.thesixonenine.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.Query;
import io.github.thesixonenine.product.dao.SpuInfoDao;
import io.github.thesixonenine.product.dto.*;
import io.github.thesixonenine.product.entity.*;
import io.github.thesixonenine.product.service.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    private SpuInfoDescService spuInfoDescService;
    @Autowired
    private SpuImagesService spuImagesService;
    @Autowired
    private ProductAttrValueService productAttrValueService;
    @Autowired
    private AttrService attrService;
    @Autowired
    private SkuInfoService skuInfoService;
    @Autowired
    private SkuImagesService skuImagesService;
    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSpuInfo(SpuInfoDTO dto) {
        // 保存基本信息
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(dto, spuInfoEntity);
        save(spuInfoEntity);
        Long spuId = spuInfoEntity.getId();
        Long brandId = spuInfoEntity.getBrandId();
        Long catalogId = spuInfoEntity.getCatalogId();

        // 保存描述图片
        String decript = dto.getDecript().stream().filter(Objects::nonNull).distinct().collect(Collectors.joining(","));
        if (StringUtils.isNotBlank(decript)) {
            SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
            spuInfoDescEntity.setDecript(decript);
            spuInfoDescEntity.setSpuId(spuId);
            spuInfoDescService.save(spuInfoDescEntity);
        }

        // 保存图片集
        List<SpuImagesEntity> spuImagesEntityList = dto.getImages().stream().filter(Objects::nonNull).distinct().map(t -> {
            SpuImagesEntity spuImagesEntity = new SpuImagesEntity();
            spuImagesEntity.setSpuId(spuId);
            spuImagesEntity.setImgUrl(t);
            return spuImagesEntity;
        }).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(spuImagesEntityList)) {
            spuImagesService.saveBatch(spuImagesEntityList);
        }

        // 保存spu的规格参数
        List<BaseAttrs> attrsList = dto.getBaseAttrs();
        if (CollectionUtils.isNotEmpty(attrsList)) {
            List<Long> attrIdList = attrsList.stream().map(BaseAttrs::getAttrId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            Map<Long, AttrEntity> attrEntityMap = new HashMap<>(attrIdList.size());
            if (CollectionUtils.isNotEmpty(attrIdList)) {
                attrService.listByIds(attrIdList).stream().collect(Collectors.toMap(AttrEntity::getAttrId, v -> v)).forEach(attrEntityMap::put);

            }
            List<ProductAttrValueEntity> attrValueEntityList = attrsList.stream().filter(Objects::nonNull).map(t -> {
                ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
                productAttrValueEntity.setAttrId(t.getAttrId());
                AttrEntity attrEntity = attrEntityMap.get(t.getAttrId());
                if (Objects.nonNull(attrEntity)) {
                    productAttrValueEntity.setAttrName(attrEntity.getAttrName());
                }
                productAttrValueEntity.setAttrValue(t.getAttrValues());
                productAttrValueEntity.setQuickShow(t.getShowDesc());
                productAttrValueEntity.setSpuId(spuId);
                return productAttrValueEntity;
            }).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(attrValueEntityList)) {
                productAttrValueService.saveBatch(attrValueEntityList);
            }
        }

        // 保存sku的信息

        List<Skus> skuList = dto.getSkus();
        if (CollectionUtils.isNotEmpty(skuList)) {
            skuList.stream().filter(Objects::nonNull).forEach(sku -> {
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(sku, skuInfoEntity);
                skuInfoEntity.setBrandId(brandId);
                skuInfoEntity.setCatalogId(catalogId);
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSpuId(spuId);
                List<ImagesBean> images = sku.getImages();
                if (CollectionUtils.isNotEmpty(images)) {
                    images.stream().filter(Objects::nonNull).filter(t -> Objects.nonNull(t.getDefaultImg())).filter(t -> t.getDefaultImg() == 1).findFirst().ifPresent(t -> {
                        skuInfoEntity.setSkuDefaultImg(t.getImgUrl());
                    });

                }
                // 只有保存sku成功后才能保存其他信息
                skuInfoService.save(skuInfoEntity);
                Long skuId = skuInfoEntity.getSkuId();

                List<SkuImagesEntity> skuImagesEntityList = images.stream().filter(Objects::nonNull).map(t -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setImgUrl(t.getImgUrl());
                    skuImagesEntity.setDefaultImg(t.getDefaultImg());
                    return skuImagesEntity;
                }).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(skuImagesEntityList)) {
                    skuImagesService.saveBatch(skuImagesEntityList);
                }

                List<Attr> attrList = sku.getAttr();
                if (CollectionUtils.isNotEmpty(attrList)) {
                    List<SkuSaleAttrValueEntity> attrValueEntityList = attrList.stream().filter(Objects::nonNull).map(t -> {
                        SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                        BeanUtils.copyProperties(t, skuSaleAttrValueEntity);
                        skuSaleAttrValueEntity.setSkuId(skuId);
                        return skuSaleAttrValueEntity;
                    }).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(attrValueEntityList)) {
                        skuSaleAttrValueService.saveBatch(attrValueEntityList);
                    }
                }

                // TODO 远程服务调用保存优惠信息
            });

        }
    }

}