package com.liubin.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liubin.common.dto.SkuReductionDto;
import com.liubin.common.utils.PageUtils;
import com.liubin.gulimall.coupon.entity.SkuFullReductionEntity;

import java.util.Map;

/**
 * 商品满减信息
 *
 * @author liubin
 * @email 870005335@qq.com
 * @date 2021-02-26 12:00:16
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * @Author liubin
     * @Description 保存sku满减优惠信息
     * @Date 16:35 2021/3/19
     * @param reductionDto
     * @return
     **/
    void saveSkuReduction(SkuReductionDto reductionDto);
}

