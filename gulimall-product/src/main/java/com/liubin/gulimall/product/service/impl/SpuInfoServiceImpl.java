package com.liubin.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liubin.gulimall.product.entity.*;
import com.liubin.gulimall.product.service.*;
import com.liubin.gulimall.product.vo.BaseAttrs;
import com.liubin.gulimall.product.vo.SpuSaveVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liubin.gulimall.common.utils.PageUtils;
import com.liubin.gulimall.common.utils.Query;

import com.liubin.gulimall.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private SpuInfoDescService spuInfoDescService;

    @Autowired
    private SpuImagesService spuImagesService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private ProductAttrValueService attrValueService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Object categoryId = params.get("categoryId");
        Object brandId = params.get("brandId");
        Object publishStatus = params.get("publishStatus");
        Object key = params.get("key");
        LambdaQueryWrapper<SpuInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(categoryId != null && Long.parseLong(categoryId.toString()) != 0, SpuInfoEntity::getCategoryId, categoryId)
                .eq(brandId != null && Long.parseLong(brandId.toString()) != 0, SpuInfoEntity::getBrandId, brandId)
                .eq(publishStatus != null && StringUtils.isNotBlank(publishStatus.toString()), SpuInfoEntity::getPublishStatus, publishStatus)
                .like(key != null && StringUtils.isNotBlank(key.toString()), SpuInfoEntity::getSpuName, key);
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity > ().getPage(params),
                queryWrapper
        );
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            handleResultDataList(page.getRecords());
        }
        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSpuInfo(SpuSaveVo vo) {
        Date date = new Date();
        // 1、保存spu基本信息 pms_spu_info
        SpuInfoEntity infoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(vo,infoEntity);
        infoEntity.setCreateTime(date);
        infoEntity.setUpdateTime(date);
        this.baseMapper.insert(infoEntity);

        // 2、保存Spu的描述图片 pms_spu_info_desc
        List<String> description = vo.getDescription();
        SpuInfoDescEntity descEntity = new SpuInfoDescEntity();
        descEntity.setSpuId(infoEntity.getId());
        descEntity.setDescription(String.join(",",description));
        spuInfoDescService.saveSpuInfoDesc(descEntity);

        // 3、保存spu的图片集 pms_spu_images
        List<String> images = vo.getImages();
        spuImagesService.saveImages(infoEntity.getId(),images);

        // 4、保存spu的规格参数;pms_product_attr_value
        List<BaseAttrs> baseAttrs = vo.getBaseAttrs();
        List<ProductAttrValueEntity> collect = baseAttrs.stream().map(attr -> {
            ProductAttrValueEntity valueEntity = new ProductAttrValueEntity();
            valueEntity.setAttrId(attr.getAttrId());
            AttrEntity id = attrService.getById(attr.getAttrId());
            valueEntity.setAttrName(id.getAttrName());
            valueEntity.setAttrValue(attr.getAttrValues());
            valueEntity.setQuickShow(attr.getShowDesc());
            valueEntity.setSpuId(infoEntity.getId());

            return valueEntity;
        }).collect(Collectors.toList());
        attrValueService.saveProductAttr(collect);

        // 5、保存spu的积分信息；gulimall_sms->sms_spu_bounds
    }

    private void handleResultDataList(List<SpuInfoEntity> resultList) {
        List<Long> categoryIdList = resultList.stream()
                .map(SpuInfoEntity::getCategoryId)
                .distinct()
                .collect(Collectors.toList());
        List<Long> brandIdList = resultList.stream()
                .map(SpuInfoEntity::getBrandId)
                .distinct()
                .collect(Collectors.toList());
        Map<Long, String> brandNameMap = brandService.getBrandNameMap(brandIdList);
        Map<Long, String> categoryNameMap = categoryService.getCategoryNameMap(categoryIdList);
        resultList.forEach(result -> {
            result.setBrandName(brandNameMap.get(result.getBrandId()));
            result.setCategoryName(categoryNameMap.get(result.getCategoryId()));
        });
    }

}