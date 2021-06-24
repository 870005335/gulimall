package com.liubin.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liubin.common.dto.SkuReductionDto;
import com.liubin.common.dto.SkuStockDto;
import com.liubin.common.dto.SpuBoundsDto;
import com.liubin.common.dto.es.SkuEsModel;
import com.liubin.common.utils.R;
import com.liubin.gulimall.product.entity.*;
import com.liubin.gulimall.product.feign.CouponFeignService;
import com.liubin.gulimall.product.feign.SearchFeignService;
import com.liubin.gulimall.product.feign.WareFeignService;
import com.liubin.gulimall.product.service.*;
import com.liubin.gulimall.product.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liubin.common.utils.PageUtils;
import com.liubin.common.utils.Query;

import com.liubin.gulimall.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    private SpuInfoDescService spuInfoDescService;

    @Autowired
    private SpuImagesService spuImagesService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private ProductAttrValueService productAttrValueService;

    @Autowired
    private SkuInfoService skuInfoService;

    @Autowired
    private SkuImagesService skuImagesService;

    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    private CouponFeignService couponFeignService;

    @Autowired
    private WareFeignService wareFeignService;

    @Autowired
    private SearchFeignService searchFeignService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSpuInfo(SpuSaveVo spuInfo) {
        Date nowDate = new Date();
        // 1、保存spu基本信息 pms_spu_info
        SpuInfoEntity saveInfo = new SpuInfoEntity();
        BeanUtils.copyProperties(spuInfo, saveInfo);
        saveInfo.setCreateTime(nowDate);
        saveInfo.setUpdateTime(nowDate);
        this.saveSpuBaseInfo(saveInfo);
        // 2、保存spu的描述图片 pms_spu_info_desc
        List<String> decript = spuInfo.getDecript();
        SpuInfoDescEntity descSave = new SpuInfoDescEntity();
        descSave.setSpuId(saveInfo.getId());
        descSave.setDecript(String.join(",", decript));
        spuInfoDescService.save(descSave);
        // 3、保存spu的图片集 pms_spu_images
        spuImagesService.saveImages(saveInfo.getId(), spuInfo.getImages());
        // 4、保存spu的规格参数 pms_product_attr_value
        List<BaseAttrs> attrsList = spuInfo.getBaseAttrs();
        List<ProductAttrValueEntity> productAttrList = attrsList.stream().map(baseAttr -> {
            ProductAttrValueEntity valueEntity = new ProductAttrValueEntity();
            valueEntity.setAttrId(baseAttr.getAttrId());
            valueEntity.setAttrValue(baseAttr.getAttrValues());
            valueEntity.setQuickShow(baseAttr.getShowDesc());
            valueEntity.setSpuId(saveInfo.getId());
            AttrEntity attr = attrService.getById(baseAttr.getAttrId());
            if (attr != null) {
                valueEntity.setAttrName(attr.getAttrName());
            }
            return valueEntity;
        }).collect(Collectors.toList());
        productAttrValueService.saveBatch(productAttrList);
        // 5、保存spu的积分信息
        SpuBoundsDto spuBoundsDto = new SpuBoundsDto();
        BeanUtils.copyProperties(spuInfo.getBounds(), spuBoundsDto);
        spuBoundsDto.setSpuId(saveInfo.getId());
        R resp = couponFeignService.saveSpuBounds(spuBoundsDto);
        if (resp.getCode() != 0) {
            log.error("远程保存spu积分信息失败");
        }
        // 6、保存spu对应的skus
       this.saveSkuInfoList(spuInfo, saveInfo);
    }

    @Override
    public void saveSpuBaseInfo(SpuInfoEntity saveInfo) {
        this.baseMapper.insert(saveInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePublishStatusUp(SpuStatusVo statusVo) throws JsonProcessingException {
        List<SkuEsModel> upProductList = new ArrayList<>();

        // 根据spuId获取spu下所有的sku信息列表
        List<SkuInfoEntity> skuList = skuInfoService.querySkuInfoListBySpuId(statusVo.getId());
        if (!CollectionUtils.isEmpty(skuList)) {
            List<Long> skuIdList = skuList.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());
            // 查询当前sku的所有可检索的规格属性
            List<SkuEsModel.Attrs> spuSearchableAttrs = getSpuSearchableAttrs(statusVo.getId());
            // 发送远程调用 查询库存系统中是否有库存
            R hasStockResp = wareFeignService.getSkusHasStock(skuIdList);
            Map<Long, Boolean> stockMap = new HashMap<>();
            if (hasStockResp.getCode() == 0) {
                ObjectMapper objectMapper = new ObjectMapper();
                List<SkuStockDto> stockDtoList = objectMapper.readValue(JSON.toJSONString(hasStockResp.get("data")), new TypeReference<List<SkuStockDto>>() {
                });
                if (!CollectionUtils.isEmpty(stockDtoList)) {
                    for (SkuStockDto stockDto : stockDtoList) {
                        stockMap.put(stockDto.getSkuId(), stockDto.getHasStock());
                    }
                }
            }
            for (SkuInfoEntity skuInfoEntity : skuList) {
                // 查询数据并处理得到es数据对象
                SkuEsModel esModel = new SkuEsModel();
                BeanUtils.copyProperties(skuInfoEntity, esModel);
                esModel.setSkuPrice(skuInfoEntity.getPrice());
                esModel.setSkuImg(skuInfoEntity.getSkuDefaultImg());
                // 库存信息
                esModel.setHasStock(true);
                if (stockMap.get(skuInfoEntity.getSkuId()) != null) {
                    esModel.setHasStock(stockMap.get(skuInfoEntity.getSkuId()));
                }
                // TODO 热度评分
                esModel.setHotScore(0L);
                // 查询分类和品牌信息
                BrandEntity brand = brandService.getById(skuInfoEntity.getBrandId());
                if (brand != null) {
                    esModel.setBrandName(brand.getName());
                    esModel.setBrandImg(brand.getLogo());
                }
                CategoryEntity category = categoryService.getById(skuInfoEntity.getCatalogId());
                esModel.setCatalogName(category == null ? "" : category.getName());
                // sku可检索属性
                esModel.setAttrs(spuSearchableAttrs);
                upProductList.add(esModel);
            }
            // 远程调用检索服务 保存es数据
            if (!CollectionUtils.isEmpty(upProductList)) {
                R response = searchFeignService.productStatusUp(upProductList);
                if (response.getCode() == 0) {
                    // 远程调用成功 修改数据库上架状态
                    SpuInfoEntity update = new SpuInfoEntity();
                    update.setId(statusVo.getId());
                    update.setPublishStatus(statusVo.getPublishStatus());
                    this.baseMapper.updateById(update);
                } else {
                    // TODO 重复调用？接口幂等性 重试机制
                }
            }
        }
    }

    @Override
    public void updatePublishStatusDown(SpuStatusVo statusVo) {
        SpuInfoEntity update = new SpuInfoEntity();
        update.setId(statusVo.getId());
        update.setPublishStatus(statusVo.getPublishStatus());
        this.baseMapper.updateById(update);
    }

    private List<SkuEsModel.Attrs> getSpuSearchableAttrs(Long spuId) {
        List<ProductAttrValueEntity> productAttrValueList = productAttrValueService.list(
                new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));
        if (!CollectionUtils.isEmpty(productAttrValueList)) {
            // 取出productAttrIdList
            List<Long> productAttrIdList = productAttrValueList.stream()
                    .map(ProductAttrValueEntity::getAttrId).collect(Collectors.toList());
            // 查询productAttrIdList中可检索的Id
            List<Long> searchableAttrIdList = attrService.getSearchableProductAttrIdList(productAttrIdList);
            if (!CollectionUtils.isEmpty(searchableAttrIdList)) {
                List<ProductAttrValueEntity> filterList = productAttrValueList.stream().filter(productAttr
                        -> searchableAttrIdList.contains(productAttr.getAttrId())).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(filterList)) {
                    return filterList.stream().map(productAttr -> {
                        SkuEsModel.Attrs attr = new SkuEsModel.Attrs();
                        BeanUtils.copyProperties(productAttr, attr);
                        return attr;
                    }).collect(Collectors.toList());
                }
            }
        }
        return new ArrayList<>();
    }

    /**
     * @Author liubin
     * @Description 保存商品skus信息
     * @Date 16:03 2021/3/19
     * @param spuInfo
     * @param saveInfo
     * @return
     **/
    private void saveSkuInfoList(SpuSaveVo spuInfo, SpuInfoEntity saveInfo) {
        List<Skus> skuList = spuInfo.getSkus();
        if (!CollectionUtils.isEmpty(skuList)) {
            skuList.forEach(sku -> {
                // 保存sku基本信息
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(sku, skuInfoEntity);
                skuInfoEntity.setBrandId(saveInfo.getBrandId());
                skuInfoEntity.setCatalogId(saveInfo.getCatalogId());
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSpuId(saveInfo.getId());
                List<Images> skuImageList = sku.getImages();
                Images defaultImg = skuImageList.stream().filter(img ->
                        img.getDefaultImg() == 1).findFirst().orElseGet(Images::new);
                skuInfoEntity.setSkuDefaultImg(defaultImg.getImgUrl());
                skuInfoService.save(skuInfoEntity);
                // 保存sku图片信息
                List<SkuImagesEntity> imagesEntityList = skuImageList.stream()
                        .filter(img -> StringUtils.isNotBlank(img.getImgUrl())).map(img -> {
                    SkuImagesEntity imagesEntity = new SkuImagesEntity();
                    imagesEntity.setSkuId(skuInfoEntity.getSkuId());
                    imagesEntity.setImgUrl(img.getImgUrl());
                    imagesEntity.setDefaultImg(img.getDefaultImg());
                    return imagesEntity;
                }).collect(Collectors.toList());
                skuImagesService.saveBatch(imagesEntityList);
                // 保存sku销售属性信息
                List<Attr> attrs = sku.getAttr();
                List<SkuSaleAttrValueEntity> valueEntityList = attrs.stream().map(attr -> {
                    SkuSaleAttrValueEntity valueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(attr, valueEntity);
                    valueEntity.setSkuId(skuInfoEntity.getSkuId());
                    return valueEntity;
                }).collect(Collectors.toList());
                skuSaleAttrValueService.saveBatch(valueEntityList);
                SkuReductionDto reductionDto = new SkuReductionDto();
                BeanUtils.copyProperties(sku, reductionDto);
                reductionDto.setSkuId(skuInfoEntity.getSkuId());
                if (reductionDto.getFullCount() > 0 || reductionDto.getFullPrice().compareTo(new BigDecimal(0)) > 0) {
                    R resp = couponFeignService.saveSkuReduction(reductionDto);
                    if (resp.getCode() != 0) {
                        log.error("远程保存sku优惠信息失败");
                    }
                }
            });
        }
    }
}