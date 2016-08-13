package com.appleframework.dubbo.cache.utils;

import com.alibaba.dubbo.common.URL;

/**
 */
public class DubboUtil {

    public static String getMethodParamKey(URL url, String param){
        return url.getParameter("method") + "." + param;
    }
}
