package com.liubin.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liubin.gulimall.common.utils.PageUtils;
import com.liubin.gulimall.product.entity.BrandEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌
 *
 * @author liubin
 * @email 870005335
 * @date 2022-08-12 15:55:28
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * @Author liubin
     * @Description 根据Id列表获取品牌名称映射
     * @Date 16:59 2022/8/29
     * @param brandIdList
     * @return java.util.Map<java.lang.Long,java.lang.String>
     **/
    Map<Long, String> getBrandNameMap(List<Long> brandIdList);
}

