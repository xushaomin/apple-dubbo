package com.appleframework.dubbo.cache;

import com.alibaba.dubbo.cache.Cache;
import com.alibaba.dubbo.cache.support.AbstractCacheFactory;
import com.alibaba.dubbo.common.URL;
import com.appleframework.dubbo.cache.config.CacheConfig;

/**
 *  * AppleCacheFactory
 * 
 * @author xusm.cruise
 */
public class AppleCacheFactory extends AbstractCacheFactory {

	@Override
	protected Cache createCache(URL url) {
		boolean cacheEnable = CacheConfig.isCacheEnable();
		if (cacheEnable) {
			boolean cacheObject = CacheConfig.isCacheObject();
			if (cacheObject) {
				return new AppleObjectCache(url);
			} else {
				return new AppleBaseCache(url);
			}
		} else {
			return null;
		}
	}
	
}