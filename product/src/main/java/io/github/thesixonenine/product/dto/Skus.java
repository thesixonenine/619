package io.github.thesixonenine.product.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/06/25 17:06
 * @since 1.0
 */
@Data
public class Skus {
    /**
     * attr : [{"attrId":9,"attrName":"颜色","attrValue":"黑色"},{"attrId":10,"attrName":"内存","attrValue":"6GB"}]
     * skuName : Apple XR 黑色 6GB
     * price : 1999
     * skuTitle : Apple XR 黑色 6GB
     * skuSubtitle : Apple XR 黑色 6GB
     * images : [{"imgUrl":"https://gulimall-hello.oss-cn-beijing.aliyuncs.com/2019-11-22//dcfcaec3-06d8-459b-8759-dbefc247845e_5b5e74d0978360a1.jpg","defaultImg":1},{"imgUrl":"https://gulimall-hello.oss-cn-beijing.aliyuncs.com/2019-11-22//5b15e90a-a161-44ff-8e1c-9e2e09929803_749d8efdff062fb0.jpg","defaultImg":0}]
     * descar : ["黑色","6GB"]
     * fullCount : 5
     * discount : 0.98
     * countStatus : 1
     * fullPrice : 1000
     * reducePrice : 10
     * priceStatus : 0
     * memberPrice : [{"id":1,"name":"aaa","price":1998.99}]
     */

    private String skuName;
    private BigDecimal price;
    private String skuTitle;
    private String skuSubtitle;
    private Integer fullCount;
    private BigDecimal discount;
    private Integer countStatus;
    private Integer fullPrice;
    private Integer reducePrice;
    private Integer priceStatus;
    private List<Attr> attr;
    private List<ImagesBean> images;
    private List<String> descar;
    private List<MemberPriceBean> memberPrice;
}
