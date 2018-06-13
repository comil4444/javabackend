package com.cn.eric.backend.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cn.eric.backend.pojo.OrderItem;

public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);
    
    List<OrderItem> getOrderItemsByUserId(Integer userId);

	List<OrderItem> getOrderItemByUserIdOrderNo(@Param("userId")Integer userId, @Param("orderNo")Long orderNo);
}