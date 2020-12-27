package io.github.thesixonenine.order.config;

import com.rabbitmq.client.Channel;
import io.github.thesixonenine.order.entity.OrderEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/11/07 23:53
 * @since 1.0
 */
@Slf4j
@Configuration
public class RabbitConfig {

    // @Autowired
    // RabbitTemplate rabbitTemplate;

    // @Bean
    // public MessageConverter messageConverter() {
    //     return new Jackson2JsonMessageConverter();
    // }

    // @PostConstruct
    @Bean
    public RabbitTemplate initRabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        // 接收端确认: 使用两个CallBack来确认消息是否成功投递到队列
        // 消费端确认: 默认是自动确认的, 但如果消息处理失败(应用被kill), 则消息就丢失了
        //            配置spring.rabbitmq.listener.simple.acknowledge-mode=manual
        //            手动回复ack确认消费

        // 设置确认Broker成功接收到(cluster下是所有Broker成功接收到)的回调, 此时Message不一定到达Queue
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {

            /**
             *
             * @param correlationData 当前消息的唯一关联数据(id)
             * @param ack Broker是否成功收到消息(还未到达队列): true-消息代理成功收到消息
             * @param cause 失败的原因
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                log.info("confirm...CorrelationData[{}], ack[{}], cause[{}]", correlationData, ack, cause);
            }
        });
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {

            /**
             * 只要消息没有正确到达指定的队列, 就会触发这个失败的回调
             *
             * @param message 投递失败的消息
             * @param replyCode 响应码
             * @param replyText 响应内容
             * @param exchange 消息发给的交换机
             * @param routingKey 消息使用的路由键
             */
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                log.error("returnedMessage...FailedMessage[{}], replyCode[{}], replyText[{}], exchange[{}], routingKey[{}]", message, replyCode, replyText, exchange, routingKey);
            }
        });
        return rabbitTemplate;
    }


    /**
     * 订单事件交换机
     */
    public static final String ORDER_EVENT_EXCHANGE = "order-event-exchange";
    /**
     * 死信队列路由key
     */
    public static final String ORDER_DELAY_ROUTING_KEY = "order.delay.order";
    public static final String ORDER_DELAY_QUEUE = "order.delay.queue";
    /**
     * 死信队列过期后路由到正常订单队列的key
     */
    public static final String ORDER_RELEASE_ROUTING_KEY = "order.release.order";
    public static final String ORDER_RELEASE_QUEUE = "order.release.queue";
    @Bean
    public Queue delayQueue() {
        // 死信队列
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", ORDER_EVENT_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", ORDER_RELEASE_ROUTING_KEY);
        arguments.put("x-message-ttl", 20 * 1000);
        // 是否持久化 true, 是否排他 false 是否自动删除false
        return new Queue(ORDER_DELAY_QUEUE, true, false, false, arguments);
    }

    @Bean
    public Queue orderReleaseQueue() {
        return new Queue(ORDER_RELEASE_QUEUE, true, false, false);
    }
    @Bean
    public Exchange orderEventExchange() {
        return new TopicExchange(ORDER_EVENT_EXCHANGE, true, false);
    }
    @Bean
    public Binding orderCreateBinding() {
        return new Binding(ORDER_DELAY_QUEUE, Binding.DestinationType.QUEUE, ORDER_EVENT_EXCHANGE, ORDER_DELAY_ROUTING_KEY, null);
    }
    @Bean
    public Binding orderReleaseBinding() {
        return new Binding(ORDER_RELEASE_QUEUE, Binding.DestinationType.QUEUE, ORDER_EVENT_EXCHANGE, ORDER_RELEASE_ROUTING_KEY, null);
    }


    public static final String ORDER_RELEASE_OTHER_KEY = "order.release.other.#";

    /**
     * 订单释放直接绑定库存释放
     */
    @Bean
    public Binding orderReleaseOtherBinding() {
        return new Binding("stock.release.queue", Binding.DestinationType.QUEUE, ORDER_EVENT_EXCHANGE, ORDER_RELEASE_OTHER_KEY, null);
    }
}
