package com.liubin.gulimall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan("com.liubin.gulimall.product.dao")
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.liubin.gulimall.product.feign")
public class GuLiMallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuLiMallProductApplication.class, args);
    }

}
