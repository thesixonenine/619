package io.github.thesixonenine.product.vo;

import io.github.thesixonenine.product.entity.AttrEntity;
import io.github.thesixonenine.product.entity.AttrGroupEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/06/14 19:15
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttrGroupVO extends AttrGroupEntity {
    private static final long serialVersionUID = 4831854447451255719L;
    private List<AttrEntity> attrs;
}
