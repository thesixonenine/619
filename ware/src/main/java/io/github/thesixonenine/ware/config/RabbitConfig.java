package io.github.thesixonenine.ware.config;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
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

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 库存事件交换机
     */
    public static final String STOCK_EVENT_EXCHANGE = "stock-event-exchange";

    public static final String STOCK_LOCKED_ROUTING_KEY = "stock.locked";

    public static final String STOCK_RELEASE_ALL_ROUTING_KEY = "stock.release.#";
    public static final String STOCK_RELEASE_QUEUE = "stock.release.queue";

    public static final String STOCK_RELEASE_ROUTING_KEY = "stock.release";
    public static final String STOCK_DELAY_QUEUE = "stock.delay.queue";

    @Bean
    public Exchange stockEventExchange() {
        return new TopicExchange(STOCK_EVENT_EXCHANGE, true, false);
    }

    @Bean
    public Queue stockReleaseQueue() {
        return new Queue(STOCK_RELEASE_QUEUE, true, false, false);
    }

    @Bean
    public Queue stockDelayQueue() {
        // 死信队列
        Map<String, Object> arguments = new HashMap<>();
        // 信死了给哪个交换机
        arguments.put("x-dead-letter-exchange", STOCK_EVENT_EXCHANGE);
        // 给交换机时传入的key
        arguments.put("x-dead-letter-routing-key", STOCK_RELEASE_ROUTING_KEY);
        arguments.put("x-message-ttl", 20 * 1000);
        // 是否持久化 true, 是否排他 false 是否自动删除false
        return new Queue(STOCK_DELAY_QUEUE, true, false, false, arguments);
    }

    @Bean
    public Binding stockReleaseBinding() {
        return new Binding(STOCK_RELEASE_QUEUE, Binding.DestinationType.QUEUE, STOCK_EVENT_EXCHANGE, STOCK_RELEASE_ALL_ROUTING_KEY, null);
    }

    @Bean
    public Binding stockLockedBinding() {
        return new Binding(STOCK_DELAY_QUEUE, Binding.DestinationType.QUEUE, STOCK_EVENT_EXCHANGE, STOCK_LOCKED_ROUTING_KEY, null);
    }





    @RabbitListener(queues = {STOCK_RELEASE_QUEUE})
    public void listener(Message message) {
        MessageProperties messageProperties = message.getMessageProperties();
        log.info("库存释放消息接收成功[{}]", messageProperties.getMessageId());
    }
}
