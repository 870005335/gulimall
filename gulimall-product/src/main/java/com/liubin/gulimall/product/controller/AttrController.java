package com.liubin.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.liubin.gulimall.product.vo.AttrRespVo;
import com.liubin.gulimall.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.liubin.gulimall.product.service.AttrService;
import com.liubin.gulimall.common.utils.PageUtils;
import com.liubin.gulimall.common.utils.R;



/**
 * 商品属性
 *
 * @author liubin
 * @email 870005335
 * @date 2022-08-12 15:55:28
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    /**
     * 列表
     */
    @RequestMapping("/{attrType}/list/{catId}")
    public R list(@RequestParam Map<String, Object> params,
                  @PathVariable("attrType") String attrType,
                  @PathVariable("catId") Long catId){
        PageUtils page = attrService.queryPage(params, attrType, catId);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId){
        AttrRespVo attr = attrService.getAttrInfo(attrId);

        return R.ok().put("attr", attr);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrVo attr){
		attrService.saveAttr(attr);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrVo attr){
		attrService.updateAttr(attr);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrIds){
        List<Long> attrIdList = Arrays.asList(attrIds);
        attrService.deleteAttrsByIds(attrIdList);

        return R.ok();
    }

}
