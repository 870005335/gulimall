package com.liubin.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.liubin.gulimall.product.entity.AttrEntity;
import com.liubin.gulimall.product.service.AttrGroupRelationService;
import com.liubin.gulimall.product.service.AttrService;
import com.liubin.gulimall.product.vo.AttrGroupWithAttrsVo;
import com.liubin.gulimall.product.vo.AttrRelationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.liubin.gulimall.product.entity.AttrGroupEntity;
import com.liubin.gulimall.product.service.AttrGroupService;
import com.liubin.gulimall.common.utils.PageUtils;
import com.liubin.gulimall.common.utils.R;



/**
 * 属性分组
 *
 * @author liubin
 * @email 870005335
 * @date 2022-08-12 15:55:28
 */
@RestController
@RequestMapping("product/attr/group")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private AttrGroupRelationService attrGroupRelationService;

    @GetMapping("/withAttr/{categoryId}")
    public R getAttrGroupWithAttrs(@PathVariable("categoryId") Long categoryId) {
        List<AttrGroupWithAttrsVo> vos =  attrGroupService.getAttrGroupWithAttrsByCategoryId(categoryId);
        return R.ok().put("data",vos);
    }

    @PostMapping("/attr/relation/save")
    public R saveAttrRelation(@RequestBody AttrRelationVo saveVo) {
        attrGroupRelationService.saveAttrRelation(saveVo);
        return R.ok();
    }

    @PostMapping("/attr/relation/delete")
    public R deleteAttrRelation(@RequestBody AttrRelationVo delVo) {
        attrGroupRelationService.deleteAttrRelation(delVo);
        return R.ok();
    }

    @GetMapping("/noAttr/relation/{attrGroupId}")
    public R getNoAttrRelation(Map<String, Object> params,
                               @PathVariable("attrGroupId") Long attrGroupId) {
        PageUtils page = attrService.getNoAttrRelation(params, attrGroupId);
        return R.ok().put("page", page);
    }

    @GetMapping("/attr/relation/{attrGroupId}")
    public R getAttrRelation(@PathVariable("attrGroupId") Long attrGroupId) {
        List<AttrEntity> relationAttrs = this.attrService.getAttrRelation(attrGroupId);
        return R.ok().put("relationAttrs", relationAttrs);
    }

    /**
     * 列表
     */
    @RequestMapping("/list/{catId}")
    public R list(@RequestParam Map<String, Object> params,
                  @PathVariable("catId") Long catId){
        PageUtils page = attrGroupService.queryPage(params, catId);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getAttrGroupById(attrGroupId);

        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.saveAttrGroup(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateAttrGroup(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrGroupIds){
        List<Long> groupIdList = Arrays.asList(attrGroupIds);
        attrGroupService.deleteAttrGroups(groupIdList);
        return R.ok();
    }

}
