package com.cn.eric.backend.controller.portal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cn.eric.backend.common.Constant;
import com.cn.eric.backend.common.ResponseCode;
import com.cn.eric.backend.common.ServerResponse;
import com.cn.eric.backend.pojo.Shipping;
import com.cn.eric.backend.pojo.User;
import com.cn.eric.backend.service.ShippingService;

@Controller
@RequestMapping("/shipping")
public class ShippingController {
	
	@Autowired
	private ShippingService shippingService;

	@RequestMapping("/add.do")
	@ResponseBody
	public ServerResponse add(HttpSession session,Shipping shipping) {
		User user = (User)session.getAttribute(Constant.CURRENTUSER);
		if(null==user)
			return ServerResponse.createErrorResponseByCode(ResponseCode.NEED_LOGIN);
		return shippingService.add(user.getId(),shipping);
	}
	
	@RequestMapping("/del.do")
	@ResponseBody
	public ServerResponse del(HttpSession session,Integer shippingId) {
		User user = (User)session.getAttribute(Constant.CURRENTUSER);
		if(null==user)
			return ServerResponse.createErrorResponseByCode(ResponseCode.NEED_LOGIN);
		return shippingService.del(user.getId(),shippingId);
	}
	
	@RequestMapping("/update.do")
	@ResponseBody
	public ServerResponse update(HttpSession session,Shipping shipping) {
		User user = (User)session.getAttribute(Constant.CURRENTUSER);
		if(null==user)
			return ServerResponse.createErrorResponseByCode(ResponseCode.NEED_LOGIN);
		return shippingService.update(user.getId(),shipping);
	}
	
	@RequestMapping("/select.do")
	@ResponseBody
	public ServerResponse select(HttpSession session,Integer shippingId) {
		User user = (User)session.getAttribute(Constant.CURRENTUSER);
		if(null==user)
			return ServerResponse.createErrorResponseByCode(ResponseCode.NEED_LOGIN);
		return shippingService.select(user.getId(),shippingId);
	}
	
	@RequestMapping("/list.do")
	@ResponseBody
	public ServerResponse list(HttpSession session,@RequestParam(value="pageNum",defaultValue="1")int pageNum,@RequestParam(value="pageSize",defaultValue="10")int pageSize) {
		User user = (User)session.getAttribute(Constant.CURRENTUSER);
		if(null==user)
			return ServerResponse.createErrorResponseByCode(ResponseCode.NEED_LOGIN);
		return shippingService.list(user.getId(),pageNum,pageSize);
	}

	public ShippingService getShippingService() {
		return shippingService;
	}

	public void setShippingService(ShippingService shippingService) {
		this.shippingService = shippingService;
	}
	
}
