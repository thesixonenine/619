package io.github.thesixonenine.order.vo;

import org.apache.commons.collections4.CollectionUtils;

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
public class OrderConfirmVO {

    /**
     * 防重令牌
     */
    private String orderToken;

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
     * 商品总数量
     */
    private Integer skuCount;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmt;

    /**
     * 应付/实付金额
     */
    private BigDecimal payAmt;


    public OrderConfirmVO() {
    }

    public BigDecimal getTotalAmt() {
        return amt();
    }

    public BigDecimal getPayAmt() {
        return amt();
    }

    private BigDecimal amt() {
        BigDecimal result = BigDecimal.ZERO;
        if (CollectionUtils.isEmpty(cartItemList)) {
            return result;
        }
        for (CartItemVO cartItemVO : cartItemList) {
            result = result.add(cartItemVO.getPrice().multiply(new BigDecimal(cartItemVO.getCount())));
        }
        return result;
    }

    public Integer getSkuCount() {
        return cartItemList.stream().mapToInt(t->t.getCount()).sum();
    }

    public String getOrderToken() {
        return orderToken;
    }

    public List<MemberReceiveAddressVO> getAddressList() {
        return addressList;
    }

    public List<CartItemVO> getCartItemList() {
        return cartItemList;
    }

    public Integer getIntegration() {
        return integration;
    }

    public void setOrderToken(String orderToken) {
        this.orderToken = orderToken;
    }

    public void setAddressList(List<MemberReceiveAddressVO> addressList) {
        this.addressList = addressList;
    }

    public void setCartItemList(List<CartItemVO> cartItemList) {
        this.cartItemList = cartItemList;
    }

    public void setIntegration(Integer integration) {
        this.integration = integration;
    }
}
