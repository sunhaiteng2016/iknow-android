package com.beyond.library.network.net.httpclient.httputil;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.beyond.library.network.net.httpclient.CustomResponseHandler;
import com.beyond.library.network.net.httpclient.cache.RequestCacheInterceptor;
import com.beyond.library.util.L;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.ConnectionPool;
import okhttp3.CookieJar;
import okhttp3.Dispatcher;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 *
 */
public class HttpUtil {

    /**
     * 默认超时时间(毫秒)
     */
    private int DEFAULT_SOCKET_TIMEOUT = 20 * 1000;
    /**
     * 上传和下载超时时间(毫秒)
     */
    private final int UD_SOCKET_TIMEOUT = 30 * 1000;

    private volatile static HttpUtil mInstance;

    private static OkHttpClient mOkHttpClient = null;
    /**
     * 超时不管什么环境都是30 S,故用上传下载专用clicent
     */
    private static OkHttpClient mDownOrUpLoadClient = null;
    /**
     *
     */
    private HttpInterceptor httpInterceptor;
    /**
     *
     */
    private boolean isDebug;
    /**
     * Key
     */
    public static final String KEY = "yunjie2514572541463841s1a4d";

    private HttpUtil() {
        init();
    }

    /**
     *
     */
    private void init() {
        DEFAULT_SOCKET_TIMEOUT *= (isDebug ? 1 : 1);
        Dispatcher mDispatcher = new Dispatcher();
        mDispatcher.setMaxRequestsPerHost(30);
        mOkHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(false)
                .cache(null)
                .cookieJar(CookieJar.NO_COOKIES)
                .connectTimeout(DEFAULT_SOCKET_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(DEFAULT_SOCKET_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(DEFAULT_SOCKET_TIMEOUT, TimeUnit.MILLISECONDS)
                .connectionPool(new ConnectionPool(20, 5, TimeUnit.MINUTES))
                .dispatcher(mDispatcher)
                .addInterceptor(new RequestCacheInterceptor())
                .build();

        Dispatcher mDownOrUploadDispatcher = new Dispatcher();
        mDispatcher.setMaxRequestsPerHost(30);
        mDownOrUpLoadClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(false)
                .cache(null)
                .cookieJar(CookieJar.NO_COOKIES)
                .connectTimeout(UD_SOCKET_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(UD_SOCKET_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(UD_SOCKET_TIMEOUT, TimeUnit.MILLISECONDS)
                .connectionPool(new ConnectionPool(20, 5, TimeUnit.MINUTES))
                .dispatcher(mDownOrUploadDispatcher)
                .build();
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public static HttpUtil getInstance() {
        if (mInstance == null) {
            synchronized (HttpUtil.class) {
                if (mInstance == null) {
                    mInstance = new HttpUtil();
                }
            }
        }
        return mInstance;
    }

    /**
     * @param builder
     */
    private void setHeaders(Request.Builder builder) {
        if (builder == null) return;

        if (httpInterceptor != null) {
            Map<String, String> headerMap = httpInterceptor.getHeaders();
            if (headerMap != null) {
                for (String key : headerMap.keySet()) {
                    String value = headerMap.get(key);
                    if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                        builder.header(key, value);
                    }
                }
            }
        }
    }

    /**
     * @param relativeUrl
     * @param isCache
     * @param responseHandler
     */
    public void get(String relativeUrl, boolean isCache, CustomResponseHandler responseHandler) {
        if (responseHandler == null) {
            responseHandler = new CustomResponseHandler();
        }
        Request.Builder builder = new Request.Builder();
        setHeaders(builder);

        String absoluteUrl = getAbsoluteUrl(relativeUrl);
        L.v("absoluteUrl=====>  " + absoluteUrl);

        responseHandler.onStart();
        Request request = builder.get()
                .url(absoluteUrl)
                .cacheControl(CacheControl.FORCE_NETWORK)
                .addHeader("Connection", "close")
                .addHeader("isCache", String.valueOf(isCache))
                .build();

        if (isCache) {    //使用缓存  请求两次 第一次强制获取cache数据 第二次正常请求网络
            request = request.newBuilder().addHeader("isReadCache", "true").build();
            Call call = mOkHttpClient.newCall(request);
            call.enqueue(responseHandler);

            request = request.newBuilder().removeHeader("isReadCache").build();
            call = mOkHttpClient.newCall(request);
            call.enqueue(responseHandler);
        } else {
            Call call = mOkHttpClient.newCall(request);
            call.enqueue(responseHandler);
        }
    }

    /**
     * @param relativeUrl
     * @param isCache     是否需要缓存
     */
    public void post(String relativeUrl,
                     String paramJson,
                     boolean isCache,
                     final CustomResponseHandler responseHandler) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), paramJson);
        Request.Builder builder = new Request.Builder();
        setHeaders(builder);

        String absoluteUrl = getAbsoluteUrl(relativeUrl);
        L.v("absoluteUrl=====>  " + absoluteUrl);
        responseHandler.onStart();
        Request request = builder.post(requestBody)
                .url(absoluteUrl)
                .cacheControl(CacheControl.FORCE_NETWORK)
                .addHeader("Connection", "close")
                .addHeader("isCache", String.valueOf(isCache))
                .build();

        if (isCache) {    //使用缓存  请求两次 第一次强制获取cache数据 第二次正常请求网络
            request = request.newBuilder().addHeader("isReadCache", "true").build();
            Call call = mOkHttpClient.newCall(request);
            call.enqueue(responseHandler);

            request = request.newBuilder().removeHeader("isReadCache").build();
            call = mOkHttpClient.newCall(request);
            call.enqueue(responseHandler);
        } else {
            Call call = mOkHttpClient.newCall(request);
            call.enqueue(responseHandler);
        }
    }

    /**
     * @param relativeUrl
     * @param isCache     是否需要缓存
     */
    public void postFrom(String relativeUrl,
                         HashMap<String, String> paramJson,
                         boolean isCache,
                         final CustomResponseHandler responseHandler) {

        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        for (Map.Entry<String, String> entry : paramJson.entrySet()) {
            requestBodyBuilder.addFormDataPart(entry.getKey(), entry.getValue());
        }
        RequestBody requestBody = requestBodyBuilder.build();
        // RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), paramJson);
        Request.Builder builder = new Request.Builder();
        setHeaders(builder);
        String absoluteUrl = getAbsoluteUrl(relativeUrl);
        L.v("absoluteUrl=====>  " + absoluteUrl);
        responseHandler.onStart();
        Request request = builder.post(requestBody)
                .url(absoluteUrl)
                .cacheControl(CacheControl.FORCE_NETWORK)
                .addHeader("Connection", "close")
                .addHeader("isCache", String.valueOf(isCache))
                .build();

        if (isCache) {    //使用缓存  请求两次 第一次强制获取cache数据 第二次正常请求网络
            request = request.newBuilder().addHeader("isReadCache", "true").build();
            Call call = mOkHttpClient.newCall(request);
            call.enqueue(responseHandler);

            request = request.newBuilder().removeHeader("isReadCache").build();
            call = mOkHttpClient.newCall(request);
            call.enqueue(responseHandler);
        } else {
            Call call = mOkHttpClient.newCall(request);
            call.enqueue(responseHandler);
        }
    }

    public void getSync(String relativeUrl, final CustomResponseHandler responseHandler) {
        Request.Builder builder = new Request.Builder();
        String absolute_url;
        absolute_url = getAbsoluteUrl(relativeUrl);
        setHeaders(builder);
        responseHandler.onStart();
        Request request = builder.get()
                .url(absolute_url)
                .cacheControl(CacheControl.FORCE_NETWORK)
                .build();
        Call call = null;
        responseHandler.setIsSync(true);
        try {
            call = mOkHttpClient.newCall(request);
            Response response = call.execute();
            if (responseHandler != null) {
                responseHandler.onResponse(call, response);
            }
        } catch (IOException e) {
            responseHandler.onFailure(call, e);
            e.printStackTrace();
        }
    }


    /**
     * @param relativeUrl
     * @param responseHandler
     */
    public void postSync(String relativeUrl,
                         String paramJson,
                         final CustomResponseHandler responseHandler) {
        String absolute_url;
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), paramJson);
        Request.Builder builder = new Request.Builder();
        absolute_url = getAbsoluteUrl(relativeUrl);
        setHeaders(builder);
        L.v("absolute_url=====>  " + absolute_url);
        responseHandler.onStart();
        Request request = builder.post(requestBody)
                .url(absolute_url)
                .cacheControl(CacheControl.FORCE_NETWORK)
                .build();
        Call call = null;
        responseHandler.setIsSync(true);
        try {
            call = mOkHttpClient.newCall(request);
            Response response = call.execute();
            if (responseHandler != null) {
                responseHandler.onResponse(call, response);
            }
        } catch (IOException e) {
            responseHandler.onFailure(call, e);
            e.printStackTrace();
        }
    }


    public void download(String url, final CustomResponseHandler httpResponseHandler) {
        Request.Builder builder = new Request.Builder();
        setHeaders(builder);
        httpResponseHandler.onStart();
        final Request request = builder
                .url(url)
                .cacheControl(CacheControl.FORCE_NETWORK)
                .build();
        /**
         * 超时不管什么环境都是30 S,故用下载专用clicent
         */
        Call call = mDownOrUpLoadClient.newCall(request);
        call.enqueue(httpResponseHandler);
    }

    public void downloadSync(String url, final CustomResponseHandler httpResponseHandler) {
        Request.Builder builder = new Request.Builder();
        setHeaders(builder);
        httpResponseHandler.onStart();
        final Request request = builder
                .url(url)
                .cacheControl(CacheControl.FORCE_NETWORK)
                .build();
        /**
         * 超时不管什么环境都是30 S,故用下载专用clicent
         */
        Call call = mDownOrUpLoadClient.newCall(request);
        try {
            Response response = call.execute();
            httpResponseHandler.setIsSync(true);
            httpResponseHandler.onResponse(call, response);
        } catch (IOException e) {
            e.printStackTrace();
            httpResponseHandler.onFailure(call, e);
        }
    }


    public void upload(String relativeUrl, String fileKey, List<File> files, Map<String, String> params, CustomResponseHandler responseHandler) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                    builder.addFormDataPart(key, value);
                }
            }
        }

        if (files != null && files.size() > 0) {
            int fileIndex = 0;
            boolean isAppendIndex = false;
            if (files.size() > 1) {
                isAppendIndex = true;
            }
            for (File file : files) {
                String key = fileKey;
                if (isAppendIndex) {
                    key = key + "[" + fileIndex + "]";
                }
                RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
                builder.addFormDataPart(key, file.getName(), fileBody);
                fileIndex++;
            }
        }

        String absolute_url = getAbsoluteUrl(relativeUrl);
        L.v("absolute_url=====>  " + absolute_url);
        responseHandler.onStart();
        Request request = new Request.Builder()
                .url(absolute_url)
                .post(builder.build())
                .build();
        /**
         * 超时不管什么环境都是30 S
         */

        Call call = mDownOrUpLoadClient.newCall(request);
        call.enqueue(responseHandler);
    }

    /**
     * @param relativeUrl
     * @return
     */
    private String getAbsoluteUrl(String relativeUrl) {
        String url = relativeUrl;
        if (relativeUrl.startsWith("http") && relativeUrl.contains("_sign")) {
            url = relativeUrl.substring(0, relativeUrl.indexOf("_sign") - 1);
        } else if (relativeUrl.startsWith("http")) {
            return url;
        } else {
            if (httpInterceptor != null) {
                url = httpInterceptor.getBaseUrl() + relativeUrl;
            }
        }
        return url;
    }


    private File getTemporaryFile(Context context) {
        asserts(context != null, "Tried creating temporary file without having Context");
        try {
            // not effective in release mode
            assert context != null;
            return File.createTempFile("temp_", "_handled", context.getCacheDir());
        } catch (IOException e) {
            Log.e("FileException", "Cannot create temporary file", e);
        }
        return null;
    }

    private void asserts(final boolean expression, final String failedMessage) {
        if (!expression) {
            throw new AssertionError(failedMessage);
        }
    }

    /**
     * @param httpInterceptor
     */
    public HttpUtil setHttpInterceptor(HttpInterceptor httpInterceptor) {
        this.httpInterceptor = httpInterceptor;
        return this;
    }

    /**
     * @return
     */
    public HttpInterceptor getHttpInterceptor() {
        return httpInterceptor;
    }

    /**
     * @param isDebug
     * @return
     */
    public HttpUtil setDebug(boolean isDebug) {
        if (this.isDebug != isDebug) {
            this.isDebug = isDebug;
            init();
        }
        return this;
    }

    public boolean getIsDebug() {
        return isDebug;
    }

}