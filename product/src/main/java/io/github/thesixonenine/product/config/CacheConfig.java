package io.github.thesixonenine.product.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 1. 为了使CacheProperties生效(默认只是与配置文件绑定), 使用@EnableConfigurationProperties(value = CacheProperties.class)放入Spring容器中
 * 2. 手动配置RedisCacheConfiguration, 指定value的序列化器使用Jackson的
 * @author Simple
 * @version 1.0
 * @date 2020/06/29 23:35
 * @since 1.0
 */
@EnableConfigurationProperties(value = CacheProperties.class)
@Configuration
public class CacheConfig {
    /**
     * 或者直接放在方法上, Spring会自动注入
     */
    @Autowired
    private CacheProperties cacheProperties;

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        CacheProperties.Redis redisProperties = cacheProperties.getRedis();

        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        config = config.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));
        // 为了直观看到redis中的数据, 不使用默认的JDK序列化, 而是使用Jackson提供的
        config = config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));


        // 如果配置文件中有配置, 则使用配置文件中的配置
        if (redisProperties.getTimeToLive() != null) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }

        if (redisProperties.getKeyPrefix() != null) {
            config = config.prefixKeysWith(redisProperties.getKeyPrefix());
        }

        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }

        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }
        return config;
    }
}
