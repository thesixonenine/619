package io.github.thesixonenine.ware.service.impl;

import io.github.thesixonenine.ware.entity.WareInfoEntity;
import io.github.thesixonenine.ware.service.WareInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class WareInfoServiceImplTest {

    @Autowired
    private WareInfoService wareInfoService;

    @Test
    public void testList(){
        List<WareInfoEntity> list = wareInfoService.list();
    }
}