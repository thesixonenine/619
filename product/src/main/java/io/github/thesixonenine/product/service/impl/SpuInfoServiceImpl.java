package io.github.thesixonenine.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.reflect.TypeToken;
import io.github.thesixonenine.common.es.SkuModel;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.Query;
import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.coupon.controller.MemberPriceController;
import io.github.thesixonenine.coupon.controller.SkuFullReductionController;
import io.github.thesixonenine.coupon.controller.SkuLadderController;
import io.github.thesixonenine.coupon.controller.SpuBoundsController;
import io.github.thesixonenine.coupon.entity.MemberPriceEntity;
import io.github.thesixonenine.coupon.entity.SkuFullReductionEntity;
import io.github.thesixonenine.coupon.entity.SkuLadderEntity;
import io.github.thesixonenine.coupon.entity.SpuBoundsEntity;
import io.github.thesixonenine.product.dao.SpuInfoDao;
import io.github.thesixonenine.product.dto.*;
import io.github.thesixonenine.product.entity.*;
import io.github.thesixonenine.product.service.*;
import io.github.thesixonenine.search.controller.ElasticSearchController;
import io.github.thesixonenine.ware.controller.WareSkuController;
import io.github.thesixonenine.ware.entity.WareSkuEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
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
    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SpuBoundsController spuBoundsController;
    @Autowired
    private SkuFullReductionController skuFullReductionController;
    @Autowired
    private SkuLadderController skuLadderController;
    @Autowired
    private MemberPriceController memberPriceController;
    @Autowired
    private WareSkuController wareSkuController;
    @Autowired
    private ElasticSearchController elasticSearchController;

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

        Bounds bounds = dto.getBounds();
        if (Objects.nonNull(bounds)) {
            SpuBoundsEntity spuBoundsEntity = new SpuBoundsEntity();
            BeanUtils.copyProperties(bounds, spuBoundsEntity);
            spuBoundsEntity.setSpuId(spuId);
            spuBoundsController.save(spuBoundsEntity);
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
                List<Images> images = sku.getImages();
                if (CollectionUtils.isNotEmpty(images)) {
                    images.stream().filter(Objects::nonNull).filter(t -> Objects.nonNull(t.getDefaultImg())).filter(t -> t.getDefaultImg() == 1).findFirst().ifPresent(t -> {
                        skuInfoEntity.setSkuDefaultImg(t.getImgUrl());
                    });

                }
                // 只有保存sku成功后才能保存其他信息
                skuInfoService.save(skuInfoEntity);
                Long skuId = skuInfoEntity.getSkuId();

                List<SkuImagesEntity> skuImagesEntityList = images.stream().filter(Objects::nonNull).filter(t -> StringUtils.isNotBlank(t.getImgUrl())).map(t -> {
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
                if (sku.getFullPrice().compareTo(BigDecimal.ZERO) > 0) {
                    SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
                    BeanUtils.copyProperties(sku, skuFullReductionEntity);
                    skuFullReductionEntity.setSkuId(skuId);
                    skuFullReductionController.save(skuFullReductionEntity);
                }

                if (sku.getFullCount() > 0) {
                    SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
                    BeanUtils.copyProperties(sku, skuLadderEntity);
                    skuLadderEntity.setSkuId(skuId);
                    skuLadderEntity.setAddOther(sku.getCountStatus());
                    skuLadderController.save(skuLadderEntity);
                }

                List<MemberPrice> memberPriceList = sku.getMemberPrice();
                if (CollectionUtils.isNotEmpty(memberPriceList)) {
                    List<MemberPriceEntity> memberPriceEntityList = memberPriceList.stream().filter(Objects::nonNull).filter(t -> t.getPrice().compareTo(BigDecimal.ZERO) > 0).map(t -> {
                        MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
                        memberPriceEntity.setSkuId(skuId);
                        memberPriceEntity.setMemberLevelId(t.getId());
                        memberPriceEntity.setMemberLevelName(t.getName());
                        memberPriceEntity.setMemberPrice(t.getPrice());
                        memberPriceEntity.setAddOther(1);
                        return memberPriceEntity;
                    }).collect(Collectors.toList());
                    memberPriceController.saveBatch(memberPriceEntityList);
                }
            });

        }
    }

    @Override
    public void up(Long spuId) {
        List<SkuInfoEntity> skuInfoEntityList = skuInfoService.list(Wrappers.<SkuInfoEntity>lambdaQuery().eq(SkuInfoEntity::getSpuId, spuId));
        if (CollectionUtils.isEmpty(skuInfoEntityList)) {
            return;
        }

        // 查询品牌的信息 brandService
        List<Long> brandIdList = skuInfoEntityList.stream().filter(Objects::nonNull).map(SkuInfoEntity::getBrandId).filter(Objects::nonNull).filter(t -> t > 0L).collect(Collectors.toList());
        Map<Long, BrandEntity> brandMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(brandIdList)) {
            brandService.list(Wrappers.<BrandEntity>lambdaQuery()
                    .select(BrandEntity::getBrandId, BrandEntity::getName)
                    .in(BrandEntity::getBrandId, brandIdList)
            ).stream().collect(Collectors.toMap(BrandEntity::getBrandId, v -> v)).forEach(brandMap::put);
        }

        // 查询分类的信息 categoryService
        List<Long> catalogIdList = skuInfoEntityList.stream().filter(Objects::nonNull).map(SkuInfoEntity::getCatalogId).filter(Objects::nonNull).filter(t -> t > 0L).collect(Collectors.toList());
        Map<Long, String> catalogMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(catalogIdList)) {
            categoryService.list(Wrappers.<CategoryEntity>lambdaQuery()
                    .select(CategoryEntity::getCatId, CategoryEntity::getName)
                    .in(CategoryEntity::getCatId, catalogIdList)
            ).stream().collect(Collectors.toMap(CategoryEntity::getCatId, CategoryEntity::getName)).forEach(catalogMap::put);
        }

        // 查询当前sku的所有可被检索的规格属性
        List<SkuModel.Attr> attrList = new ArrayList<>();
        List<ProductAttrValueEntity> productAttrValueEntityList = productAttrValueService.list(Wrappers.<ProductAttrValueEntity>lambdaQuery().eq(ProductAttrValueEntity::getSpuId, spuId));
        if (CollectionUtils.isNotEmpty(productAttrValueEntityList)) {
            List<Long> attrIdList = productAttrValueEntityList.stream().map(ProductAttrValueEntity::getAttrId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(attrIdList)) {
                attrService.list(Wrappers.<AttrEntity>lambdaQuery()
                        .in(AttrEntity::getAttrId, attrIdList)
                        .eq(AttrEntity::getSearchType, AttrEntity.SearchType.ENABLE.getKey())
                ).forEach(t -> {
                    SkuModel.Attr attr = new SkuModel.Attr();
                    BeanUtils.copyProperties(t, attr);
                    attrList.add(attr);
                });
            }
        }

        // 查询库存
        List<Long> skuIdList = skuInfoEntityList.stream().filter(Objects::nonNull).map(SkuInfoEntity::getSkuId).filter(Objects::nonNull).filter(t -> t > 0).distinct().collect(Collectors.toList());
        Map<Long, Integer> skuStock = new HashMap<>();
        if (CollectionUtils.isNotEmpty(skuIdList)) {
            R r = wareSkuController.listByIds(skuIdList);
            List<WareSkuEntity> list = r.getData(new TypeToken<List<WareSkuEntity>>() {});
            if (CollectionUtils.isNotEmpty(list)) {
                list.stream().collect(Collectors.groupingBy(WareSkuEntity::getSkuId)
                ).forEach((key, value) -> {
                    int stock = value.stream().mapToInt(WareSkuEntity::getStock).sum();
                    int stockLocked = value.stream().mapToInt(WareSkuEntity::getStockLocked).sum();
                    skuStock.put(key, stock - stockLocked);
                });
            }
        }

        List<SkuModel> skuModelList = skuInfoEntityList.stream().map(skuInfoEntity -> {
            Long skuId = skuInfoEntity.getSkuId();
            SkuModel skuModel = new SkuModel();
            BeanUtils.copyProperties(skuInfoEntity, skuModel);
            // 查询是否有库存
            Integer stock = skuStock.get(skuId);
            skuModel.setHasStock(Objects.nonNull(stock) && stock > 0);
            skuModel.setHotScore(0L);
            // 查询品牌, 分类
            BrandEntity brandEntity = brandMap.get(skuInfoEntity.getBrandId());
            skuModel.setBrandName(Objects.nonNull(brandEntity) ? brandEntity.getName() : null);
            skuModel.setBrandImg(Objects.nonNull(brandEntity) ? brandEntity.getLogo() : null);
            skuModel.setCategoryName(catalogMap.get(skuInfoEntity.getCatalogId()));
            // 查询当前sku的所有可被检索的规格属性
            skuModel.setAttrs(attrList);
            return skuModel;
        }).collect(Collectors.toList());
        // 发送给检索服务, 写到es中
        R r = elasticSearchController.up(skuModelList);
        if (r.getCode()==0){
            // 修改spu的发布状态
            SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
            spuInfoEntity.setId(spuId);
            spuInfoEntity.setPublishStatus(SpuInfoEntity.PublishStatusType.UP.getKey());
            updateById(spuInfoEntity);
        } else {
            // TODO 失败 接口幂等
        }
    }

}