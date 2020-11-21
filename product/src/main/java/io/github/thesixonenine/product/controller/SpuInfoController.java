package io.github.thesixonenine.product.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.product.dto.SpuInfoDTO;
import io.github.thesixonenine.product.entity.SkuInfoEntity;
import io.github.thesixonenine.product.entity.SpuInfoEntity;
import io.github.thesixonenine.product.service.SkuInfoService;
import io.github.thesixonenine.product.service.SpuInfoService;
import io.swagger.annotations.Api;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * spu信息
 *
 * @author thesixonenine
 * @date 2020-06-06 00:59:35
 */
@Api(value = "spu信息")
@RestController
public class SpuInfoController implements ISpuInfoController {
    @Autowired
    private SpuInfoService spuInfoService;
    @Autowired
    private SkuInfoService skuInfoService;

    /**
     * 列表
     */
    @Override
    public R list(Map<String, Object> params) {
        PageUtils page = spuInfoService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @Override
    public R info(Long id) {
        SpuInfoEntity spuInfo = spuInfoService.getById(id);
        return R.ok().put("spuInfo", spuInfo);
    }

    /**
     * 保存
     */
    @Override
    public R save(SpuInfoEntity spuInfo) {
        spuInfoService.save(spuInfo);
        return R.ok();
    }

    /**
     * 保存
     */
    @Override
    public R saveSpuInfo(SpuInfoDTO spuInfoDTO) {
        spuInfoService.saveSpuInfo(spuInfoDTO);
        return R.ok();
    }

    /**
     * 修改
     */
    @Override
    public R update(SpuInfoEntity spuInfo) {
        spuInfoService.updateById(spuInfo);
        return R.ok();
    }

    /**
     * 删除
     */
    @Override
    public R delete(Long[] ids) {
        spuInfoService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

    /**
     * 上架
     */
    @Override
    public R up(Long spuId) {
        spuInfoService.up(spuId);
        return R.ok();
    }

    @Override
    public SpuInfoEntity getBySkuId(Long skuId) {
        return Optional.ofNullable(skuInfoService.getById(skuId))
                .map(t->spuInfoService.getById(t.getSpuId()))
                .orElse(new SpuInfoEntity());
    }

    @Override
    public Map<Long, SpuInfoEntity> listBySkuId(List<Long> skuIdList) {
        Map<Long, SpuInfoEntity> result = new HashMap<>();
        if (CollectionUtils.isEmpty(skuIdList)) {
            return result;
        }
        Map<Long/* SkuId */, Long/* SpuId */> map = skuInfoService.list(Wrappers.<SkuInfoEntity>lambdaQuery().select(SkuInfoEntity::getSkuId, SkuInfoEntity::getSpuId).eq(SkuInfoEntity::getSkuId, skuIdList)).stream().collect(Collectors.toMap(SkuInfoEntity::getSkuId, SkuInfoEntity::getSpuId));
        if (MapUtils.isEmpty(map)) {
            return result;
        }
        List<Long> spuIdList = map.values().stream().distinct().collect(Collectors.toList());
        Map<Long, SpuInfoEntity> spuInfoEntityMap = spuInfoService.list(Wrappers.<SpuInfoEntity>lambdaQuery().in(SpuInfoEntity::getId, spuIdList)).stream().collect(Collectors.toMap(SpuInfoEntity::getId, Function.identity()));

        for (Map.Entry<Long, Long> entry : map.entrySet()) {
            SpuInfoEntity spuInfoEntity = spuInfoEntityMap.get(entry.getValue());
            if (Objects.nonNull(spuInfoEntity)) {
                result.put(entry.getKey(), spuInfoEntity);
            }
        }
        return result;
    }
}
