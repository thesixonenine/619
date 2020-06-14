package io.github.thesixonenine.product.vo;

import io.github.thesixonenine.product.entity.AttrEntity;
import lombok.*;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/06/14 1:17
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttrVO extends AttrEntity {
    private static final long serialVersionUID = 4479668556592772375L;
    /**
     * 分组Id
     */
    private Long attrGroupId;
    /**
     * 所属分类名称
     */
    private String catelogName;
    /**
     * 所属分组名称
     */
    private String groupName;

    /**
     * 分类完整路径
     */
    private Long[] catelogPath;
}
