package com.beyond.library.network.net.httpclient.cache;


import android.text.TextUtils;

import com.beyond.library.network.net.httpclient.httputil.HttpUtil;

import java.io.File;
import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Internal;
import okio.Buffer;
import okio.BufferedSource;

/**
 * http缓存
 * Created by linjinfa on 2017/6/8.
 * email 331710168@qq.com
 */
public class RequestCacheInterceptor implements Interceptor  {

    private static final ResponseBody EMPTY_BODY = new ResponseBody() {
        @Override public MediaType contentType() {
            return null;
        }

        @Override public long contentLength() {
            return 0;
        }

        @Override public BufferedSource source() {
            return new Buffer();
        }
    };

    /**
     * 缓存文件最大限制大小20M
     */
    private final long cacheSize = 1024 * 1024 * 20;
    /**
     *
     */
    private RequestCache cache;

    @Override
    public Response intercept(Chain chain) throws IOException {
        if(cache == null){
            String cacheDir = null;
            if(HttpUtil.getInstance().getHttpInterceptor() != null){
                cacheDir = HttpUtil.getInstance().getHttpInterceptor().getCacheDir();
            }
            if(!TextUtils.isEmpty(cacheDir)){
                cache = new RequestCache(new File(cacheDir), cacheSize);
            }
        }
        Request request = chain.request();

        if(cache!=null){
            String isReadCacheStr = request.header("isReadCache");
            boolean isReadCache = parseBoolean(isReadCacheStr);
            if(isReadCache){    //直接读取缓存
                Response cacheResponse = null;
                try {
                    cacheResponse = cache.getCacheResponse(chain.request());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(cacheResponse == null){
                    return new Response.Builder()
                            .request(chain.request().newBuilder().build())
                            .protocol(Protocol.HTTP_1_1)
                            .code(200)
                            .message("Unsatisfiable Request (only-if-cached)")
                            .body(EMPTY_BODY)
                            .sentRequestAtMillis(-1L)
                            .receivedResponseAtMillis(System.currentTimeMillis())
                            .addHeader("isFromCache", "true")
                            .build();
                }
                return cacheResponse.newBuilder().addHeader("isFromCache", "true").build();
            }
        }

        Response networkResponse = chain.proceed(request.newBuilder().build());

        if(cache!=null){
            //需要缓存
            String isCacheStr = request.header("isCache");
            boolean isCache = parseBoolean(isCacheStr);
            if(isCache){    //需要缓存  更新 response
                try {
                    return cache.putCacheResponse(networkResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return networkResponse;
    }

    /**
     * 转 boolean
     * @param booleanValue
     * @return
     */
    private boolean parseBoolean(String booleanValue){
        try {
            return Boolean.parseBoolean(booleanValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static Response stripBody(Response response) {
        return response != null && response.body() != null
                ? response.newBuilder().body(null).build()
                : response;
    }

    private static Headers combine(Headers cachedHeaders, Headers networkHeaders) {
        Headers.Builder result = new Headers.Builder();

        for (int i = 0, size = cachedHeaders.size(); i < size; i++) {
            String fieldName = cachedHeaders.name(i);
            String value = cachedHeaders.value(i);
            if ("Warning".equalsIgnoreCase(fieldName) && value.startsWith("1")) {
                continue; // Drop 100-level freshness warnings.
            }
            if (!isEndToEnd(fieldName) || networkHeaders.get(fieldName) == null) {
                Internal.instance.addLenient(result, fieldName, value);
            }
        }

        for (int i = 0, size = networkHeaders.size(); i < size; i++) {
            String fieldName = networkHeaders.name(i);
            if ("Content-Length".equalsIgnoreCase(fieldName)) {
                continue; // Ignore content-length headers of validating responses.
            }
            if (isEndToEnd(fieldName)) {
                Internal.instance.addLenient(result, fieldName, networkHeaders.value(i));
            }
        }

        return result.build();
    }

    /**
     * Returns true if {@code fieldName} is an end-to-end HTTP header, as defined by RFC 2616,
     * 13.5.1.
     */
    static boolean isEndToEnd(String fieldName) {
        return !"Connection".equalsIgnoreCase(fieldName)
                && !"Keep-Alive".equalsIgnoreCase(fieldName)
                && !"Proxy-Authenticate".equalsIgnoreCase(fieldName)
                && !"Proxy-Authorization".equalsIgnoreCase(fieldName)
                && !"TE".equalsIgnoreCase(fieldName)
                && !"Trailers".equalsIgnoreCase(fieldName)
                && !"Transfer-Encoding".equalsIgnoreCase(fieldName)
                && !"Upgrade".equalsIgnoreCase(fieldName);
    }

}
