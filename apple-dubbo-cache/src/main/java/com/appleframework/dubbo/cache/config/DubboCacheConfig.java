package com.appleframework.dubbo.cache.config;


public class DubboCacheConfig {
	
	private static boolean isCacheObject = false;

	private static boolean isCacheEnable = true;
	
	public static boolean isCacheEnable() {
		return isCacheEnable;
	}

	public static void setCacheEnable(boolean isCacheEnable) {
		DubboCacheConfig.isCacheEnable = isCacheEnable;
	}

	public static boolean isCacheObject() {
		return isCacheObject;
	}

	public static void setCacheObject(boolean isCacheObject) {
		DubboCacheConfig.isCacheObject = isCacheObject;
	}

}
