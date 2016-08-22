package com.appleframework.dubbo.cache;

import org.apache.log4j.Logger;

import com.alibaba.dubbo.cache.Cache;
import com.alibaba.dubbo.common.URL;
import com.appleframework.cache.core.CacheManager;
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
	
	private static String CACHE_MANAGER_ID = "dubboCacheManager";

	private String baseCacheKey;

	private int expireTime = -1;

	private CacheManager dubboCacheManager;

	public AppleCache(URL url) {
		this.baseCacheKey = getBaseCacheKey(url);
		this.expireTime = url.getParameter(DubboUtil.getMethodParamKey(url, Constants.EXPIRE_TIME), -1);
		if (expireTime == -1) {
			this.expireTime = url.getParameter(Constants.EXPIRE_TIME, -1);
		}
		dubboCacheManager = (CacheManager)SpringUtility.getApplicationContext().getBean(CACHE_MANAGER_ID);
		if(null == dubboCacheManager) {
			dubboCacheManager = SpringUtility.getApplicationContext().getBean(CacheManager.class);
		}
	}

	public void put(Object param, Object value) {
		if (Constants.CACHE_ENABLE) {
			try {
				String key = getCacheKey(param);
				if(logger.isDebugEnabled()) {
					logger.debug("dubbo set cache key = " + key);
				}
				if (expireTime == -1) {
					getDubboCacheManager().set(key, value);
				} else {
					getDubboCacheManager().set(key, value, expireTime);
				}
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}

	public Object get(Object param) {
		if (Constants.CACHE_ENABLE) {
			try {
				String key = getCacheKey(param);
				if(logger.isDebugEnabled()) {
					logger.debug("dubbo get cache key = " + key);
				}
				return getDubboCacheManager().get(key);
			} catch (Exception e) {
				logger.error(e);
			}
		}
		return null;
	}

	private String getCacheKey(Object param) {
		return baseCacheKey + "_" + param.toString();
	}

	public CacheManager getDubboCacheManager() {
		return dubboCacheManager;
	}

	public void setDubboCacheManager(CacheManager dubboCacheManager) {
		this.dubboCacheManager = dubboCacheManager;
	}
	
	public String getBaseCacheKey(URL url) {
		String cacheKey = null;
		if (url.hasParameter(url.getParameter("method") + "." + "paramTypes")) {
			String paramTypesStr = url.getParameter(DubboUtil.getMethodParamKey(url, "paramTypes"));
			cacheKey = url.getPath() + "." + url.getParameter("method") + "." + MD5Util.string2MD5(paramTypesStr);
		} else {
			cacheKey = url.getPath() + "." + url.getParameter("method");
		}
		return cacheKey;
	}

}