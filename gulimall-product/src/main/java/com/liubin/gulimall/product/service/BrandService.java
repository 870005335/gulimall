package com.liubin.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liubin.common.utils.PageUtils;
import com.liubin.gulimall.product.entity.BrandEntity;

import java.util.Map;

/**
 * 品牌
 *
 * @author liubin
 * @email 870005335@qq.com
 * @date 2021-02-25 18:48:14
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * @description: 更新品牌信息并维护中间表
     * @param brand
     * @author: liubin
     * @date: 2021/3/8 22:51
     * @return: void
     */
    void updateBrandAndRelation(BrandEntity brand);
}

