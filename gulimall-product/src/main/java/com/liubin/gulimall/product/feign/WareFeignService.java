package com.liubin.gulimall.product.feign;

import com.liubin.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Description ware远程调用服务接口
 * @Author liubin
 * @Date 2021/4/9 16:41
 * @Version 1.0
 */
@FeignClient("gulimall-ware")
public interface WareFeignService {

    @PostMapping("ware/waresku/hasstock")
    R getSkusHasStock(@RequestBody List<Long> skuIds);
}
