package com.cn.eric.backend.vo;

import java.util.List;

public class OrderCartVO {

	private List<OrderItemVO> orderItemVoList;
	private String imageHost;
	private double productTotalPrice;
	public List<OrderItemVO> getOrderItemVoList() {
		return orderItemVoList;
	}
	public void setOrderItemVoList(List<OrderItemVO> orderItemVoList) {
		this.orderItemVoList = orderItemVoList;
	}
	public String getImageHost() {
		return imageHost;
	}
	public void setImageHost(String imageHost) {
		this.imageHost = imageHost;
	}
	public double getProductTotalPrice() {
		return productTotalPrice;
	}
	public void setProductTotalPrice(double productTotalPrice) {
		this.productTotalPrice = productTotalPrice;
	}
	
	

}
