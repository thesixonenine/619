package io.github.thesixonenine.product.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/06/25 17:05
 * @since 1.0
 */
@Data
public class Bounds {
    /**
     * buyBounds : 500
     * growBounds : 6000
     */

    private BigDecimal buyBounds;
    private BigDecimal growBounds;
}
