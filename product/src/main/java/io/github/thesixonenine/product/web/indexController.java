package io.github.thesixonenine.product.web;

import io.github.thesixonenine.product.entity.CategoryEntity;
import io.github.thesixonenine.product.service.CategoryService;
import io.github.thesixonenine.product.vo.Catalog2VO;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/06/27 13:17
 * @since 1.0
 */
@Slf4j
@Controller
public class indexController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping(value = {"/", "/index.html"})
    public String index(Model model) {
        log.info("进入首页");
        // 查询一级分类
        List<CategoryEntity> list = categoryService.catalogLevel1();
        model.addAttribute("categorys", list);
        return "index";
    }

    @GetMapping(value = {"/index/json/catalog.json"})
    @ResponseBody
    public Map<String, List<Catalog2VO>> catalog() {
        return categoryService.catalog();
    }


    @Autowired
    private RedissonClient redissonClient;

    @GetMapping(value = {"/test/redisson"})
    @ResponseBody
    public String testRedisson() {
        String lockName = "LOCK";
        RLock lock = redissonClient.getLock(lockName);
        lock.lock();
        try {
            System.out.println("执行业务");
        } finally {
            lock.unlock();
        }

        return "Hi";
    }
}
