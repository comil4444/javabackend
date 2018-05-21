package com.cn.eric.backend.controller.portal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cn.eric.backend.common.Constant;
import com.cn.eric.backend.common.ResponseCode;
import com.cn.eric.backend.common.ServerResponse;
import com.cn.eric.backend.pojo.User;
import com.cn.eric.backend.service.UserService;

@Controller
@RequestMapping(value ="/user/")
public class UserController {

	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "login.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> login(@RequestParam("username") String username,@RequestParam("password") String password,HttpSession session) {
		ServerResponse<User> rs = userService.login(username, password);
		if(rs.isSuccess()) {
			session.setAttribute(Constant.CURRENTUSER, rs.getData());
		}
		return rs;
	}
	
	@RequestMapping(value = "logout.do",method=RequestMethod.GET)
	@ResponseBody
	public ServerResponse<String> logout(HttpSession session) {
		session.removeAttribute(Constant.CURRENTUSER);
		return ServerResponse.createSuccessResponseByMsg("logout successfully!");
	}
	
	@RequestMapping(value = "register.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> register(User user) {
		return userService.register(user);
	}
	
	@RequestMapping(value = "check_valid.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> checkValid(String str,String type) {
		return userService.checkValid(str, type);
	}
	
	@RequestMapping(value = "get_user_info.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> getUserInfo(HttpSession session) {
		User user = (User)session.getAttribute(Constant.CURRENTUSER);
		if(null!=user)
			return ServerResponse.createSuccessResponseByData(user);
		return ServerResponse.createErrorResponseByMsg("用户未登录！");
	}
	
	@RequestMapping(value = "forget_get_question.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> forgetGetQuestion(String username){
		ServerResponse sr = checkValid(username,Constant.USERNAME);
		if(sr.isSuccess()) {
			return ServerResponse.createErrorResponseByMsg("用户不存在！");
		}
		return userService.getForgetQuestion(username);
	}
	
	@RequestMapping(value = "forget_check_answer.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> forgetCheckAnswer(String username,String question,String answer){
		return userService.checkAnswer(username, question, answer);
	}
	
	@RequestMapping(value = "forget_reset_password.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> forgetResetPassword(String username,String newPassword,String token){
		return userService.forgetResetPassword(username,newPassword,token);
	}
	
	@RequestMapping(value = "reset_password.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> resetPassword(HttpSession session, String oldPassword,String newPassword){
		User user = (User)session.getAttribute(Constant.CURRENTUSER);
		if(null==user)
			return ServerResponse.createErrorResponseByMsg("用户未登录！");
		return userService.resetPassword(oldPassword, newPassword, user);
	}
	
	@RequestMapping(value = "update_user_info.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> updateUserInfo(HttpSession session,User user){
		User currentUser = (User)session.getAttribute(Constant.CURRENTUSER);
		if(null==currentUser) {
			return ServerResponse.createErrorResponseByMsg("用户未登录");
		}
		user.setId(currentUser.getId());
		ServerResponse<User> response =  userService.updateUserInfo(user);
		if(response.isSuccess()) {
			response.getData().setUsername(currentUser.getUsername());
			session.setAttribute(Constant.CURRENTUSER, response.getData());
		}
		return response;
	}
	
	@RequestMapping(value = "get_info.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> getInformation(HttpSession session){
		User currentUser = (User)session.getAttribute(Constant.CURRENTUSER);
		if(null==currentUser) {
			return ServerResponse.createErrorResponseByCode(ResponseCode.NEED_LOGIN);
		}
		return userService.getUserById(currentUser.getId());
	}
	
	

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
}
