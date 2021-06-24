package com.liubin.gulimall.member.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.liubin.common.utils.HttpUtils;
import com.liubin.gulimall.member.entity.MemberLevelEntity;
import com.liubin.gulimall.member.exception.PhoneExistException;
import com.liubin.gulimall.member.exception.UserNameExistException;
import com.liubin.gulimall.member.service.MemberLevelService;
import com.liubin.gulimall.member.vo.MemberLoginVo;
import com.liubin.gulimall.member.vo.MemberRegisterVo;
import com.liubin.gulimall.member.vo.SocialUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liubin.common.utils.PageUtils;
import com.liubin.common.utils.Query;

import com.liubin.gulimall.member.dao.MemberDao;
import com.liubin.gulimall.member.entity.MemberEntity;
import com.liubin.gulimall.member.service.MemberService;
import org.springframework.util.CollectionUtils;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Autowired
    private MemberLevelService memberLevelService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<MemberEntity> queryWrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) {
            queryWrapper.eq("mobile", key).or().like("username", key);
        }
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void register(MemberRegisterVo user) {

        MemberEntity member  = new MemberEntity();

        //设置基本信息
        //检查手机号和用户名是否唯一
        checkPhoneUnique(user.getPhone());
        checkUserNameUnique(user.getUsername());

        member.setUsername(user.getUsername());
        member.setMobile(user.getPhone());
        member.setNickname(user.getUsername());
        member.setIntegration(0);
        member.setGrowth(0);
        member.setStatus(1);
        member.setCreateTime(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        //密码加密
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(user.getPassword());
        member.setPassword(encode);

        //设置会员默认等级
        MemberLevelEntity levelEntity = memberLevelService.getDefaultLevel();
        member.setLevelId(levelEntity.getId());

        this.baseMapper.insert(member);
    }

    @Override
    public MemberEntity login(MemberLoginVo loginVo) {
        String loginAcct = loginVo.getLoginAcct();
        String password = loginVo.getPassword();
        // 查询数据库
        QueryWrapper<MemberEntity> queryWrapper = new QueryWrapper<MemberEntity>().eq("username", loginAcct)
                .or().eq("mobile", loginAcct);
        MemberEntity member = this.baseMapper.selectOne(queryWrapper.eq("status", 1));
        if (member != null) {
            String passwordDB = member.getPassword();
            // 校验密码
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            boolean flag = passwordEncoder.matches(password, passwordDB);
            if (flag) {
                return member;
            }
        }
        return null;
    }

    @Override
    public void checkPhoneUnique(String phone) throws PhoneExistException {
        Integer count = this.baseMapper.selectCount(new QueryWrapper<MemberEntity>().eq("mobile", phone));
        if(count > 0){
            throw new PhoneExistException();
        }

    }

    @Override
    public void checkUserNameUnique(String username) throws UserNameExistException {
        Integer count = this.baseMapper.selectCount(new QueryWrapper<MemberEntity>().eq("username", username));
        if(count > 0){
            throw new UserNameExistException();
        }
    }

    @Override
    public MemberEntity login(SocialUser socialUser) {
        // 判断当前社交用户是否登录过系统
        MemberEntity entity = this.getOne(new QueryWrapper<MemberEntity>().eq("social_uid", socialUser.getUid()));
        if (entity != null) {
            // 更新过期时间
            MemberEntity update = new MemberEntity();
            update.setId(entity.getId());
            update.setAccessToken(socialUser.getAccess_token());
            update.setExpiresIn(String.valueOf(socialUser.getExpires_in()));
            this.updateById(update);
            entity.setAccessToken(socialUser.getAccess_token());
            entity.setExpiresIn(String.valueOf(socialUser.getExpires_in()));
            return entity;
        } else {
            // 没有查询当前社交用户信息 需要注册
            MemberEntity register = new MemberEntity();
            try {
                // 查询当前社交用户的信息
                Map<String, String> query = new HashMap<>();
                query.put("access_token", socialUser.getAccess_token());
                query.put("uid", socialUser.getUid());
                HttpResponse response = HttpUtils.doGet("https://api.weibo.com", "/2/users/show.json", "GET", new HashMap<>(), query);
                if (response.getStatusLine().getStatusCode() == 200) {
                    String respJson = EntityUtils.toString(response.getEntity());
                    JSONObject jsonObject = JSON.parseObject(respJson);
                    String name = jsonObject.getString("name");
                    String gender = jsonObject.getString("gender");
                    String profileImageUrl = jsonObject.getString("profile_image_url");
                    register.setUsername(name);
                    register.setNickname(name);
                    register.setGender(StringUtils.equals("m", gender) ? 1 : 0);
                    register.setHeader(profileImageUrl);
                }
            } catch (Exception ignored) {}
            register.setSocialUid(socialUser.getUid());
            register.setAccessToken(socialUser.getAccess_token());
            register.setExpiresIn(String.valueOf(socialUser.getExpires_in()));
            register.setIntegration(0);
            register.setGrowth(0);
            register.setStatus(1);
            register.setCreateTime(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            //设置会员默认等级
            MemberLevelEntity levelEntity = memberLevelService.getDefaultLevel();
            register.setLevelId(levelEntity.getId());
            this.save(register);
            return register;
        }
    }
}