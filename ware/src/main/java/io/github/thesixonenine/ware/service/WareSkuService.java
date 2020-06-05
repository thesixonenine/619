package io.github.thesixonenine.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.ware.entity.WareSkuEntity;

import java.util.Map;

/**
 * 商品库存
 *
 * @author thesixonenine
 * @date 2020-06-06 01:52:04
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

