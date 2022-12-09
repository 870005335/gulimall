package com.liubin.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liubin.gulimall.product.entity.AttrEntity;
import com.liubin.gulimall.product.vo.AttrRelationVo;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liubin.gulimall.common.utils.PageUtils;
import com.liubin.gulimall.common.utils.Query;

import com.liubin.gulimall.product.dao.AttrGroupRelationDao;
import com.liubin.gulimall.product.entity.AttrGroupRelationEntity;
import com.liubin.gulimall.product.service.AttrGroupRelationService;
import org.springframework.util.CollectionUtils;


@Service("attrGroupRelationService")
public class AttrGroupRelationServiceImpl extends ServiceImpl<AttrGroupRelationDao, AttrGroupRelationEntity> implements AttrGroupRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupRelationEntity> page = this.page(
                new Query<AttrGroupRelationEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public Map<Long, String> getAttrGroupNameMap(List<Long> attrIds) {
        Map<Long, String> attrGroupNameMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(attrIds)) {
            List<AttrGroupRelationEntity> groupRelationList = this.baseMapper.getAttrGroupListByAttrIds(attrIds);
            if (!CollectionUtils.isEmpty(groupRelationList)) {
                attrGroupNameMap = groupRelationList.stream().collect(Collectors
                        .toMap(AttrGroupRelationEntity::getAttrId, AttrGroupRelationEntity::getAttrGroupName));
            }
        }
        return attrGroupNameMap;
    }

    @Override
    public void deleteAttrRelation(AttrRelationVo delVo) {
        LambdaQueryWrapper<AttrGroupRelationEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AttrGroupRelationEntity::getAttrGroupId, delVo.getAttrGroupId());
        queryWrapper.in(AttrGroupRelationEntity::getAttrId, delVo.getAttrIds());
        this.remove(queryWrapper);
    }

    @Override
    public void saveAttrRelation(AttrRelationVo saveVo) {
        Long attrGroupId = saveVo.getAttrGroupId();
        List<Long> attrIds = saveVo.getAttrIds();
        AtomicInteger index = new AtomicInteger(0);
        List<AttrGroupRelationEntity> relationEntities = attrIds.stream().map(attrId -> {
            AttrGroupRelationEntity relation = new AttrGroupRelationEntity();
            relation.setAttrGroupId(attrGroupId);
            relation.setAttrId(attrId);
            relation.setAttrSort(index.getAndIncrement());
            return relation;
        }).collect(Collectors.toList());
        this.saveBatch(relationEntities);
    }

    @Override
    public List<AttrEntity> queryAttrsByGroupIds(List<Long> groupIdList) {
        return this.baseMapper.queryAttrsByGroupIds(groupIdList);
    }
}