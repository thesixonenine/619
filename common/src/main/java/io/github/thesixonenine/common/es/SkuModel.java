package io.github.thesixonenine.common.es;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Sku在ES中的模型
 *
 * @author Simple
 * @version 1.0
 * @date 2020/06/27 0:09
 * @since 1.0
 */
@Data
public class SkuModel {
// {
//     "mappings":{
//         "properties":{
//             "skuId":{
//                 "type":"long"
//             },
//             "spuId":{
//                 "type":"keyword"
//             },
//             "skuTitle":{
//                 "type":"text"
//             },
//             "price":{
//                 "type":"keyword"
//             },
//             "skuDefaultImg":{
//                 "type":"keyword",
//                 "index": false,
//                 "doc_values": false
//             },
//             "saleCount":{
//                 "type":"long"
//             },
//             "hasStock":{
//                 "type":"boolean"
//             },
//             "hotScore":{
//                 "type":"long"
//             },
//             "brandId":{
//                 "type":"long"
//             },
//             "catalogId":{
//                 "type":"long"
//             },
//             "brandName":{
//                 "type":"keyword",
//                 "index": false,
//                 "doc_values": false
//             },
//             "brandImg":{
//                 "type":"keyword",
//                 "index": false,
//                 "doc_values": false
//             },
//             "categoryName":{
//                 "type":"keyword",
//                 "index": false,
//                 "doc_values": false
//             },
//             "attrs":{
//                 "type": "nested",
//                 "properties":{
//                     "attrId":{
//                         "type":"long"
//                     },
//                     "attrName":{
//                         "type":"keyword",
//                         "index": false,
//                         "doc_values": false
//                     },
//                     "attrValue":{
//                         "type":"keyword"
//                     }
//                 }
//             }
//         }
//     }
// }
    private Long skuId;
    private Long spuId;
    private String skuTitle;
    private BigDecimal price;
    private String skuDefaultImg;
    private Long saleCount;
    private Boolean hasStock;
    private Long hotScore;
    private Long brandId;
    private Long catalogId;
    private String brandName;
    private String brandImg;
    private String categoryName;
    private List<Attr> attrs;

    @Data
    public static class Attr {
        private Long attrId;
        private String attrName;
        private String attrValue;
    }
}
