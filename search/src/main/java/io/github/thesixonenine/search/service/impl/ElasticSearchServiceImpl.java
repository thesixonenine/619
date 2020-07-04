package io.github.thesixonenine.search.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.thesixonenine.common.es.SkuModel;
import io.github.thesixonenine.search.service.ElasticSearchService;
import io.github.thesixonenine.search.util.Const;
import io.github.thesixonenine.search.vo.SearchCondition;
import io.github.thesixonenine.search.vo.SearchResp;
import io.github.thesixonenine.search.vo.SearchResp.AttrVO;
import io.github.thesixonenine.search.vo.SearchResp.BrandVO;
import io.github.thesixonenine.search.vo.SearchResp.CatalogVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
    @Resource
    private RestHighLevelClient client;
    private static final int AGGS_SIZE = 100;
    private static final int PAGE_SIZE = 2;

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
        SearchRequest searchRequest = buildRequest(condition);
        // 3. 执行检索
        try {
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            return buildResponse(response, condition);
        } catch (Exception ignored) {

        }
        return null;
    }

    /**
     * - 模糊匹配
     * - 过滤(属性, 分类, 品牌, 价格区间, 库存)
     * - 排序
     * - 分页
     * - 高亮
     * - 聚合分析
     *
     * @param condition 传入的条件
     * @return 构造好的请求
     */
    private SearchRequest buildRequest(SearchCondition condition) {
        // 1. 创建请求
        SearchRequest searchRequest = new SearchRequest();
        // 执行索引
        searchRequest.indices(Const.ES_PRODUCT_INDEX);
        // 2. 指定条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        // 模糊匹配
        if (StringUtils.isNotBlank(condition.getKeyword())) {
            boolQuery.must(QueryBuilders.matchQuery("skuTitle", condition.getKeyword()));
        }
        // 过滤(属性, 分类, 品牌, 价格区间, 库存)
        if (Objects.nonNull(condition.getCatalog3Id()) && condition.getCatalog3Id() > 0) {
            boolQuery.filter(QueryBuilders.termQuery("catalogId", condition.getCatalog3Id()));
        }
        if (Objects.nonNull(condition.getHasStock())) {
            if (1 == condition.getHasStock()) {
                boolQuery.filter(QueryBuilders.termQuery("hasStock", "true"));
            } else if (0 == condition.getHasStock()) {
                boolQuery.filter(QueryBuilders.termQuery("hasStock", "false"));
            }
        }
        if (CollectionUtils.isNotEmpty(condition.getBrandId())) {
            boolQuery.filter(QueryBuilders.termsQuery("brandId", condition.getBrandId()));
        }
        if (CollectionUtils.isNotEmpty(condition.getAttrs())) {
            condition.getAttrs().forEach(attr -> {
                // eg: attrs=1_安卓:苹果&attrs=2_2G:4G
                String[] s = attr.split("_");
                String attrId = s[0];
                String attrValues = s[1];
                List<String> attrValueList = Arrays.stream(attrValues.split(":")).collect(Collectors.toList());
                BoolQueryBuilder nestBoolQuery = QueryBuilders.boolQuery();
                nestBoolQuery.must(QueryBuilders.termQuery("attrs.attrId", attrId));
                nestBoolQuery.must(QueryBuilders.termsQuery("attrs.attrValue", attrValueList));
                boolQuery.filter(QueryBuilders.nestedQuery("attrs", nestBoolQuery, ScoreMode.None));
            });
        }
        if (StringUtils.isNotBlank(condition.getSkuPrice())) {
            // 价格区间[1_500/_500/500_]
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("price");
            String[] s = condition.getSkuPrice().split("_");
            String start = s[0];
            String end = "";
            if (s.length > 1) {
                end = s[1];
            }
            if (StringUtils.isNotBlank(start)) {
                rangeQuery.gte(start);
            }
            if (StringUtils.isNotBlank(end)) {
                rangeQuery.lte(end);
            }
            boolQuery.filter(rangeQuery);
        }

        builder.query(boolQuery);

        // 排序
        if (StringUtils.isNotBlank(condition.getSort())) {
            // saleCount_desc
            // skuPrice_asc
            String[] s = condition.getSort().split("_");
            String field = s[0];
            String order = s[1];
            FieldSortBuilder sortBuilder = SortBuilders.fieldSort(field);
            if (StringUtils.equalsIgnoreCase("desc", order)) {
                sortBuilder.order(SortOrder.DESC);
            } else if (StringUtils.equalsIgnoreCase("asc", order)) {
                sortBuilder.order(SortOrder.ASC);
            }
            builder.sort(sortBuilder);
        }

        // 分页
        builder.from((condition.getPageNum() - 1) * PAGE_SIZE);
        builder.size(PAGE_SIZE);

        // 聚合
        builder.aggregation(AggregationBuilders.terms("brand_agg").field("brandId").size(AGGS_SIZE)
                .subAggregation(AggregationBuilders.terms("brand_name_agg").field("brandName").size(1))
                .subAggregation(AggregationBuilders.terms("brand_img_agg").field("brandImg").size(1))
        );
        builder.aggregation(AggregationBuilders.terms("catalog_agg").field("catalogId").size(AGGS_SIZE)
                .subAggregation(AggregationBuilders.terms("catalog_name_agg").field("categoryName").size(1)));
        builder.aggregation(
                AggregationBuilders.nested("attr_agg", "attrs")
                        .subAggregation(
                                AggregationBuilders.terms("attr_id_agg").field("attrs.attrId").size(AGGS_SIZE)
                                        .subAggregation(AggregationBuilders.terms("attr_name_agg").field("attrs.attrName").size(1))
                                        .subAggregation(AggregationBuilders.terms("attr_value_agg").field("attrs.attrValue").size(AGGS_SIZE))
                        )
        );

        // 高亮
        if (StringUtils.isNotBlank(condition.getKeyword())) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuTitle");
            highlightBuilder.preTags("<b style='color:red'>");
            highlightBuilder.postTags("</b>");
            builder.highlighter(highlightBuilder);
        }

        log.debug("DSL语句: [{}]", builder.toString());

        searchRequest.source(builder);
        return searchRequest;
    }

    private SearchResp buildResponse(SearchResponse response, SearchCondition condition) {
        SearchHits searchHits = response.getHits();
        SearchResp resp = new SearchResp();
        // 第一部分: 商品信息
        SearchHit[] hits = searchHits.getHits();
        if (ArrayUtils.isNotEmpty(hits)) {
            List<SkuModel> skuList = new ArrayList<>(hits.length);
            for (SearchHit hit : hits) {
                String source = hit.getSourceAsString();
                SkuModel skuModel = new Gson().<SkuModel>fromJson(source, new TypeToken<SkuModel>() {
                }.getType());
                if (StringUtils.isNotBlank(condition.getKeyword())) {
                    // 有关键字, 需要高亮
                    skuModel.setSkuTitle(hit.getHighlightFields().get("skuTitle").getFragments()[0].toString());
                }
                skuList.add(skuModel);
            }
            resp.setSkuList(skuList);
        }
        // 第二部分: 商品聚合信息
        Aggregations aggs = response.getAggregations();

        ParsedLongTerms brandAgg = aggs.get("brand_agg");
        List<BrandVO> brands = new ArrayList<>();
        for (Terms.Bucket bucket : brandAgg.getBuckets()) {
            BrandVO vo = new BrandVO();
            vo.setBrandId(bucket.getKeyAsNumber().longValue());
            Aggregations aggregations = bucket.getAggregations();
            ParsedStringTerms brandNameAgg = aggregations.get("brand_name_agg");
            vo.setBrandName(brandNameAgg.getBuckets().get(0).getKeyAsString());
            ParsedStringTerms brandImgAgg = aggregations.get("brand_img_agg");
            vo.setBrandImg(brandImgAgg.getBuckets().get(0).getKeyAsString());
            brands.add(vo);
        }
        resp.setBrands(brands);

        ParsedLongTerms catalogAgg = aggs.get("catalog_agg");
        List<CatalogVO> catalogs = new ArrayList<>();
        for (Terms.Bucket bucket : catalogAgg.getBuckets()) {
            CatalogVO vo = new CatalogVO();
            vo.setCatalogId(bucket.getKeyAsNumber().longValue());
            ParsedStringTerms catalogNameAgg = bucket.getAggregations().get("catalog_name_agg");
            vo.setCategoryName(catalogNameAgg.getBuckets().get(0).getKeyAsString());
            catalogs.add(vo);
        }
        resp.setCatalogs(catalogs);

        ParsedNested attrAgg = aggs.get("attr_agg");
        List<AttrVO> attrs = new ArrayList<>();
        ParsedLongTerms attrIdAgg = attrAgg.getAggregations().get("attr_id_agg");
        for (Terms.Bucket bucket : attrIdAgg.getBuckets()) {
            AttrVO vo = new AttrVO();
            vo.setAttrId(bucket.getKeyAsNumber().longValue());
            ParsedStringTerms attrNameAgg = bucket.getAggregations().get("attr_name_agg");
            vo.setAttrName(attrNameAgg.getBuckets().get(0).getKeyAsString());
            ParsedStringTerms attrValueAgg = bucket.getAggregations().get("attr_value_agg");
            vo.setAttrValue(attrValueAgg.getBuckets().stream().map(MultiBucketsAggregation.Bucket::getKeyAsString).collect(Collectors.toList()));
            attrs.add(vo);
        }
        resp.setAttrs(attrs);


        // 第三部分: 分页信息

        // 页码 从请求中获得
        resp.setPageNum(condition.getPageNum());
        // 总记录数
        long total = searchHits.getTotalHits().value;
        resp.setTotal(total);
        // 总页码
        resp.setTotalPages((int) (total % PAGE_SIZE == 0 ? total / PAGE_SIZE : total / PAGE_SIZE + 1));

        List<Integer> pageNavs = new ArrayList<>();
        for (int i = 1; i <= resp.getTotalPages(); i++) {
            pageNavs.add(i);
        }
        resp.setPageNavs(pageNavs);
        return resp;
    }
}
