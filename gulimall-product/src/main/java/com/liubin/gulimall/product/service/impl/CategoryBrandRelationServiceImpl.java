package com.liubin.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liubin.gulimall.common.exception.GuLiMallException;
import com.liubin.gulimall.product.service.BrandService;
import com.liubin.gulimall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liubin.gulimall.common.utils.PageUtils;
import com.liubin.gulimall.common.utils.Query;

import com.liubin.gulimall.product.dao.CategoryBrandRelationDao;
import com.liubin.gulimall.product.entity.CategoryBrandRelationEntity;
import com.liubin.gulimall.product.service.CategoryBrandRelationService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryBrandRelationEntity> queryList(Map<String, Object> params) {
        LambdaQueryWrapper<CategoryBrandRelationEntity> queryWrapper = new LambdaQueryWrapper<>();
        Object brandId = params.get("brandId");
        Object catId = params.get("catId");
        queryWrapper.eq(brandId != null, CategoryBrandRelationEntity::getBrandId, brandId)
                .eq(catId != null, CategoryBrandRelationEntity::getCategoryId, catId);
        List<CategoryBrandRelationEntity> resultList = this.list(queryWrapper);
        if (!CollectionUtils.isEmpty(resultList)) {
            handleResultDataList(resultList);
        }
        return resultList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCateBrandRelation(CategoryBrandRelationEntity categoryBrandRelation) {
        // 查询是否已经存在关联关系
        List<CategoryBrandRelationEntity> relationList = this.list(new LambdaQueryWrapper<CategoryBrandRelationEntity>()
                .eq(CategoryBrandRelationEntity::getBrandId, categoryBrandRelation.getBrandId())
                .eq(CategoryBrandRelationEntity::getCategoryId, categoryBrandRelation.getCategoryId()));
        if (!CollectionUtils.isEmpty(relationList)) {
            throw new GuLiMallException("该分类已与品牌存在关联关系");
        }
        this.save(categoryBrandRelation);
    }

    /**
     * @Author liubin
     * @Description 处理查询出来的结果集
     * @Date 16:56 2022/8/29
     * @param resultList
     * @return void
     **/
    private void handleResultDataList(List<CategoryBrandRelationEntity> resultList) {
        List<Long> brandIdList = resultList.stream()
                .map(CategoryBrandRelationEntity::getBrandId)
                .distinct()
                .collect(Collectors.toList());
        Map<Long, String> brandNameMap = brandService.getBrandNameMap(brandIdList);
        List<Long> categoryIdList = resultList.stream()
                .map(CategoryBrandRelationEntity::getCategoryId)
                .distinct()
                .collect(Collectors.toList());
        Map<Long, String> categoryNameMap = categoryService.getCategoryNameMap(categoryIdList);
        resultList.forEach(result -> {
            result.setBrandName(brandNameMap.get(result.getBrandId()));
            result.setCategoryName(categoryNameMap.get(result.getCategoryId()));
        });
    }
}