package io.github.thesixonenine.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/06/27 14:07
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Catalog2VO {
    private String catalog1Id;
    private List<Catalog3VO> catalog3List;
    private String id;
    private String name;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Catalog3VO {
        private String catalog2Id;
        private String id;
        private String name;
    }
}
