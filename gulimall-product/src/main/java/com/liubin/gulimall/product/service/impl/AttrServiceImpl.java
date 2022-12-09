package com.liubin.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liubin.gulimall.common.enums.product.AttrEnum;
import com.liubin.gulimall.common.exception.GuLiMallException;
import com.liubin.gulimall.product.entity.AttrGroupEntity;
import com.liubin.gulimall.product.entity.AttrGroupRelationEntity;
import com.liubin.gulimall.product.entity.CategoryEntity;
import com.liubin.gulimall.product.service.AttrGroupRelationService;
import com.liubin.gulimall.product.service.AttrGroupService;
import com.liubin.gulimall.product.service.CategoryService;
import com.liubin.gulimall.product.vo.AttrRespVo;
import com.liubin.gulimall.product.vo.AttrVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liubin.gulimall.common.utils.PageUtils;
import com.liubin.gulimall.common.utils.Query;

import com.liubin.gulimall.product.dao.AttrDao;
import com.liubin.gulimall.product.entity.AttrEntity;
import com.liubin.gulimall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    private AttrGroupRelationService attrGroupRelationService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrGroupService attrGroupService;

    @Override
    public PageUtils queryPage(Map<String, Object> params, String attrType, Long catId) {
        LambdaQueryWrapper<AttrEntity> queryWrapper = new LambdaQueryWrapper<>();
        int type = AttrEnum.ATTR_TYPE_BASE.getCode();
        if (StringUtils.endsWithIgnoreCase(attrType, AttrEnum.ATTR_TYPE_SALE.getValue())) {
            type = AttrEnum.ATTR_TYPE_SALE.getCode();
        }
        queryWrapper.eq(AttrEntity::getAttrType, type);
        if (catId != 0) {
            queryWrapper.eq(AttrEntity::getCategoryId, catId);
        }
        Object key = params.get("key");
        if (key != null) {
            queryWrapper.like(AttrEntity::getAttrName, key);
        }
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                queryWrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            // 所属分类
            List<Long> categoryIds = page.getRecords().stream()
                    .map(AttrEntity::getCategoryId)
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.toList());
            Map<Long, String> categoryNameMap = categoryService.getCategoryNameMap(categoryIds);
            // 查询规格参数分组信息
            Map<Long, String> attrGroupNameMap = null;
            if (Objects.equals(AttrEnum.ATTR_TYPE_BASE.getValue(), attrType)) {
                List<Long> attrIds = page.getRecords().stream()
                        .map(AttrEntity::getAttrId)
                        .collect(Collectors.toList());
                attrGroupNameMap = attrGroupRelationService.getAttrGroupNameMap(attrIds);
            }
            List<AttrRespVo> resultList = handlePageRecordList(page.getRecords(), categoryNameMap, attrGroupNameMap);
            pageUtils.setList(resultList);
            return pageUtils;
        }

        return pageUtils;
    }

    /**
     * @Author liubin
     * @Description 处理分页列表数据
     * @Date 15:17 2022/8/31
     * @param records
     * @param categoryNameMap
     * @param attrGroupNameMap
     * @return java.util.List<com.liubin.gulimall.product.vo.AttrRespVo>
     **/
    private List<AttrRespVo> handlePageRecordList(List<AttrEntity> records, Map<Long, String> categoryNameMap, Map<Long, String> attrGroupNameMap) {
        return records.stream().map(record -> {
            AttrRespVo respVo = new AttrRespVo();
            BeanUtils.copyProperties(record, respVo);
            respVo.setCategoryName(categoryNameMap.get(record.getCategoryId()));
            if (!CollectionUtils.isEmpty(attrGroupNameMap)) {
                respVo.setAttrGroupName(attrGroupNameMap.get(record.getAttrId()));
            }
            return respVo;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAttr(AttrVo attr) {
        // 名称重复验证
        Boolean isRepeat = checkAttrNameRepeat(attr);
        if (isRepeat) {
            throw new GuLiMallException(-1, "属性名称已存在");
        }
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        this.save(attrEntity);
        // 保存分组属性关系
        if (AttrEnum.ATTR_TYPE_BASE.getCode() == attr.getAttrType() && attr.getAttrGroupId() != null) {
            AttrGroupRelationEntity relationEntity = new AttrGroupRelationEntity();
            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            relationEntity.setAttrSort(attr.getSort());
            relationEntity.setAttrId(attrEntity.getAttrId());
            this.attrGroupRelationService.save(relationEntity);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAttr(AttrVo attr) {
        // 名称重复验证
        Boolean isRepeat = checkAttrNameRepeat(attr);
        if (isRepeat) {
            throw new GuLiMallException(-1, "属性名称已存在");
        }
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        this.updateById(attrEntity);
        // 修改分组属性关系
        if (AttrEnum.ATTR_TYPE_BASE.getCode() == attr.getAttrType()) {
            // 先进行删除操作
            LambdaQueryWrapper<AttrGroupRelationEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AttrGroupRelationEntity::getAttrGroupId, attr.getAttrGroupId());
            queryWrapper.eq(AttrGroupRelationEntity::getAttrId, attr.getAttrId());
            this.attrGroupRelationService.remove(queryWrapper);
            if (attr.getAttrGroupId() != null) {
                AttrGroupRelationEntity relationEntity = new AttrGroupRelationEntity();
                relationEntity.setAttrGroupId(attr.getAttrGroupId());
                relationEntity.setAttrSort(attr.getSort());
                relationEntity.setAttrId(attrEntity.getAttrId());
                this.attrGroupRelationService.save(relationEntity);
            }
        }
    }

    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        AttrRespVo respVo = new AttrRespVo();
        // 查询详细信息
        AttrEntity attr = this.getById(attrId);
        if (attr != null) {
            BeanUtils.copyProperties(attr, respVo);
            // 查询分类信息
            Long categoryId = attr.getCategoryId();
            List<Long> categoryPath = categoryService.queryCategoryPath(categoryId);
            respVo.setCategoryPath(categoryPath);
            CategoryEntity category = categoryService.getById(categoryId);
            respVo.setCategoryName(category != null?category.getName() : "");
            // 基本属性查询分组相关信息
            if (AttrEnum.ATTR_TYPE_BASE.getCode() == attr.getAttrType()) {
                AttrGroupRelationEntity groupRelation = this.attrGroupRelationService.getOne(new LambdaQueryWrapper<AttrGroupRelationEntity>()
                        .eq(AttrGroupRelationEntity::getAttrId, attr.getAttrId()));
                if (groupRelation != null) {
                    respVo.setAttrGroupId(groupRelation.getAttrGroupId());
                    // 获取分组名称
                    AttrGroupEntity attrGroup = attrGroupService.getById(groupRelation.getAttrGroupId());
                    respVo.setAttrGroupName(attrGroup == null? "" : attrGroup.getAttrGroupName());
                }
            }
        }
        return respVo;
    }

    @Override
    public List<AttrEntity> getAttrRelation(Long attrGroupId) {
        List<AttrEntity> relationAttrs = new ArrayList<>();
        // 查询关联关系表中数据
        LambdaQueryWrapper<AttrGroupRelationEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AttrGroupRelationEntity::getAttrGroupId, attrGroupId);
        List<AttrGroupRelationEntity> relationList = this.attrGroupRelationService.list(queryWrapper);
        if (!CollectionUtils.isEmpty(relationList)) {
            // 取出属性Id列
            List<Long> attrIds = relationList.stream()
                    .map(AttrGroupRelationEntity::getAttrId)
                    .collect(Collectors.toList());
            relationAttrs = this.list(new LambdaQueryWrapper<AttrEntity>().in(AttrEntity::getAttrId, attrIds));
        }
        return relationAttrs;
    }

    @Override
    public PageUtils getNoAttrRelation(Map<String, Object> params, Long attrGroupId) {
        // 1、当前分组只能关联自己所属的分类里面的所有属性
        AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        Long categoryId = attrGroup.getCategoryId();
        //2、当前分组只能关联别的分组没有引用的属性
        //2.1）、当前分类下的其它分组
        List<AttrGroupEntity> groupEntityList = this.attrGroupService.list(new LambdaQueryWrapper<AttrGroupEntity>()
                .eq(AttrGroupEntity::getCategoryId, categoryId));
        List<Long> groupIdList = groupEntityList.stream()
                .map(AttrGroupEntity::getAttrGroupId)
                .collect(Collectors.toList());
        // 查询这些分组关联的属性
        List<AttrGroupRelationEntity> attrs = this.attrGroupRelationService.list(new LambdaQueryWrapper<AttrGroupRelationEntity>()
                .in(AttrGroupRelationEntity::getAttrGroupId, groupIdList));
        List<Long> attrIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(attrs)) {
            attrIds = attrs.stream().map(AttrGroupRelationEntity::getAttrId).collect(Collectors.toList());
        }
        LambdaQueryWrapper<AttrEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AttrEntity::getCategoryId, categoryId)
                .eq(AttrEntity::getAttrType, AttrEnum.ATTR_TYPE_BASE.getCode())
                .notIn(!CollectionUtils.isEmpty(attrIds), AttrEntity::getAttrId, attrIds)
                .like(params.get("key") != null, AttrEntity::getAttrName, params.get("key"));
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), queryWrapper);
        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAttrsByIds(List<Long> attrIdList) {
        this.removeByIds(attrIdList);
        this.attrGroupRelationService.remove(new LambdaQueryWrapper<AttrGroupRelationEntity>()
                .in(AttrGroupRelationEntity::getAttrId, attrIdList));
    }

    /**
     * @Author liubin
     * @Description 校验attrName是否重复
     * @Date 17:36 2022/8/30
     * @param attr
     * @return java.lang.Boolean
     **/
    private Boolean checkAttrNameRepeat(AttrVo attr) {
        boolean isRepeat = false;
        LambdaQueryWrapper<AttrEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AttrEntity::getAttrName, attr.getAttrName());
        if (attr.getAttrId() != 0){
            queryWrapper.ne(AttrEntity::getAttrId, attr.getAttrId());
        }
        List<AttrEntity> attrEntityList = this.list(queryWrapper);
        if (!CollectionUtils.isEmpty(attrEntityList)) {
            isRepeat = true;
        }
        return isRepeat;
    }
}