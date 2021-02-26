package com.liubin.gulimall.product;

import com.liubin.gulimall.product.entity.BrandEntity;
import com.liubin.gulimall.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GuLiMallProductApplicationTests {

    @Autowired
    private BrandService brandService;

    @Test
    void contextLoads() {
        BrandEntity byId = brandService.getById(1);
        System.out.println(byId.toString());

    }


}
