package com.liubin.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liubin.gulimall.common.exception.GuLiMallException;
import com.liubin.gulimall.product.entity.AttrEntity;
import com.liubin.gulimall.product.entity.AttrGroupRelationEntity;
import com.liubin.gulimall.product.service.AttrGroupRelationService;
import com.liubin.gulimall.product.service.AttrService;
import com.liubin.gulimall.product.service.CategoryService;
import com.liubin.gulimall.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    private AttrGroupRelationService attrGroupRelationService;

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

    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCategoryId(Long categoryId) {
        List<AttrGroupWithAttrsVo> resultList = new ArrayList<>();
        // 查询分组信息
        List<AttrGroupEntity> groupList = this.list(new LambdaQueryWrapper<AttrGroupEntity>()
                .eq(AttrGroupEntity::getCategoryId, categoryId));
        if (!CollectionUtils.isEmpty(groupList)) {
            List<Long> groupIdList = groupList.stream()
                    .map(AttrGroupEntity::getAttrGroupId)
                    .collect(Collectors.toList());
            List<AttrEntity> attrs = attrGroupRelationService.queryAttrsByGroupIds(groupIdList);
            Map<Long, List<AttrEntity>> attrsMap;
            if (!CollectionUtils.isEmpty(attrs)) {
                attrsMap = attrs.stream().collect(Collectors.groupingBy(AttrEntity::getAttrGroupId));
            } else {
                attrsMap = new HashMap<>();
            }
            resultList = groupList.stream().map(group -> {
                AttrGroupWithAttrsVo attrsVo = new AttrGroupWithAttrsVo();
                BeanUtils.copyProperties(group, attrsVo);
                attrsVo.setAttrs(attrsMap.get(group.getAttrGroupId()));
                return attrsVo;
            }).collect(Collectors.toList());
        }
        return resultList;
    }

    @Override
    public void deleteAttrGroups(List<Long> groupIdList) {
        this.removeBatchByIds(groupIdList);
        // 移除关联关系
        this.attrGroupRelationService.remove(new LambdaQueryWrapper<AttrGroupRelationEntity>()
                .in(AttrGroupRelationEntity::getAttrGroupId, groupIdList));
    }
}