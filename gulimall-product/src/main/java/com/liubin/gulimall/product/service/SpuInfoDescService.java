package com.liubin.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liubin.gulimall.common.utils.PageUtils;
import com.liubin.gulimall.product.entity.SpuInfoDescEntity;

import java.util.Map;

/**
 * spu信息介绍
 *
 * @author liubin
 * @email 870005335
 * @date 2022-08-12 15:55:28
 */
public interface SpuInfoDescService extends IService<SpuInfoDescEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * @Author liubin
     * @Description 保存spu信息介绍
     * @Date 14:32 2022/10/9
     * @param descEntity
     * @return void
     **/
    void saveSpuInfoDesc(SpuInfoDescEntity descEntity);
}

