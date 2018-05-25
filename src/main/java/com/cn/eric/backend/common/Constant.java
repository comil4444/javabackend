package com.cn.eric.backend.common;

import java.util.Set;

import com.google.common.collect.Sets;

public interface Constant {
	String CURRENTUSER="currentUser";
	String EMAIL = "email";
	String USERNAME="username";
	
	public interface Role{
		int ADMIN = 1;
		int USER = 0;
	}
	
	public interface OrderBy{
		Set ORDER_BY = Sets.newHashSet("price_desc","price_asc");
	}
	
	public enum ProductStatus{
		
		ON_SALE("在线",1),
		OFF_LINE("下架",0);
		
		private String status;
		private int code;
		
		ProductStatus(String status,int code) {
			this.status = status;
			this.code = code;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}
	}
}
