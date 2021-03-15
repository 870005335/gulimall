package com.liubin.gulimall.product.dao;

import com.liubin.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性&属性分组关联
 * 
 * @author liubin
 * @email 870005335@qq.com
 * @date 2021-02-25 18:48:14
 */
@Mapper
public interface AttrAttrGroupRelationDao extends BaseMapper<AttrAttrgroupRelationEntity> {

    /**
     * @Author liubin
     * @Description 删除属性分组和属性关联信息（支持批量）
     * @Date 18:44 2021/3/15
     * @param entities
     * @return
     **/
    void deleteBatchRelation(@Param("entities") List<AttrAttrgroupRelationEntity> entities);
}
