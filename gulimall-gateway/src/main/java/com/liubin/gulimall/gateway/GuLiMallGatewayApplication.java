package com.liubin.gulimall.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GuLiMallGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuLiMallGatewayApplication.class, args);
    }
}
