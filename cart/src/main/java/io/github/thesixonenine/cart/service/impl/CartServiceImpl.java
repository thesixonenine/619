package io.github.thesixonenine.cart.service.impl;

import io.github.thesixonenine.cart.service.ICartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/09/10 22:04
 * @since 1.0
 */
@Slf4j
@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    private StringRedisTemplate redisTemplate;
}
