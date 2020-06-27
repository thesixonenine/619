package io.github.thesixonenine.product.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @author Simple
 * @version 1.0
 * @date 2020/06/27 19:47
 * @since 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void testRedis() {
        System.out.println(redisTemplate);
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set("Hi", "World" + System.currentTimeMillis());
        String s = ops.get("Hi");
        System.out.println(s);
    }
}
