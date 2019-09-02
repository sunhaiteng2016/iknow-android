package com.beyond.popscience.frame.net.base;


import android.os.Build;
import android.util.Base64;
import android.util.Log;

import com.beyond.library.network.net.httpclient.BaseRestUsage;
import com.beyond.library.network.net.httpclient.NewCustomResponseHandler;
import com.beyond.library.network.net.httpclient.httputil.HttpInterceptor;
import com.beyond.library.network.net.httpclient.httputil.SimpleHttpInterceptor;
import com.beyond.popscience.BuildConfig;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.thirdsdk.ThirdSDKManager;
import com.beyond.popscience.frame.util.FileUtil;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.frame.util.Util;
import com.beyond.popscience.frame.util.VKConstants;
import com.beyond.popscience.utils.SystemUtil;
import com.google.gson.Gson;
import com.helper.utils.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by linjinfa on 2017/5/19.
 * email 331710168@qq.com
 */
public class AppBaseRestUsageV1 extends BaseRestUsage {

    protected Gson gson = new Gson();

    @Override
    protected HttpInterceptor getHttpInterceptor() {

        return new SimpleHttpInterceptor() {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headerMap = new HashMap<>();
                headerMap.put("baseParams", UserInfoUtil.getInstance().getUserInfo().getToken());
                String romType = SystemUtil.getDeviceBrand();
                String id = "";
                if (romType.contains("Xiaomi") || romType.contains("xiaomi")) {
                    id = BeyondApplication.xiaomiCid;
                } else if (romType.contains("HUAWEI") || romType.contains("Huawei") || romType.contains("huawei")) {
                    id = BeyondApplication.HuaWeiCid;
                } else if (romType.contains("Oppo") || romType.contains("oppo") || romType.contains("OPPO")) {
                    id = BeyondApplication.OPPOCid;
                } else if (romType.contains("vivo")||romType.contains("Vivo")||romType.contains("ViVo")){
                    id = BeyondApplication.VivoCid;
                }else{
                    id=ThirdSDKManager.getInstance().getGetuiDeviceid();
                }
                headerMap.put("cid", id);
                headerMap.put("Content-Type", "multipart/form-data");

                Map<String, String> systemMap = new HashMap<>();
                systemMap.put("appIdentifier", BeyondApplication.getInstance().getPackageName());
                systemMap.put("application", "ANDROID");
                systemMap.put("appVersion", Util.getVersionName(BeyondApplication.getInstance()));
                systemMap.put("systemVersion", Build.VERSION.RELEASE);
                systemMap.put("hardware", Build.ID);
//                systemMap.put("cid", ThirdSDKManager.getInstance().getGetuiDeviceid());
                String systemJson = null;
                try {
                    systemJson = gson.toJson(systemMap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (systemJson != null) {
                    headerMap.put("system", new String(Base64.encode(systemJson.getBytes(), Base64.NO_WRAP)));
                }

                Log.e("======请求头信息======", headerMap.size() + "=====" + headerMap.toString());
                return headerMap;
            }

            @Override
            public String getCacheDir() {
                return FileUtil.getDirectory(VKConstants.CACHE_DATA).getAbsolutePath();
            }

            @Override
            public String getBaseUrl() {
                return BuildConfig.BASE_URL;
            }
        };
    }

    /**
     * post请求
     *
     * @param relativeUrl
     * @param object
     * @param responseHandler
     */
    private void requestPost(String relativeUrl, HashMap<String, String> object, boolean isCache, NewCustomResponseHandler responseHandler) {
        if (responseHandler != null) {
            responseHandler.setIdentification(getIdentification());
        }
        super.postFrom(BeyondApplication.getInstance(), relativeUrl, object, isCache, responseHandler);
    }

    /**
     * get请求
     *
     * @param relativeUrl
     * @param responseHandler
     */
    private void requestGet(String relativeUrl, Map<String, String> paramMap, boolean isCache, NewCustomResponseHandler responseHandler) {
        if (responseHandler != null) {
            responseHandler.setIdentification(getIdentification());
        }
        super.get(relativeUrl, paramMap, isCache, responseHandler);
    }

    /**
     * post请求 没缓存
     *
     * @param relativeUrl
     * @param object
     * @param responseHandler
     */
    public void post(String relativeUrl, HashMap object, NewCustomResponseHandler responseHandler) {
        requestPost(relativeUrl, object, false, responseHandler);
    }

    /**
     * post请求 有缓存
     *
     * @param relativeUrl
     * @param object
     * @param responseHandler
     */
    public void postCache(String relativeUrl, HashMap object, NewCustomResponseHandler responseHandler) {
        requestPost(relativeUrl, object, true, responseHandler);
    }

    /**
     * get请求 没缓存
     *
     * @param relativeUrl
     * @param responseHandler
     */
    public void get(String relativeUrl, Map<String, String> paramMap, NewCustomResponseHandler responseHandler) {
        requestGet(relativeUrl, paramMap, false, responseHandler);
    }

    /**
     * get请求 有缓存
     *
     * @param relativeUrl
     * @param responseHandler
     */
    public void getCache(String relativeUrl, Map<String, String> paramMap, NewCustomResponseHandler responseHandler) {
        requestGet(relativeUrl, paramMap, true, responseHandler);
    }

    /**
     * get请求 有缓存
     *
     * @param relativeUrl
     * @param responseHandler
     */
    public void getCacheTwo(String relativeUrl, HashMap<String, String> paramMap, NewCustomResponseHandler responseHandler) {
        requestPost(relativeUrl, paramMap, false, responseHandler);
    }
}
