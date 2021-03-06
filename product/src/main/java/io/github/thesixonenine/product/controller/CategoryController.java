package io.github.thesixonenine.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

// import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.github.thesixonenine.product.dto.CategoryDTO;
import io.github.thesixonenine.product.entity.CategoryBrandRelationEntity;
import io.github.thesixonenine.product.service.CategoryBrandRelationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.github.thesixonenine.product.entity.CategoryEntity;
import io.github.thesixonenine.product.service.CategoryService;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.R;



/**
 * 商品三级分类
 *
 * @author thesixonenine
 * @date 2020-06-06 00:59:35
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 学习部分
     */

    @GetMapping(value = "/list/tree")
    public R listTree() {
        List<CategoryDTO> list = categoryService.listTree();
        return R.ok().put("data", list);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("product:category:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    // @RequiresPermissions("product:category:info")
    public R info(@PathVariable("catId") Long catId){
		CategoryEntity category = categoryService.getById(catId);
        return R.ok().put("category", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("product:category:save")
    public R save(@RequestBody CategoryEntity category){
		categoryService.save(category);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("product:category:update")
    public R update(@RequestBody CategoryEntity category){
		// categoryService.updateById(category);
        // 同时更新冗余信息
        categoryService.updateWithBrand(category);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("product:category:delete")
    public R delete(@RequestBody Long[] catIds){
		categoryService.removeByIds(Arrays.asList(catIds));
        return R.ok();
    }

}
