package io.github.thesixonenine.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.Query;
import io.github.thesixonenine.product.dao.CategoryDao;
import io.github.thesixonenine.product.dto.CategoryDTO;
import io.github.thesixonenine.product.entity.CategoryBrandRelationEntity;
import io.github.thesixonenine.product.entity.CategoryEntity;
import io.github.thesixonenine.product.service.CategoryBrandRelationService;
import io.github.thesixonenine.product.service.CategoryService;
import io.github.thesixonenine.product.vo.Catalog2VO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryDTO> listTree() {
        // 查询所有的节点
        List<CategoryEntity> all = list();
        // 将对象转换成DTO
        List<CategoryDTO> allDTO = new ArrayList<>(all.size());
        all.forEach(t -> {
            CategoryDTO dto = new CategoryDTO();
            BeanUtils.copyProperties(t, dto);
            allDTO.add(dto);
        });
        // 对DTO筛选根节点数据
        List<CategoryDTO> rootDTO = allDTO.stream().filter(t -> t.getParentCid() == 0).collect(Collectors.toList());
        // 根节点数据排序, 循环设置子节点并返回
        return rootDTO.stream().sorted(Comparator.comparingInt(CategoryEntity::getSort))
                .peek(dto -> setChildren(dto, allDTO))
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> findParentCid(Long catelogId, List<Long> allCidList) {
        allCidList.add(catelogId);
        CategoryEntity categoryEntity = this.getOne(Wrappers.<CategoryEntity>lambdaQuery().select(CategoryEntity::getParentCid).eq(CategoryEntity::getCatId, catelogId));
        if (Objects.nonNull(categoryEntity) && Objects.nonNull(categoryEntity.getParentCid()) &&
                categoryEntity.getParentCid() > 0) {
            findParentCid(categoryEntity.getParentCid(), allCidList);
        }
        Collections.reverse(allCidList);
        return allCidList;
    }

    @Override
    public void updateWithBrand(CategoryEntity category) {
        updateById(category);
        // 更新冗余字段
        if (StringUtils.isNotBlank(category.getName())) {
            categoryBrandRelationService.update(CategoryBrandRelationEntity.builder().catelogName(category.getName()).build(),
                    Wrappers.<CategoryBrandRelationEntity>lambdaUpdate().eq(CategoryBrandRelationEntity::getBrandId, category.getCatId()));
        }
    }

    /**
     * 设置菜单的子菜单
     *
     * @param categoryDTO 当前菜单
     * @param allDTO      所有菜单
     */
    private void setChildren(CategoryDTO categoryDTO, List<CategoryDTO> allDTO) {
        Long catId = categoryDTO.getCatId();
        List<CategoryDTO> list = allDTO.stream()
                .filter(t -> t.getParentCid().equals(catId))
                .sorted(Comparator.comparingInt(CategoryEntity::getSort))
                .peek(dto -> setChildren(dto, allDTO))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(list)) {
            categoryDTO.setChildren(list);
        }
    }

    /**
     * 门户侧所需接口
     */

    @Override
    public List<CategoryEntity> catalogLevel1() {
        return list(Wrappers.<CategoryEntity>lambdaQuery()
                .eq(CategoryEntity::getParentCid, 0)
                .eq(CategoryEntity::getCatLevel, 1)
        );
    }

    @Override
    public Map<String, List<Catalog2VO>> catalog() {
        long id = Thread.currentThread().getId();
        String catalog = redisTemplate.opsForValue().get("catalog");
        if (StringUtils.isBlank(catalog)) {
            // 需要从数据库中拿, 但是要加分布式锁
            RLock lock = redissonClient.getLock("catalog-lock");
            log.debug("线程[{}]没有从redis中拿到数据, 等待拿锁", id);
            lock.lock();
            String catalog1 = redisTemplate.opsForValue().get("catalog");
            if (StringUtils.isBlank(catalog1)) {
                log.debug("线程[{}]已拿到锁, 但redis中没有数据, 去数据库拿数据并写入redis后释放锁", id);
                Map<String, List<Catalog2VO>> map = getCatalogFormDB();

                String s = new Gson().toJson(map);
                redisTemplate.opsForValue().set("catalog", s, 60, TimeUnit.SECONDS);

                lock.unlock();
                log.debug("线程[{}]已释放锁并返回", id);
                return map;
            } else {
                lock.unlock();
                log.debug("线程[{}]已拿到锁, 且已从redis中拿到数据, 直接返回", id);

                return new Gson().fromJson(catalog1, new TypeToken<Map<String, List<Catalog2VO>>>() {
                }.getType());

            }
        }
        log.debug("线程[{}]无需拿锁, 且已从redis中拿到数据, 直接返回", id);

        return new Gson().fromJson(catalog, new TypeToken<Map<String, List<Catalog2VO>>>() {
        }.getType());
    }

    private Map<String, List<Catalog2VO>> getCatalogFormRedis() {
        // 缓存更新策略
        // 双写
        // 失效

        // 失效模式 + 分布式读写锁

        // canal 订阅MySQL的binlog来更新redis  解决数据异构 (访问记录表, 订单表, 商品表, 购物车表) => 推荐表
        return null;
    }

    private Map<String, List<Catalog2VO>> getCatalogFormDB() {
        log.debug("线程[{}]执行数据库查询", Thread.currentThread().getId());
        // 查询一级分类
        List<CategoryEntity> list = list(Wrappers.<CategoryEntity>lambdaQuery()
                .eq(CategoryEntity::getParentCid, 0)
                .eq(CategoryEntity::getCatLevel, 1)
        );
        return list.stream().collect(Collectors.toMap(k -> String.valueOf(k.getCatId()), v -> {
            List<CategoryEntity> list2 = list(Wrappers.<CategoryEntity>lambdaQuery()
                    .eq(CategoryEntity::getParentCid, v.getCatId())
                    .eq(CategoryEntity::getCatLevel, 2)
            );
            if (CollectionUtils.isNotEmpty(list2)) {
                return list2.stream().map(t -> {
                    Long catId2 = t.getCatId();
                    Catalog2VO catalog2VO = new Catalog2VO(String.valueOf(v.getCatId()), null, String.valueOf(catId2), t.getName());
                    List<CategoryEntity> list3 = list(Wrappers.<CategoryEntity>lambdaQuery()
                            .eq(CategoryEntity::getParentCid, catId2)
                            .eq(CategoryEntity::getCatLevel, 3)
                    );
                    if (CollectionUtils.isNotEmpty(list3)) {
                        List<Catalog2VO.Catalog3VO> catalog3VOList = list3.stream().map(item -> new Catalog2VO.Catalog3VO(String.valueOf(catId2), String.valueOf(item.getCatId()), item.getName())).collect(Collectors.toList());
                        catalog2VO.setCatalog3List(catalog3VOList);
                    }

                    return catalog2VO;
                }).collect(Collectors.toList());
            }
            return new ArrayList<>();
        }));
    }

}