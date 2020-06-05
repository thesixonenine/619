package io.github.thesixonenine.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.ware.entity.PurchaseEntity;

import java.util.Map;

/**
 * 采购信息
 *
 * @author thesixonenine
 * @date 2020-06-06 01:52:05
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

