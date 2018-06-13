package com.cn.eric.backend.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class OrderVO {

	private Long orderNo;
	private BigDecimal payment;
	private Integer paymentType;
	private String paymentDesc;
	private Integer postage;
	private Integer status;
	private Date paymentTime;
    private Date sendTime;
    private Date endTime;
    private Date closeTime;
    private Date createTime;
    private List<OrderItemVO> orderItemVoList;
    private long shippingId;
    private String imageHost;
    private String receiverName;
    private ShippingVO shippingVO;
    
    
    
    public String getImageHost() {
		return imageHost;
	}
	public void setImageHost(String imageHost) {
		this.imageHost = imageHost;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public ShippingVO getShippingVO() {
		return shippingVO;
	}
	public void setShippingVO(ShippingVO shippingVO) {
		this.shippingVO = shippingVO;
	}
	public String getPaymentDesc() {
    	return paymentDesc;
    }
    public void setPaymentDesc(String paymentDesc) {
    	this.paymentDesc = paymentDesc;
    }
	public Long getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}
	public BigDecimal getPayment() {
		return payment;
	}
	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}
	public Integer getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}
	public Integer getPostage() {
		return postage;
	}
	public void setPostage(Integer postage) {
		this.postage = postage;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getPaymentTime() {
		return paymentTime;
	}
	public void setPaymentTime(Date paymentTime) {
		this.paymentTime = paymentTime;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Date getCloseTime() {
		return closeTime;
	}
	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public List<OrderItemVO> getOrderItemVoList() {
		return orderItemVoList;
	}
	public void setOrderItemVoList(List<OrderItemVO> orderItemVoList) {
		this.orderItemVoList = orderItemVoList;
	}
	public long getShippingId() {
		return shippingId;
	}
	public void setShippingId(long shippingId) {
		this.shippingId = shippingId;
	}
    
}
