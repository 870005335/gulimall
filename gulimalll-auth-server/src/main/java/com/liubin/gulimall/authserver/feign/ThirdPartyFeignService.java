package com.liubin.gulimall.authserver.feign;

import com.liubin.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description TODO
 * @Author liubin
 * @Date 2021/5/28 16:46
 * @Version 1.0
 */
@FeignClient("gulimall-third-party")
public interface ThirdPartyFeignService {

    @GetMapping("sms/sendCode")
    R sendCode(@RequestParam("phone") String phone,
               @RequestParam("code") String code);
}
