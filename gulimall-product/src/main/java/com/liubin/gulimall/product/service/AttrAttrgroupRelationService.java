package com.liubin.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liubin.common.utils.PageUtils;
import com.liubin.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.liubin.gulimall.product.vo.AttrGroupRelationVo;

import java.util.List;
import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author liubin
 * @email 870005335@qq.com
 * @date 2021-02-25 18:48:14
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * @description: 属性分组批量关联属性
     * @param relationVoList
     * @author: liubin
     * @date: 2021/3/16 23:24
     * @return: void
     */
    void saveRelationBatch(List<AttrGroupRelationVo> relationVoList);
}

