package com.liubin.gulimall.search;

import com.alibaba.fastjson.JSONObject;
import com.liubin.gulimall.search.config.ElasticSearchConfig;
import lombok.Data;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@SpringBootTest
class GuLiMallSearchApplicationTests {

    @Autowired
    private RestHighLevelClient elasticSearchClient;

//    @Test
//    public void searchData() throws IOException {
//        SearchRequest searchRequest = new SearchRequest();
//        searchRequest.indices("bank");
//        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//        sourceBuilder.query(QueryBuilders.matchAllQuery());
//        sourceBuilder.aggregation(AggregationBuilders.terms("ageAgg").field("age").size(100).subAggregation(AggregationBuilders.avg("balanceAvg").field("balance")));
//        searchRequest.source(sourceBuilder);
//        SearchResponse searchResponse = elasticSearchClient.search(searchRequest, ElasticSearchConfig.COMMON_OPTIONS);
//        System.out.println(searchResponse);
//    }
//
//    @Test
//    public void getData() throws IOException {
//        GetRequest getRequest = new GetRequest("users", "1");
//        GetResponse response = elasticSearchClient.get(getRequest, ElasticSearchConfig.COMMON_OPTIONS);
//        System.out.println(response);
//    }
//
//    @Test
//    public void indexData() throws IOException {
//        IndexRequest indexRequest = new IndexRequest("users");
//        indexRequest.id("1");
//        User user = new User();
//        user.setName("zhangsan");
//        user.setGender("男");
//        user.setAge(18);
//        indexRequest.source(JSONObject.toJSONString(user), XContentType.JSON);
//        IndexResponse response = elasticSearchClient.index(indexRequest, ElasticSearchConfig.COMMON_OPTIONS);
//        System.out.println(response);
//    }
//
//    @Test
//    public void contextLoads() {
//        System.out.println(JSONObject.toJSONString(elasticSearchClient));
//    }
//
//    @Data
//    static class User {
//        private String name;
//
//        private String gender;
//
//        private Integer age;
//    }
}
