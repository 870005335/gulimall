package com.liubin.gulimall.order.dao;

import com.liubin.gulimall.order.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 * 
 * @author liubin
 * @email 870005335@qq.com
 * @date 2021-02-26 11:55:20
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {
	
}
