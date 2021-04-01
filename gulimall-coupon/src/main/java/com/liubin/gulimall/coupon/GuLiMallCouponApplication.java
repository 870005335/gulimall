package com.liubin.gulimall.coupon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@MapperScan("com.liubin.gulimall.coupon.dao")
@SpringBootApplication
@EnableDiscoveryClient
public class GuLiMallCouponApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuLiMallCouponApplication.class, args);
    }

}
