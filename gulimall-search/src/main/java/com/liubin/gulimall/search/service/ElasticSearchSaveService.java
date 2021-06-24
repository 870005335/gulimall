package com.liubin.gulimall.search.service;

import com.liubin.common.dto.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * @Description ElasticSearch保存数据Service接口
 * @Author liubin
 * @Date 2021/4/10 12:07
 * @Version 1.0
 */
public interface ElasticSearchSaveService {

    /**
     * @Author liubin
     * @Description 商品上架保存数据到ES
     * @Date 12:10 2021/4/10
     * @param esModelList
     * @return
     **/
    void productStatusUp(List<SkuEsModel> esModelList) throws IOException;
}
