package com.liubin.gulimall.authserver.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @Description 用户注册vo
 * @Author liubin
 * @Date 2021/6/5 13:26
 * @Version 1.0
 */
@Data
public class UserRegisterVo {

    @NotEmpty(message = "用户名不能为空")
    @Length(min = 6, max = 10, message = "用户名必须是6-10位字符")
    private String username;

    @NotEmpty(message = "密码不能为空")
    @Length(min = 6, max = 16, message = "用户名必须是6-16位字符")
    private String password;

    @NotEmpty(message = "手机号不能为空")
    @Pattern(regexp = "^[1]([3-9])[0-9]{9}$", message = "手机号格式不正确")
    private String phone;

    @NotEmpty(message = "验证码不能为空")
    private String code;
}
