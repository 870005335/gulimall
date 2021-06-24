package com.liubin.gulimall.search.feign;

import com.liubin.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Description
 * @Author liubin
 * @Date 2021/5/13 11:50
 * @Version 1.0
 */
@FeignClient("gulimall-product")
public interface ProductFeignService {

    @GetMapping("/product/attr/info/{attrId}")
    R info(@PathVariable("attrId") Long attrId);
}
