package com.cn.eric.backend.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> {
	
	private int status;
	private String msg;
	private T data;
	
	private ServerResponse(int status) {
		this.status = status;
	}
	
	private ServerResponse(int status,T data) {
		this.status = status;
		this.data = data;
	}
	
	private ServerResponse(int status,String msg) {
		this.status = status;
		this.msg = msg;
	}
	
	private ServerResponse(int status,String msg,T data) {
		this.status = status;
		this.msg = msg;
		this.data = data;
	}
	
	@JsonIgnore
	public boolean isSuccess() {
		return this.status==ResponseCode.SUCCESS.getCode();
	}
	
	public static ServerResponse createSuccessResonse() {
		return new ServerResponse(ResponseCode.SUCCESS.getCode());
	}
	
	public static ServerResponse createSuccessResponseByMsg(String msg) {
		return new ServerResponse(ResponseCode.SUCCESS.getCode(),msg);
	}
	
	public static ServerResponse createSuccessResponseByData(Object data) {
		return new ServerResponse(ResponseCode.SUCCESS.getCode(),null,data);
	}
	
	public static ServerResponse createSuccessResponseByMsgData(String msg,Object data) {
		return new ServerResponse(ResponseCode.SUCCESS.getCode(),msg,data);
	}
	
	public static ServerResponse createErrorResonse() {
		return new ServerResponse(ResponseCode.ERROR.getCode());
	}
	
	public static ServerResponse createErrorResponseByMsg(String msg) {
		return new ServerResponse(ResponseCode.ERROR.getCode(),msg);
	}
	
	public static ServerResponse createErrorResponseByData(Object data) {
		return new ServerResponse(ResponseCode.ERROR.getCode(),null,data);
	}
	
	public static ServerResponse createErrorResponseByMsgData(String msg,Object data) {
		return new ServerResponse(ResponseCode.ERROR.getCode(),msg,data);
	}
	
	public static ServerResponse createErrorResponseByCode(ResponseCode code) {
		return new ServerResponse(code.getCode(),code.getMessage());
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
}