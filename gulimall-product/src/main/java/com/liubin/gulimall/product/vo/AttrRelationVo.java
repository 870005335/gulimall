package com.liubin.gulimall.product.vo;

import lombok.Data;

import java.util.List;

@Data
public class AttrRelationVo {

    private Long attrGroupId;

    private List<Long> attrIds;
}
