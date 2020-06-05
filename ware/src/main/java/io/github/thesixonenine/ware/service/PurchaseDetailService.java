package io.github.thesixonenine.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.ware.entity.PurchaseDetailEntity;

import java.util.Map;

/**
 * 
 *
 * @author thesixonenine
 * @date 2020-06-06 01:52:05
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

