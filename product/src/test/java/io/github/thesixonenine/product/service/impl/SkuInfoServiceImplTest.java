package io.github.thesixonenine.product.service.impl;

import io.github.thesixonenine.product.service.SkuInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class SkuInfoServiceImplTest {

    @Autowired
    private SkuInfoService skuInfoService;

    @Test
    public void testItem(){
        skuInfoService.item(1L);
    }
}