package com.liubin.gulimall.product.vo;

import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Author liubin
 * @Date 2021/5/24 17:15
 * @Version 1.0
 */

@Data
public class SkuItemSaleAttrsVo {

    private Long attrId;

    private String attrName;

    private List<SkuAttrValueVo> attrValues;
}