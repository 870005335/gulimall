package com.liubin.gulimall.member.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liubin.common.utils.PageUtils;
import com.liubin.common.utils.Query;

import com.liubin.gulimall.member.dao.MemberDao;
import com.liubin.gulimall.member.entity.MemberEntity;
import com.liubin.gulimall.member.service.MemberService;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

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

}