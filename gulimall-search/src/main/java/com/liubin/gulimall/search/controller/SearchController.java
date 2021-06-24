package com.liubin.gulimall.search.controller;

import com.liubin.gulimall.search.service.GuLiMallSearchService;
import com.liubin.gulimall.search.vo.SearchParam;
import com.liubin.gulimall.search.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description
 * @Author liubin
 * @Date 2021/4/21 22:09
 * @Version 1.0
 */
@Controller
public class SearchController {

    @Autowired
    private GuLiMallSearchService guLiMallSearchService;

    @GetMapping({"list.html", "search.html"})
    public String listPage(SearchParam param, Model model, HttpServletRequest servletRequest) {
        String queryString = servletRequest.getQueryString();
        param.setQueryString(queryString);
        SearchResult result = guLiMallSearchService.search(param);
        model.addAttribute("result", result);
        return "list";
    }

}
