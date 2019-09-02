package com.beyond.library.network.net.httpclient.httputil;

import java.util.Map;

/**
 * Created by East.K on 2017/2/7.
 */

public interface HttpInterceptor {

    /**
     * 缓存目录
     * @return
     */
    String getCacheDir();
    /**
     *
     * @return
     */
    String getToken();

    /**
     *
     * @return
     */
    String getWid();

    /**
     *
     * @return
     */
    String getShopId();

    /**
     *
     * @return
     */
    Map<String, String> getHeaders();

    /**
     *
     * @return
     */
    String getBaseUrl();

}
