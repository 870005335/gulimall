package com.liubin.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liubin.gulimall.common.utils.PageUtils;
import com.liubin.gulimall.product.entity.AttrEntity;
import com.liubin.gulimall.product.vo.AttrRespVo;
import com.liubin.gulimall.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author liubin
 * @email 870005335
 * @date 2022-08-12 15:55:28
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params, String attrType, Long catId);

    /**
     * @Author liubin
     * @Description 保存
     * @Date 17:21 2022/8/30
     * @param attr
     * @return void
     **/
    void saveAttr(AttrVo attr);

    /**
     * @Author liubin
     * @Description 修改
     * @Date 17:43 2022/8/30
     * @param attr
     * @return void
     **/
    void updateAttr(AttrVo attr);

    /**
     * @Author liubin
     * @Description 查询
     * @Date 19:16 2022/8/30
     * @param attrId
     * @return com.liubin.gulimall.product.entity.AttrEntity
     **/
    AttrRespVo getAttrInfo(Long attrId);

    /**
     * @Author liubin
     * @Description 查询与属性分组关联的属性
     * @Date 18:06 2022/8/31
     * @param attrGroupId
     * @return java.util.List<com.liubin.gulimall.product.entity.AttrEntity>
     **/
    List<AttrEntity> getAttrRelation(Long attrGroupId);

    /**
     * @Author liubin
     * @Description 分页查询与属性分组未关联的属性
     * @Date 0:37 2022/9/1
     * @param attrGroupId
     * @return com.liubin.gulimall.common.utils.PageUtils
     **/
    PageUtils getNoAttrRelation(Map<String, Object> params, Long attrGroupId);

    /**
     * @Author liubin
     * @Description 删除属性
     * @Date 16:45 2022/9/5
     * @param attrIdList
     * @return void
     **/
    void deleteAttrsByIds(List<Long> attrIdList);
}

