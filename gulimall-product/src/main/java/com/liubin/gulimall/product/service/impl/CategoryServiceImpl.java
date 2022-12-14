package com.liubin.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.liubin.gulimall.product.service.CategoryBrandRelationService;
import com.liubin.gulimall.product.vo.Catalog2Vo;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liubin.common.utils.PageUtils;
import com.liubin.common.utils.Query;

import com.liubin.gulimall.product.dao.CategoryDao;
import com.liubin.gulimall.product.entity.CategoryEntity;
import com.liubin.gulimall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        // ??????????????????
        List<CategoryEntity> entities = baseMapper.selectList(null);
        // ???????????????????????????
        return entities.stream()
                .filter(categoryEntity -> categoryEntity.getParentCid() == 0)
                .peek(menu-> menu.setChildren(getChildren(menu,entities)))
                .sorted(Comparator.comparingInt(menu -> (menu.getSort() == null ? 0 : menu.getSort())))
                .collect(Collectors.toList());
    }

    @Override
    public void removeMenuByIds(List<Long> catIds) {
        //TODO  ?????????????????????????????????????????????????????????

        //????????????
        baseMapper.deleteBatchIds(catIds);
    }

    @Override
    public Long[] queryCategoryPath(Long catId) {
        List<Long> catPath = new ArrayList<>();
        queryCategoryPath(catId, catPath);
        Collections.reverse(catPath);
        return catPath.toArray(new Long[0]);
    }

    /**
     * @description: ????????????Id??????????????????id??????
     * @param catId
     * @param catPath
     * @author: liubin
     * @date: 2021/3/7 22:32
     * @return: void
     */
    private void queryCategoryPath(Long catId, List<Long> catPath) {
        catPath.add(catId);
        CategoryEntity categoryEntity = baseMapper.selectById(catId);
        if (categoryEntity.getParentCid() != 0) {
            queryCategoryPath(categoryEntity.getParentCid(), catPath);
        }
    }

    /**
     * @Author liubin
     * @Description ?????????????????????????????????
     * @Date 14:34 2021/3/2
     * @param root
     * @param all
     * @return {@link List< CategoryEntity>}
     **/
    private List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> all) {
        return all.stream()
                .filter(categoryEntity -> categoryEntity.getParentCid().equals(root.getCatId()))
                .peek(categoryEntity -> categoryEntity.setChildren(getChildren(categoryEntity,all)))
                .sorted(Comparator.comparingInt(menu -> (menu.getSort() == null ? 0 : menu.getSort())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "category", key = "'getFirstLevelCategoryList'"),
            @CacheEvict(value = "category", key = "'getCatalogJson'")
    })
    public void updateCategoryAndRelation(CategoryEntity category) {
        this.updateById(category);
        if (StringUtils.isNotBlank(category.getName())) {
            categoryBrandRelationService.updateCategoryRelation(category.getCatId(), category.getName());
        }
    }

    @Cacheable(value = {"category"}, key = "#root.method.name")
    @Override
    public List<CategoryEntity> getFirstLevelCategoryList() {
        return baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("cat_level", 1));
    }


//    @Override
//    public Map<String, List<Catalog2Vo>> getCatalogJson() {
//        // ??????redis??????
//        String catalogJson = redisTemplate.opsForValue().get("catalogJson");
//        if (StringUtils.isBlank(catalogJson)) {
//            // ??????????????????????????????????????????????????????
//            Map<String, List<Catalog2Vo>> catalogJsonFromDB = getCatalogJsonFromDB();
//            // ?????????????????????????????????
//            if (CollectionUtils.isEmpty(catalogJsonFromDB)) {
//                Random random = new Random();
//                int time = random.nextInt(10) % (10 - 5 + 1) + 5;;
//                redisTemplate.opsForValue().set("catalogJson", "catalogJson", time, TimeUnit.MINUTES);
//            } else {
//                redisTemplate.opsForValue().set("catalogJson", JSON.toJSONString(catalogJsonFromDB));
//            }
//            return catalogJsonFromDB;
//        }
//        // ????????????????????? ????????????
//        return JSON.parseObject(catalogJson, new TypeReference< Map<String, List<Catalog2Vo>>>(){});
//    }

    /**
     * 1.????????????????????????????????? ======??????????????????
     * 2.????????????????????????????????? ======??????????????????????????????????????????????????????
     * 3.???????????????????????????
     * */
    @Override
    @Cacheable(value = {"category"}, key = "#root.method.name", sync = true)
    public Map<String, List<Catalog2Vo>> getCatalogJson() {
        return getCatalogData();
//        // ??????redis???????????? ?????????
//        RLock lock = redissonClient.getLock("catalogJson-lock");
//        // ??????
//        lock.lock();
//        Map<String, List<Catalog2Vo>> catalogData;
//        try {
//            // ???????????? ??????????????????
//             catalogData = getCatalogData();
//        } finally {
//            // ?????????????????? ??????????????????
//            lock.unlock();
//        }
    }


