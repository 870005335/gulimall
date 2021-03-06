package com.liubin.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liubin.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.liubin.gulimall.product.entity.AttrEntity;
import com.liubin.gulimall.product.service.AttrAttrgroupRelationService;
import com.liubin.gulimall.product.service.AttrService;
import com.liubin.gulimall.product.service.CategoryService;
import com.liubin.gulimall.product.vo.AttrGroupRelationVo;
import com.liubin.gulimall.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import com.liubin.gulimall.product.entity.AttrGroupEntity;
import com.liubin.gulimall.product.service.AttrGroupService;
import com.liubin.common.utils.PageUtils;
import com.liubin.common.utils.R;



/**
 * 属性分组
 *
 * @author liubin
 * @email 870005335@qq.com
 * @date 2021-02-26 11:21:23
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    @GetMapping("{catelogId}/withattr")
    public R getAttrGroupWithAttrs(@PathVariable("catelogId") Long catelogId) {
        List<AttrGroupWithAttrsVo> resultList = attrGroupService.getAttrGroupWithAttrsByCategoryId(catelogId);
        return R.ok().put("data", resultList);
    }

    @PostMapping("attr/relation")
    public R saveRelationBatch(@RequestBody List<AttrGroupRelationVo> relationVoList) {
        attrAttrgroupRelationService.saveRelationBatch(relationVoList);
        return R.ok();
    }

    @GetMapping("/{attrGroupId}/noattr/relation")
    public R attrNoRelation(@PathVariable("attrGroupId") Long attrGroupId,
                            @RequestParam Map<String, Object> params) {
        PageUtils page = attrService.getAttrNoRelation(attrGroupId, params);
        return R.ok().put("page", page);
    }

    @PostMapping("attr/relation/delete")
    public R deleteRelation(@RequestBody AttrGroupRelationVo[] relationVos) {
        attrGroupService.deleteRelation(Stream.of(relationVos).collect(Collectors.toList()));
        return R.ok();
    }

    @GetMapping("{attrGroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrGroupId") Long attrGroupId) {
        List<AttrEntity> attrList = attrService.getAttrRelation(attrGroupId);
        return R.ok().put("data", attrList);
    }

    /**
     * 列表
     */
    @RequestMapping("/list/{categoryId}")
    // @RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params,
                  @PathVariable("categoryId")Integer categoryId){
        PageUtils page = attrGroupService.queryPage(params, categoryId);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    // @RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
        // 根据属性分组Id查询详情
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
		// 根据属性分组中的分类id查询分类id路径
		Long[] paths = categoryService.queryCategoryPath(attrGroup.getCatelogId());
        attrGroup.setCatelogPath(paths);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds){
        List<Long> groupIdList = Arrays.asList(attrGroupIds);
        // 属性分组下有关联属性 不能删除
        List<AttrAttrgroupRelationEntity> relationList = attrAttrgroupRelationService.list(
                new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", groupIdList));
        if (!CollectionUtils.isEmpty(relationList)) {
            return R.error("所选分组下有关联属性,请先移除关联");
        }
        attrGroupService.removeByIds(groupIdList);

        return R.ok();
    }

}
