package com.liubin.gulimall.authserver.vo;

import lombok.Data;

/**
 * @Description 用户登录Vo
 * @Author liubin
 * @Date 2021/6/5 23:58
 * @Version 1.0
 */
@Data
public class UserLoginVo {

    private String loginAcct;

    private String password;
}
