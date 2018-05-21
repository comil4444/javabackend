package com.cn.eric.backend.dao;

import org.apache.ibatis.annotations.Param;

import com.cn.eric.backend.pojo.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    
    boolean checkUsername(String username);
    
    boolean checkEmail(String email);
    
    User login(@Param("username") String username,@Param("password")String password);
    
    String getQuestionByUsername(String username);
    
    int checkQuestionAnswer(@Param("username") String username,@Param("question") String question,@Param("answer") String answer);
    
    int updatePasswordByUsername(@Param("username") String username,@Param("newPassword")String newPassword);

	int checkPasswordById(@Param("id")Integer id, @Param("oldPassword")String oldPassword);

	int checkEmailByUserId(@Param("email")String email, @Param("id")Integer id);
}