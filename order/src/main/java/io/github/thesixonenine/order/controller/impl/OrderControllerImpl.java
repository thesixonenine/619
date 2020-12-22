package io.github.thesixonenine.order.controller.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.order.controller.OrderController;
import io.github.thesixonenine.order.entity.OrderEntity;
import io.github.thesixonenine.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;


/**
 * 订单
 *
 * @author thesixonenine
 * @date 2020-06-06 01:47:54
 */
@RestController
public class OrderControllerImpl implements OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 列表
     */
    @Override
    public R list(Map<String, Object> params) {
        PageUtils page = orderService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @Override
    public R info(Long id) {
        OrderEntity order = orderService.getById(id);
        return R.ok().put("order", order);
    }

    /**
     * 保存
     */
    @Override
    public R save(OrderEntity order) {
        orderService.save(order);
        return R.ok();
    }

    /**
     * 修改
     */
    @Override
    public R update(OrderEntity order) {
        orderService.updateById(order);
        return R.ok();
    }

    /**
     * 删除
     */
    @Override
    public R delete(Long[] ids) {
        orderService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

    @Override
    public OrderEntity getByOrderSn(String orderSn) {
        return orderService.getOne(Wrappers.<OrderEntity>lambdaQuery().eq(OrderEntity::getOrderSn, orderSn), false);
    }

}
