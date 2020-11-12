package io.github.thesixonenine.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.order.entity.OrderEntity;
import io.github.thesixonenine.order.vo.OrderConfirmVO;

import java.util.Map;

/**
 * 订单
 *
 * @author thesixonenine
 * @date 2020-06-06 01:47:54
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 确认订单
     *
     * @return 确认订单信息
     */
    OrderConfirmVO confirmOrder();

}

