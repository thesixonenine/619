package io.github.thesixonenine.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.order.entity.OrderItemEntity;

import java.util.Map;

/**
 * 订单项信息
 *
 * @author thesixonenine
 * @date 2020-06-06 01:47:54
 */
public interface OrderItemService extends IService<OrderItemEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

