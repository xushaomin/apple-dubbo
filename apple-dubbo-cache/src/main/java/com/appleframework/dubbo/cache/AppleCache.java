package com.appleframework.dubbo.cache;

import org.apache.log4j.Logger;

import com.alibaba.dubbo.cache.Cache;
import com.alibaba.dubbo.common.URL;
import com.appleframework.cache.core.CacheManager;
import com.appleframework.config.core.PropertyConfigurer;
import com.appleframework.core.utils.SpringUtility;
import com.appleframework.dubbo.cache.utils.Constants;
import com.appleframework.dubbo.cache.utils.DubboUtil;
import com.appleframework.dubbo.cache.utils.MD5Util;

/**
 *  * AppleCache
 * 
 * @author xusm.cruise
 */
public class AppleCache implements Cache {
	
	private static final Logger logger = Logger.getLogger(AppleCache.class);
	
	private String baseCacheKey;

	private int expireTime = -1;

	private CacheManager dubboCacheManager;

	public AppleCache(URL url) {
		this.baseCacheKey = getBaseCacheKey(url);
		this.expireTime = url.getParameter(DubboUtil.getMethodParamKey(url, Constants.KEY_EXPIRE_TIME), -1);
		if (expireTime == -1) {
			this.expireTime = url.getParameter(Constants.KEY_EXPIRE_TIME, -1);
		}
		Object cacheManager = null;
		try {
			cacheManager = SpringUtility.getApplicationContext().getBean(Constants.KEY_CACHE_MANAGER_NAME);
		} catch (Exception e) {
			logger.debug("The Bean id=DubboCacheManager is not exist !");
		}
		if(null == cacheManager) {
			try {
				dubboCacheManager = SpringUtility.getApplicationContext().getBean(CacheManager.class);
			} catch (Exception e) {
				logger.debug("The Bean class=CacheManager is not exist !");
			}
		}
		else {
			dubboCacheManager = (CacheManager)cacheManager;
		}
	}

	public void put(Object param, Object value) {
		boolean cacheEnable = PropertyConfigurer.getBoolean(Constants.KEY_DUBBO_CACHE_ENABLE, true);
		if (cacheEnable && null != dubboCacheManager) {
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
		boolean cacheEnable = PropertyConfigurer.getBoolean(Constants.KEY_DUBBO_CACHE_ENABLE, true);
		if (cacheEnable && null != dubboCacheManager) {
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

	private String getCacheKey(Object param) {
		return baseCacheKey + "_" + param.toString();
	}

	public String getBaseCacheKey(URL url) {
		String cacheKey = null;
		String method = url.getParameter("method");
		if (url.hasParameter(method + "." + "paramTypes")) {
			String paramTypesStr = url.getParameter(DubboUtil.getMethodParamKey(url, "paramTypes"));
			cacheKey = url.getPath() + "." + method + "." + MD5Util.string2MD5(paramTypesStr);
		} else {
			cacheKey = url.getPath() + "." + method;
		}
		return cacheKey;
	}

}