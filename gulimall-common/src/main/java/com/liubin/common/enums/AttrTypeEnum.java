package com.liubin.common.enums;

/**
 * @Description 商品属性类型枚举类
 * @Author liubin
 * @Date 2021/3/9 18:28
 * @Version 1.0
 */
public enum AttrTypeEnum {
    ATTR_TYPE_SALE(0, "销售属性"),
    ATTR_TYPE_BASE(1, "基本属性"),
    ATTR_TYPE_BOTH(2, "既是销售属性又是基本属性");


    private final int code;

    private final String attrType;

    AttrTypeEnum(int code, String attrType) {
        this.code = code;
        this.attrType = attrType;
    }

    public int getCode() {
        return code;
    }

    public String getAttrType() {
        return attrType;
    }
}
