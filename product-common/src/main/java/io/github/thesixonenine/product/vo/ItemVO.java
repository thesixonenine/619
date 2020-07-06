package io.github.thesixonenine.product.vo;

import io.github.thesixonenine.product.entity.SkuImagesEntity;
import io.github.thesixonenine.product.entity.SkuInfoEntity;
import io.github.thesixonenine.product.entity.SpuInfoDescEntity;
import lombok.Data;

import java.util.List;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/07/06 0:20
 * @since 1.0
 */
@Data
public class ItemVO {
    private SkuInfoEntity skuInfoEntity;
    private List<SkuImagesEntity> images;
    private List<ItemSaleAttrsVO> saleAttrsList;
    private SpuInfoDescEntity desc;
    private List<ItemAttrGroupVO> attrGroups;

    @Data
    public static class ItemSaleAttrsVO{
        /**
         * 属性id
         */
        private Long attrId;
        /**
         * 属性名
         */
        private String attrName;
        private List<String> attrValues;
    }

    @Data
    public static class ItemAttrGroupVO {
        private String groupName;
        private List<BaseAttrVO> attrs;
    }

    @Data
    public static class BaseAttrVO{
        private String attrName;
        private String attrValue;
    }
}
