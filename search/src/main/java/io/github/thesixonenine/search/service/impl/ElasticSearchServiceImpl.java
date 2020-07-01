package io.github.thesixonenine.search.service.impl;

import com.google.gson.Gson;
import io.github.thesixonenine.common.es.SkuModel;
import io.github.thesixonenine.search.service.ElasticSearchService;
import io.github.thesixonenine.search.util.Const;
import io.github.thesixonenine.search.vo.SearchCondition;
import io.github.thesixonenine.search.vo.SearchResp;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/06/27 10:55
 * @since 1.0
 */
@Slf4j
@Service
public class ElasticSearchServiceImpl implements ElasticSearchService {
    @Autowired
    private RestHighLevelClient client;

    @Override
    public boolean up(List<SkuModel> skuModelList) throws IOException {
        // es建立索引:product  在kibana中执行 ElasticSearch_product_mapping.md 建立映射关系
        // 保存数据
        BulkRequest bulkRequest = new BulkRequest();
        skuModelList.forEach(t -> {
            // 构造保存请求
            IndexRequest indexRequest = new IndexRequest(Const.ES_PRODUCT_INDEX);
            indexRequest.id(String.valueOf(t.getSkuId()));
            indexRequest.source(new Gson().toJson(t), XContentType.JSON);
            bulkRequest.add(indexRequest);
        });

        BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        // 上架失败的
        boolean hasFailures = bulk.hasFailures();
        if (hasFailures) {
            // 如果有错误的
            String s = Arrays.stream(bulk.getItems()).map(BulkItemResponse::getId).collect(Collectors.joining(","));
            log.error("商品上架错误[{}]", s);
        }
        return !hasFailures;
    }

    @Override
    public SearchResp search(SearchCondition condition) {
        return null;
    }
}
