package com.liubin.gulimall.product.vo;

import com.liubin.gulimall.product.entity.AttrEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AttrVo extends AttrEntity {

    private Long attrGroupId;

    private Integer sort;
}
