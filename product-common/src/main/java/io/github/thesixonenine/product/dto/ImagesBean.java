package io.github.thesixonenine.product.dto;

import lombok.Data;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/06/25 17:08
 * @since 1.0
 */
@Data
public class ImagesBean {
    /**
     * imgUrl : https://gulimall-hello.oss-cn-beijing.aliyuncs.com/2019-11-22//dcfcaec3-06d8-459b-8759-dbefc247845e_5b5e74d0978360a1.jpg
     * defaultImg : 1
     */

    private String imgUrl;
    private Integer defaultImg;
}
