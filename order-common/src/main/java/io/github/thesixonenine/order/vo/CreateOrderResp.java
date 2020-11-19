package io.github.thesixonenine.order.vo;

import io.github.thesixonenine.order.entity.OrderEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/11/19 23:18
 * @since 1.0
 */
@Data
public class CreateOrderResp implements Serializable {
    private static final long serialVersionUID = -871250011830245756L;

    private OrderEntity orderEntity;

    /**
     * 状态码 0 - 成功
     */
    private Integer code;

}
