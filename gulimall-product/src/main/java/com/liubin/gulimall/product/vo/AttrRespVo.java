package com.liubin.gulimall.product.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description 商品属性返回对象
 * @Author liubin
 * @Date 2021/3/9 18:52
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AttrRespVo extends AttrVo {

    private String catelogName;

    private String groupName;

    private Long[] catelogPath;
}
