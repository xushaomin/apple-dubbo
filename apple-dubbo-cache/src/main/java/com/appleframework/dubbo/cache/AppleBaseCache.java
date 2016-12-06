package com.appleframework.dubbo.cache;

import org.apache.log4j.Logger;

import com.alibaba.dubbo.common.URL;

/**
 *  * AppleCache
 * 
 * @author xusm.cruise
 */
public class AppleBaseCache extends AppleCache {
	
	private static final Logger logger = Logger.getLogger(AppleBaseCache.class);
	
	public AppleBaseCache(URL url) {
		super(url);
	}

	public void put(Object param, Object value) {
		if (null != dubboCacheManager) {
			try {
				String key = getCacheKey(param);
				if(logger.isDebugEnabled()) {
					logger.debug("dubbo set cache key = " + key);
				}
				if (expireTime == -1) {
					dubboCacheManager.set(key, value);
				} else {
					dubboCacheManager.set(key, value, expireTime);
				}
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}

	public Object get(Object param) {
		if (null != dubboCacheManager) {
			try {
				String key = getCacheKey(param);
				if(logger.isDebugEnabled()) {
					logger.debug("dubbo get cache key = " + key);
				}
				return dubboCacheManager.get(key);
			} catch (Exception e) {
				logger.error(e);
			}
		}
		return null;
	}

}