package com.liubin.gulimall.member.dao;

import com.liubin.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author liubin
 * @email 870005335@qq.com
 * @date 2021-02-26 12:08:24
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
