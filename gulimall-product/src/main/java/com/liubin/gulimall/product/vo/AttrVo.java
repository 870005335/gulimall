package com.liubin.gulimall.product.vo;

import com.liubin.gulimall.product.entity.AttrEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description 商品属性Vo对象
 * @Author liubin
 * @Date 2021/3/9 17:57
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AttrVo extends AttrEntity {

    private Long attrGroupId;
}
