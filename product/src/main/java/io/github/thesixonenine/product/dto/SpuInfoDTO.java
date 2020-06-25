package io.github.thesixonenine.product.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/06/25 13:56
 * @since 1.0
 */
@Data
public class SpuInfoDTO {

    /**
     * spuName : Apple XR
     * spuDescription : Apple XR
     * catalogId : 225
     * brandId : 12
     * weight : 0.048
     * publishStatus : 0
     * decript : ["https://gulimall-hello.oss-cn-beijing.aliyuncs.com/2019-11-22//66d30b3f-e02f-48b1-8574-e18fdf454a32_f205d9c99a2b4b01.jpg"]
     * images : ["https://gulimall-hello.oss-cn-beijing.aliyuncs.com/2019-11-22//dcfcaec3-06d8-459b-8759-dbefc247845e_5b5e74d0978360a1.jpg","https://gulimall-hello.oss-cn-beijing.aliyuncs.com/2019-11-22//5b15e90a-a161-44ff-8e1c-9e2e09929803_749d8efdff062fb0.jpg"]
     * bounds : {"buyBounds":500,"growBounds":6000}
     * baseAttrs : [{"attrId":7,"attrValues":"aaa;bb","showDesc":1},{"attrId":8,"attrValues":"2019","showDesc":0}]
     * skus : [{"attr":[{"attrId":9,"attrName":"颜色","attrValue":"黑色"},{"attrId":10,"attrName":"内存","attrValue":"6GB"}],"skuName":"Apple XR 黑色 6GB","price":"1999","skuTitle":"Apple XR 黑色 6GB","skuSubtitle":"Apple XR 黑色 6GB","images":[{"imgUrl":"https://gulimall-hello.oss-cn-beijing.aliyuncs.com/2019-11-22//dcfcaec3-06d8-459b-8759-dbefc247845e_5b5e74d0978360a1.jpg","defaultImg":1},{"imgUrl":"https://gulimall-hello.oss-cn-beijing.aliyuncs.com/2019-11-22//5b15e90a-a161-44ff-8e1c-9e2e09929803_749d8efdff062fb0.jpg","defaultImg":0}],"descar":["黑色","6GB"],"fullCount":5,"discount":0.98,"countStatus":1,"fullPrice":1000,"reducePrice":10,"priceStatus":0,"memberPrice":[{"id":1,"name":"aaa","price":1998.99}]},{"attr":[{"attrId":9,"attrName":"颜色","attrValue":"黑色"},{"attrId":10,"attrName":"内存","attrValue":"12GB"}],"skuName":"Apple XR 黑色 12GB","price":"2999","skuTitle":"Apple XR 黑色 12GB","skuSubtitle":"Apple XR 黑色 6GB","images":[{"imgUrl":"","defaultImg":0},{"imgUrl":"","defaultImg":0}],"descar":["黑色","12GB"],"fullCount":0,"discount":0,"countStatus":0,"fullPrice":0,"reducePrice":0,"priceStatus":0,"memberPrice":[{"id":1,"name":"aaa","price":1998.99}]},{"attr":[{"attrId":9,"attrName":"颜色","attrValue":"白色"},{"attrId":10,"attrName":"内存","attrValue":"6GB"}],"skuName":"Apple XR 白色 6GB","price":"1998","skuTitle":"Apple XR 白色 6GB","skuSubtitle":"Apple XR 黑色 6GB","images":[{"imgUrl":"","defaultImg":0},{"imgUrl":"","defaultImg":0}],"descar":["白色","6GB"],"fullCount":0,"discount":0,"countStatus":0,"fullPrice":0,"reducePrice":0,"priceStatus":0,"memberPrice":[{"id":1,"name":"aaa","price":1998.99}]},{"attr":[{"attrId":9,"attrName":"颜色","attrValue":"白色"},{"attrId":10,"attrName":"内存","attrValue":"12GB"}],"skuName":"Apple XR 白色 12GB","price":"2998","skuTitle":"Apple XR 白色 12GB","skuSubtitle":"Apple XR 黑色 6GB","images":[{"imgUrl":"","defaultImg":0},{"imgUrl":"","defaultImg":0}],"descar":["白色","12GB"],"fullCount":0,"discount":0,"countStatus":0,"fullPrice":0,"reducePrice":0,"priceStatus":0,"memberPrice":[{"id":1,"name":"aaa","price":1998.99}]}]
     */

    private String spuName;
    private String spuDescription;
    private Integer catalogId;
    private Integer brandId;
    private BigDecimal weight;
    private Integer publishStatus;
    private Bounds bounds;
    private List<String> decript;
    private List<String> images;
    private List<BaseAttrs> baseAttrs;
    private List<Skus> skus;
}
