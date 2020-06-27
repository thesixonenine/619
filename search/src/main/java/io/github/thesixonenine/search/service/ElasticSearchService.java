package io.github.thesixonenine.search.service;

import io.github.thesixonenine.common.es.SkuModel;

import java.io.IOException;
import java.util.List;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/06/27 10:54
 * @since 1.0
 */
public interface ElasticSearchService {
    boolean up(List<SkuModel> skuModelList) throws IOException;
}
