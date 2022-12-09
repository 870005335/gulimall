package com.liubin.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liubin.gulimall.common.utils.PageUtils;
import com.liubin.gulimall.product.entity.SpuImagesEntity;

import java.util.List;
import java.util.Map;

/**
 * spu图片
 *
 * @author liubin
 * @email 870005335
 * @date 2022-08-12 15:55:28
 */
public interface SpuImagesService extends IService<SpuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * @Author liubin
     * @Description 保存Spu图集
     * @Date 14:41 2022/10/9
     * @param id
     * @param images
     * @return void
     **/
    void saveImages(Long id, List<String> images);
}

