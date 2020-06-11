package io.github.thesixonenine.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/06/10 22:45
 * @since 1.0
 */
@Configuration
public class ElasticConfig {
    @Bean
    public RestHighLevelClient esRestClient() {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("192.168.137.10", 9200, "http")
                        ));
        return client;
    }
}
