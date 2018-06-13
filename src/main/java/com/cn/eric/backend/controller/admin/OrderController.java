package com.cn.eric.backend.controller.admin;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cn.eric.backend.common.Constant;
import com.cn.eric.backend.common.ResponseCode;
import com.cn.eric.backend.common.ServerResponse;
import com.cn.eric.backend.pojo.User;
import com.cn.eric.backend.service.OrderService;
import com.cn.eric.backend.service.UserService;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/admin/order/")
public class OrderController {
	
	private final static Logger logger = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private OrderService orderService;
	@Autowired
	private UserService userService;
	
	
	@RequestMapping("list.do")
	@ResponseBody
	public ServerResponse<PageInfo> list(HttpSession session,
			@RequestParam(value="pageNum",defaultValue="1")int pageNum,
			@RequestParam(value="pageSize",defaultValue="10")int pageSize){
		User user = (User) session.getAttribute(Constant.CURRENTUSER);
		if(null==user) {
			return ServerResponse.createErrorResponseByCode(ResponseCode.NEED_LOGIN);
		}
		if(!userService.checkAdminPermission(user)) {
			return ServerResponse.createErrorResponseByMsg("需要管理员权限！");
		}
		return orderService.list(user.getId(),pageNum,pageSize);
	}
	
	@RequestMapping("detail.do")
	@ResponseBody
	public ServerResponse detail(HttpSession session,Long orderNo){
		User user = (User) session.getAttribute(Constant.CURRENTUSER);
		if(null==user) {
			return ServerResponse.createErrorResponseByCode(ResponseCode.NEED_LOGIN);
		}
		if(!userService.checkAdminPermission(user)) {
			return ServerResponse.createErrorResponseByMsg("需要管理员权限！");
		}
		return orderService.detail(user.getId(),orderNo);
	}
	
	@RequestMapping("send_goods.do")
	@ResponseBody
	public ServerResponse cancel(HttpSession session,Long orderNo){
		User user = (User) session.getAttribute(Constant.CURRENTUSER);
		if(null==user) {
			return ServerResponse.createErrorResponseByCode(ResponseCode.NEED_LOGIN);
		}
		if(!userService.checkAdminPermission(user)) {
			return ServerResponse.createErrorResponseByMsg("需要管理员权限！");
		}
		return orderService.sendGoods(user.getId(),orderNo);
	}
	
}
