package com.cn.eric.backend.service;

import com.cn.eric.backend.common.ServerResponse;
import com.cn.eric.backend.pojo.User;

public interface UserService {

	ServerResponse<User> login(String username,String password);
	
	ServerResponse<String> checkValid(String str,String type);
	
	ServerResponse<String> register(User user);
	
	ServerResponse<String> getForgetQuestion(String username);
	
	ServerResponse<String> checkAnswer(String username,String question,String answer);

	ServerResponse<String> forgetResetPassword(String username, String newPassword, String token);

	ServerResponse<String> resetPassword(String oldPassword, String newPassword, User user);

	ServerResponse<User> updateUserInfo(User user);

	ServerResponse getUserById(Integer id);
	
	boolean checkAdminPermission(User user);
}
