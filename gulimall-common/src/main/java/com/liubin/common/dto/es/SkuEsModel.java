package com.liubin.common.dto.es;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description sku es数据模型
 * @Author liubin
 * @Date 2021/4/9 10:51
 * @Version 1.0
 */
@Data
public class SkuEsModel {

    private Long skuId;

    private Long spuId;

    private String skuTitle;

    private BigDecimal skuPrice;

    private String skuImg;

    private Long saleCount;

    private Boolean hasStock;

    private Long hotScore;

    private Long brandId;

    private Long catalogId;

    private String brandName;

    private String brandImg;

    private String catalogName;

    private List<Attrs> attrs;

    @Data
    public static class Attrs {

        private Long attrId;

        private String attrName;

        private String attrValue;
    }
}
