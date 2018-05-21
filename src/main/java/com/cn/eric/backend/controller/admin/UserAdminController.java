package com.cn.eric.backend.controller.admin;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cn.eric.backend.common.Constant;
import com.cn.eric.backend.common.ServerResponse;
import com.cn.eric.backend.pojo.User;
import com.cn.eric.backend.service.UserService;

@Controller
@RequestMapping("/admin/user")
public class UserAdminController {

	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "login.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> login(String userName,String password,HttpSession session) {
		ServerResponse<User> rs = userService.login(userName, password);
		if(rs.isSuccess()) {
			if(rs.getData().getRole()!=Constant.Role.ADMIN)
				return ServerResponse.createErrorResponseByMsg("非管理员，不允许登录");
			else {
				session.setAttribute(Constant.CURRENTUSER, rs.getData());
				return rs;
			}
		}
		return rs;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
}
