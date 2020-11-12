package io.github.thesixonenine.cart.vo;

import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 购物车
 *
 * @author Simple
 * @version 1.0
 * @date 2020/09/10 21:38
 * @since 1.0
 */
public class Cart {

    private List<CartItem> itemList;

    /**
     * 商品数量
     */
    private Integer countNum;

    /**
     * 商品类型数量
     */
    private Integer countType;

    /**
     * 商品总价
     */
    private BigDecimal amount;

    /**
     * 减免价格
     */
    private BigDecimal reduce = BigDecimal.ZERO;

    public List<CartItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<CartItem> itemList) {
        this.itemList = itemList;
    }

    public Integer getCountNum() {
        if (CollectionUtils.isNotEmpty(this.itemList)) {
            return itemList.stream().mapToInt(CartItem::getCount).sum();
        }
        return 0;
    }

    // public void setCountNum(Integer countNum) {
    //     this.countNum = countNum;
    // }

    public Integer getCountType() {
        if (CollectionUtils.isNotEmpty(this.itemList)) {
            return itemList.size();
        }
        return 0;
    }

    // public void setCountType(Integer countType) {
    //     this.countType = countType;
    // }

    public BigDecimal getAmount() {
        BigDecimal result = BigDecimal.ZERO;
        if (CollectionUtils.isNotEmpty(this.itemList)) {
            result = result.add(itemList.stream().filter(t-> (Objects.nonNull(t.getCheck()) && t.getCheck())).map(CartItem::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
        }
        result = result.subtract(getReduce());
        if (result.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        return result;
    }

    // public void setAmount(BigDecimal amount) {
    //     this.amount = amount;
    // }

    public BigDecimal getReduce() {
        return reduce;
    }

    public void setReduce(BigDecimal reduce) {
        this.reduce = reduce;
    }
}
