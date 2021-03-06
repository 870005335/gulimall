package com.liubin.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liubin.common.utils.PageUtils;
import com.liubin.gulimall.coupon.entity.HomeSubjectEntity;

import java.util.Map;

/**
 * 首页专题表【jd首页下面很多专题，每个专题链接新的页面，展示专题商品信息】
 *
 * @author liubin
 * @email 870005335@qq.com
 * @date 2021-02-26 12:00:16
 */
public interface HomeSubjectService extends IService<HomeSubjectEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

