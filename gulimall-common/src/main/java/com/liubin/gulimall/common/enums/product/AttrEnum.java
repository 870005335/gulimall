package com.liubin.gulimall.common.enums.product;

public enum AttrEnum {

    ATTR_TYPE_BASE(1, "base","基本属性"),
    ATTR_TYPE_SALE(0, "sale","销售属性");

    private int code;

    private String value;
    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getValue() {
        return value;
    }

    AttrEnum(int code, String value, String msg) {
        this.code = code;
        this.value = value;
        this.msg = msg;
    }
}
