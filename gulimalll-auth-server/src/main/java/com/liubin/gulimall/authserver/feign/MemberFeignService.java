package com.liubin.gulimall.authserver.feign;

import com.liubin.common.utils.R;
import com.liubin.gulimall.authserver.vo.SocialUser;
import com.liubin.gulimall.authserver.vo.UserLoginVo;
import com.liubin.gulimall.authserver.vo.UserRegisterVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Description 会员远程调用服务
 * @Author liubin
 * @Date 2021/6/5 23:34
 * @Version 1.0
 */
@FeignClient("gulimall-member")
public interface MemberFeignService {

    @PostMapping("member/member/register")
    R register(@RequestBody UserRegisterVo registerVo);

    @PostMapping("member/member/login")
    R login(@RequestBody UserLoginVo loginVo);

    @PostMapping("member/member/oauth2/login")
    R oauthLogin(@RequestBody SocialUser socialUser);
}
