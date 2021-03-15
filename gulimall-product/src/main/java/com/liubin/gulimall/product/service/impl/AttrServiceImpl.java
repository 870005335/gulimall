package com.liubin.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.liubin.common.enums.AttrTypeEnum;
import com.liubin.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.liubin.gulimall.product.entity.AttrGroupEntity;
import com.liubin.gulimall.product.entity.CategoryEntity;
import com.liubin.gulimall.product.service.AttrAttrgroupRelationService;
import com.liubin.gulimall.product.service.AttrGroupService;
import com.liubin.gulimall.product.service.CategoryService;
import com.liubin.gulimall.product.vo.AttrRespVo;
import com.liubin.gulimall.product.vo.AttrVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liubin.common.utils.PageUtils;
import com.liubin.common.utils.Query;

import com.liubin.gulimall.product.dao.AttrDao;
import com.liubin.gulimall.product.entity.AttrEntity;
import com.liubin.gulimall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void saveAttr(AttrVo attr) {
        AttrEntity attrSave = new AttrEntity();
        // 保存基本数据到商品属性表
        BeanUtils.copyProperties(attr, attrSave);
        this.save(attrSave);
        // 保存关联关系
        if (AttrTypeEnum.ATTR_TYPE_BASE.getCode() == (attr.getAttrType())) {
            AttrAttrgroupRelationEntity attrRelationSave = new AttrAttrgroupRelationEntity();
            attrRelationSave.setAttrGroupId(attr.getAttrGroupId());
            attrRelationSave.setAttrId(attrSave.getAttrId());
            attrAttrgroupRelationService.save(attrRelationSave);
        }
    }

    @Override
    public PageUtils queryAttrPage(Map<String, Object> params, String attrType, Long catelogId) {
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<AttrEntity>().eq("attr_type",
                "base".equals(attrType) ? AttrTypeEnum.ATTR_TYPE_BASE.getCode() : AttrTypeEnum.ATTR_TYPE_SALE.getCode());
        if (catelogId != 0) {
            queryWrapper.eq("catelog_id", catelogId);
        }
        // 检索字段不为空
        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) {
            queryWrapper.and(wrapper -> {
                wrapper.eq("attr_id", key).or().like("attr_name", key);
            });
        }
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), queryWrapper);
        PageUtils pageUtils = new PageUtils(page);
        // 查询数据不为空，获取分类名称和分组名称
        List<AttrEntity> records = page.getRecords();
        List<AttrRespVo> respList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(records)) {
            // 查询分类名称
            List<Long> catIdList = records.stream()
                    .map(AttrEntity::getCatelogId)
                    .distinct()
                    .collect(Collectors.toList());
            List<CategoryEntity> categoryEntityList = categoryService.listByIds(catIdList);
            Map<Long, String> categoryNameMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(categoryEntityList)) {
                categoryNameMap = categoryEntityList.stream()
                        .collect(Collectors.toMap(CategoryEntity::getCatId, CategoryEntity::getName));
            }
            // 查询分组名称
            Map<Long, AttrGroupEntity> attrGroupMap = queryAttrGroupNameMap(records);
            // 数据处理
            respList = handleAttrPageDataList(records, categoryNameMap, attrGroupMap);

        }
        pageUtils.setList(respList);
        return pageUtils;
    }

    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        AttrRespVo resp = new AttrRespVo();
        AttrEntity attr = this.getById(attrId);
        BeanUtils.copyProperties(attr, resp);
        if (AttrTypeEnum.ATTR_TYPE_BASE.getCode() == attr.getAttrType()) {
            // 查询属性分组名称
            AttrAttrgroupRelationEntity relation = attrAttrgroupRelationService.getOne(
                    new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));
            if (relation != null) {
                Long attrGroupId = relation.getAttrGroupId();
                if (attrGroupId != 0) {
                    AttrGroupEntity groupEntity = attrGroupService.getById(attrGroupId);
                    resp.setAttrGroupId(attrGroupId);
                    resp.setGroupName(groupEntity.getAttrGroupName());
                }
            }
        }
        // 查询分类路径
        Long[] categoryPath = categoryService.queryCategoryPath(attr.getCatelogId());
        resp.setCatelogPath(categoryPath);
        return resp;
    }

    @Override
    public void updateAttr(AttrVo attr) {
        AttrEntity update = new AttrEntity();
        // 更新属性表
        BeanUtils.copyProperties(attr, update);
        this.updateById(update);
        // 更新属性关系表
        AttrAttrgroupRelationEntity relationUpdate = new AttrAttrgroupRelationEntity();
        relationUpdate.setAttrId(attr.getAttrId());
        Long attrGroupId = attr.getAttrGroupId();
        if (attrGroupId == null) {
            attrGroupId = 0L;
        }
        relationUpdate.setAttrGroupId(attrGroupId);
        if (AttrTypeEnum.ATTR_TYPE_BASE.getCode() == (attr.getAttrType())) {
            Wrapper<AttrAttrgroupRelationEntity> updateWrapper = new
                    UpdateWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId());
            attrAttrgroupRelationService.saveOrUpdate(relationUpdate, updateWrapper);
        }
    }

    @Override
    public List<AttrEntity> getAttrRelation(Long attrGroupId) {
        List<AttrEntity> attrEntityList = new ArrayList<>();
        // 查询中间表
        List<AttrAttrgroupRelationEntity> attrRelationList = attrAttrgroupRelationService.list(
                new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrGroupId));
        if (attrRelationList != null) {
            List<Long> attrIdList = attrRelationList
                    .stream()
                    .map(AttrAttrgroupRelationEntity::getAttrId)
                    .collect(Collectors.toList());
            attrEntityList = attrService.listByIds(attrIdList);
        }
        return attrEntityList;
    }

    /**
     * @description: 处理分页返回数据
     * @param records
     * @param categoryNameMap
     * @param attrGroupMap
     * @author: liubin
     * @date: 2021/3/10 0:16
     * @return: java.util.List<com.liubin.gulimall.product.vo.AttrRespVo>
     */
    private List<AttrRespVo> handleAttrPageDataList(List<AttrEntity> records,
                                                    Map<Long, String> categoryNameMap,
                                                    Map<Long, AttrGroupEntity> attrGroupMap) {
        List<AttrRespVo> respList = new ArrayList<>();
        for (AttrEntity record : records) {
            AttrRespVo resp = new AttrRespVo();
            BeanUtils.copyProperties(record, resp);
            // 分类名称
            if (StringUtils.isNotBlank(categoryNameMap.get(record.getCatelogId()))) {
                resp.setCatelogName(categoryNameMap.get(record.getCatelogId()));
            }
            // 属性分组名称
            AttrGroupEntity attrGroupEntity = attrGroupMap.get(record.getAttrId());
            if (attrGroupEntity != null) {
                resp.setAttrGroupId(attrGroupEntity.getAttrGroupId());
                resp.setGroupName(attrGroupEntity.getAttrGroupName());
            }
            respList.add(resp);
        }
        return respList;
    }

    /**
     * @description: 根据商品属性id列表查询属性分组Map
     * @param records
     * @author: liubin
     * @date: 2021/3/9 21:44
     * @return: java.util.Map<java.lang.Long,com.liubin.gulimall.product.entity.AttrGroupEntity>
     */
    private Map<Long, AttrGroupEntity> queryAttrGroupNameMap(List<AttrEntity> records) {
        Map<Long, AttrGroupEntity> attrGroupMap = new HashMap<>();
        // 取出attrId列表
        List<Long> attrIdList = records.stream()
                .map(AttrEntity::getAttrId)
                .collect(Collectors.toList());
        // 查询中间表
        List<AttrAttrgroupRelationEntity> attrRelationList = attrAttrgroupRelationService.list(
                new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_id", attrIdList));
        if (!CollectionUtils.isEmpty(attrRelationList)) {
            // 查询属性分组表
            List<Long> attrGroupIdList = attrRelationList.stream()
                    .map(AttrAttrgroupRelationEntity::getAttrGroupId)
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(attrGroupIdList)) {
                List<AttrGroupEntity> attrGroupEntityList = attrGroupService.listByIds(attrGroupIdList);
                if (!CollectionUtils.isEmpty(attrGroupEntityList)) {
                    for (AttrAttrgroupRelationEntity relationEntity : attrRelationList) {
                        Long attrId = relationEntity.getAttrId();
                        Long attrGroupId = relationEntity.getAttrGroupId();
                        AttrGroupEntity attrGroup = attrGroupEntityList.stream()
                                .filter(groupEntity -> attrGroupId.equals(groupEntity.getAttrGroupId())).findFirst()
                                .orElseGet(AttrGroupEntity::new);
                        attrGroup.setAttrGroupId(attrGroupId);
                        attrGroupMap.put(attrId, attrGroup);
                    }
                }
            }
        }
        return attrGroupMap;
    }
}