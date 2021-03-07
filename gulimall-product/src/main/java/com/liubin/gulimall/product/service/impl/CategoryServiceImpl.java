package com.liubin.gulimall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liubin.common.utils.PageUtils;
import com.liubin.common.utils.Query;

import com.liubin.gulimall.product.dao.CategoryDao;
import com.liubin.gulimall.product.entity.CategoryEntity;
import com.liubin.gulimall.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

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
        // 查出所有分类
        List<CategoryEntity> entities = baseMapper.selectList(null);
        // 组装成三级树形结构
        return entities.stream()
                .filter(categoryEntity -> categoryEntity.getParentCid() == 0)
                .peek(menu-> menu.setChildren(getChildren(menu,entities)))
                .sorted(Comparator.comparingInt(menu -> (menu.getSort() == null ? 0 : menu.getSort())))
                .collect(Collectors.toList());
    }

    @Override
    public void removeMenuByIds(List<Long> catIds) {
        //TODO  检查当前删除的菜单，是否被别的地方引用

        //逻辑删除
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
     * @description: 根据分类Id递归查询分类id路径
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
     * @Description 查找当前分类的子节点树
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
}