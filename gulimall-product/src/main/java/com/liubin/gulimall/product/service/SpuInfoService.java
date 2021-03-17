package com.liubin.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liubin.common.utils.PageUtils;
import com.liubin.gulimall.product.entity.SpuInfoEntity;

import java.util.Map;

/**
 * spu信息
 *
 * @author liubin
 * @email 870005335@qq.com
 * @date 2021-02-25 18:48:14
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);


    /**
     * @Author liubin
     * @Description 分页查询spu列表
     * @Date 16:44 2021/3/17
     * @param params
     * @return {@link PageUtils}
     **/
    PageUtils queryPageByCondition(Map<String, Object> params);
}

