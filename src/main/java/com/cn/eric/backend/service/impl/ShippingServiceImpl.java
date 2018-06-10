package com.cn.eric.backend.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.eric.backend.common.ResponseCode;
import com.cn.eric.backend.common.ServerResponse;
import com.cn.eric.backend.dao.ShippingMapper;
import com.cn.eric.backend.pojo.Shipping;
import com.cn.eric.backend.service.ShippingService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service
public class ShippingServiceImpl implements ShippingService{
	
	@Autowired
	private ShippingMapper shippingMapper;

	@Override
	public ServerResponse add(Integer userId, Shipping shipping) {
		shipping.setUserId(userId);
		int count = shippingMapper.insert(shipping);
		if(count>0) {
			Map<String, Integer> data = Maps.newHashMap();
			data.put("shippingId", shipping.getId());
			return ServerResponse.createSuccessResponseByMsgData("新建地址成功", data);
		}
		return ServerResponse.createErrorResponseByMsg("增加地址失敗！");
	}

	@Override
	public ServerResponse del(Integer userId, Integer shippingId) {
		if(userId==null||null==shippingId)
			return ServerResponse.createErrorResponseByCode(ResponseCode.ILLEGAL_PARAM);
		int count = shippingMapper.deleteByPrimaryKeyUserId(userId,shippingId);
		if(count>0)
			return ServerResponse.createSuccessResponseByMsg("刪除成功！");
		return ServerResponse.createErrorResponseByMsg("刪除失敗！");
	}

	@Override
	public ServerResponse update(Integer userId, Shipping shipping) {
		if(userId==null||null==shipping)
			return ServerResponse.createErrorResponseByCode(ResponseCode.ILLEGAL_PARAM);
		shipping.setUserId(userId);
		int count = shippingMapper.updateByPrimaryKeyUserId(shipping);
		if(count>0)
			return ServerResponse.createSuccessResponseByMsg("更新成功！");
		return ServerResponse.createErrorResponseByMsg("更新失敗！");
	}

	@Override
	public ServerResponse select(Integer userId, Integer shippingId) {
		if(userId==null||null==shippingId)
			return ServerResponse.createErrorResponseByCode(ResponseCode.ILLEGAL_PARAM);
		Shipping shipping = shippingMapper.selectByPKUserId(userId,shippingId);
		if(null!=shipping)
			return ServerResponse.createSuccessResponseByData(shipping);
		return ServerResponse.createErrorResponseByMsg("查詢失敗！");
	}

	@Override
	public ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		List<Shipping> list =Lists.newArrayList();
		list = shippingMapper.selectByUserId(userId);
		PageInfo pageInfo = new PageInfo(list);
		return ServerResponse.createSuccessResponseByData(pageInfo);
	}

	public ShippingMapper getShippingMapper() {
		return shippingMapper;
	}

	public void setShippingMapper(ShippingMapper shippingMapper) {
		this.shippingMapper = shippingMapper;
	}
	
}
