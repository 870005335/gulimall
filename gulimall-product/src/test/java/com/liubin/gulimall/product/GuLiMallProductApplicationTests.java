package com.liubin.gulimall.product;

import com.alibaba.fastjson.JSON;
import com.liubin.gulimall.product.dao.AttrGroupDao;
import com.liubin.gulimall.product.dao.SkuSaleAttrValueDao;
import com.liubin.gulimall.product.entity.BrandEntity;
import com.liubin.gulimall.product.service.BrandService;
import com.liubin.gulimall.product.service.CategoryService;
import com.liubin.gulimall.product.vo.SkuItemSaleAttrsVo;
import com.liubin.gulimall.product.vo.SkuItemVo;
import com.liubin.gulimall.product.vo.SpuItemAttrGroupVo;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.*;

@SpringBootTest
class GuLiMallProductApplicationTests {

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private AttrGroupDao attrGroupDao;

    @Autowired
    private SkuSaleAttrValueDao skuSaleAttrValueDao;

    @Test
    void contextLoads() {
        Map<String, String> map = new HashMap<>();

    }


    @Autowired
    private RedissonClient redissonClient;

    @Test
    void redisson() {
        System.out.println(redissonClient);
    }

}
