package com.liubin.gulimall.order.dao;

import com.liubin.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author liubin
 * @email 870005335@qq.com
 * @date 2021-02-26 11:55:20
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
