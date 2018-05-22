package com.cn.eric.backend.controller.admin;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cn.eric.backend.common.Constant;
import com.cn.eric.backend.common.ServerResponse;
import com.cn.eric.backend.pojo.User;
import com.cn.eric.backend.service.CategoryService;
import com.cn.eric.backend.service.UserService;

@Controller
@RequestMapping("/admin/category")
public class CategoryAdminController {

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "get_category",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse getCategory(HttpSession session, Integer categoryId) {
		User user = (User)session.getAttribute(Constant.CURRENTUSER);
		if(null==user||!userService.checkAdminPermission(user)) {
			return ServerResponse.createErrorResponseByMsg("需要管理员！");
		}
		return categoryService.getCategoryById(categoryId);
	}
	
	@RequestMapping(value = "add_category",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse addCategory(HttpSession session, String categoryName, @RequestParam(value = "parentId",defaultValue="0")Integer parentId) {
		User user = (User)session.getAttribute(Constant.CURRENTUSER);
		if(null==user||!userService.checkAdminPermission(user)) {
			return ServerResponse.createErrorResponseByMsg("需要管理员！");
		}
		return categoryService.addCategory(categoryName,parentId);
	}
	
	@RequestMapping(value = "update_category",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse updateCategory(HttpSession session, String categoryName, Integer categoryId) {
		User user = (User)session.getAttribute(Constant.CURRENTUSER);
		if(null==user||!userService.checkAdminPermission(user)) {
			return ServerResponse.createErrorResponseByMsg("需要管理员！");
		}
		return categoryService.updateCategory(categoryName,categoryId);
	}
	
	@RequestMapping(value = "get_child",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse getChildCatetory(HttpSession session,@RequestParam(value = "parentId",defaultValue="0")Integer parentId) {
		User user = (User)session.getAttribute(Constant.CURRENTUSER);
		if(null==user||!userService.checkAdminPermission(user)) {
			return ServerResponse.createErrorResponseByMsg("需要管理员！");
		}
		return categoryService.fetchChildCategory(parentId);
	}
	
	@RequestMapping(value = "get_deep_child",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse getDeepChildCategory(HttpSession session,@RequestParam(value = "parentId",defaultValue="0")Integer parentId) {
		User user = (User)session.getAttribute(Constant.CURRENTUSER);
		if(null==user||!userService.checkAdminPermission(user)) {
			return ServerResponse.createErrorResponseByMsg("需要管理员！");
		}
		return categoryService.fetchDeepChildCategory(parentId);
	}

	public CategoryService getCategoryService() {
		return categoryService;
	}

	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	
	
	
	
}
