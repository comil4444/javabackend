package com.cn.eric.backend.vo;

import java.util.List;

public class CartVO {
	private List<CartListVO> cartProductVoList;
	private boolean allChecked;
	private double cartTotalPrice;
	private String hostImage;
	public List<CartListVO> getCartProductVoList() {
		return cartProductVoList;
	}
	public void setCartProductVoList(List<CartListVO> cartProductVoList) {
		this.cartProductVoList = cartProductVoList;
	}
	public boolean isAllChecked() {
		return allChecked;
	}
	public void setAllChecked(boolean allChecked) {
		this.allChecked = allChecked;
	}
	public double getCartTotalPrice() {
		return cartTotalPrice;
	}
	public void setCartTotalPrice(double cartTotalPrice) {
		this.cartTotalPrice = cartTotalPrice;
	}
	public String getHostImage() {
		return hostImage;
	}
	public void setHostImage(String hostImage) {
		this.hostImage = hostImage;
	}
}
