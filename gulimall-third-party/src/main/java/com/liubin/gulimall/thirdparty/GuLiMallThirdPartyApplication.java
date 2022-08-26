package com.liubin.gulimall.thirdparty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {"com.liubin.gulimall.thirdparty", "com.liubin.gulimall.common.exception"})
@EnableDiscoveryClient
public class GuLiMallThirdPartyApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuLiMallThirdPartyApplication.class, args);
    }

}
