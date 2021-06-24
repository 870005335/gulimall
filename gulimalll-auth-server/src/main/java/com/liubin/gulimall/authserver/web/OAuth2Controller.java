package com.liubin.gulimall.authserver.web;

import com.alibaba.fastjson.JSON;
import com.liubin.common.constant.AuthServerConstant;
import com.liubin.common.utils.HttpUtils;
import com.liubin.common.utils.R;
import com.liubin.gulimall.authserver.feign.MemberFeignService;
import com.liubin.common.vo.MemberRespVo;
import com.liubin.gulimall.authserver.vo.SocialUser;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 处理社交登录请求
 * @Author liubin
 * @Date 2021/6/11 11:32
 * @Version 1.0
 */
@Controller
public class OAuth2Controller {

    @Autowired
    private MemberFeignService memberFeignService;

    @GetMapping("/oauth2.0/weibo/success")
    public String weiboLogin(@RequestParam("code") String code, HttpSession session, HttpServletResponse servletResponse) throws Exception {
        Map<String, String> headers = new HashMap<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("client_id", "308229278");
        map.put("client_secret", "2355db31cfa5394c84816b70aee3456a");
        map.put("grant_type", "authorization_code");
        map.put("redirect_uri", "http://auth.gulimall.com/oauth2.0/weibo/success");
        map.put("code", code);
        // 根据code换取access_token
        HttpResponse response = HttpUtils.doPost("https://api.weibo.com", "/oauth2/access_token", "POST", headers, null, map);
        if (response.getStatusLine().getStatusCode() == 200) {
            String respJson = EntityUtils.toString(response.getEntity());
            SocialUser socialUser = JSON.parseObject(respJson, SocialUser.class);
            // 将社交用户与会员服务关联（登录或者注册社交用户）
            R resp = memberFeignService.oauthLogin(socialUser);
            if (resp.getCode() == 0) {
                MemberRespVo memberResp = JSON.parseObject(JSON.toJSONString(resp.get("member")), MemberRespVo.class);
                // 让默认发的令牌 扩大作用域范围
                session.setAttribute(AuthServerConstant.LOGIN_USER, memberResp);
                return  "redirect:http://www.gulimall.com";
            } else {
                return "redirect:http://auth.gulimall.com/login.html";
            }
        } else {
            return "redirect:http://auth.gulimall.com/login.html";
        }
    }
}
