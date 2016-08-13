package com.appleframework.dubbo.cache;

import com.alibaba.dubbo.cache.Cache;
import com.alibaba.dubbo.cache.CacheFactory;
import com.alibaba.dubbo.common.URL;
import com.appleframework.dubbo.cache.utils.DubboUtil;
import com.appleframework.dubbo.cache.utils.MD5Util;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 */
public class AppleCacheFactory implements CacheFactory {

	private static final Logger logger = Logger.getLogger(AppleCacheFactory.class);

	private Map<String, Cache> cacheUrlMap = new HashMap<String, Cache>();

	public Cache getCache(URL url) {
		if(logger.isDebugEnabled()) {
			logger.debug("dubbo url = " + url.toString());
		}
		String cacheKey = getCacheKey(url);
		if (!cacheUrlMap.containsKey(cacheKey)) {
			try {
				cacheUrlMap.put(cacheKey, new AppleCache(url, cacheKey));
			} catch (Exception e) {
				logger.error("缓存初始化失败! url=" + url, e);
			}
		}
		return cacheUrlMap.get(cacheKey);
	}

	public static String getCacheKey(URL url) {
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