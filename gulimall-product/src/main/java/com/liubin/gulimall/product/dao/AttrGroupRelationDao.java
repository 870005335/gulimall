package com.liubin.gulimall.product.dao;

import com.liubin.gulimall.product.entity.AttrEntity;
import com.liubin.gulimall.product.entity.AttrGroupRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性&属性分组关联
 * 
 * @author liubin
 * @email 870005335
 * @date 2022-08-12 15:55:28
 */
@Mapper
public interface AttrGroupRelationDao extends BaseMapper<AttrGroupRelationEntity> {

    List<AttrGroupRelationEntity> getAttrGroupListByAttrIds(@Param("attrIds") List<Long> attrIds);

    List<AttrEntity> queryAttrsByGroupIds(@Param("groupIdList") List<Long> groupIdList);
}
