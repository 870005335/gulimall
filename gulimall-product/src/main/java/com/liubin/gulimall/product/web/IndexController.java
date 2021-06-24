package com.liubin.gulimall.product.web;

import com.liubin.common.constant.AuthServerConstant;
import com.liubin.gulimall.product.entity.CategoryEntity;
import com.liubin.gulimall.product.service.CategoryService;
import com.liubin.gulimall.product.vo.Catalog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author liubin
 * @Date 2021/4/10 20:24
 * @Version 1.0
 */
@Controller
public class IndexController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping({"/", "index.html"})
    public String indexPage(Model model) {
        // 查询所有的一级分类
        List<CategoryEntity> categoryList = categoryService.getFirstLevelCategoryList();
        model.addAttribute("firstCategories", categoryList);
        return "index";
    }

    @ResponseBody
    @GetMapping("index/catalog.json")
    public Map<String, List<Catalog2Vo>> getCatalogJson() {
        return categoryService.getCatalogJson();
    }

    @PostMapping("index/logout")
    @ResponseBody
    public void logout(HttpSession session) {
        session.removeAttribute(AuthServerConstant.LOGIN_USER);
    }
}
