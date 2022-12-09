package com.liubin.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liubin.gulimall.common.utils.PageUtils;
import com.liubin.gulimall.product.entity.CategoryBrandRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author liubin
 * @email 870005335
 * @date 2022-08-12 15:55:29
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryBrandRelationEntity> queryList(Map<String, Object> params);

    /**
     * @Author liubin
     * @Description 保存关联关系
     * @Date 18:12 2022/9/5
     * @param categoryBrandRelation
     * @return void
     **/
    void saveCateBrandRelation(CategoryBrandRelationEntity categoryBrandRelation);
}

