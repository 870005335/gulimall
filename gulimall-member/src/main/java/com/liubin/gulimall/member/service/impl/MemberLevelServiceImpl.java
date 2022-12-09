package com.liubin.gulimall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liubin.gulimall.common.utils.PageUtils;
import com.liubin.gulimall.common.utils.Query;

import com.liubin.gulimall.member.dao.MemberLevelDao;
import com.liubin.gulimall.member.entity.MemberLevelEntity;
import com.liubin.gulimall.member.service.MemberLevelService;


@Service("memberLevelService")
public class MemberLevelServiceImpl extends ServiceImpl<MemberLevelDao, MemberLevelEntity> implements MemberLevelService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<MemberLevelEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(params.get("key") != null && StringUtils.isNotBlank(params.get("key").toString()), MemberLevelEntity::getName, params.get("key"));
        IPage<MemberLevelEntity> page = this.page(
                new Query<MemberLevelEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

}