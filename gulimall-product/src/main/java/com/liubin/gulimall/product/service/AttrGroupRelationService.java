package com.liubin.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liubin.gulimall.common.utils.PageUtils;
import com.liubin.gulimall.product.entity.AttrEntity;
import com.liubin.gulimall.product.entity.AttrGroupRelationEntity;
import com.liubin.gulimall.product.vo.AttrRelationVo;

import java.util.List;
import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author liubin
 * @email 870005335
 * @date 2022-08-12 15:55:28
 */
public interface AttrGroupRelationService extends IService<AttrGroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * @Author liubin
     * @Description 查询属性分组名称Map
     * @Date 14:10 2022/8/31
     * @param attrIds
     * @return java.util.Map<java.lang.Long,java.lang.String>
     **/
    Map<Long, String> getAttrGroupNameMap(List<Long> attrIds);

    /**
     * @Author liubin
     * @Description 移除关联关系
     * @Date 23:50 2022/8/31
     * @param delVo
     * @return void
     **/
    void deleteAttrRelation(AttrRelationVo delVo);

    /**
     * @Author liubin
     * @Description 添加关联关系
     * @Date 11:04 2022/9/1
     * @param saveVo
     * @return void
     **/
    void saveAttrRelation(AttrRelationVo saveVo);

    /**
     * @Author liubin
     * @Description 查询分组Id列表下所有属性列表
     * @Date 16:18 2022/9/5
     * @param groupIdList
     * @return java.util.List<com.liubin.gulimall.product.entity.AttrEntity>
     **/
    List<AttrEntity> queryAttrsByGroupIds(List<Long> groupIdList);
}

