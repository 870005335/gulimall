package com.liubin.gulimall.product.controller;

import java.util.Arrays;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.liubin.gulimall.product.vo.SpuSaveVo;
import com.liubin.gulimall.product.vo.SpuStatusVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.liubin.gulimall.product.entity.SpuInfoEntity;
import com.liubin.gulimall.product.service.SpuInfoService;
import com.liubin.common.utils.PageUtils;
import com.liubin.common.utils.R;



/**
 * spu信息
 *
 * @author liubin
 * @email 870005335@qq.com
 * @date 2021-02-26 11:21:23
 */
@Slf4j
@RestController
@RequestMapping("product/spuinfo")
public class SpuInfoController {
    @Autowired
    private SpuInfoService spuInfoService;

    @PostMapping("updatePublishStatus")
    public R updatePublishStatus(@RequestBody SpuStatusVo statusVo) {
        try {
            if (statusVo.getPublishStatus() == 1) {
                spuInfoService.updatePublishStatusUp(statusVo);
            } else {
                spuInfoService.updatePublishStatusDown(statusVo);
            }
        } catch (Exception e) {
            log.error("商品上架异常", e);
            throw new RuntimeException(e.getMessage());
        }

        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("product:spuinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = spuInfoService.queryPageByCondition(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("product:spuinfo:info")
    public R info(@PathVariable("id") Long id){
		SpuInfoEntity spuInfo = spuInfoService.getById(id);

        return R.ok().put("spuInfo", spuInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("product:spuinfo:save")
    public R save(@RequestBody SpuSaveVo spuInfo){
        spuInfoService.saveSpuInfo(spuInfo);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("product:spuinfo:update")
    public R update(@RequestBody SpuInfoEntity spuInfo){
		spuInfoService.updateById(spuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("product:spuinfo:delete")
    public R delete(@RequestBody Long[] ids){
		spuInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
