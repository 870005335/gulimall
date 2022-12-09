package com.liubin.gulimall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liubin.gulimall.common.utils.PageUtils;
import com.liubin.gulimall.common.utils.Query;

import com.liubin.gulimall.member.dao.MemberDao;
import com.liubin.gulimall.member.entity.MemberEntity;
import com.liubin.gulimall.member.service.MemberService;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<MemberEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(params.get("key") != null, MemberEntity::getUsername, params.get("key"));
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

}