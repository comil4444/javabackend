package com.cn.eric.backend.common;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class TokenCache {

	private static Logger logger = LoggerFactory.getLogger(TokenCache.class);
	
	public static final String TOKEN_PREFIX = "token_";
	
	private static LoadingCache<String,String> loadingCache = CacheBuilder.newBuilder()
			.initialCapacity(1000)
			.maximumSize(5000)
			.expireAfterAccess(12, TimeUnit.HOURS)
			.build(new CacheLoader<String,String>(){
				@Override
				public String load(String key) throws Exception {
					// TODO Auto-generated method stub
					return null;
				}
		
	});
	
	public static String getValue(String key) {
		String value = StringUtils.EMPTY;
		try {
			return loadingCache.get(key);
		}catch(Exception e){
			logger.error("loading cache:"+e);
		}
		return null;
	}
	
	public static void setValue(String key,String value) {
		loadingCache.put(key, value);
	}
}
