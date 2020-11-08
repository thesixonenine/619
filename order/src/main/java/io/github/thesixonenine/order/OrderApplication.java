package io.github.thesixonenine.order;

import com.rabbitmq.client.Channel;
import io.github.thesixonenine.order.entity.OrderEntity;
import io.github.thesixonenine.order.entity.OrderReturnReasonEntity;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.IOException;

@EnableRabbit
@EnableRedisHttpSession
@EnableSwagger2
@EnableFeignClients(basePackages = {"io.github.thesixonenine"})
@RefreshScope
@MapperScan(value = "io.github.thesixonenine.order.dao")
@SpringBootApplication
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }


    /**
     * 消息监听
     * 1.同一个消息只有一个客户端收到
     * 2.只有一个消息处理完成, 才能收到下一个消息
     *
     * 更推荐将RabbitListener注解加在类上
     * 在方法上使用RabbitHandler注解
     * 这样可以接受队列中的不同类型的消息
     *
     * @param message     消息原始报文, 包含消息头和消息体
     * @param orderEntity 发送时的消息体, 自动转换
     * @param channel     当前传输数据的通道
     */
    /*
    @RabbitListener(queues = {"hello-java-queue"})
    public void rec(Message message, OrderEntity orderEntity, Channel channel) {
        MessageProperties messageProperties = message.getMessageProperties();
        // deliveryTag在channel中是自增的
        long deliveryTag = messageProperties.getDeliveryTag();
        byte[] body = message.getBody();
        System.out.println("RabbitListener接受到消息: " + orderEntity);
        // 消费者收到消息后, 默认自动回复ack, 从broker中移除
        // 设置spring.rabbitmq.listener.simple.acknowledge-mode=manual后, 手动确认消息

        try {
            // 确认收到消息, 可以从broker中移除
            // multiple 是否批量ack, 批量ack这个channel中的消息
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            // ack失败
        }
        try {
            // 拒收消息, 并确定是否重新放入队列, false则直接丢弃, 可批量
            channel.basicNack(deliveryTag, false, true);
        } catch (IOException e) {
            // 拒收失败
        }
        try {
            // 同Nack, 但不能批量
            channel.basicReject(deliveryTag, false);
        } catch (IOException e) {
            // 拒收失败
        }
    }

    @RabbitListener(queues = {"hello-java-queue"})
    @Component
    class RecMsg {

        @RabbitHandler
        public void recOrderEntity(OrderEntity orderEntity) {
            System.out.println("RabbitHandler接收到" + orderEntity);
        }

        @RabbitHandler
        public void recOrderEntity(OrderReturnReasonEntity orderReturnReasonEntity) {
            System.out.println("RabbitHandler接收到" + orderReturnReasonEntity);
        }
    }
    */
}
