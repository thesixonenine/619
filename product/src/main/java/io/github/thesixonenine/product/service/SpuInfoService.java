package io.github.thesixonenine.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.product.dto.SpuInfoDTO;
import io.github.thesixonenine.product.entity.SpuInfoEntity;

import java.util.Map;

/**
 * spu信息
 *
 * @author thesixonenine
 * @date 2020-06-06 00:59:35
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfo(SpuInfoDTO spuInfoDTO);

    void up(Long spuId);
}

