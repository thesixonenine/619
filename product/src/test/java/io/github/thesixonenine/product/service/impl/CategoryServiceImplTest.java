package io.github.thesixonenine.product.service.impl;

import io.github.thesixonenine.product.service.CategoryService;
import io.github.thesixonenine.product.vo.Catalog2VO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceImplTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    public void listTree() {
    }

    @Test
    public void findParentCid() {
        List<Long> list = categoryService.findParentCid(225L, new ArrayList<>());
        log.info("三级分类的完整路径: {}", list);
    }

    @Test
    public void testCache() {
        for (int i = 0; i < 10; i++) {
            Map<String, List<Catalog2VO>> map = categoryService.catalog();
            System.out.println(map);
        }
    }
}