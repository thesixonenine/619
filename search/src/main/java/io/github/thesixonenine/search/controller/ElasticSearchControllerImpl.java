package io.github.thesixonenine.search.controller;

import io.github.thesixonenine.common.es.SkuModel;
import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.search.service.ElasticSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/06/27 10:51
 * @since 1.0
 */
@Slf4j
@RestController
public class ElasticSearchControllerImpl implements ElasticSearchController {
    @Autowired
    private ElasticSearchService elasticSearchService;

    @Override
    public R up(List<SkuModel> skuModelList) {
        boolean b;
        try {
            b = elasticSearchService.up(skuModelList);
        } catch (IOException e) {
            log.error("ES上架商品异常", e);
            return R.error(1, "ES上架商品失败" + e.getMessage());
        }
        return b ? R.ok() : R.error(1, "ES上架商品失败");
    }
}
