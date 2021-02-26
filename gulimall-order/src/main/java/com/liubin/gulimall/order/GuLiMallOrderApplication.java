package com.liubin.gulimall.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@MapperScan("com.liubin.gulimall.order")
@SpringBootApplication
@EnableDiscoveryClient
public class GuLiMallOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuLiMallOrderApplication.class, args);
    }

}
