package io.github.thesixonenine.ware.mq.handler;

import io.github.thesixonenine.order.controller.OrderController;
import io.github.thesixonenine.order.entity.OrderEntity;
import io.github.thesixonenine.ware.config.RabbitConfig;
import io.github.thesixonenine.ware.dto.mq.StockLockedDTO;
import io.github.thesixonenine.ware.entity.WareOrderTaskDetailEntity;
import io.github.thesixonenine.ware.entity.WareOrderTaskEntity;
import io.github.thesixonenine.ware.service.WareOrderTaskDetailService;
import io.github.thesixonenine.ware.service.WareOrderTaskService;
import io.github.thesixonenine.ware.service.WareSkuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/12/22 23:21
 * @since 1.0
 */
@Slf4j
@Component
@RabbitListener(queues = {RabbitConfig.STOCK_RELEASE_QUEUE})
public class StockReleaseQueueHandler {
    @Autowired
    private WareSkuService wareSkuService;
    @Autowired
    private WareOrderTaskService wareOrderTaskService;
    @Autowired
    private WareOrderTaskDetailService wareOrderTaskDetailService;

    @Autowired
    private OrderController orderController;

    /**
     * 库存解锁
     *
     * @param dto
     * @param message
     */
    @RabbitHandler
    public void listener(StockLockedDTO dto, Message message) {
        log.info("收到库存解锁的消息[{}]", dto);
        Long detailId = dto.getDetailId();
        Long taskId = dto.getTaskId();
        // 查询工作单详情, 没有则证明锁定库存的事务已经回滚, 没有锁定任何库存, 无需解锁
        WareOrderTaskDetailEntity taskDetail = wareOrderTaskDetailService.getById(detailId);
        if (Objects.isNull(taskDetail)) {
            // 无需解锁
            return;
        }
        // 查询订单是否存在, 没有则证明下订单的事务已经回滚, 没有创建任何订单, 需要解锁库存
        WareOrderTaskEntity task = wareOrderTaskService.getById(taskId);
        String orderSn = task.getOrderSn();
        OrderEntity order = orderController.getByOrderSn(orderSn);
        // 查询订单状态, 如果是已取消, 则需要解锁库存, 否则不能解锁(待支付/已付款/已发货...)
        if (Objects.isNull(order) || OrderEntity.OrderStatusEnum.CANCLED.getCode().equals(order.getStatus())) {
            // 解锁库存
            wareSkuService.unLockStock(dto);
        }
    }
}