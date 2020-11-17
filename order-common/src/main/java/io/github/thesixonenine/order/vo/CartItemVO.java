package io.github.thesixonenine.order.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/11/12 21:40
 * @since 1.0
 */
@Data
public class CartItemVO {
    private Long skuId;
    private String title;
    private String image;
    private BigDecimal price;
    private Integer count;
    private BigDecimal totalPrice;
    private List<String> skuAttr;
    private Boolean hasStock;
}