//    /**
//     * @Author liubin
//     * @Description ?????????????????????
//     * @Date 18:21 2021/4/16
//     * @param
//     * @return java.util.Map<java.lang.String,java.util.List<com.liubin.gulimall.product.vo.Catalog2Vo>>
//     **/
//    private Map<String, List<Catalog2Vo>> getCatalogJsonFromDB() {
//        // ??????redis???????????? ?????????
//        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", "locked", 100, TimeUnit.MILLISECONDS);
//        if (lock) {
////            // ???????????????????????? ????????????????????????????????? ???????????????
////            redisTemplate.expire("lock", 1000, TimeUnit.MILLISECONDS);
//            // ???????????? ??????????????????
//            Map<String, List<Catalog2Vo>> catalogData = getCatalogData();
//            // ?????????????????? ??????????????????
//            redisTemplate.delete("lock");
//            return catalogData;
//        } else {
//            // ???????????? ????????????
//
//            return getCatalogJsonFromDB(); // ?????????
//        }
//    }

    private Map<String, List<Catalog2Vo>> getCatalogData() {
        String catalogJson = redisTemplate.opsForValue().get("catalogJson");
        if (StringUtils.isNotBlank(catalogJson)) {
            return JSON.parseObject(catalogJson, new TypeReference< Map<String, List<Catalog2Vo>>>(){});
        }
        Map<String, List<Catalog2Vo>> catalogMap = new HashMap<>();
        // ??????????????????
        List<CategoryEntity> categoryList = this.list();
        if (!CollectionUtils.isEmpty(categoryList)) {
            // ???????????????????????????Id
            List<Long> firstCatIdList = categoryList
                    .stream()
                    .filter(category -> category.getCatLevel() == 1)
                    .map(CategoryEntity::getCatId)
                    .collect(Collectors.toList());
            firstCatIdList.forEach(firstCatId -> {
                List<Catalog2Vo> catalog2VoList = new ArrayList<>();
                List<CategoryEntity> secondCategoryList = categoryList
                        .stream()
                        .filter(category -> category.getParentCid().equals(firstCatId))
                        .collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(secondCategoryList)) {
                    catalog2VoList = secondCategoryList.stream().map(secondCategory -> {
                        Catalog2Vo catalog2Vo = new Catalog2Vo();
                        catalog2Vo.setCatalogId(firstCatId.toString());
                        catalog2Vo.setId(secondCategory.getCatId().toString());
                        catalog2Vo.setName(secondCategory.getName());
                        List<Catalog2Vo.Catalog3Vo> catalog3VoList = new ArrayList<>();
                        List<CategoryEntity> thirdCategoryList = categoryList
                                .stream()
                                .filter(category -> category.getParentCid().equals(secondCategory.getCatId()))
                                .collect(Collectors.toList());
                        if (!CollectionUtils.isEmpty(thirdCategoryList)) {
                            catalog3VoList = thirdCategoryList.stream().map(thirdCategory -> {
                                Catalog2Vo.Catalog3Vo catalog3Vo = new Catalog2Vo.Catalog3Vo();
                                catalog3Vo.setCatalog2Id(secondCategory.getCatId().toString());
                                catalog3Vo.setId(thirdCategory.getCatId().toString());
                                catalog3Vo.setName(thirdCategory.getName());
                                return catalog3Vo;
                            }).collect(Collectors.toList());
                            catalog2Vo.setCatalog3List(catalog3VoList);
                        }
                        return catalog2Vo;
                    }).collect(Collectors.toList());
                }
                catalogMap.put(firstCatId.toString(), catalog2VoList);
            });
        }
        return catalogMap;
    }
}