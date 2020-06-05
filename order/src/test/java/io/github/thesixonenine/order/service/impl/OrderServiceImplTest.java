package io.github.thesixonenine.order.service.impl;

import io.github.thesixonenine.order.entity.OrderEntity;
import io.github.thesixonenine.order.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceImplTest {

    @Autowired
    private OrderService orderService;

    @Test
    public void testList() {
        List<OrderEntity> list = orderService.list();
    }
}