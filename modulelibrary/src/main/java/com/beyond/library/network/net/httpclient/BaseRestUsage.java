package com.beyond.library.network.net.httpclient;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.beyond.library.network.net.bean.BaseRequest;
import com.beyond.library.network.net.bean.model.Application;
import com.beyond.library.network.net.bean.model.Session;
import com.beyond.library.network.net.bean.model.SystemParam;
import com.beyond.library.network.net.httpclient.httputil.HttpInterceptor;
import com.beyond.library.network.net.httpclient.httputil.HttpUtil;
import com.beyond.library.util.L;
import com.beyond.library.util.MD5Util;
import com.google.gson.Gson;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 以后的RestUsage都不用静态方法，通过new对象的方式，并且走父类的构造
 * <p>
 * Created by East.K on 2016/12/18.
 */

public class BaseRestUsage {

    protected final String KEY_PAGENAME = "pageName";
    /**
     *
     */
    protected Gson gson = new Gson();

    private String identification;

    public BaseRestUsage() {
        HttpUtil.getInstance().setHttpInterceptor(getHttpInterceptor());
        HttpUtil.getInstance().setDebug(isDebug());
    }

    /**
     * @param context
     * @param relativeUrl
     * @param object
     * @param responseHandler
     */
    public void post(Context context, String relativeUrl, Object object, boolean isCache, CustomResponseHandler responseHandler) {
//        String parmJson = appendTokenParam(object, context);
        String paramJson = "";
        if (object != null) {
            try {
                paramJson = gson.toJson(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        L.v("paramJson======> " + paramJson);
        HttpUtil.getInstance().post(relativeUrl, paramJson, isCache, responseHandler);
    }

    public void postFrom(Context context, String relativeUrl, HashMap<String, String> object, boolean isCache, CustomResponseHandler responseHandler) {
//        String parmJson = appendTokenParam(object, context);
       /* String paramJson = "";
        if(object!=null){
            try {
                paramJson = gson.toJson(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
        //L.v("paramJson======> "+paramJson);
        HttpUtil.getInstance().postFrom(relativeUrl, object, isCache, responseHandler);
    }

    public void download(String url, CustomResponseHandler httpResponseHandler) {
        HttpUtil.getInstance().download(url, httpResponseHandler);
    }

    public void downloadSync(String url, CustomResponseHandler httpResponseHandler) {
        HttpUtil.getInstance().downloadSync(url, httpResponseHandler);
    }

    /**
     * @param relativeUrl
     * @param responseHandler
     */
    public void getSync(String relativeUrl, CustomResponseHandler responseHandler) {
        HttpUtil.getInstance().getSync(relativeUrl, responseHandler);
    }

    /**
     * @param relativeUrl
     * @param responseHandler
     */
    public void get(String relativeUrl, Map<String, String> paramMap, boolean isCache, CustomResponseHandler responseHandler) {
        relativeUrl = appendUrlParam(relativeUrl, paramMap);
        HttpUtil.getInstance().get(relativeUrl, isCache, responseHandler);
    }

    /**
     * 追加Url参数
     *
     * @param url
     * @return
     */
    private String appendUrlParam(String url, Map<String, String> paramMap) {
        if (paramMap == null || paramMap.size() == 0) {
            return url;
        }
        StringBuffer sbUrl = new StringBuffer(url + (url.indexOf("?") > 0 ? "&" : "?"));
        Iterator<?> keys = paramMap.keySet().iterator();
        String keyStr;
        Object valueObj;
        int len = -1;
        while (keys.hasNext()) {
            len++;
            keyStr = keys.next().toString();
            valueObj = paramMap.get(keyStr);
            sbUrl.append(keyStr);
            sbUrl.append("=");
            sbUrl.append(valueObj);
            if (len != paramMap.size() - 1) {
                sbUrl.append("&");
            }
        }
        L.v("appendUrlParam =======> " + sbUrl.toString());
        return sbUrl.toString();
    }

    /**
     * @param context
     * @param relativeUrl
     * @param responseHandler
     */
    public void postSync(Context context,
                         String relativeUrl,
                         Object object,
                         CustomResponseHandler responseHandler) {
        String parmJson = appendTokenParam(object, context);
        HttpUtil.getInstance().postSync(relativeUrl, parmJson, responseHandler);
    }

    /**
     * @param relativeUrl
     * @param requestParams
     * @param responseHandler
     */
    public void upload(String relativeUrl,
                       String fileKey,
                       List<File> files,
                       Map<String, String> requestParams,
                       CustomResponseHandler responseHandler) {
        HttpUtil.getInstance().upload(relativeUrl, fileKey, files, requestParams, responseHandler);
    }

    /**
     * 追加token,_wid_参数
     */
    private String appendTokenParam(Object object, Context context) {
        BaseRequest<Object> request = new BaseRequest<>();
        // data
        request.setData(object);

        // Session
        String pageName = null;
        HttpInterceptor httpInterceptor = getHttpInterceptor();
        Session session = new Session();
        if (httpInterceptor != null) {
            if (httpInterceptor.getHeaders() != null) {
                pageName = httpInterceptor.getHeaders().get(KEY_PAGENAME);
            }
            session.setToken(httpInterceptor.getToken());
            try {
                session.setWid(Long.parseLong(httpInterceptor.getWid()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        request.setSession(session);

        // SystemParam
        SystemParam systemParam = new SystemParam();
        systemParam.setApplication(Application.ANDROID);
        systemParam.setAppVersion(getVersionName(context));
        systemParam.setPageName(pageName);
        systemParam.setSystemVersion(Build.VERSION.RELEASE);
        systemParam.setHardware(Build.DEVICE);
        request.setSystem(systemParam);

        // sign
//        request.setSign(getSignStr(request.toJson()));
        String requestJson = gson.toJson(request);
        L.v("======param json:   >  " + requestJson);
        return requestJson;
    }

    /**
     * 获取 APP 的版本名称
     *
     * @param context
     * @return
     */
    private String getVersionName(Context context) {
        String version = "";
        if (context == null) return version;
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }


    private String getTimeFormat() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        return timeFormat.format(Calendar.getInstance().getTime());
    }

    /**
     * 获取sign
     */
    private String getSignStr(String paramJson) {
        return MD5Util.md5(paramJson + HttpUtil.KEY).toUpperCase();
    }

    /**
     * @return
     */
    protected HttpInterceptor getHttpInterceptor() {
        return null;
    }

    /**
     * @return
     */
    protected boolean isDebug() {
        return false;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }
}
