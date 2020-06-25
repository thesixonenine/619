package io.github.thesixonenine.product.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/06/25 17:09
 * @since 1.0
 */
@Data
public class MemberPriceBean {
    /**
     * id : 1
     * name : aaa
     * price : 1998.99
     */

    private Integer id;
    private String name;
    private BigDecimal price;
}
