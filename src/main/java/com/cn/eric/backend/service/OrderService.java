package com.cn.eric.backend.service;

import java.util.Map;

import com.cn.eric.backend.common.ServerResponse;
import com.github.pagehelper.PageInfo;

public interface OrderService {

	ServerResponse pay(Integer id, Long orderNo, String path);

	ServerResponse callBack(Map<String,String> params);

	ServerResponse queryOrderPayStatus(Integer id, Long orderNo);

	ServerResponse createOrder(Integer userId, Long shippingId);

	ServerResponse getOrderCartProduct(Integer userId);

	ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize);

	ServerResponse detail(Integer userId, Long orderNo);

	ServerResponse cancel(Integer userId, Long orderNo);

	ServerResponse sendGoods(Integer userId, Long orderNo);

}
