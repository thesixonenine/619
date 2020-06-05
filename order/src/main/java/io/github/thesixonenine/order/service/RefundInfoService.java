package io.github.thesixonenine.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.order.entity.RefundInfoEntity;

import java.util.Map;

/**
 * 退款信息
 *
 * @author thesixonenine
 * @date 2020-06-06 01:47:54
 */
public interface RefundInfoService extends IService<RefundInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

