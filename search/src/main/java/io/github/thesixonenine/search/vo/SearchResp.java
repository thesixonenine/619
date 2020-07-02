package io.github.thesixonenine.search.vo;

import io.github.thesixonenine.common.es.SkuModel;
import lombok.Data;

import java.util.List;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/07/01 20:54
 * @since 1.0
 */
@Data
public class SearchResp {
    // 查询到的商品信息
    private List<SkuModel> skuList;

    /*
     * 分页信息
     */

    /**
     * 当前页码
     */
    private Integer pageNum;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 总页码
     */
    private Integer totalPages;

    /**
     * 查询结果中涉及到的品牌
     */
    private List<BrandVO> brands;

    /**
     * 查询结果中涉及到的分类
     */
    private List<CatalogVO> catalogs;

    /**
     * 查询结果中涉及到的属性
     */
    private List<AttrVO> attrs;

    @Data
    public static class BrandVO {
        private Long brandId;
        private String brandName;
        private String brandImg;
    }

    @Data
    public static class CatalogVO {
        private Long catalogId;
        private String categoryName;
    }

    @Data
    public static class AttrVO {
        private Long attrId;
        private String attrName;
        private List<String> attrValue;
    }
}
