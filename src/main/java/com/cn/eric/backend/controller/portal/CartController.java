package com.cn.eric.backend.controller.portal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cn.eric.backend.common.Constant;
import com.cn.eric.backend.common.ResponseCode;
import com.cn.eric.backend.common.ServerResponse;
import com.cn.eric.backend.pojo.User;
import com.cn.eric.backend.service.CartService;
import com.cn.eric.backend.vo.CartVO;

@Controller
@RequestMapping("/cart/")
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	@RequestMapping("list.do")
	@ResponseBody
	public ServerResponse<CartVO> list(HttpSession session) {
		User user = (User)session.getAttribute(Constant.CURRENTUSER);
		if(null==user)
			return ServerResponse.createErrorResponseByCode(ResponseCode.NEED_LOGIN);
		return cartService.list(user.getId());
	}
	
	@RequestMapping("add.do")
	@ResponseBody
	public ServerResponse<CartVO> add(HttpSession session,Integer productId,int count) {
		User user = (User)session.getAttribute(Constant.CURRENTUSER);
		if(null==user)
			return ServerResponse.createErrorResponseByCode(ResponseCode.NEED_LOGIN);
		return cartService.add(user.getId(),productId,count);
	}
	
	@RequestMapping("update.do")
	@ResponseBody
	public ServerResponse<CartVO>  update(HttpSession session,Integer productId,int count) {
		User user = (User)session.getAttribute(Constant.CURRENTUSER);
		if(null==user)
			return ServerResponse.createErrorResponseByCode(ResponseCode.NEED_LOGIN);
		return cartService.update(user.getId(),productId,count);
	}
	
	@RequestMapping("delete_product.do")
	@ResponseBody
	public ServerResponse<CartVO> deleteProductInCart(HttpSession session,String productIds) {
		User user = (User)session.getAttribute(Constant.CURRENTUSER);
		if(null==user)
			return ServerResponse.createErrorResponseByCode(ResponseCode.NEED_LOGIN);
		return cartService.deleteProduct(user.getId(),productIds);
	}
	
	@RequestMapping("get_Product_Count.do")
	public Integer getProductCountInCart(HttpSession session) {
		User user = (User)session.getAttribute(Constant.CURRENTUSER);
		if(null==user)
			return 0;
		return cartService.getProductCountInCart(user.getId());
	}
	
	
	@RequestMapping("select.do")
	@ResponseBody
	public ServerResponse<CartVO> select(HttpSession session, int productId) {
		User user = (User)session.getAttribute(Constant.CURRENTUSER);
		if(null==user)
			return ServerResponse.createErrorResponseByCode(ResponseCode.NEED_LOGIN);
		return cartService.selectProduct(user.getId(),productId);
	}
	
	@RequestMapping("unselect.do")
	@ResponseBody
	public ServerResponse<CartVO> unSelect(HttpSession session, int productId) {
		User user = (User)session.getAttribute(Constant.CURRENTUSER);
		if(null==user)
			return ServerResponse.createErrorResponseByCode(ResponseCode.NEED_LOGIN);
		return null;
	}
	
	@RequestMapping("select_all.do")
	@ResponseBody
	public ServerResponse<CartVO> selectAll(HttpSession session) {
		User user = (User)session.getAttribute(Constant.CURRENTUSER);
		if(null==user)
			return ServerResponse.createErrorResponseByCode(ResponseCode.NEED_LOGIN);
		return null;
	}
	
	@RequestMapping("unselect_all.do")
	@ResponseBody
	public ServerResponse<CartVO> unSelectAll(HttpSession session) {
		User user = (User)session.getAttribute(Constant.CURRENTUSER);
		if(null==user)
			return ServerResponse.createErrorResponseByCode(ResponseCode.NEED_LOGIN);
		return null;
	}
	

	public CartService getCartService() {
		return cartService;
	}

	public void setCartService(CartService cartService) {
		this.cartService = cartService;
	}
	
	
}
