package com.liubin.gulimall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@MapperScan("com.liubin.gulimall.product.dao")
@EnableDiscoveryClient
public class GuLiMallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuLiMallProductApplication.class, args);
    }

}
