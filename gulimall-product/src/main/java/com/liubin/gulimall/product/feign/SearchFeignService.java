package com.liubin.gulimall.product.feign;

import com.liubin.common.dto.es.SkuEsModel;
import com.liubin.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Description 商品服务远程调用检索服务
 * @Author liubin
 * @Date 2021/4/10 12:30
 * @Version 1.0
 */
@FeignClient("gulimall-search")
public interface SearchFeignService {

    @PostMapping("search/save/product/up")
    public R productStatusUp(@RequestBody List<SkuEsModel> esModelList);
}
