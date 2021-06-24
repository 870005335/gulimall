package com.liubin.gulimall.product.vo;

import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Author liubin
 * @Date 2021/5/24 16:50
 * @Version 1.0
 */
@Data
public class SpuItemAttrGroupVo {

    private String groupName;

    private List<Attr> attrs;
}
