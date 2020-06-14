package io.github.thesixonenine.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.Query;
import io.github.thesixonenine.product.dao.AttrDao;
import io.github.thesixonenine.product.entity.AttrAttrgroupRelationEntity;
import io.github.thesixonenine.product.entity.AttrEntity;
import io.github.thesixonenine.product.entity.AttrEntity.AttrType;
import io.github.thesixonenine.product.entity.AttrGroupEntity;
import io.github.thesixonenine.product.entity.CategoryEntity;
import io.github.thesixonenine.product.service.AttrAttrgroupRelationService;
import io.github.thesixonenine.product.service.AttrGroupService;
import io.github.thesixonenine.product.service.AttrService;
import io.github.thesixonenine.product.service.CategoryService;
import io.github.thesixonenine.product.vo.AttrVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AttrGroupService attrGroupService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageWithType(Map<String, Object> params, Long catelogId, AttrType attrType) {
        LambdaQueryWrapper<AttrEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (catelogId > 0L) {
            queryWrapper.eq(AttrEntity::getCatelogId, catelogId);
        }
        queryWrapper.eq(AttrEntity::getAttrType, attrType.getKey());
        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) {
            queryWrapper.and((obj) -> {
                obj.eq(AttrEntity::getAttrId, key).or().like(AttrEntity::getAttrName, key);
            });
        }
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                queryWrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        List<AttrEntity> records = page.getRecords();
        List<AttrVO> voList = convert2VO(records, attrType);
        pageUtils.setList(voList);
        return pageUtils;
    }

    private List<AttrVO> convert2VO(List<AttrEntity> records, AttrType attrType) {
        // 查询节点名称
        List<Long> catelogIdList = records.stream().map(AttrEntity::getCatelogId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<Long, String> catelogIdNameMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(catelogIdList)) {
            catelogIdNameMap.putAll(categoryService.list(Wrappers.<CategoryEntity>lambdaQuery()
                    .select(CategoryEntity::getCatId, CategoryEntity::getName)
                    .in(CategoryEntity::getCatId, catelogIdList)
            ).stream().collect(Collectors.toMap(CategoryEntity::getCatId, CategoryEntity::getName)));
        }
        // 查询分组名称
        Map<Long, Long> attrIdGroupIdMap = new HashMap<>();
        Map<Long, String> attrGroupIdNameMap = new HashMap<>();
        if (attrType.getKey() == AttrType.BASE.getKey()) {
            List<Long> attrIdList = records.stream().map(AttrEntity::getAttrId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(attrIdList)) {
                attrIdGroupIdMap.putAll(getAttrIdGroupIdByAttrIdList(attrIdList));
                if (MapUtils.isNotEmpty(attrIdGroupIdMap)) {
                    List<Long> attrGroupIdList = attrIdGroupIdMap.values().stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(attrGroupIdList)) {
                        attrGroupIdNameMap.putAll(getAttrGroupIdNameByAttrGroupId(attrGroupIdList));
                    }
                }
            }
        }
        return records.stream().map(t -> {
            AttrVO vo = new AttrVO();
            BeanUtils.copyProperties(t, vo);

            // 组装节点名称
            vo.setCatelogName(catelogIdNameMap.get(vo.getCatelogId()));
            // 组装分组名称
            Long attrGroupId = attrIdGroupIdMap.get(vo.getAttrId());
            if (Objects.nonNull(attrGroupId)) {
                vo.setGroupName(attrGroupIdNameMap.get(attrGroupId));
            }
            return vo;
        }).collect(Collectors.toList());
    }

    private Map<Long/*attrGroupId*/, String/*attrGroupName*/> getAttrGroupIdNameByAttrGroupId(List<Long> attrGroupIdList) {
        return attrGroupService.list(Wrappers.<AttrGroupEntity>lambdaQuery()
                .select(AttrGroupEntity::getAttrGroupId, AttrGroupEntity::getAttrGroupName)
                .in(AttrGroupEntity::getAttrGroupId, attrGroupIdList)
        ).stream().collect(Collectors.toMap(AttrGroupEntity::getAttrGroupId, AttrGroupEntity::getAttrGroupName));
    }

    private Map<Long/*attrId*/, Long/*attrGroupId*/> getAttrIdGroupIdByAttrIdList(List<Long> attrIdList) {
        // 万一AttrId与AttrGroupId是一对多的关系就会有问题
        return attrAttrgroupRelationService.list(Wrappers.<AttrAttrgroupRelationEntity>lambdaQuery()
                .select(AttrAttrgroupRelationEntity::getAttrId, AttrAttrgroupRelationEntity::getAttrGroupId)
                .in(AttrAttrgroupRelationEntity::getAttrId, attrIdList)
        ).stream().filter(t->Objects.nonNull(t.getAttrId())).filter(t->Objects.nonNull(t.getAttrGroupId())
        ).collect(Collectors.toMap(AttrAttrgroupRelationEntity::getAttrId, AttrAttrgroupRelationEntity::getAttrGroupId));
    }

    @Override
    public void save(AttrVO attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        this.save(attrEntity);
        AttrEntity entity = this.getById(attrEntity.getAttrId());
        if (AttrType.BASE.getKey() == entity.getAttrType() && Objects.nonNull(attr.getAttrGroupId())) {
            attrAttrgroupRelationService.save(AttrAttrgroupRelationEntity
                    .builder()
                    .attrGroupId(attr.getAttrGroupId())
                    .attrId(entity.getAttrId())
                    .attrSort(null)
                    .build());
        }
    }

    @Override
    public AttrVO info(Long attrId) {
        AttrEntity attrEntity = getById(attrId);
        AttrVO vo = new AttrVO();
        BeanUtils.copyProperties(attrEntity, vo);

        // 设置分组信息
        if (AttrType.BASE.getKey() == attrEntity.getAttrType()) {
            vo.setAttrGroupId(getAttrIdGroupIdByAttrIdList(Collections.singletonList(attrId)).get(attrId));
            vo.setGroupName(Objects.nonNull(vo.getAttrGroupId()) ?
                    getAttrGroupIdNameByAttrGroupId(Collections.singletonList(vo.getAttrGroupId())).get(vo.getAttrGroupId())
                    : null
            );
        }
        // 设置节点信息
        Long catelogId = vo.getCatelogId();
        List<Long> allCid = categoryService.findParentCid(catelogId, new ArrayList<>());
        Long[] array = allCid.toArray(new Long[0]);
        vo.setCatelogPath(array);
        vo.setCatelogName(Optional.ofNullable(categoryService.getById(catelogId)).map(CategoryEntity::getName).orElse(null));
        return vo;
    }

    @Override
    public void update(AttrVO attr) {
        updateById(attr);
        AttrEntity attrEntity = getById(attr.getAttrId());
        // 修改分组关联, 注意分组可能为空
        if (AttrType.BASE.getKey() == attrEntity.getAttrType() && Objects.nonNull(attr.getAttrGroupId())) {
            int count = attrAttrgroupRelationService.count(Wrappers.<AttrAttrgroupRelationEntity>lambdaQuery()
                    .eq(AttrAttrgroupRelationEntity::getAttrId, attr.getAttrId())
                    .eq(AttrAttrgroupRelationEntity::getAttrGroupId, attr.getAttrGroupId()));
            if (count > 0) {
                // 原来有值, 进行修改
                attrAttrgroupRelationService.update(AttrAttrgroupRelationEntity.builder().attrGroupId(attr.getAttrGroupId()).build(),
                        Wrappers.<AttrAttrgroupRelationEntity>lambdaUpdate().eq(AttrAttrgroupRelationEntity::getAttrId, attr.getAttrId()));
            } else {
                // 原来没有值, 进行新增
                attrAttrgroupRelationService.save(AttrAttrgroupRelationEntity.builder().attrId(attr.getAttrId()).attrGroupId(attr.getAttrGroupId()).build());
            }
        }
    }

    @Override
    public List<AttrEntity> getRelationAttr(Long attrGroupId) {
        List<Long> list = attrAttrgroupRelationService.list(Wrappers.<AttrAttrgroupRelationEntity>lambdaQuery().select(AttrAttrgroupRelationEntity::getAttrId)
                .eq(AttrAttrgroupRelationEntity::getAttrGroupId, attrGroupId)
        ).stream().map(AttrAttrgroupRelationEntity::getAttrId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(this.listByIds(list));
    }

    @Override
    public PageUtils listNoAttrRelation(Map<String, Object> params, Long attrGroupId) {
        // 只能关联本分组所属分类中的属性
        // 只能关联别的分组没有引用的属性
        AttrGroupEntity attrGroupEntity = attrGroupService.getById(attrGroupId);
        Long catelogId = attrGroupEntity.getCatelogId();

        List<AttrGroupEntity> otherAttrGroupEntityList = attrGroupService.list(Wrappers.<AttrGroupEntity>lambdaQuery().eq(AttrGroupEntity::getCatelogId, catelogId));

        List<Long> list = otherAttrGroupEntityList.stream().map(AttrGroupEntity::getAttrGroupId).filter(Objects::nonNull).distinct().collect(Collectors.toList());

        List<Long> otherAttrIds = attrAttrgroupRelationService.list(Wrappers.<AttrAttrgroupRelationEntity>lambdaQuery()
                .in(CollectionUtils.isNotEmpty(list), AttrAttrgroupRelationEntity::getAttrGroupId, list)
        ).stream().map(AttrAttrgroupRelationEntity::getAttrId).filter(Objects::nonNull).distinct().collect(Collectors.toList());


        LambdaQueryWrapper<AttrEntity> queryWrapper = Wrappers.<AttrEntity>lambdaQuery()
                .eq(AttrEntity::getCatelogId, catelogId)
                .eq(AttrEntity::getAttrType, AttrType.BASE.getKey())
                .notIn(CollectionUtils.isNotEmpty(otherAttrIds), AttrEntity::getAttrId, otherAttrIds);
        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) {
            queryWrapper.and((obj)->{
                obj.eq(AttrEntity::getAttrId, key).or().like(AttrEntity::getAttrName, key);
            });
        }

        return PageUtils.of(page(new Query<AttrEntity>().getPage(params), queryWrapper));
    }

}