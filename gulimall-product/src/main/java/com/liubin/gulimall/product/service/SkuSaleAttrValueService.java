package com.liubin.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liubin.common.utils.PageUtils;
import com.liubin.gulimall.product.entity.SkuSaleAttrValueEntity;
import com.liubin.gulimall.product.vo.SkuItemSaleAttrsVo;

import java.util.List;
import java.util.Map;

/**
 * sku销售属性&值
 *
 * @author liubin
 * @email 870005335@qq.com
 * @date 2021-02-25 18:48:14
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * @Author liubin
     * @Description 查询spuId下的销售属性列表
     * @Date 17:45 2021/5/24
     * @param spuId
     * @return java.util.List<com.liubin.gulimall.product.vo.SkuItemSaleAttrsVo>
     **/
    List<SkuItemSaleAttrsVo> getSaleAttrsBySpuId(Long spuId);
}

