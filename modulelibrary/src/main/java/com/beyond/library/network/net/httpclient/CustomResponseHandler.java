package com.beyond.library.network.net.httpclient;

import android.app.Activity;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.beyond.library.network.enity.MSG;
import com.beyond.library.network.net.httpclient.httputil.HttpUtil;
import com.beyond.library.network.task.UIOnMainThread;
import com.beyond.library.util.L;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by East.K on 2016/9/5.  For OkHttp
 */

public class CustomResponseHandler implements Callback {


    /**
     * 封店Code
     */
    protected final String ERROR_CLOSED_SHOP_CODE = "103404";
    protected boolean isShowingExit = false;
    /**
     * 是否显示了封店提示Dialog
     */
    private static boolean isShowingClosedShopTip = false;
    protected Activity activity;
    private URI requestUri;
    /**
     * 是否同步请求
     */
    protected boolean isSync;
    /**
     * 是否显示正在加载
     */
    protected boolean isShowLoading = true;
    private Object targetObject = null;
    /**
     *
     */
    private Handler mainHandler;

    /**
     * 成功
     *
     * @param httpStatusCode
     * @param headerMap
     * @param responseStr
     */
    public void onSuccess(int httpStatusCode, Map<String, List<String>> headerMap, String responseStr) {

    }

    public void onSuccess(int httpStatusCode, Map<String, List<String>> headerMap, String responseStr, Object targetObject) {

    }

    public void onSuccess(int httpStatusCode, Map<String, List<String>> headerMap, File file) {

    }

    public void onSuccess(int httpStatusCode, Map<String, List<String>> headerMap, byte[] responseBody) {

    }

    public void onFailure(int statusCode, Map<String, List<String>> headerMap, String responseString, Throwable throwable) {

    }

    public void onFailure(int statusCode, Map<String, List<String>> headerMap, String responseString, Throwable throwable, Object targetObject) {

    }

    public void onStart() {
        if (activity != null && isShowLoading && !activity.isFinishing()){

//            D.showProgress(activity, activity.getString(R.string.chulizhong));
        }
    }

    public void onFinish() {
//        D.dismissProgress();
    }

    public void setIsSync(boolean isSync) {
        this.isSync = isSync;
    }

    public Object getTargetObject() {
        return targetObject;
    }

    public CustomResponseHandler setTargetObject(Object targetObj) {
        this.targetObject = targetObj;
        return this;
    }

    /**
     * 处理异常信息
     *
     * @return
     */
    protected MSG handlerError(int statusCode, String responseString, JSONObject errorResponse, Throwable throwable) {
        L.v("throwable==========>  statusCode:  " + statusCode + "  throwable:  " + throwable + "  Url:" + getRequestURI() + "  Response:" + responseString);
        String msg = null;

        if (statusCode == 404 || statusCode == 500) {
            msg = "服务端错误";
        } else if (!TextUtils.isEmpty(responseString)) {
            msg = responseString;
        } else if (errorResponse != null && !TextUtils.isEmpty(errorResponse.toString())) {
            msg = errorResponse.toString();
        } else if (throwable != null) {
            if (throwable instanceof JSONException) {
                msg = "数据解析异常";
            } else if (throwable instanceof SocketTimeoutException || throwable instanceof org.apache.http.conn.ConnectTimeoutException) {
                msg = "服务链接超时";
            } else if (throwable instanceof UnknownHostException) {
                msg = "网络出小差了，请检查网络连接";
            } else if (!TextUtils.isEmpty(throwable.getMessage())) {
                if (throwable.getMessage()
                        .toLowerCase()
                        .contains("UnknownHostException".toLowerCase())) {    //服务器连接异常
                    msg = "网络出小差了，请检查网络连接";
                } else if (throwable.getMessage().toLowerCase().contains("timed out")) {   //服务链接超时
                    msg = "服务链接超时";
                }
            }
        } else {
            msg = "未知异常";
        }

        if (HttpUtil.getInstance().getIsDebug()) {
            msg = null;
        }
        return new MSG(false, false, msg);
    }



    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setRequestURI(URI uri) {
        requestUri = uri;
    }

    public URI getRequestURI() {
        return requestUri;
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        onFinish();
        if (isSync) {
            onFailure(-1, null, null, e);
            onFailure(-1, null, null, e, targetObject);
        } else {
            UIOnMainThread.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onFailure(-1, null, null, e);
                    onFailure(-1, null, null, e, targetObject);
                }
            });
        }
    }

    @Override
    public void onResponse(Call call, final Response response) {
        onFinish();
        byte[] responseByte = null;
        try {
            responseByte = response.body().bytes();
        } catch (Exception e) {
            e.printStackTrace();
            onFailure(null, new IOException(e));
            return;
        }
        if (isSync) {
            // 同步请求，直接回调
            responseStright(response, responseByte);
        } else {
            final byte[] finalResponseByte = responseByte;
            UIOnMainThread.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    responseStright(response, finalResponseByte);
                }
            });
        }

    }

    private void responseStright(Response response, byte[] finalResponseByte) {
        String finalResponseStr = new String(finalResponseByte);
        if (response.code() == 200) {
            Log.e("json---->",finalResponseStr);
            onSuccess(response.code(), response.headers().toMultimap(), finalResponseByte);
            onSuccess(response.code(), response.headers().toMultimap(), finalResponseStr);
            onSuccess(response.code(), response.headers().toMultimap(), finalResponseStr, targetObject);
        } else {
            onFailure(response.code(), response.headers()
                    .toMultimap(), finalResponseStr, null);
            onFailure(response.code(), response.headers()
                    .toMultimap(), finalResponseStr, null, targetObject);
        }
    }

}