package com.appleframework.dubbo.cache;

import com.alibaba.dubbo.cache.Cache;
import com.alibaba.dubbo.cache.support.AbstractCacheFactory;
import com.alibaba.dubbo.common.URL;
import com.appleframework.dubbo.cache.config.DubboCacheConfig;

/**
 *  * AppleCacheFactory
 * 
 * @author xusm.cruise
 */
public class AppleCacheFactory extends AbstractCacheFactory {
	
	private static NoCache noCache = new NoCache();

	@Override
	protected Cache createCache(URL url) {
		boolean cacheEnable = DubboCacheConfig.isCacheEnable();
		if (cacheEnable) {
			boolean cacheObject = DubboCacheConfig.isCacheObject();
			if (cacheObject) {
				return new AppleCacheObject(url);
			} else {
				return new AppleCacheOrdinary(url);
			}
		} else {
			return noCache;
		}
	}
	
}