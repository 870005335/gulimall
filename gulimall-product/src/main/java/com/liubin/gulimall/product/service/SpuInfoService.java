package com.liubin.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.liubin.common.utils.PageUtils;
import com.liubin.gulimall.product.controller.SpuInfoController;
import com.liubin.gulimall.product.entity.SpuInfoEntity;
import com.liubin.gulimall.product.vo.SpuSaveVo;
import com.liubin.gulimall.product.vo.SpuStatusVo;

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

    /**
     * @Author liubin
     * @Description 保存商品信息
     * @Date 15:25 2021/3/19
     * @param spuInfo
     * @return
     **/
    void saveSpuInfo(SpuSaveVo spuInfo);

    /**
     * @Author liubin
     * @Description 保存商品spu基本信息
     * @Date 15:25 2021/3/19
     * @param saveInfo
     * @return
     **/
    void saveSpuBaseInfo(SpuInfoEntity saveInfo);

    /**
     * @Author liubin
     * @Description 商品上架
     * @Date 10:45 2021/4/9
     * @param statusVo
     * @return
     **/
    void updatePublishStatusUp(SpuStatusVo statusVo) throws JsonProcessingException;

    /**
     * @Author liubin
     * @Description 商品下架
     * @Date 10:45 2021/4/9
     * @param statusVo
     * @return
     **/
    void updatePublishStatusDown(SpuStatusVo statusVo);

}

