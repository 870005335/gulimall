package com.liubin.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liubin.common.utils.PageUtils;
import com.liubin.gulimall.product.entity.AttrEntity;
import com.liubin.gulimall.product.vo.AttrRespVo;
import com.liubin.gulimall.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author liubin
 * @email 870005335@qq.com
 * @date 2021-02-25 18:48:14
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * @Author liubin
     * @Description 自定义attr保存方法
     * @Date 18:00 2021/3/9
     * @param attr
     * @return
     **/
    void saveAttr(AttrVo attr);

    /**
     * @Author liubin
     * @Description 分页查询商品销售属性
     * @Date 18:23 2021/3/9
     * @param params
     * @param attrType
     * @param catelogId
     * @return {@link PageUtils}
     **/
    PageUtils queryAttrPage(Map<String, Object> params, String attrType, Long catelogId);

    /**
     * @description: 根据属性id查询详情
     * @param attrId
     * @author: liubin
     * @date: 2021/3/10 0:15
     * @return: com.liubin.gulimall.product.vo.AttrRespVo
     */
    AttrRespVo getAttrInfo(Long attrId);

    /**
     * @Author liubin
     * @Description 自定义attr编辑方法
     * @Date 10:32 2021/3/10
     * @param attr
     * @return
     **/
    void updateAttr(AttrVo attr);

    /**
     * @Author liubin
     * @Description  根据属性分组id查询关联属性
     * @Date 0:28 2021/3/14
     * @param attrGroupId
     * @return {@link List<AttrEntity>}
     **/
    List<AttrEntity> getAttrRelation(Long attrGroupId);

    /**
     * @Author liubin
     * @Description 根据属性分组id查询未关联属性
     * @Date 18:24 2021/3/16
     * @param attrGroupId
     * @param params
     * @return {@link PageUtils}
     **/
    PageUtils getAttrNoRelation(Long attrGroupId, Map<String, Object> params);

    /**
     * @Author liubin
     * @Description 删除属性并移除关联关系
     * @Date 11:32 2021/3/19
     * @param idList
     * @return
     **/
    void deleteAttrs(List<Long> idList, String type);
}

