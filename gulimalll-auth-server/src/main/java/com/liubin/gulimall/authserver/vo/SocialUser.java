package com.liubin.gulimall.authserver.vo;

import lombok.Data;

/**
 * @Description
 * @Author liubin
 * @Date 2021/6/11 16:20
 * @Version 1.0
 */
@Data
public class SocialUser {

    private String access_token;
    private String remind_in;
    private long expires_in;
    private String uid;
    private String isRealName;
}
