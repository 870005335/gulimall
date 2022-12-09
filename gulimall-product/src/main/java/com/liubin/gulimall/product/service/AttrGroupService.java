package com.liubin.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liubin.gulimall.common.utils.PageUtils;
import com.liubin.gulimall.product.entity.AttrGroupEntity;
import com.liubin.gulimall.product.vo.AttrGroupWithAttrsVo;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author liubin
 * @email 870005335
 * @date 2022-08-12 15:55:28
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params, Long catId);

    void saveAttrGroup(AttrGroupEntity attrGroup);

    AttrGroupEntity getAttrGroupById(Long attrGroupId);

    void updateAttrGroup(AttrGroupEntity attrGroup);

    /**
     * @Author liubin
     * @Description 查出当前分类下的所有属性分组以及分组下属性
     * @Date 15:53 2022/9/5
     * @param categoryId
     * @return java.util.List<com.liubin.gulimall.product.vo.AttrGroupWithAttrsVo>
     **/
    List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCategoryId(Long categoryId);

    /**
     * @Author liubin
     * @Description 删除属性分组
     * @Date 18:01 2022/9/5
     * @param groupIdList
     * @return void
     **/
    void deleteAttrGroups(List<Long> groupIdList);
}

