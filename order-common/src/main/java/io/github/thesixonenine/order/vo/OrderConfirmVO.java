package io.github.thesixonenine.order.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单确认页需要的数据
 *
 * @author Simple
 * @version 1.0
 * @date 2020/11/12 21:29
 * @since 1.0
 */
@Data
public class OrderConfirmVO {

    /**
     * 收货地址 ums_member_receive_address
     */
    private List<MemberReceiveAddressVO> addressList;

    private List<CartItemVO> cartItemList;

    /**
     * 积分
     */
    private Integer integration;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmt;

    /**
     * 应付/实付金额
     */
    private BigDecimal payAmt;
}
