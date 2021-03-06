package io.github.thesixonenine.ware.controller.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.ware.controller.WareSkuController;
import io.github.thesixonenine.ware.entity.WareSkuEntity;
import io.github.thesixonenine.ware.service.WareSkuService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 商品库存
 *
 * @author thesixonenine
 * @date 2020-06-06 01:52:04
 */
@RestController
public class WareSkuControllerImpl implements WareSkuController {
    @Autowired
    private WareSkuService wareSkuService;

    /**
     * 列表
     */
    @Override
    public R list(Map<String, Object> params) {
        PageUtils page = wareSkuService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @Override
    public R info(Long id) {
        WareSkuEntity wareSku = wareSkuService.getById(id);
        return R.ok().put("wareSku", wareSku);
    }

    @Override
    public R listByIds(List<Long> idList) {
        idList = idList.stream().filter(Objects::nonNull).filter(t -> t > 0).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(idList)) {
            return R.ok().setData(wareSkuService.listByIds(idList));
        } else {
            return R.ok().setData(null);
        }
    }

    /**
     * 保存
     */
    @Override
    public R save(WareSkuEntity wareSku) {
        wareSkuService.save(wareSku);
        return R.ok();
    }

    /**
     * 修改
     */
    @Override
    public R update(WareSkuEntity wareSku) {
        wareSkuService.updateById(wareSku);
        return R.ok();
    }

    /**
     * 删除
     */
    @Override
    public R delete(Long[] ids) {
        wareSkuService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

    @Override
    public Map<Long, Integer> getSkuHasStock(List<Long> skuIdList) {
        Map<Long, Integer> result = new HashMap<>();
        Map<Long, List<WareSkuEntity>> map = wareSkuService.list(Wrappers.<WareSkuEntity>lambdaQuery().in(WareSkuEntity::getSkuId, skuIdList)).stream().collect(Collectors.groupingBy(WareSkuEntity::getSkuId));
        for (Map.Entry<Long, List<WareSkuEntity>> entry : map.entrySet()) {
            Long key = entry.getKey();
            int sum = entry.getValue().stream().mapToInt(t -> t.getStock() - t.getStockLocked()).sum();
            result.put(key, sum);
        }
        return result;
    }

    @Override
    public void lockStock(String orderSn, Map<Long/* skuId */, Integer/* lockNum */> map) throws RuntimeException {
        wareSkuService.lockStock(orderSn, map);
    }

}
