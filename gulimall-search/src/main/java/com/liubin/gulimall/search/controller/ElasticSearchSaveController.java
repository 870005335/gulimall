package com.liubin.gulimall.search.controller;

import com.liubin.common.dto.es.SkuEsModel;
import com.liubin.common.enums.BizCodeEnum;
import com.liubin.common.utils.R;
import com.liubin.gulimall.search.service.ElasticSearchSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @Description ElasticSearch保存数据接口
 * @Author liubin
 * @Date 2021/4/10 12:04
 * @Version 1.0
 */
@RestController
@RequestMapping("search/save")
public class ElasticSearchSaveController {

    @Autowired
    private ElasticSearchSaveService elasticSearchSaveService;

    @PostMapping("product/up")
    public R productStatusUp(@RequestBody List<SkuEsModel> esModelList) {
        try {
            elasticSearchSaveService.productStatusUp(esModelList);
        } catch (IOException e) {
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(), BizCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
        }
        return R.ok();
    }
}
