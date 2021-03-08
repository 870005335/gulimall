package com.liubin.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liubin.common.utils.PageUtils;
import com.liubin.gulimall.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author liubin
 * @email 870005335@qq.com
 * @date 2021-02-25 18:48:14
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * @Author liubin
     * @Description 查询三级分类树形结构列表
     * @Date 11:08 2021/3/2
     * @param
     * @return {@link List< CategoryEntity>}
     **/
    List<CategoryEntity> listWithTree();

    /**
     * @Author liubin
     * @Description TODO
     * @Date 16:42 2021/3/3
     * @param catIds
     * @return
     **/
    void removeMenuByIds(List<Long> catIds);

    /**
     * @description: 根据属性分组中的分类id查询分类id路径
     * @param catId
     * @author: liubin
     * @date: 2021/3/7 22:29
     * @return: java.lang.Long[]
     */
    Long[] queryCategoryPath(Long catId);

    /**
     * @description: 更新分类信息并维护中间表
     * @param category
     * @author: liubin
     * @date: 2021/3/8 23:11
     * @return: void
     */
    void updateCategoryAndRelation(CategoryEntity category);
}

