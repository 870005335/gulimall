package com.liubin.gulimall.product.vo;

import com.liubin.gulimall.product.entity.AttrEntity;
import lombok.Data;

import java.util.List;

/**
 * @author liubin
 * @version 1.0.0
 * @ClassName AttrGroupWithAttrsVo.java
 * @Description
 * @createTime 2021年03月19日 00:17:00
 */
@Data
public class AttrGroupWithAttrsVo {

    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;

    private List<AttrEntity> attrs;
}
