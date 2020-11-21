package io.github.thesixonenine.product.controller.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

// import org.apache.shiro.authz.annotation.RequiresPermissions;
import io.github.thesixonenine.product.controller.ISkuSaleAttrValueController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.thesixonenine.product.entity.SkuSaleAttrValueEntity;
import io.github.thesixonenine.product.service.SkuSaleAttrValueService;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.R;



/**
 * sku销售属性&值
 *
 * @author thesixonenine
 * @date 2020-06-06 00:59:35
 */
@RestController
public class SkuSaleAttrValueController implements ISkuSaleAttrValueController {
    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    /**
     * 列表
     */
    @Override
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = skuSaleAttrValueService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @Override
    public R info(@PathVariable("id") Long id){
		SkuSaleAttrValueEntity skuSaleAttrValue = skuSaleAttrValueService.getById(id);
        return R.ok().put("skuSaleAttrValue", skuSaleAttrValue);
    }

    /**
     * 保存
     */
    @Override
    public R save(@RequestBody SkuSaleAttrValueEntity skuSaleAttrValue){
		skuSaleAttrValueService.save(skuSaleAttrValue);
        return R.ok();
    }

    /**
     * 修改
     */
    @Override
    public R update(@RequestBody SkuSaleAttrValueEntity skuSaleAttrValue){
		skuSaleAttrValueService.updateById(skuSaleAttrValue);
        return R.ok();
    }

    /**
     * 删除
     */
    @Override
    public R delete(@RequestBody Collection<Long> ids){
		skuSaleAttrValueService.removeByIds(ids);
        return R.ok();
    }

}
