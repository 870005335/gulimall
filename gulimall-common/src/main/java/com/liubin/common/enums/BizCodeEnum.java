package com.liubin.common.enums;

/**
 * @Description 错误码和错误信息定义类
 * @Author liubin
 * @Date 2021/3/5 16:17
 * @Version 1.0
 */
public enum BizCodeEnum {
    UNKNOWN_EXCEPTION(10000,"系统未知异常"),
    VALID_EXCEPTION(10001,"参数格式校验失败"),
    SMS_CODE_EXCEPTION(10001,"短信验证频繁获取"),
    PRODUCT_UP_EXCEPTION(11000, "商品上架异常"),
    PHONE_EXIST_EXCEPTION(15000,"手机号已存在"),
    USER_EXIST_EXCEPTION(15001,"用户名已存在"),;

    private int code;
    private String msg;
    BizCodeEnum(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
