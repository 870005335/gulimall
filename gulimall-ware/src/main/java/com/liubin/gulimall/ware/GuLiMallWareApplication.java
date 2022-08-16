package com.liubin.gulimall.ware;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@MapperScan("com.liubin.gulimall.ware.dao")
@EnableDiscoveryClient
public class GuLiMallWareApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuLiMallWareApplication.class, args);
    }

}
