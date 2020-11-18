package io.github.thesixonenine.order.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单提交数据
 *
 * @author Simple
 * @version 1.0
 * @date 2020/11/18 23:33
 * @since 1.0
 */
@Data
public class CreateOrderReq implements Serializable {
    private static final long serialVersionUID = -508151183229318348L;

    private Long addrId;

    private Integer payType;

    // 不需要商品信息, 再去购物车查询一次即可

    private BigDecimal fare;

    private String orderToken;

    private BigDecimal payPrice;

    // 不需要用户信息, session中有
}
