package com.liubin.gulimall.product;

import com.liubin.gulimall.product.entity.BrandEntity;
import com.liubin.gulimall.product.service.AttrGroupRelationService;
import com.liubin.gulimall.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@SpringBootTest
class GuLiMallProductApplicationTests {

    @Autowired
    private BrandService brandService;

    @Autowired
    private AttrGroupRelationService attrGroupRelationService;

    @Test
    void contextLoads() {
    }

}
