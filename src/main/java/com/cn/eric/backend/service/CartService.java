package com.cn.eric.backend.service;

import com.cn.eric.backend.common.ServerResponse;
import com.cn.eric.backend.vo.CartVO;

public interface CartService {

	ServerResponse<CartVO> list(Integer id);

	ServerResponse<CartVO> add(Integer id, Integer productId, int count);

	ServerResponse<CartVO> update(Integer userId, Integer productId, int count);

	ServerResponse<CartVO> deleteProduct(Integer userId, String productIds);

	Integer getProductCountInCart(Integer id);

	ServerResponse<CartVO> selectProduct(Integer userId,int productId);
	
	ServerResponse<CartVO> selectAll(Integer userId);
	
	ServerResponse<CartVO> unSelectProduct(Integer userId,int productId);
	
	ServerResponse<CartVO> unSelectAll(Integer userId);

}
