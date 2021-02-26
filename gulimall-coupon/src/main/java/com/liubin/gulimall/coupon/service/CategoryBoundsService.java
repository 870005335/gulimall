package com.liubin.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liubin.common.utils.PageUtils;
import com.liubin.gulimall.coupon.entity.CategoryBoundsEntity;

import java.util.Map;

/**
 * 商品分类积分设置
 *
 * @author liubin
 * @email 870005335@qq.com
 * @date 2021-02-26 12:00:16
 */
public interface CategoryBoundsService extends IService<CategoryBoundsEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

