package com.liubin.gulimall.product.service.impl;

import com.liubin.gulimall.product.dao.BrandDao;
import com.liubin.gulimall.product.dao.CategoryDao;
import com.liubin.gulimall.product.entity.BrandEntity;
import com.liubin.gulimall.product.entity.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liubin.common.utils.PageUtils;
import com.liubin.common.utils.Query;

import com.liubin.gulimall.product.dao.CategoryBrandRelationDao;
import com.liubin.gulimall.product.entity.CategoryBrandRelationEntity;
import com.liubin.gulimall.product.service.CategoryBrandRelationService;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Autowired
    private BrandDao brandDao;

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation) {
        // 根据品牌Id查询品牌名称
        BrandEntity brandEntity = brandDao.selectById(categoryBrandRelation.getBrandId());
        if (brandEntity != null) {
            categoryBrandRelation.setBrandName(brandEntity.getName());
        }
        // 根据分类Id查询分类名称
        CategoryEntity categoryEntity = categoryDao.selectById(categoryBrandRelation.getCatelogId());
        if (categoryEntity != null) {
            categoryBrandRelation.setBrandName(categoryEntity.getName());
        }
        this.save(categoryBrandRelation);
    }
}