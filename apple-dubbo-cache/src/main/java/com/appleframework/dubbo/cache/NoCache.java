package com.appleframework.dubbo.cache;

import com.alibaba.dubbo.cache.Cache;

/**
 * * AppleCache
 * 
 * @author xusm.cruise
 */
public class NoCache implements Cache {

	@Override
	public Object get(Object arg0) {
		return null;
	}

	@Override
	public void put(Object arg0, Object arg1) {

	}

}