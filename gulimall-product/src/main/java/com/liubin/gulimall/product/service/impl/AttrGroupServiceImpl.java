package com.liubin.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liubin.common.utils.PageUtils;
import com.liubin.common.utils.Query;
import com.liubin.gulimall.product.dao.AttrAttrGroupRelationDao;
import com.liubin.gulimall.product.dao.AttrGroupDao;
import com.liubin.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.liubin.gulimall.product.entity.AttrGroupEntity;
import com.liubin.gulimall.product.service.AttrGroupService;
import com.liubin.gulimall.product.service.AttrService;
import com.liubin.gulimall.product.vo.AttrGroupRelationVo;
import com.liubin.gulimall.product.vo.AttrGroupWithAttrsVo;
import com.liubin.gulimall.product.vo.SkuItemVo;
import com.liubin.gulimall.product.vo.SpuItemAttrGroupVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    private AttrAttrGroupRelationDao relationDao;

    @Autowired
    private AttrService attrService;

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
        String key = (String) params.get("key");
        QueryWrapper<AttrGroupEntity> wrapper = new
                QueryWrapper<>();
        if (StringUtils.isNotBlank(key)) {
            wrapper.and((obj) -> obj.eq("attr_group_id", key).or().like("attr_group_name", key));
        }
        IPage<AttrGroupEntity> page;
        if (categoryId != 0) {
            wrapper.eq("catelog_id", categoryId);
        }
        page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                wrapper
        );
        return new PageUtils(page);
    }

    @Override
    public void deleteRelation(List<AttrGroupRelationVo> attrGroupRelationVoList) {
        List<AttrAttrgroupRelationEntity> entities = attrGroupRelationVoList.stream().map(relation -> {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(relation, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());
        relationDao.deleteBatchRelation(entities);
    }

    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCategoryId(Long catelogId) {
        List<AttrGroupWithAttrsVo> resultList = new ArrayList<>();
        // 根据分类id查询分类下所有分组
        List<AttrGroupEntity> groupList = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        if (!CollectionUtils.isEmpty(groupList)) {
            resultList = groupList.stream().map(group -> {
                AttrGroupWithAttrsVo groupWithAttrsVo = new AttrGroupWithAttrsVo();
                BeanUtils.copyProperties(group, groupWithAttrsVo);
                groupWithAttrsVo.setAttrs(attrService.getAttrRelation(group.getAttrGroupId()));
                return groupWithAttrsVo;
            }).collect(Collectors.toList());
        }
        return resultList;
    }

    @Override
    public List<SpuItemAttrGroupVo> getAttrGroupWithAttrsBySpuId(Long spuId, Long catalogId) {
        return this.baseMapper.getAttrGroupWithAttrsBySpuId(spuId, catalogId);
    }
}