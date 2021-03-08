package com.liubin.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liubin.common.utils.PageUtils;
import com.liubin.gulimall.product.entity.CategoryBrandRelationEntity;

import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author liubin
 * @email 870005335@qq.com
 * @date 2021-03-08 17:45:25
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * @Author liubin
     * @Description 品牌分类关联表保存
     * @Date 17:58 2021/3/8
     * @param categoryBrandRelation
     * @return
     **/
    void saveDetail(CategoryBrandRelationEntity categoryBrandRelation);
}

