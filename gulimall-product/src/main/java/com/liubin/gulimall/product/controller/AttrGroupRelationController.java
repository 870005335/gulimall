package com.liubin.gulimall.product.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.liubin.gulimall.product.entity.AttrGroupRelationEntity;
import com.liubin.gulimall.product.service.AttrGroupRelationService;
import com.liubin.gulimall.common.utils.PageUtils;
import com.liubin.gulimall.common.utils.R;



/**
 * 属性&属性分组关联
 *
 * @author liubin
 * @email 870005335
 * @date 2022-08-12 15:55:28
 */
@RestController
@RequestMapping("product/attr/group/relation")
public class AttrGroupRelationController {
    @Autowired
    private AttrGroupRelationService attrGroupRelationService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrGroupRelationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		AttrGroupRelationEntity attrGroupRelation = attrGroupRelationService.getById(id);

        return R.ok().put("attrGroupRelation", attrGroupRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrGroupRelationEntity attrGroupRelation){
		attrGroupRelationService.save(attrGroupRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrGroupRelationEntity attrGroupRelation){
		attrGroupRelationService.updateById(attrGroupRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		attrGroupRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
