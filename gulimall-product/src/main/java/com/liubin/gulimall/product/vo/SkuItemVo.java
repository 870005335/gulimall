package com.liubin.gulimall.product.vo;

import com.liubin.gulimall.product.entity.SkuImagesEntity;
import com.liubin.gulimall.product.entity.SkuInfoEntity;
import com.liubin.gulimall.product.entity.SpuInfoDescEntity;
import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Author liubin
 * @Date 2021/5/17 15:21
 * @Version 1.0
 */
@Data
public class SkuItemVo {

    // sku基本信息
    private SkuInfoEntity info;

    // sku图片信息
    private List<SkuImagesEntity> images;

    // spu的销售属性组合
    private List<SkuItemSaleAttrsVo> saleAttr;

    // spu的介绍
    private SpuInfoDescEntity desc;

    // spu的规格参数信息
    private List<SpuItemAttrGroupVo> groupAttrs;

    private Boolean hasStock = true;
}
