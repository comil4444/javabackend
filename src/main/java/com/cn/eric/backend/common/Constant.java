package com.cn.eric.backend.common;

import java.util.Set;

import com.google.common.collect.Sets;

public interface Constant {
	String CURRENTUSER = "currentUser";
	String EMAIL = "email";
	String USERNAME = "username";

	public interface Role {
		int ADMIN = 1;
		int USER = 0;
	}

	public interface Cart {
		String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
		String UN_LIMIT_NUM_SUCCESS = "UN_LIMIT_NUM_SUCCESS";

		int CHECKED = 1;
		int UN_CHECKED = 1;
	}

	public interface OrderBy {
		Set ORDER_BY = Sets.newHashSet("price_desc", "price_asc");
	}

	public enum ProductStatus {

		ON_SALE("在线", 1), OFF_LINE("下架", 0);

		private String status;
		private int code;

		ProductStatus(String status, int code) {
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
			for (ProductStatus ps : ProductStatus.values()) {
				if (ps.getCode() == code)
					return true;
			}
			return false;
		}
	}

	public interface AlipayCallBack {
		String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
		String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";

		String RESPONSE_SUCCESS = "success";
		String RESPONSE_FAILED = "failed";
	}
	
	public enum PayInfoStatus{
		CANCELED(0,"已取消"),
        NO_PAY(10,"未支付"),
        PAID(20,"已付款"),
        SHIPPED(40,"已发货"),
        ORDER_SUCCESS(50,"订单完成"),
        ORDER_CLOSE(60,"订单关闭");
		private String status;
		private int code;

		PayInfoStatus(int code,String status) {
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
	
	public enum PayPlatformEnum{
        ALIPAY(1,"支付宝");

        PayPlatformEnum(int code,String value){
            this.code = code;
            this.value = value;
        }
        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }
	
	public enum PaymentType{
		ON_LINE(1,"在线支付");

		PaymentType(int code,String value){
            this.code = code;
            this.value = value;
        }
        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
        
        public static PaymentType codeFor(int code) {
        		for(PaymentType item:values()) {
        			if(item.getCode()==code)
        				return item;
        		}
        		return null;
        }
	}
}
