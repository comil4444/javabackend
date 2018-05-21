package com.cn.eric.backend.service.impl;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.eric.backend.common.Constant;
import com.cn.eric.backend.common.ResponseCode;
import com.cn.eric.backend.common.ServerResponse;
import com.cn.eric.backend.common.TokenCache;
import com.cn.eric.backend.dao.UserMapper;
import com.cn.eric.backend.pojo.User;
import com.cn.eric.backend.service.UserService;
import com.cn.eric.backend.util.MD5Util;

@Service
@SuppressWarnings("unchecked")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;
	
	
	@Override
	public ServerResponse<User> login(String username, String password) {
		
		if(userMapper.checkUsername(username)==false)
			return ServerResponse.createErrorResponseByCode(ResponseCode.NO_USER);

		String md5Pw = MD5Util.MD5EncodeUtf8(password);
		User user = userMapper.login(username, md5Pw);
		if(user!=null) {
			user.setPassword(StringUtils.EMPTY);
			return ServerResponse.createSuccessResponseByMsgData("login success", user);
		}
		
		return ServerResponse.createErrorResponseByMsg("password error!");
	}

	@Override
	public ServerResponse<String> checkValid(String str, String type) {
		if(StringUtils.isNotBlank(str)) {
			if(Constant.EMAIL.equals(type)) {
				if(userMapper.checkEmail(str))
					return ServerResponse.createErrorResponseByMsg("邮箱已经注册！");
				return ServerResponse.createSuccessResponseByMsg("邮箱可以注册！");
			}
			else if(Constant.USERNAME.equals(type)) {
				if(userMapper.checkUsername(str))
					return ServerResponse.createErrorResponseByMsg("用户已经注册！");
				return ServerResponse.createSuccessResponseByMsg("用户名可以注册！");
			}
		}
		return ServerResponse.createErrorResponseByMsg("参数错误！");
	}
	
	@Override
	public ServerResponse<String> register(User user) {
		ServerResponse<String> rs = checkValid(user.getUsername(),Constant.USERNAME);
		if(!rs.isSuccess())
			return rs;
		rs = checkValid(user.getEmail(),Constant.EMAIL);
		if(!rs.isSuccess())
			return rs;
		user.setRole(Constant.Role.USER);
		user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
		int count = userMapper.insert(user);
		if(count==0)
			return ServerResponse.createErrorResonse();
		return ServerResponse.createSuccessResonse();
	}
	
	@Override
	public ServerResponse<String> getForgetQuestion(String username) {
		String question = userMapper.getQuestionByUsername(username);
		if(StringUtils.isNotBlank(question)) {
			return ServerResponse.createSuccessResponseByData(question);
		}
		return ServerResponse.createErrorResponseByMsg("没有设置问题！");
	}
	
	@Override
	public ServerResponse<String> checkAnswer(String username, String question, String answer) {
		int resultCount = userMapper.checkQuestionAnswer(username, question, answer);
		if(resultCount>0) {
			String token = UUID.randomUUID().toString();
			TokenCache.setValue(TokenCache.TOKEN_PREFIX+username, token);
			return ServerResponse.createSuccessResponseByMsg(token);
		}
		return ServerResponse.createErrorResponseByMsg("答案错误！");
	}
	
	@Override
	public ServerResponse<String> forgetResetPassword(String username, String newPassword, String token) {
		//check token
		if(StringUtils.isBlank(token))
			return ServerResponse.createErrorResponseByMsg("token错误");
		//check username
		ServerResponse<String> rs = checkValid(username,Constant.USERNAME);
		if(rs.isSuccess())
			return rs;
		String tokenCache = TokenCache.getValue(TokenCache.TOKEN_PREFIX+username);
		//check cached token
		if(StringUtils.isBlank(tokenCache)) {
			return ServerResponse.createErrorResponseByMsg("token不匹配或者过期");
		}
		
		if(StringUtils.equals(tokenCache, token)) {
			String md5Password = MD5Util.MD5EncodeUtf8(newPassword);
			int resultCount  = userMapper.updatePasswordByUsername(username,md5Password);
			if(resultCount>0)
				return ServerResponse.createSuccessResponseByMsg("更改成功！");
			
		}else {
			return ServerResponse.createErrorResponseByMsg("token不匹配或者过期");
		}
		return ServerResponse.createErrorResponseByMsg("更新密码失败");
	}
	
	public UserMapper getUserMapper() {
		return userMapper;
	}

	public void setUserMapper(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	@Override
	public ServerResponse<String> resetPassword(String oldPassword, String newPassword, User user) {
		int resultCount = userMapper.checkPasswordById(user.getId(),MD5Util.MD5EncodeUtf8(oldPassword));
		if(resultCount==0)
			return ServerResponse.createErrorResponseByMsg("旧密码错误！");
		user.setPassword(MD5Util.MD5EncodeUtf8(newPassword));
		resultCount = userMapper.updateByPrimaryKeySelective(user);
		if(resultCount>0)
			return ServerResponse.createSuccessResponseByMsg("更新密码成功");
		return ServerResponse.createErrorResponseByMsg("更新密码失败");
	}

	@Override
	public ServerResponse<User> updateUserInfo(User user) {
		//user更新的email是否已经注册
		int resultCount = userMapper.checkEmailByUserId(user.getEmail(),user.getId());
		if(resultCount>0)
			return ServerResponse.createErrorResponseByMsg("email已经被注册");
		User updateUser = new User();
		updateUser.setId(user.getId());
		updateUser.setEmail(user.getEmail());
		updateUser.setPhone(user.getPhone());
		updateUser.setQuestion(user.getQuestion());
		updateUser.setAnswer(user.getAnswer());
		
		resultCount = userMapper.updateByPrimaryKeySelective(updateUser);
		if(resultCount==0)
			return ServerResponse.createErrorResponseByMsg("更新信息失败");
		return ServerResponse.createSuccessResponseByMsgData("更新成功", updateUser);
	}

	@Override
	public ServerResponse<User> getUserById(Integer id) {
		User user = userMapper.selectByPrimaryKey(id);
		if(null==user) {
			return ServerResponse.createErrorResponseByMsg("用户不存在");
		}
		user.setPassword(StringUtils.EMPTY);
		return ServerResponse.createSuccessResponseByData(user);
	}

	@Override
	public boolean checkAdminPermission(User user) {
		if(null==user||user.getRole().equals(Constant.Role.USER))
			return false;
		return true;
	}



}
