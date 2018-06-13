package com.cn.eric.backend.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cn.eric.backend.pojo.Order;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

	Order getOrderByIdUserId(@Param(value="userId")Integer userId, @Param(value="orderNo")Long orderNo);

	Order selectByOrderNo(Long orderNo);

	List<Order> getOrderByUserId(Integer userId);
}