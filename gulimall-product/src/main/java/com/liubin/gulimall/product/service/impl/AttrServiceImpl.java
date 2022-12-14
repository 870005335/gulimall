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

import java.util.*;
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
        // ????????????????????????????????????
        BeanUtils.copyProperties(attr, attrSave);
        this.save(attrSave);
        // ??????????????????
        if (AttrTypeEnum.ATTR_TYPE_BASE.getCode() == (attr.getAttrType()) && attr.getAttrGroupId() != null) {
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
        // ?????????????????????
        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) {
            queryWrapper.and(wrapper -> {
                wrapper.eq("attr_id", key).or().like("attr_name", key);
            });
        }
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), queryWrapper);
        PageUtils pageUtils = new PageUtils(page);
        // ?????????????????????????????????????????????????????????
        List<AttrEntity> records = page.getRecords();
        List<AttrRespVo> respList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(records)) {
            // ??????????????????
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
            // ??????????????????
            Map<Long, AttrGroupEntity> attrGroupMap = queryAttrGroupNameMap(records);
            // ????????????
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
            // ????????????????????????
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
        // ??????????????????
        Long[] categoryPath = categoryService.queryCategoryPath(attr.getCatelogId());
        resp.setCatelogPath(categoryPath);
        return resp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAttr(AttrVo attr) {
        AttrEntity update = new AttrEntity();
        // ???????????????
        BeanUtils.copyProperties(attr, update);
        this.updateById(update);
        // ?????????????????????
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
        // ???????????????
        List<AttrAttrgroupRelationEntity> attrRelationList = attrAttrgroupRelationService.list(
                new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrGroupId));
        if (!CollectionUtils.isEmpty(attrRelationList)) {
            List<Long> attrIdList = attrRelationList
                    .stream()
                    .map(AttrAttrgroupRelationEntity::getAttrId)
                    .collect(Collectors.toList());
            attrEntityList = attrService.listByIds(attrIdList);
        }
        return attrEntityList;
    }

    @Override
    public PageUtils getAttrNoRelation(Long attrGroupId, Map<String, Object> params) {
        // ??????????????????????????????????????????????????????????????????
        AttrGroupEntity groupEntity = attrGroupService.getById(attrGroupId);
        if (groupEntity != null) {
            // ?????????????????????????????????????????????????????????
            Long catelogId = groupEntity.getCatelogId();
            // ????????????Id???????????????id
            Wrapper<AttrGroupEntity> queryGroupWrapper = new QueryWrapper<AttrGroupEntity>()
                    .eq("catelog_id", catelogId).ne("attr_group_id", attrGroupId);
            List<AttrGroupEntity> otherGroupList = attrGroupService.list(queryGroupWrapper);
            List<Long> attrIdList = new ArrayList<>();
            otherGroupList.add(groupEntity);
            // ??????attrGroupId??????
            List<Long> otherGroupIdList = otherGroupList
                    .stream()
                    .map(AttrGroupEntity::getAttrGroupId)
                    .collect(Collectors.toList());
            // ??????attrGroupId??????????????????????????????
            Wrapper<AttrAttrgroupRelationEntity>  queryRelationWrapper = new QueryWrapper<AttrAttrgroupRelationEntity>()
                    .in("attr_group_id", otherGroupIdList);
            List<AttrAttrgroupRelationEntity> relationList = attrAttrgroupRelationService.list(queryRelationWrapper);
            if (!CollectionUtils.isEmpty(otherGroupList)) {
                attrIdList = relationList.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());
            }
            QueryWrapper<AttrEntity> attrQueryWrapper = new QueryWrapper<>();
            attrQueryWrapper.eq("catelog_id", catelogId).eq("attr_type", AttrTypeEnum.ATTR_TYPE_BASE.getCode());
            String key = (String) params.get("key");
            if (StringUtils.isNotBlank(key)) {
                attrQueryWrapper.like("attr_name", key);
            }
            if (!CollectionUtils.isEmpty(attrIdList)) {
                attrQueryWrapper.notIn("attr_id", attrIdList);
            }
            IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), attrQueryWrapper);
            return new PageUtils(page);
        }
        return null;
    }

    /**
     * @description: ????????????????????????
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
            // ????????????
            if (StringUtils.isNotBlank(categoryNameMap.get(record.getCatelogId()))) {
                resp.setCatelogName(categoryNameMap.get(record.getCatelogId()));
            }
            // ??????????????????
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
     * @description: ??????????????????id????????????????????????Map
     * @param records
     * @author: liubin
     * @date: 2021/3/9 21:44
     * @return: java.util.Map<java.lang.Long,com.liubin.gulimall.product.entity.AttrGroupEntity>
     */
    private Map<Long, AttrGroupEntity> queryAttrGroupNameMap(List<AttrEntity> records) {
        Map<Long, AttrGroupEntity> attrGroupMap = new HashMap<>();
        // ??????attrId??????
        List<Long> attrIdList = records.stream()
                .map(AttrEntity::getAttrId)
                .collect(Collectors.toList());
        // ???????????????
        List<AttrAttrgroupRelationEntity> attrRelationList = attrAttrgroupRelationService.list(
                new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_id", attrIdList));
        List<AttrAttrgroupRelationEntity> filterRelationList = attrRelationList
                .stream()
                .filter(relation -> relation.getAttrGroupId() != null)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(filterRelationList)) {
            // ?????????????????????
            List<Long> attrGroupIdList = filterRelationList.stream()
                    .map(AttrAttrgroupRelationEntity::getAttrGroupId)
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(attrGroupIdList)) {
                List<AttrGroupEntity> attrGroupEntityList = attrGroupService.listByIds(attrGroupIdList);
                if (!CollectionUtils.isEmpty(attrGroupEntityList)) {
                    for (AttrAttrgroupRelationEntity relationEntity : filterRelationList) {
                        Long attrId = relationEntity.getAttrId();
                        Long attrGroupId = relationEntity.getAttrGroupId();
                        AttrGroupEntity attrGroup = attrGroupEntityList.stream()
                                .filter(groupEntity -> attrGroupId.equals(groupEntity.getAttrGroupId()))
                                .findFirst()
                                .orElseGet(AttrGroupEntity::new);
                        attrGroup.setAttrGroupId(attrGroupId);
                        attrGroupMap.put(attrId, attrGroup);
                    }
                }
            }
        }
        return attrGroupMap;
    }

    @Override
    public void deleteAttrs(List<Long> idList, String type) {
        this.removeByIds(idList);
        if ("base".equals(type)) {
            attrAttrgroupRelationService.remove(
                    new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_id", idList));
        }
    }

    @Override
    public List<Long> getSearchableProductAttrIdList(List<Long> productAttrIdList) {
        List<AttrEntity> attrEntityList = this.list(new QueryWrapper<AttrEntity>().in("attr_id", productAttrIdList)
                .eq("search_type", "1").eq("enable", "1"));
        if (!CollectionUtils.isEmpty(attrEntityList)) {
            return attrEntityList.stream().map(AttrEntity::getAttrId).collect(Collectors.toList());
        }
        return null;
    }
}