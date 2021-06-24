package com.liubin.gulimall.member;

import com.alibaba.fastjson.JSON;
import com.liubin.gulimall.member.entity.MemberEntity;
import com.liubin.gulimall.member.service.MemberService;
import com.liubin.gulimall.member.vo.MemberLoginVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GuLiMallMemberApplicationTests {

    @Autowired
    private MemberService memberService;

    @Test
    void contextLoads() {
        MemberLoginVo loginVo = new MemberLoginVo();
        loginVo.setLoginAcct("liubin");
        MemberEntity login = memberService.login(loginVo);
        System.out.println(JSON.toJSONString(login));
    }

}
