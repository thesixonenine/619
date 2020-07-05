package io.github.thesixonenine.product.service.impl;

import io.github.thesixonenine.product.vo.ItemVO;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.Query;

import io.github.thesixonenine.product.dao.SkuInfoDao;
import io.github.thesixonenine.product.entity.SkuInfoEntity;
import io.github.thesixonenine.product.service.SkuInfoService;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public ItemVO item(Long skuId) {
        // 1. sku基本信息 pms_sku_info
        // 2. sku图片信息 pms_sku_images
        // 3. spu销售属性
        // 4. spu介绍信息
        // 5. spu规格参数
        return null;
    }

}