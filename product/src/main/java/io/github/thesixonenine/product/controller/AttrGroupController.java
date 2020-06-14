package io.github.thesixonenine.product.controller;

import java.util.*;

// import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.github.thesixonenine.common.utils.Query;
import io.github.thesixonenine.product.dto.AttrGroupDTO;
import io.github.thesixonenine.product.entity.AttrAttrgroupRelationEntity;
import io.github.thesixonenine.product.entity.AttrEntity;
import io.github.thesixonenine.product.service.AttrAttrgroupRelationService;
import io.github.thesixonenine.product.service.AttrService;
import io.github.thesixonenine.product.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.github.thesixonenine.product.entity.AttrGroupEntity;
import io.github.thesixonenine.product.service.AttrGroupService;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.R;



/**
 * 属性分组
 *
 * @author thesixonenine
 * @date 2020-06-06 00:59:35
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;
    @Autowired
    private AttrService attrService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AttrAttrgroupRelationService relationService;

    @ApiOperation(value = "查询分组下关联的所有属性")
    @GetMapping(value = "/{attrGroupId}/attr/relation")
    public R listAttrRelation(@PathVariable(value = "attrGroupId") Long attrGroupId){
        List<AttrEntity> list = attrService.getRelationAttr(attrGroupId);
        return R.ok().put("data", list);
    }

    @ApiOperation(value = "查询分组下没有关联的属性")
    @GetMapping(value = "/{attrGroupId}/noattr/relation")
    public R listNoAttrRelation(@PathVariable(value = "attrGroupId") Long attrGroupId,
                                @RequestParam Map<String, Object> params){
        PageUtils page = attrService.listNoAttrRelation(params, attrGroupId);
        return R.ok().put("page", page);
    }

    @PostMapping(value = "/attr/relation")
    public R addAttrRelation(@RequestBody List<AttrAttrgroupRelationEntity> relation){
        relationService.saveBatch(relation);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/attr/relation/delete")
    public R deleteAttrRelation(@RequestBody List<AttrAttrgroupRelationEntity> relation){
        relationService.deleteAttrRelation(relation);
        return R.ok();
    }

    @GetMapping(value = "/list/{catelogId}")
    public R listByCatelogId(@PathVariable(value = "catelogId") Long catelogId,
                             @RequestParam Map<String, Object> params) {
        IPage<AttrGroupEntity> iPage;
        String key = (String) params.get("key");
        if (catelogId==0L){
            // 查询全部
            iPage = attrGroupService.page(new Query<AttrGroupEntity>().getPage(params),
                    Wrappers.<AttrGroupEntity>lambdaQuery().eq(AttrGroupEntity::getAttrGroupId, key).or().like(AttrGroupEntity::getAttrGroupName, key));

        } else {
            iPage = attrGroupService.page(new Query<AttrGroupEntity>().getPage(params),
                    Wrappers.<AttrGroupEntity>lambdaQuery().eq(AttrGroupEntity::getCatelogId, catelogId)
                            .and(StringUtils.isNotBlank(key), (obj)->{
                                obj.eq(AttrGroupEntity::getAttrGroupId, key).or().like(AttrGroupEntity::getAttrGroupName, key);
                            })

            );
        }
        return R.ok().put("page", new PageUtils(iPage));
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrGroupService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    // @RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        AttrGroupDTO dto = new AttrGroupDTO();
        BeanUtils.copyProperties(attrGroup, dto);

        // 查询完整的分类id
        List<Long> allCidList = new ArrayList<Long>();
        List<Long> allCid = categoryService.findParentCid(dto.getCatelogId(), allCidList);
        Long[] array = allCid.toArray(new Long[0]);
        dto.setCatelogPath(array);
        return R.ok().put("attrGroup", dto);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));
        return R.ok();
    }

}
