package com.liubin.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liubin.gulimall.common.utils.PageUtils;
import com.liubin.gulimall.common.utils.Query;

import com.liubin.gulimall.product.dao.BrandDao;
import com.liubin.gulimall.product.entity.BrandEntity;
import com.liubin.gulimall.product.service.BrandService;
import org.springframework.util.CollectionUtils;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<BrandEntity> queryWrapper = new LambdaQueryWrapper<>();
        Object key = params.get("key");
        if (key != null) {
            queryWrapper.like(BrandEntity::getName, key);
        }
        queryWrapper.orderByAsc(BrandEntity::getSort);
        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public Map<Long, String> getBrandNameMap(List<Long> brandIdList) {
        Map<Long, String> brandNameMap = new HashMap<>();
        List<BrandEntity> brandList = this.listByIds(brandIdList);
        if (!CollectionUtils.isEmpty(brandList)) {
            brandNameMap = brandList.stream().collect(Collectors.toMap(BrandEntity::getBrandId, BrandEntity::getName));
        }
        return brandNameMap;
    }
}