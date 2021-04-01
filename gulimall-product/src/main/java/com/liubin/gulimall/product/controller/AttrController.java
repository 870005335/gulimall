package com.liubin.gulimall.product.controller;

import java.util.Arrays;
import java.util.Map;

import com.liubin.gulimall.product.vo.AttrRespVo;
import com.liubin.gulimall.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.liubin.gulimall.product.entity.AttrEntity;
import com.liubin.gulimall.product.service.AttrService;
import com.liubin.common.utils.PageUtils;
import com.liubin.common.utils.R;



/**
 * 商品属性
 *
 * @author liubin
 * @email 870005335@qq.com
 * @date 2021-02-26 11:21:23
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    @GetMapping("/{attrType}/list/{catelogId}")
    public R queryAttrList(@RequestParam Map<String, Object> params,
                           @PathVariable("attrType") String attrType,
                           @PathVariable("catelogId") Long catelogId) {
        PageUtils page = attrService.queryAttrPage(params, attrType, catelogId);
        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("product:attr:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    // @RequiresPermissions("product:attr:info")
    public R info(@PathVariable("attrId") Long attrId){
		AttrRespVo attr = attrService.getAttrInfo(attrId);

        return R.ok().put("attr", attr);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("product:attr:save")
    public R save(@RequestBody AttrVo attr){
		attrService.saveAttr(attr);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("product:attr:update")
    public R update(@RequestBody AttrVo attr){
		attrService.updateAttr(attr);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("{type}/delete")
    // @RequiresPermissions("product:attr:delete")
    public R delete(@PathVariable("type") String type, @RequestBody Long[] attrIds){
		attrService.deleteAttrs(Arrays.asList(attrIds), type);

        return R.ok();
    }

}
