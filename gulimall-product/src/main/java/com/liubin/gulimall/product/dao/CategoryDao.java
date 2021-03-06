package com.liubin.gulimall.product.dao;

import com.liubin.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author liubin
 * @email 870005335@qq.com
 * @date 2021-02-25 18:48:14
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
