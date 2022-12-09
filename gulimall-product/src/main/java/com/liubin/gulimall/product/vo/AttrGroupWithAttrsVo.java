package com.liubin.gulimall.product.vo;

import com.liubin.gulimall.product.entity.AttrEntity;
import com.liubin.gulimall.product.entity.AttrGroupEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class AttrGroupWithAttrsVo extends AttrGroupEntity {

    private List<AttrEntity> attrs;

}
