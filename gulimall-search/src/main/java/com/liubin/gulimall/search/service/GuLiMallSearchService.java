package com.liubin.gulimall.search.service;

import com.liubin.gulimall.search.vo.SearchParam;
import com.liubin.gulimall.search.vo.SearchResult;

/**
 * @Description TODO
 * @Author liubin
 * @Date 2021/4/21 22:50
 * @Version 1.0
 */
public interface GuLiMallSearchService {

    /**
     * @Author liubin
     * @Description 检索es
     * @Date 21:48 2021/4/22
     * @param param
     * @return com.liubin.gulimall.search.vo.SearchResult
     **/
    SearchResult search(SearchParam param);
}
