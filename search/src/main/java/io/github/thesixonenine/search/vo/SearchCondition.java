package io.github.thesixonenine.search.vo;

import lombok.Data;

import java.util.List;

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

    /**
     * 排序
     * saleCount_desc
     * saleCount_asc
     * skuPrice_desc
     * skuPrice_asc
     * hotScore_desc
     * hotScore_asc
     */
    private String sort;

    /**
     * 是否只显示有货[1有0无]
     */
    private Integer hasStock;

    /**
     * 价格区间[1_500/_500/500_]
     */
    private String skuPrice;

    /**
     * 品牌id
     */
    private List<Long> brandId;

    /**
     * 属性
     * eg: attrs=1_安卓:苹果&attrs=2_2G:4G
     * 代表: 1号属性选择 安卓 苹果; 2号属性选择 2G 4G
     */
    private List<String> attrs;

    /**
     * 页码
     */
    private Integer pageNum;
}
