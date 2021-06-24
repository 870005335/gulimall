package com.liubin.gulimall.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description ElasticSearch配置类
 * @Author liubin
 * @Date 2021/4/2 17:40
 * @Version 1.0
 */
@Configuration
public class ElasticSearchConfig {

    @Value("${spring.elasticsearch.url}")
    private String url;

    @Value("${spring.elasticsearch.port}")
    private Integer port;

    @Value("${spring.elasticsearch.protocol}")
    private String protocol;


    public static final RequestOptions COMMON_OPTIONS;
    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
//        builder.addHeader("Authorization", "Bearer " + TOKEN);
//        builder.setHttpAsyncResponseConsumerFactory(
//                new HttpAsyncResponseConsumerFactory
//                        .HeapBufferedResponseConsumerFactory(30 * 1024 * 1024 * 1024));
        COMMON_OPTIONS = builder.build();
    }

    @Bean
    public RestHighLevelClient elasticSearchClient() {
        return new RestHighLevelClient(RestClient.builder(new HttpHost(url, port, protocol)));
    }
}
