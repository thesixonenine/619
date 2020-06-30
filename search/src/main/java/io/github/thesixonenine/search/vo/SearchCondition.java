package io.github.thesixonenine.search.vo;

import lombok.Data;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/07/01 0:04
 * @since 1.0
 */
@Data
public class SearchCondition {
    /**
     * 搜索框 全文匹配关键字
     * skuTitle
     */
    private String keyword;
    /**
     * 选择三级分类id
     */
    private Integer catalog3Id;
}
