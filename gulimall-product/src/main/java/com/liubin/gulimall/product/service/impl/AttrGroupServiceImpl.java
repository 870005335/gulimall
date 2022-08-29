package com.liubin.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liubin.gulimall.common.exception.GuLiMallException;
import com.liubin.gulimall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liubin.gulimall.common.utils.PageUtils;
import com.liubin.gulimall.common.utils.Query;

import com.liubin.gulimall.product.dao.AttrGroupDao;
import com.liubin.gulimall.product.entity.AttrGroupEntity;
import com.liubin.gulimall.product.service.AttrGroupService;
import org.springframework.util.CollectionUtils;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    private CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catId) {
        LambdaQueryWrapper<AttrGroupEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (catId != 0) {
            queryWrapper.eq(AttrGroupEntity::getCategoryId, catId);
        }
        Object key = params.get("key");
        if (key != null) {
            queryWrapper.like(AttrGroupEntity::getAttrGroupName, key);
        }
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void saveAttrGroup(AttrGroupEntity attrGroup) {
        LambdaQueryWrapper<AttrGroupEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AttrGroupEntity::getAttrGroupName, attrGroup.getAttrGroupName());
        List<AttrGroupEntity> attrGroupList = this.list(queryWrapper);
        if (!CollectionUtils.isEmpty(attrGroupList)) {
            throw new GuLiMallException(-1, "分组名称已存在");
        }
        this.save(attrGroup);
    }

    @Override
    public AttrGroupEntity getAttrGroupById(Long attrGroupId) {
        AttrGroupEntity attrGroup = this.getById(attrGroupId);
        if (attrGroup != null && attrGroup.getCategoryId() != null) {
            List<Long> categoryPath = categoryService.queryCategoryPath(attrGroup.getCategoryId());
            attrGroup.setCategoryPath(categoryPath);
        }
        return attrGroup;
    }

    @Override
    public void updateAttrGroup(AttrGroupEntity attrGroup) {
        LambdaQueryWrapper<AttrGroupEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AttrGroupEntity::getAttrGroupName, attrGroup.getAttrGroupName());
        queryWrapper.ne(AttrGroupEntity::getAttrGroupId, attrGroup.getAttrGroupId());
        List<AttrGroupEntity> attrGroupList = this.list(queryWrapper);
        if (!CollectionUtils.isEmpty(attrGroupList)) {
            throw new GuLiMallException(-1, "分组名称已存在");
        }
        this.updateById(attrGroup);
    }
}