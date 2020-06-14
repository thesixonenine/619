package io.github.thesixonenine.product.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.product.entity.BrandEntity;
import io.github.thesixonenine.product.entity.CategoryBrandRelationEntity;
import io.github.thesixonenine.product.entity.CategoryEntity;
import io.github.thesixonenine.product.service.BrandService;
import io.github.thesixonenine.product.service.CategoryBrandRelationService;
import io.github.thesixonenine.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

// import org.apache.shiro.authz.annotation.RequiresPermissions;


/**
 * 品牌分类关联
 *
 * @author thesixonenine
 * @date 2020-06-06 00:59:35
 */
@RestController
@RequestMapping("product/categorybrandrelation")
public class CategoryBrandRelationController {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 获取brandId关联的分类
     *
     * @param brandId brandId
     * @return 关联信息
     */
    @GetMapping("/listBy/brandId")
    public R list(@RequestParam(value = "brandId") Long brandId) {
        List<CategoryBrandRelationEntity> list = categoryBrandRelationService.list(Wrappers.<CategoryBrandRelationEntity>lambdaQuery()
                .eq(CategoryBrandRelationEntity::getBrandId, brandId)
        );
        return R.ok().put("data", list);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("product:categorybrandrelation:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = categoryBrandRelationService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("product:categorybrandrelation:info")
    public R info(@PathVariable("id") Long id) {
        CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);
        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("product:categorybrandrelation:save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation) {
        Long brandId = categoryBrandRelation.getBrandId();
        Long catelogId = categoryBrandRelation.getCatelogId();
        BrandEntity brandEntity = brandService.getById(brandId);
        CategoryEntity categoryEntity = categoryService.getById(catelogId);
        categoryBrandRelation.setBrandName(brandEntity.getName());
        categoryBrandRelation.setCatelogName(categoryEntity.getName());
        categoryBrandRelationService.save(categoryBrandRelation);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("product:categorybrandrelation:update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation) {
        categoryBrandRelationService.updateById(categoryBrandRelation);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("product:categorybrandrelation:delete")
    public R delete(@RequestBody Long[] ids) {
        categoryBrandRelationService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
