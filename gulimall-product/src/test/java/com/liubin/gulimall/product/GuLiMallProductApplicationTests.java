package com.liubin.gulimall.product;

import com.liubin.gulimall.product.entity.BrandEntity;
import com.liubin.gulimall.product.service.BrandService;
import com.liubin.gulimall.product.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
class GuLiMallProductApplicationTests {

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Test
    void contextLoads() {
        Long[] categoryPath = categoryService.queryCategoryPath(225L);
        System.out.println(Arrays.toString(categoryPath));

    }


}
