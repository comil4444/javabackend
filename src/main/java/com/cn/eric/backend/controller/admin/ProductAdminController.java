package com.cn.eric.backend.controller.admin;

import java.io.File;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cn.eric.backend.common.Constant;
import com.cn.eric.backend.common.ResponseCode;
import com.cn.eric.backend.common.ServerResponse;
import com.cn.eric.backend.pojo.Product;
import com.cn.eric.backend.pojo.User;
import com.cn.eric.backend.service.ProductService;
import com.cn.eric.backend.service.UserService;
import com.cn.eric.backend.vo.ProductDetailVO;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/admin/product")
public class ProductAdminController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private UserService userService;
	
	
	@RequestMapping("save.do")
	@ResponseBody
	public ServerResponse<String> saveOrUpdatePro(HttpSession session,Product product) {
		User user = (User) session.getAttribute(Constant.CURRENTUSER);
		if(null==user) {
			return ServerResponse.createErrorResponseByCode(ResponseCode.NEED_LOGIN);
		}
		if(!userService.checkAdminPermission(user)) {
			return ServerResponse.createErrorResponseByMsg("需要管理员权限！");
		}
		
		return productService.saveOrUpdatePro(product);
	}
	
	@RequestMapping("udpate_status.do")
	@ResponseBody
	public ServerResponse updateProductStatus(HttpSession session,Integer productId,Integer status) {
		User user = (User) session.getAttribute(Constant.CURRENTUSER);
		if(null==user) {
			return ServerResponse.createErrorResponseByCode(ResponseCode.NEED_LOGIN);
		}
		if(!userService.checkAdminPermission(user)) {
			return ServerResponse.createErrorResponseByMsg("需要管理员权限！");
		}
		return productService.updateProStatus(productId,status);
	}
	
	@RequestMapping("get_product_detail.do")
	@ResponseBody
	public ServerResponse<ProductDetailVO> getProductDetail(HttpSession session,Integer productId) {
		User user = (User) session.getAttribute(Constant.CURRENTUSER);
		if(null==user) {
			return ServerResponse.createErrorResponseByCode(ResponseCode.NEED_LOGIN);
		}
		if(!userService.checkAdminPermission(user)) {
			return ServerResponse.createErrorResponseByMsg("需要管理员权限！");
		}
		return productService.getProductDetail(productId);
	}
	
	@RequestMapping("list.do")
	@ResponseBody
	public ServerResponse<PageInfo> getList(HttpSession session,@RequestParam(value="pageNum",defaultValue="1")Integer pageNum,@RequestParam(value="pageSize",defaultValue="10")Integer pageSize) {
		User user = (User) session.getAttribute(Constant.CURRENTUSER);
		if(null==user) {
			return ServerResponse.createErrorResponseByCode(ResponseCode.NEED_LOGIN);
		}
		if(!userService.checkAdminPermission(user)) {
			return ServerResponse.createErrorResponseByMsg("需要管理员权限！");
		}
		return productService.getProductList(pageNum,pageSize);
	}
	
	@RequestMapping("search.do")
	@ResponseBody
	public ServerResponse<PageInfo> searchList(HttpSession session,@RequestParam(value="pageNum",defaultValue="1")Integer pageNum,@RequestParam(value="pageSize",defaultValue="10")Integer pageSize,String productName,Integer productId) {
		User user = (User) session.getAttribute(Constant.CURRENTUSER);
		if(null==user) {
			return ServerResponse.createErrorResponseByCode(ResponseCode.NEED_LOGIN);
		}
		if(!userService.checkAdminPermission(user)) {
			return ServerResponse.createErrorResponseByMsg("需要管理员权限！");
		}
		return productService.searchProducts(productName,productId,pageNum,pageSize);
	}
	
	@RequestMapping("upload.do")
	@ResponseBody
	public ServerResponse<String> upload(HttpSession session,File uploadFile) {
		return null;
	}
	
	@RequestMapping("richtext_img_upload.do")
	@ResponseBody
	public ServerResponse<String> uploadRichText(HttpSession session,File uploadFile) {
		return null;
	}
	
	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	
}
