package com.cn.eric.backend.controller.portal;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.cn.eric.backend.common.Constant;
import com.cn.eric.backend.common.ResponseCode;
import com.cn.eric.backend.common.ServerResponse;
import com.cn.eric.backend.pojo.User;
import com.cn.eric.backend.service.OrderService;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;

@Controller
@RequestMapping("/order/")
public class OrderController {
	
	private final static Logger logger = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private OrderService orderService;
	
	
	@RequestMapping("create.do")
	@ResponseBody
	public ServerResponse create(HttpSession session,Long shippingId) {
		User user = (User)session.getAttribute(Constant.CURRENTUSER);
		if(null==user)
			return ServerResponse.createErrorResponseByCode(ResponseCode.NEED_LOGIN);
		return orderService.createOrder(user.getId(),shippingId);
	}
	
	@RequestMapping("get_order_cart_product.do")
	@ResponseBody
	public ServerResponse getOrderCartProduct(HttpSession session) {
		User user = (User)session.getAttribute(Constant.CURRENTUSER);
		if(null==user)
			return ServerResponse.createErrorResponseByCode(ResponseCode.NEED_LOGIN);
		return orderService.getOrderCartProduct(user.getId());
	}
	
	@RequestMapping("list.do")
	@ResponseBody
	public ServerResponse<PageInfo> list(HttpSession session,
			@RequestParam(value="pageNum",defaultValue="1")int pageNum,
			@RequestParam(value="pageSize",defaultValue="10")int pageSize){
		User user = (User)session.getAttribute(Constant.CURRENTUSER);
		if(null==user)
			return ServerResponse.createErrorResponseByCode(ResponseCode.NEED_LOGIN);
		return orderService.list(user.getId(),pageNum,pageSize);
	}
	
	@RequestMapping("detail.do")
	@ResponseBody
	public ServerResponse detail(HttpSession session,Long orderNo){
		User user = (User)session.getAttribute(Constant.CURRENTUSER);
		if(null==user)
			return ServerResponse.createErrorResponseByCode(ResponseCode.NEED_LOGIN);
		return orderService.detail(user.getId(),orderNo);
	}
	
	@RequestMapping("cancel.do")
	@ResponseBody
	public ServerResponse cancel(HttpSession session,Long orderNo){
		User user = (User)session.getAttribute(Constant.CURRENTUSER);
		if(null==user)
			return ServerResponse.createErrorResponseByCode(ResponseCode.NEED_LOGIN);
		return orderService.cancel(user.getId(),orderNo);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

	@RequestMapping("pay.do")
	@ResponseBody
	public ServerResponse pay(HttpSession session, Long orderNo, HttpServletRequest req) {
		User user = (User) session.getAttribute(Constant.CURRENTUSER);
		if (null == user)
			return ServerResponse.createErrorResponseByCode(ResponseCode.NEED_LOGIN);
		String path = req.getSession().getServletContext().getRealPath("upload");
		return orderService.pay(user.getId(), orderNo, path);
	}

	@RequestMapping("alipay_callback.do")
	@ResponseBody
	public Object callBack(HttpServletRequest req) {
		Map map = req.getParameterMap();
		Map<String,String> params = Maps.newHashMap();
		
		for(Iterator it = map.keySet().iterator();it.hasNext();) {
			String name = (String)it.next();
			String[] values = (String[])map.get(name);
			
			String temp=StringUtils.EMPTY;
			for(int i=0;i<values.length;i++) {
				temp += i==values.length-1?values[i]:values[i]+",";
			}
			
			params.put(name, temp);
		}
		
		params.remove("sign_type");
		
		try {
			boolean rsa2Check = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());
			if(!rsa2Check)
				return ServerResponse.createErrorResponseByMsg("非法请求,验证不通过,再恶意请求我就报警找网警了");
		} catch (AlipayApiException e) {
			logger.error("支付宝验证回调异常",e);
		}
		
		ServerResponse sr = orderService.callBack(params);
		if(sr.isSuccess())
			return Constant.AlipayCallBack.RESPONSE_SUCCESS;
		return Constant.AlipayCallBack.RESPONSE_FAILED;
	}

	@RequestMapping("query_order_pay_status.do")
	@ResponseBody
	public ServerResponse queryOrderPayStatus(HttpSession session,Long orderNo) {
		User user = (User) session.getAttribute(Constant.CURRENTUSER);
		if (null == user)
			return ServerResponse.createErrorResponseByCode(ResponseCode.NEED_LOGIN);
		return orderService.queryOrderPayStatus(user.getId(),orderNo);
	}
}
