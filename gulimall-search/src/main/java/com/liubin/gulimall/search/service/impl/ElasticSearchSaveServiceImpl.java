package com.liubin.gulimall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.liubin.common.dto.es.SkuEsModel;
import com.liubin.gulimall.search.config.ElasticSearchConfig;
import com.liubin.gulimall.search.constant.EsConstant;
import com.liubin.gulimall.search.service.ElasticSearchSaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class ElasticSearchSaveServiceImpl implements ElasticSearchSaveService {

    @Autowired
    private RestHighLevelClient elasticSearchClient;

    @Override
    public void productStatusUp(List<SkuEsModel> esModelList) throws IOException {
        // 向ES建立所有 并建立映射关系
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModel model : esModelList) {
            // 构造保存请求
            IndexRequest indexRequest = new IndexRequest(EsConstant.PRODUCT_INDEX);
            indexRequest.id(model.getSkuId().toString());
            indexRequest.source(JSON.toJSONString(model), XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        elasticSearchClient.bulk(bulkRequest, ElasticSearchConfig.COMMON_OPTIONS);
        // TODO 对上架错误的商品重新处理
    }
}
