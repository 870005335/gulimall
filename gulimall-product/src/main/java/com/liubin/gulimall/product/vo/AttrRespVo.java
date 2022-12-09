package com.liubin.gulimall.product.vo;

import com.liubin.gulimall.product.entity.AttrEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class AttrRespVo extends AttrEntity {
    private Long attrGroupId;

    private String attrGroupName;

    private String categoryName;

    private List<Long> categoryPath;

    private Integer sort;
}
