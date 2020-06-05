package io.github.thesixonenine.coupon.service.impl;

import io.github.thesixonenine.coupon.entity.CouponEntity;
import io.github.thesixonenine.coupon.service.CouponService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class CouponServiceImplTest {

    @Autowired
    private CouponService couponService;

    @Test
    public void testList() {
        List<CouponEntity> list = couponService.list();

    }
}