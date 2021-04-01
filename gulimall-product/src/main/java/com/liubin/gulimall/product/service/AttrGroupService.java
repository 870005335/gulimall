package com.liubin.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liubin.common.utils.PageUtils;
import com.liubin.gulimall.product.entity.AttrGroupEntity;
import com.liubin.gulimall.product.vo.AttrGroupRelationVo;
import com.liubin.gulimall.product.vo.AttrGroupWithAttrsVo;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author liubin
 * @email 870005335@qq.com
 * @date 2021-02-25 18:48:14
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * @description: 根据分类id分页查询属性分组
     * @param params
     * @param categoryId
     * @author: liubin
     * @date: 2021/3/7 18:25
     * @return: com.liubin.common.utils.PageUtils
     */
    PageUtils queryPage(Map<String, Object> params, Integer categoryId);

    /**
     * @Author liubin
     * @Description 删除属性分组和属性关联信息（支持批量）
     * @Date 17:35 2021/3/15
     * @param attrGroupRelationVoList
     * @return
     **/
    void deleteRelation(List<AttrGroupRelationVo> attrGroupRelationVoList);

    /**
     * @Author liubin
     * @Description 查询分类下分组及属性
     * @Date 10:47 2021/3/19
     * @param catelogId
     * @return {@link List< AttrGroupWithAttrsVo>}
     **/
    List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCategoryId(Long catelogId);
}

