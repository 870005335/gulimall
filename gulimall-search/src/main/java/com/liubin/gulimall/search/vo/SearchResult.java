package com.liubin.gulimall.search.vo;

import com.liubin.common.dto.es.SkuEsModel;
import lombok.Data;

import java.util.List;

/**
 * @Description 检索页面检索返回结果
 * @Author liubin
 * @Date 2021/4/22 16:04
 * @Version 1.0
 */
@Data
public class SearchResult {

    // 查询到的所有商品信息
    private List<SkuEsModel> products;

    // 当前页码
    private Integer pageNum;

    // 总记录数
    private Long total;

    // 总页数
    private Integer totalPages;

    private List<Integer> pageNavs;

    // 查询结果所涉及到的品牌
    private List<Brand> brands;

    // 查询结果所涉及到的属性列表
    private List<Attr> attrs;

    // 分类
    private List<category> catalogs;

    // 面包屑导航数据
    private List<NavVo> navs;

    @Data
    public static class NavVo {

        private String navName;

        private String navValue;

        private String link;
    }

    @Data
    public static class Brand {
        private Long brandId;

        private String brandName;

        private String brandImg;
    }

    @Data
    public static class Attr {
        private Long attrId;

        private String attrName;

        private List<String> attrValue;
    }

    @Data
    public static class category {
        private Long catalogId;

        private String catalogName;
    }
}
