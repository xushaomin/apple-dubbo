package com.appleframework.dubbo.cache;

import org.apache.log4j.Logger;

import com.alibaba.dubbo.common.URL;
import com.appleframework.cache.core.CacheObject;
import com.appleframework.cache.core.CacheObjectImpl;

/**
 *  * AppleCache
 * 
 * @author xusm.cruise
 */
public class AppleCacheObject extends AppleCache {
	
	private static final Logger logger = Logger.getLogger(AppleCacheObject.class);
	
	public AppleCacheObject(URL url) {
		super(url);
	}

	private long getExpiredTime() {
		long lastTime = 2592000000L;
		if (expireTime > 0) {
			lastTime = expireTime * 1000;
		}
		return System.currentTimeMillis() + lastTime;
	}
	
	public void put(Object param, Object value) {
		if (null != dubboCacheManager) {
			try {
				String key = getCacheKey(param);
				if(logger.isDebugEnabled()) {
					logger.debug("dubbo set cache key = " + key);
				}
				CacheObject cache = new CacheObjectImpl(value, getExpiredTime());
				if (expireTime == -1) {
					dubboCacheManager.set(key, cache);
				} else {
					dubboCacheManager.set(key, cache, expireTime * 2);
				}
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}

	public Object get(Object param) {
		Object value = null;
		if (null != dubboCacheManager) {
			try {
				String key = getCacheKey(param);
				if (logger.isDebugEnabled()) {
					logger.debug("dubbo get cache key = " + key);
				}
				CacheObject cache = dubboCacheManager.get(key, CacheObject.class);
				if (null != cache) {
					if (cache.isExpired()) {
						this.resetCacheObject(key, cache);
					} else {
						value = cache.getObject();
					}
				}
			} catch (Exception e) {
				logger.error(e);
			}
		}
		return value;
	}
	
	private void resetCacheObject(String key, CacheObject cache) {
		cache.setExpiredTime(getExpiredTime());
		if (expireTime == -1) {
			dubboCacheManager.set(key, cache);
		} else {
			dubboCacheManager.set(key, cache, expireTime * 2);
		}
	}

}