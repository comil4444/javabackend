package com.cn.eric.backend.service;

import com.cn.eric.backend.common.ServerResponse;
import com.cn.eric.backend.pojo.Shipping;

public interface ShippingService {

	ServerResponse add(Integer id, Shipping shipping);

	ServerResponse del(Integer id, Integer shippingId);

	ServerResponse update(Integer id, Shipping shipping);

	ServerResponse select(Integer id, Integer shippingId);

	ServerResponse list(Integer id, int pageNum, int pageSize);

}
