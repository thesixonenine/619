package io.github.thesixonenine.ware.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.thesixonenine.ware.dto.mq.StockLockedDTO;
import io.github.thesixonenine.ware.entity.WareSkuEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品库存
 *
 * @author thesixonenine
 * @date 2020-06-06 01:52:04
 */
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    /**
     * 查商品在那些仓库有库存
     *
     * @param skuId 商品id
     * @return 仓库id
     */
    List<Long> selectWareId(@Param(value = "skuId") Long skuId);

    Integer lockStock(@Param(value = "skuId") Long skuId,
                      @Param(value = "wareId") Long wareId,
                      @Param(value = "num") Integer num);

    int unLockStock(StockLockedDTO dto);
}
