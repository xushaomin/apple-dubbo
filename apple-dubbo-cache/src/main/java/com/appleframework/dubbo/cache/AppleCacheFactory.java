package com.appleframework.dubbo.cache;

import com.alibaba.dubbo.cache.Cache;
import com.alibaba.dubbo.cache.support.AbstractCacheFactory;
import com.alibaba.dubbo.common.URL;

/**
 *  * AppleCacheFactory
 * 
 * @author xusm.cruise
 */
public class AppleCacheFactory extends AbstractCacheFactory {

	@Override
	protected Cache createCache(URL url) {
		return new AppleCache(url);
	}

	
}