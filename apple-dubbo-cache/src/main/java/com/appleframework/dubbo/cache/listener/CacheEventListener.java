package com.appleframework.dubbo.cache.listener;

import java.util.Properties;

import com.appleframework.config.core.event.ConfigListener;
import com.appleframework.dubbo.cache.config.DubboCacheConfig;

public class CacheEventListener implements ConfigListener {

	private static String KEY_CACHE_ENABLE = "dubbo.cache.enable";

	private static String KEY_CACHE_OBJECT = "dubbo.cache.object";

	@Override
	public void receiveConfigInfo(Properties props) {
		Object cacheEnable = props.get(KEY_CACHE_ENABLE);
		if (null != cacheEnable) {
			DubboCacheConfig.setCacheEnable(Boolean.valueOf(cacheEnable.toString()));
		}
		Object cacheObject = props.get(KEY_CACHE_OBJECT);
		if (null != cacheEnable) {
			DubboCacheConfig.setCacheObject(Boolean.valueOf(cacheObject.toString()));
		}
	}

}
