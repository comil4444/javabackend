package com.cn.eric.backend.util;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateUtil {

	public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static String date2Str(Date date, String format) {
		if(null==date)
			return StringUtils.EMPTY;
		DateTime dt = new DateTime(date);
		return dt.toString(format);
	}

	public static Date str2Date(String dateStr, String format) {
		DateTimeFormatter formatter = DateTimeFormat.forPattern(format);
		DateTime dt = formatter.parseDateTime(dateStr);
		return dt.toDate();
	}
	
	public static String date2Str(Date date) {
		if(null==date)
			return StringUtils.EMPTY;
		DateTime dt = new DateTime(date);
		return dt.toString(STANDARD_FORMAT);
	}
	
	public static Date str2Date(String dateStr) {
		DateTimeFormatter formatter = DateTimeFormat.forPattern(STANDARD_FORMAT);
		DateTime dt = formatter.parseDateTime(dateStr);
		return dt.toDate();
	}

}
