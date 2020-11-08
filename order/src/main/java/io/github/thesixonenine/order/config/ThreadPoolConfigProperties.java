package io.github.thesixonenine.order.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/07/09 0:05
 * @since 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "thread")
public class ThreadPoolConfigProperties {
    private int corePoolSize;
    private int maximumPoolSize;
    private long keepAliveTime;
}
