package io.github.thesixonenine.order.mq.handler;

import com.rabbitmq.client.Channel;
import io.github.thesixonenine.order.config.RabbitConfig;
import io.github.thesixonenine.order.entity.OrderEntity;
import io.github.thesixonenine.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/12/27 21:55
 * @since 1.0
 */
@Slf4j
@Component
@RabbitListener(queues = {RabbitConfig.ORDER_RELEASE_QUEUE})
public class OrderCloseHandler {

    @Autowired
    private OrderService orderService;

    @RabbitHandler
    public void listener(OrderEntity orderEntity, Message message, Channel channel) throws IOException {
        MessageProperties messageProperties = message.getMessageProperties();
        // deliveryTag在channel中是自增的
        long deliveryTag = messageProperties.getDeliveryTag();
        log.info("订单[" + orderEntity.getOrderSn() + "]关单接收成功");
        try {
            orderService.closeOrder(orderEntity);
            // 确认收到消息, 可以从broker中移除
            // multiple 是否批量ack, 批量ack这个channel中的消息
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            // ack失败
            channel.basicReject(deliveryTag, true);
        }
    }
}
