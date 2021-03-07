package com.liubin.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liubin.common.utils.PageUtils;
import com.liubin.common.utils.Query;
import com.liubin.gulimall.product.dao.AttrGroupDao;
import com.liubin.gulimall.product.entity.AttrGroupEntity;
import com.liubin.gulimall.product.service.AttrGroupService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Integer categoryId) {
        IPage<AttrGroupEntity> page;
        if (categoryId == 0) {
            page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    new QueryWrapper<>()
            );
        } else {
            String key = (String) params.get("key");
            QueryWrapper<AttrGroupEntity> wrapper = new
                    QueryWrapper<AttrGroupEntity>().eq("catelog_id", categoryId);
            if (StringUtils.isNotBlank(key)) {
                wrapper.and((obj) -> obj.eq("attr_group_id", key).or().like("attr_group_name", key));
            }
            page = this.page(new Query<AttrGroupEntity>().getPage(params), wrapper);
        }
        System.out.println(JSON.toJSONString(page));
        return new PageUtils(page);
    }
}