package com.liubin.gulimall.product.service.impl;

import com.liubin.common.enums.AttrTypeEnum;
import com.liubin.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.liubin.gulimall.product.service.AttrAttrgroupRelationService;
import com.liubin.gulimall.product.vo.AttrVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liubin.common.utils.PageUtils;
import com.liubin.common.utils.Query;

import com.liubin.gulimall.product.dao.AttrDao;
import com.liubin.gulimall.product.entity.AttrEntity;
import com.liubin.gulimall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

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
        AttrAttrgroupRelationEntity attrRelationSave = new AttrAttrgroupRelationEntity();
        attrRelationSave.setAttrGroupId(attr.getAttrGroupId());
        attrRelationSave.setAttrId(attr.getAttrId());
        attrAttrgroupRelationService.save(attrRelationSave);
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
        return null;
    }
}