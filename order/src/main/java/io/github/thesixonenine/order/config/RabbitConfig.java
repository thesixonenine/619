package io.github.thesixonenine.order.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

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

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void initRabbitTemplate() {
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
    }
}
