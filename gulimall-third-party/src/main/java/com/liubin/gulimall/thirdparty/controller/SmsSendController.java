package com.liubin.gulimall.thirdparty.controller;

import com.liubin.common.utils.R;
import com.liubin.gulimall.thirdparty.component.SmsComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author liubin
 * @Date 2021/5/28 16:08
 * @Version 1.0
 */
@RestController
@RequestMapping("/sms")
public class SmsSendController {

    @Autowired
    private SmsComponent smsComponent;

    @GetMapping("sendCode")
    public R sendCode(@RequestParam("phone") String phone,
                      @RequestParam("code") String code) {
        smsComponent.sendSmsCode(phone, code);
        return R.ok();
    }
}
