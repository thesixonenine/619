package io.github.thesixonenine.product.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.github.thesixonenine.product.entity.AttrEntity;
import io.github.thesixonenine.product.service.AttrService;
import io.github.thesixonenine.product.vo.AttrGroupVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.Query;

import io.github.thesixonenine.product.dao.AttrGroupDao;
import io.github.thesixonenine.product.entity.AttrGroupEntity;
import io.github.thesixonenine.product.service.AttrGroupService;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    private AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<AttrGroupVO> listAttrGroupWithAttrsByCatelogId(Long catelogId) {
        List<AttrGroupEntity> attrGroupEntityList = list(Wrappers.<AttrGroupEntity>lambdaQuery().eq(AttrGroupEntity::getCatelogId, catelogId));
        List<Long> attrGroupIds = attrGroupEntityList.stream().map(AttrGroupEntity::getAttrGroupId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<Long, List<AttrEntity>> map = attrService.getRelationAttrBatch(attrGroupIds);

        List<AttrGroupVO> voList = new ArrayList<>(attrGroupEntityList.size());
        for (AttrGroupEntity attrGroupEntity : attrGroupEntityList) {
            AttrGroupVO vo = new AttrGroupVO();
            BeanUtils.copyProperties(attrGroupEntity, vo);
            vo.setAttrs(map.get(vo.getAttrGroupId()));
        }
        return voList;
    }

}