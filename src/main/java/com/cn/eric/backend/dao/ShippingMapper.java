package com.cn.eric.backend.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cn.eric.backend.pojo.Shipping;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);
    
    int deleteByPrimaryKeyUserId(@Param(value="userId")Integer userId,@Param(value="shippingId")Integer shippingId);
    
    int updateByPrimaryKeyUserId(Shipping shipping);
    
    Shipping selectByPKUserId(@Param(value="userId")Integer userId,@Param(value="shippingId")Integer shippingId);
    
    List<Shipping> selectByUserId(Integer userId);
}