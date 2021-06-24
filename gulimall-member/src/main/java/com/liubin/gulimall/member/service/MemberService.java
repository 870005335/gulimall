package com.liubin.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liubin.common.utils.PageUtils;
import com.liubin.gulimall.member.entity.MemberEntity;
import com.liubin.gulimall.member.exception.PhoneExistException;
import com.liubin.gulimall.member.exception.UserNameExistException;
import com.liubin.gulimall.member.vo.MemberLoginVo;
import com.liubin.gulimall.member.vo.MemberRegisterVo;
import com.liubin.gulimall.member.vo.SocialUser;

import java.util.Map;

/**
 * 会员
 *
 * @author liubin
 * @email 870005335@qq.com
 * @date 2021-02-26 12:08:24
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * @Author liubin
     * @Description 会员注册
     * @Date 22:46 2021/6/5
     * @param registerVo
     * @return void
     **/
    void register(MemberRegisterVo registerVo);

    /**
     * @Author liubin
     * @Description 用户登录
     * @Date 0:13 2021/6/6
     * @param loginVo
     * @return com.liubin.gulimall.member.entity.MemberEntity
     **/
    MemberEntity login(MemberLoginVo loginVo);

    /**
     * @Author liubin
     * @Description 检查手机号码唯一
     * @Date 22:55 2021/6/5
     * @param phone
     * @return void
     **/
    void checkPhoneUnique(String phone) throws PhoneExistException;

    /**
     * @Author liubin
     * @Description 检验用户名唯一
     * @Date 22:58 2021/6/5
     * @param username
     * @return void
     **/
    void checkUserNameUnique(String username) throws UserNameExistException;

    /**
     * @Author liubin
     * @Description 社交用户登录
     * @Date 14:05 2021/6/12
     * @param socialUser
     * @return com.liubin.gulimall.member.entity.MemberEntity
     **/
    MemberEntity login(SocialUser socialUser);
}

