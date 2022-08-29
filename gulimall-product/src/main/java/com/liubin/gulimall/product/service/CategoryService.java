package com.liubin.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liubin.gulimall.common.utils.PageUtils;
import com.liubin.gulimall.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author liubin
 * @email 870005335
 * @date 2022-08-12 15:55:29
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * @Author liubin
     * @Description 查询三级树形结构
     * @Date 10:42 2022/8/17
     * @return java.util.List<com.liubin.gulimall.product.entity.CategoryEntity>
     **/
    List<CategoryEntity> listWithTree();

    /**
     * @param category
     * @return void
     * @Author liubin
     * @Description 新增分类
     * @Date 10:41 2022/8/23
     **/
    String saveCategory(CategoryEntity category);

    /**
     * @Author liubin
     * @Description 编辑分类
     * @Date 11:22 2022/8/23
     * @param category
     * @return java.lang.String
     **/
    String updateCategory(CategoryEntity category);

    /**
     * @Author liubin
     * @Description 查询三级分类路径
     * @Date 11:29 2022/8/29 
     * @param categoryId
     * @return java.util.List<java.lang.Integer>
     **/
    List<Long> queryCategoryPath(Long categoryId);

    /**
     * @Author liubin
     * @Description 根据Id列表查询分类名称映射
     * @Date 17:01 2022/8/29
     * @param categoryIdList
     * @return java.util.Map<java.lang.Long,java.lang.String>
     **/
    Map<Long, String> getCategoryNameMap(List<Long> categoryIdList);
}

