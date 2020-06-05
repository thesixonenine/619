package io.github.thesixonenine.product.service.impl;

import io.github.thesixonenine.product.entity.BrandEntity;
import io.github.thesixonenine.product.service.BrandService;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BrandServiceImplTest {

    @Autowired
    private BrandService brandService;

    @Test
    public void testList(){
        List<BrandEntity> list = brandService.list();
    }

    @Ignore
    @Test
    public void testInsert() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setName("苹果");
        brandEntity.setLogo("http://pic.3h3.com/up/2012-11/20121111331105116169.jpg");
        brandEntity.setDescript("苹果公司");
        brandEntity.setShowStatus(1);
        brandEntity.setFirstLetter("A");
        brandEntity.setSort(1);
        boolean b = brandService.save(brandEntity);
        Assert.assertTrue(b);
    }
}