package com.liubin.gulimall.order.dao;

import com.liubin.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author liubin
 * @email 870005335
 * @date 2022-08-15 16:24:31
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
