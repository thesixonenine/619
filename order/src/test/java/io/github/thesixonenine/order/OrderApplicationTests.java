package io.github.thesixonenine.order;

import io.github.thesixonenine.order.entity.OrderEntity;
import io.github.thesixonenine.order.entity.OrderReturnReasonEntity;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class OrderApplicationTests {

    @Disabled
    @Test
    void contextLoads() {
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private AmqpTemplate amqpTemplate;


    /**
     * 发送消息
     */
    @Disabled
    @Test
    public void rabbitmqTestSendMsg() throws InterruptedException {
        String exchange = "hello-java-direct-exchange";
        String routingKey = "hello-java";
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(2L);
        orderEntity.setOrderSn("20201107234501");
        // rabbitTemplate.convertAndSend(exchange, routingKey, orderEntity);
        // amqpTemplate.convertAndSend(exchange, routingKey, orderEntity);

        for (long i = 0; ; i++) {
            TimeUnit.SECONDS.sleep(5);
            if (i / 2 == 0) {
                OrderEntity order = new OrderEntity();
                order.setId(i);
                // CorrelationData 消息的唯一id
                rabbitTemplate.convertAndSend(exchange, routingKey, order, new CorrelationData(UUID.randomUUID().toString()));
            } else {
                OrderReturnReasonEntity orderReturnReason = new OrderReturnReasonEntity();
                orderReturnReason.setId(i);
                rabbitTemplate.convertAndSend(exchange, routingKey, orderReturnReason, new CorrelationData(UUID.randomUUID().toString()));
            }
        }
    }


    @Autowired
    private AmqpAdmin amqpAdmin;

    /**
     * 定义一个直接交换机
     */
    @Disabled
    @Test
    public void rabbitmqTestDeclareDirectExchange() {
        // 是否持久化 true, 是否自动删除false
        DirectExchange exchange = new DirectExchange("hello-java-direct-exchange", true, false);
        amqpAdmin.declareExchange(exchange);
    }

    /**
     * 定义一个队列
     */
    @Disabled
    @Test
    public void rabbitmqTestDeclareQueue() {
        // 是否持久化 true, 是否排他 false 是否自动删除false
        Queue queue = new Queue("hello-java-queue", true, false, false);
        amqpAdmin.declareQueue(queue);
    }

    /**
     * 定义一个binding
     */
    @Disabled
    @Test
    public void rabbitmqTestDeclareBinding() {
        // 传入 目的地 目的地类型 交换机 路由键 参数
        // 将交换机与目的地类型的目的地用路由键来进行绑定
        Binding binding = new Binding("hello-java-queue", Binding.DestinationType.QUEUE, "hello-java-direct-exchange", "hello-java", null);
        amqpAdmin.declareBinding(binding);
    }

}
