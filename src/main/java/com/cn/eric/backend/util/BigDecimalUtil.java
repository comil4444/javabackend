package com.cn.eric.backend.util;

import java.math.BigDecimal;

public class BigDecimalUtil {

	public static BigDecimal add(Double b1,Double b2) {
		BigDecimal bd1 = new BigDecimal(b1.toString());
		BigDecimal bd2 = new BigDecimal(b2.toString());
		return bd1.add(bd2);
	}
	
	public static BigDecimal minus(Double b1,Double b2) {
		BigDecimal bd1 = new BigDecimal(b1.toString());
		BigDecimal bd2 = new BigDecimal(b2.toString());
		return bd1.subtract(bd2);
	}
	
	public static BigDecimal mul(Double b1,Double b2) {
		BigDecimal bd1 = new BigDecimal(b1.toString());
		BigDecimal bd2 = new BigDecimal(b2.toString());
		return bd1.multiply(bd2);
	}
	
	public static BigDecimal div(Double b1,Double b2) {
		BigDecimal bd1 = new BigDecimal(b1.toString());
		BigDecimal bd2 = new BigDecimal(b2.toString());
		return bd1.divide(bd2, 2, BigDecimal.ROUND_HALF_UP);
	}
}
