package com.cn.eric.backend.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cn.eric.backend.pojo.Cart;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

	List<Cart> getCartsByUserId(Integer userId);

	int isAllChecked(Integer userId);

	Cart getCartByUserIdProductId(@Param(value="userId")Integer userId, @Param(value="productId")Integer productId);

	int deleteByUserIdProId(@Param(value="userId")Integer userId, @Param(value="productIds")String productIds);

	int getCountInCart(Integer userId);
	
	int updateChecked(@Param(value="userId")Integer userId,@Param(value="productId")Integer productId,@Param(value="checkedStatus")int checkedStatus);
}