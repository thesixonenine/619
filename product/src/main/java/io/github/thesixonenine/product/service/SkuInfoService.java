package io.github.thesixonenine.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.product.entity.SkuInfoEntity;
import io.github.thesixonenine.product.vo.ItemVO;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * sku信息
 *
 * @author thesixonenine
 * @date 2020-06-06 00:59:35
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    ItemVO item(Long skuId) throws ExecutionException, InterruptedException;
}

