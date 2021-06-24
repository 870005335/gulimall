package com.liubin.gulimall.product.web;

import com.liubin.gulimall.product.service.SkuInfoService;
import com.liubin.gulimall.product.vo.SkuItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.ExecutionException;

/**
 * @Description 商品详情
 * @Author liubin
 * @Date 2021/5/17 15:00
 * @Version 1.0
 */
@Controller
public class ItemController {

    @Autowired
    private SkuInfoService skuInfoService;

    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable("skuId") Long skuId, Model model) throws ExecutionException, InterruptedException {
        System.out.println("准备查询" + skuId);
        SkuItemVo data = skuInfoService.item(skuId);
        model.addAttribute("item", data);
        return "item";
    }
}
