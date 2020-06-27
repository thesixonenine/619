package io.github.thesixonenine.product.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/06/27 21:49
 * @since 1.0
 */
@Configuration
public class RedissonConfig {

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient(){
        Config config = new Config();
        // config.setTransportMode(TransportMode.EPOLL);
        config.useSingleServer().setAddress("redis://192.168.137.10:6379");
                //可以用"rediss://"来启用SSL连接
                // .addNodeAddress("redis://127.0.0.1:7181");
        return Redisson.create(config);
    }
}
