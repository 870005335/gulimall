package com.liubin.gulimall.member.dao;

import com.liubin.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author liubin
 * @email 870005335
 * @date 2022-08-15 16:44:00
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}