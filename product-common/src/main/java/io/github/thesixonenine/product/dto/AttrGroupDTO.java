package io.github.thesixonenine.product.dto;

import io.github.thesixonenine.product.entity.AttrGroupEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/06/13 22:37
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AttrGroupDTO extends AttrGroupEntity {
    private Long[] catelogPath;
}
