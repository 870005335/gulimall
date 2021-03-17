package com.liubin.gulimall.product.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liubin.common.utils.PageUtils;
import com.liubin.common.utils.Query;

import com.liubin.gulimall.product.dao.SpuInfoDao;
import com.liubin.gulimall.product.entity.SpuInfoEntity;
import com.liubin.gulimall.product.service.SpuInfoService;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> queryWrapper = new QueryWrapper<>();
        String status = (String) params.get("status");
        if(StringUtils.isNotBlank(status)) {
            queryWrapper.eq("publish_status",status);
        }
        String brandId = (String) params.get("brandId");
        if(StringUtils.isNotBlank(brandId) && !"0".equals(brandId)) {
            queryWrapper.eq("brand_id",brandId);
        }
        String catelogId = (String) params.get("catelogId");
        if(StringUtils.isNotBlank(catelogId) && !"0".equals(catelogId)) {
            queryWrapper.eq("catalog_id",catelogId);
        }
        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) {
            queryWrapper.like("spu_name", key);
        }
        IPage<SpuInfoEntity> page = this.page(new Query<SpuInfoEntity>().getPage(params), queryWrapper);
        return new PageUtils(page);
    }
}