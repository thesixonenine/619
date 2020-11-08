package io.github.thesixonenine.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/07/09 0:01
 * @since 1.0
 */
@Configuration
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolExecutor searchItem(ThreadPoolConfigProperties prop) {
        return new ThreadPoolExecutor(prop.getCorePoolSize(), prop.getMaximumPoolSize(), prop.getKeepAliveTime(), TimeUnit.SECONDS, new LinkedBlockingQueue<>(1000), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
    }
}
