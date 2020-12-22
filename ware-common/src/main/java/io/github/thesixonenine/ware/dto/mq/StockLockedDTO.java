package io.github.thesixonenine.ware.dto.mq;

import lombok.Data;

import java.util.List;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/12/21 23:55
 * @since 1.0
 */
@Data
public class StockLockedDTO {

    /**
     * 工作单id
     */
    private Long taskId;
    /**
     * 工作单详情id
     */
    private Long detailId;
    /**
     * sku_id
     */
    private Long skuId;
    /**
     * 购买个数
     */
    private Integer skuNum;
    /**
     * 仓库id
     */
    private Long wareId;
    /**
     * 状态: 1-锁定 2-解锁 3-已扣减
     */
    private Integer lockStatus;
}
