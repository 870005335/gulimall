package com.liubin.gulimall.search.vo;

import lombok.Data;

import java.util.List;

/**
 * @Description 检索页面检索条件
 * @Author liubin
 * @Date 2021/4/21 22:47
 * @Version 1.0
 */
@Data
public class SearchParam {

    // 全文匹配关键字
    private String keyword;

    // 三级分类Id
    private Long catalog3Id;

    // 排序条件
    private String sort;

    // 是否只显示有货
    private Integer hasStock;

    // 价格区间
    private String skuPrice;

    // 品牌 可以多选
    private List<Long> brandId;

    // 按照属性筛选
    private List<String> attrs;

    // 页码
    private Integer pageNum = 1;

    private String queryString;
}
