package com.liubin.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liubin.gulimall.common.utils.PageUtils;
import com.liubin.gulimall.common.utils.Query;

import com.liubin.gulimall.product.dao.CategoryDao;
import com.liubin.gulimall.product.entity.CategoryEntity;
import com.liubin.gulimall.product.service.CategoryService;
import org.springframework.util.CollectionUtils;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        // 查询所有分类数据列表
        List<CategoryEntity> categoryList = this.list();
        return categoryList.stream()
                .filter(category -> category.getParentCid() == 0)
                .peek(menu -> menu.setChildren(getChildren(menu, categoryList)))
                .sorted(Comparator.comparing(CategoryEntity::getSort))
                .collect(Collectors.toList());
    }

    @Override
    public String saveCategory(CategoryEntity category) {
        // 查询分类名称重复
        LambdaQueryWrapper<CategoryEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CategoryEntity::getName, category.getName());
        queryWrapper.eq(CategoryEntity::getShowStatus, category.getShowStatus());
        List<CategoryEntity> categoryList = this.list(queryWrapper);
        if (!CollectionUtils.isEmpty(categoryList)) {
            return "分类已存在";
        }
        this.save(category);
        return null;
    }

    @Override
    public String updateCategory(CategoryEntity category) {
        // 查询分类名称重复
        LambdaQueryWrapper<CategoryEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CategoryEntity::getName, category.getName());
        queryWrapper.eq(CategoryEntity::getShowStatus, category.getShowStatus());
        queryWrapper.ne(CategoryEntity::getCatId, category.getCatId());
        List<CategoryEntity> categoryList = this.list(queryWrapper);
        if (!CollectionUtils.isEmpty(categoryList)) {
            return "分类已存在";
        }
        this.updateById(category);
        return null;
    }

    private List<CategoryEntity> getChildren(CategoryEntity node, List<CategoryEntity> all) {
        return all.stream()
                .filter(category -> Objects.equals(category.getParentCid(), node.getCatId()))
                .peek(menu -> menu.setChildren(getChildren(menu, all)))
                .sorted(Comparator.comparing(CategoryEntity::getSort))
                .collect(Collectors.toList());
    }
}