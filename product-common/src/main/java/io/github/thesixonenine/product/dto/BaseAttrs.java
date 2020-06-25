package io.github.thesixonenine.product.dto;

import lombok.Data;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/06/25 17:06
 * @since 1.0
 */
@Data
public class BaseAttrs {
    /**
     * attrId : 7
     * attrValues : aaa;bb
     * showDesc : 1
     */

    private Long attrId;
    private String attrValues;
    private Integer showDesc;
}
