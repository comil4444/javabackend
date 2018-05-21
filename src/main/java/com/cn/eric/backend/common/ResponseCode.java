package com.cn.eric.backend.common;

public enum ResponseCode {
	SUCCESS(0,"SUCCESS"),
	ERROR(1,"ERROR"),
	ILLEGAL_PARAM(2,"ILLEGAL_PARAM"),
	NO_USER(3,"NO USER"),
	NEED_LOGIN(10,"NEED LOGIN");
	
	private int code;
	private String msg;
	
	ResponseCode(int code,String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	public int getCode() {
		return this.code;
	}
	
	public String getMessage() {
		return this.msg;
	}
	
	
}
