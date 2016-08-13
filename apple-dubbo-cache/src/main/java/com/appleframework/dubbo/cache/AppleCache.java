package com.appleframework.dubbo.cache;

import org.apache.log4j.Logger;

import com.alibaba.dubbo.cache.Cache;
import com.alibaba.dubbo.common.URL;
import com.appleframework.cache.core.CacheManager;
import com.appleframework.core.utils.SpringUtility;
import com.appleframework.dubbo.cache.utils.Constants;
import com.appleframework.dubbo.cache.utils.DubboUtil;

/**
 * dubbo结果集缓存 key的生成规则 propKey > key > 自动生成key
 * 1.配置中心的key规则优先，获取key为dubbo：reference的propKey 2.key只支持表达式变量替换，规则为: -变量-
 */
public class AppleCache implements Cache {

	private static final Logger logger = Logger.getLogger(AppleCache.class);

	private String name;

	private int timeout = -1;

	private CacheManager dubboCacheManager;

	public AppleCache(URL url, String name) {
		this.name = name;
		this.timeout = url.getParameter(DubboUtil.getMethodParamKey(url, Constants.TIMEOUT), -1);
		if (timeout == -1) {
			this.timeout = url.getParameter(Constants.TIMEOUT, -1);
		}
	}

	public void put(Object param, Object value) {
		// 给予缓存开关
		if (Constants.CACHE_ENABLE) {
			try {
				String key = getCacheKey(param);
				if(logger.isDebugEnabled()) {
					logger.debug("dubbo set cache key = " + key);
				}
				if (timeout == -1) {
					getDubboCacheManager().set(key, value);
				} else {
					getDubboCacheManager().set(key, value, timeout);
				}
			} catch (Exception e) {
				logger.error("添加缓存数据失败！", e);
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
				logger.error("获取缓存数据失败！", e);
			}
		}
		return null;
	}

	private String getCacheKey(Object param) {
		return name + "_" + param.toString();
	}

	public CacheManager getDubboCacheManager() {
		if (null == dubboCacheManager) {
			dubboCacheManager = SpringUtility.getApplicationContext().getBean(CacheManager.class);
		}
		return dubboCacheManager;
	}

	public void setDubboCacheManager(CacheManager dubboCacheManager) {
		this.dubboCacheManager = dubboCacheManager;
	}

}