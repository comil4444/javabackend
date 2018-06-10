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
	
	public interface Cart{
		String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
		String UN_LIMIT_NUM_SUCCESS = "UN_LIMIT_NUM_SUCCESS";
		
		int CHECKED = 1;
		int UN_CHECKED = 1;
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
		
		public static boolean contains(int code) {
			for(ProductStatus ps:ProductStatus.values()) {
				if(ps.getCode()==code)
					return true;
			}
			return false;
		}
	}
}
