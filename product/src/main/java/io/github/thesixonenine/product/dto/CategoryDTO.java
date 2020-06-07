package io.github.thesixonenine.product.dto;

import io.github.thesixonenine.product.entity.CategoryEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/06/07 18:33
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CategoryDTO extends CategoryEntity {
    private List<CategoryDTO> children;
}
