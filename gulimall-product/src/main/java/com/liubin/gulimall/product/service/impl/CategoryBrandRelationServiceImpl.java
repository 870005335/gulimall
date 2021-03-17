package com.liubin.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.liubin.gulimall.product.dao.BrandDao;
import com.liubin.gulimall.product.dao.CategoryDao;
import com.liubin.gulimall.product.entity.BrandEntity;
import com.liubin.gulimall.product.entity.CategoryEntity;
import com.liubin.gulimall.product.vo.BrandVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liubin.common.utils.PageUtils;
import com.liubin.common.utils.Query;

import com.liubin.gulimall.product.dao.CategoryBrandRelationDao;
import com.liubin.gulimall.product.entity.CategoryBrandRelationEntity;
import com.liubin.gulimall.product.service.CategoryBrandRelationService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Autowired
    private BrandDao brandDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private CategoryBrandRelationDao categoryBrandRelationDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation) {
        // 根据品牌Id查询品牌名称
        BrandEntity brandEntity = brandDao.selectById(categoryBrandRelation.getBrandId());
        if (brandEntity != null) {
            categoryBrandRelation.setBrandName(brandEntity.getName());
        }
        // 根据分类Id查询分类名称
        CategoryEntity categoryEntity = categoryDao.selectById(categoryBrandRelation.getCatelogId());
        if (categoryEntity != null) {
            categoryBrandRelation.setCatelogName(categoryEntity.getName());
        }
        this.save(categoryBrandRelation);
    }

    @Override
    @Transactional
    public void updateBrandRelation(Long brandId, String name) {
        CategoryBrandRelationEntity updateEntity = new CategoryBrandRelationEntity();
        updateEntity.setBrandName(name);
        this.update(updateEntity, new UpdateWrapper<CategoryBrandRelationEntity>().eq("brand_id", brandId));
    }

    @Override
    @Transactional
    public void updateCategoryRelation(Long catId, String name) {
        CategoryBrandRelationEntity updateEntity = new CategoryBrandRelationEntity();
        updateEntity.setCatelogName(name);
        this.update(updateEntity, new UpdateWrapper<CategoryBrandRelationEntity>().eq("catelog_id", catId));
    }

    @Override
    public List<BrandVo> getBrandsByCatId(Long catId) {
        List<BrandVo> resultList = new ArrayList<>();
        List<CategoryBrandRelationEntity> relationList = categoryBrandRelationDao.selectList(
                new QueryWrapper<CategoryBrandRelationEntity>().eq("catelog_id", catId));
        if (!CollectionUtils.isEmpty(relationList)) {
            resultList = relationList.stream().map(relation -> {
                BrandVo brandVo = new BrandVo();
                brandVo.setBrandId(relation.getBrandId());
                brandVo.setBrandName(relation.getBrandName());
                return brandVo;
            }).collect(Collectors.toList());
        }
        return resultList;
    }
}