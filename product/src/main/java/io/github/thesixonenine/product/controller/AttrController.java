package io.github.thesixonenine.product.controller;

import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.product.entity.AttrEntity;
import io.github.thesixonenine.product.service.AttrService;
import io.github.thesixonenine.product.vo.AttrVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

// import org.apache.shiro.authz.annotation.RequiresPermissions;


/**
 * 商品属性
 *
 * @author thesixonenine
 * @date 2020-06-06 00:59:35
 */
@Slf4j
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    @GetMapping("/{type}/list/{catelogId}")
    public R baseList(@RequestParam Map<String, Object> params,
                      @PathVariable(value = "type") String type,
                      @PathVariable(value = "catelogId") Long catelogId) {
        if ("base".equals(type)) {
            log.debug("基础查询catelogId[{}]", catelogId);
            PageUtils page = attrService.queryPageWithType(params, catelogId, AttrEntity.AttrType.BASE);
            return R.ok().put("page", page);
        } else if ("sale".equals(type)) {
            log.debug("销售属性查询catelogId[{}]", catelogId);
            PageUtils page = attrService.queryPageWithType(params, catelogId, AttrEntity.AttrType.SALE);
            return R.ok().put("page", page);
        }
        return R.ok();

    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("product:attr:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = attrService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    // @RequiresPermissions("product:attr:info")
    public R info(@PathVariable("attrId") Long attrId) {
        // AttrEntity attr = attrService.getById(attrId);
        AttrVO vo = attrService.info(attrId);
        return R.ok().put("attr", vo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("product:attr:save")
    public R save(@RequestBody AttrVO attr) {
        // attrService.save(attr);
        // 同时保存关联信息
        attrService.save(attr);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("product:attr:update")
    public R update(@RequestBody AttrVO attr) {
        attrService.update(attr);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("product:attr:delete")
    public R delete(@RequestBody Long[] attrIds) {
        attrService.removeByIds(Arrays.asList(attrIds));
        return R.ok();
    }

}
