package com.liubin.gulimall.authserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableDiscoveryClient
@EnableRedisHttpSession
public class GuLiMallAuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuLiMallAuthServerApplication.class, args);
    }

}
