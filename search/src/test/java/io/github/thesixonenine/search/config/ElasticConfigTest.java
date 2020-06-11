package io.github.thesixonenine.search.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import lombok.Data;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticConfigTest {

    @Autowired
    private RestHighLevelClient client;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    @Test
    public void testSearchData() throws IOException {
        // 1. 创建请求
        SearchRequest searchRequest = new SearchRequest();
        // 执行索引
        searchRequest.indices("bank");
        // 2. 指定条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
        MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("balance", 39225);
        System.out.println("检索条件:" + queryBuilder.toString());
        builder.query(queryBuilder);
        // 聚合
        TermsAggregationBuilder balanceAgg = AggregationBuilders.terms("balanceAgg").field("balance").size(10);
        builder.aggregation(balanceAgg);

        searchRequest.source(builder);
        // 3. 执行检索
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println("执行结果:" +gson.toJson(JsonParser.parseString(response.toString())));
        // 4. 分析结果
        SearchHits hits = response.getHits();
        SearchHit[] hitsHits = hits.getHits();
        for (SearchHit searchHit : hitsHits) {

            String index = searchHit.getIndex();
            String id = searchHit.getId();
            String type = searchHit.getType();
        }
    }

    @Test
    public void testIndexData() throws IOException {
        IndexRequest request = new IndexRequest("users");
        request.id("1");
        User user = new User();
        user.setId(1L);
        user.setName("Simple");
        user.setBalance(BigDecimal.TEN);

        ObjectMapper objectMapper = new ObjectMapper();
        String value = objectMapper.writeValueAsString(user);
        request.source(value, XContentType.JSON);
        System.out.println(client.getClass());
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        System.out.println(response);
    }

    @Test
    public void testESRestClient() {
        System.out.println(client.getClass());
    }

    @Data
    class User {
        private Long id;
        private String name;
        private BigDecimal balance;
        private int age;
    }
}