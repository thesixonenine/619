package io.github.thesixonenine.search.controller;

import io.github.thesixonenine.common.es.SkuModel;
import io.github.thesixonenine.common.utils.R;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/06/27 10:50
 * @since 1.0
 */
@Api(value = "ElasticSearch")
@FeignClient(name = "search")
public interface ElasticSearchController {

    @PostMapping(value = "/search/save/sku")
    R up(@RequestBody List<SkuModel> skuModelList);
}
