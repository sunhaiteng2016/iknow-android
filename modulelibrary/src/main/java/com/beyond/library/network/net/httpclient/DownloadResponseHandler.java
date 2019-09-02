package com.beyond.library.network.net.httpclient;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * 下载专用ResponseHandler
 * Created by East.K on 2016/11/30.
 */

public class DownloadResponseHandler extends CustomResponseHandler {

    /**
     * 文件下载的目的路径，默认是缓存目录
     */
    protected String destPath;
    /**
     *
     */
    protected Object targetObj;

    protected Context context;
    public DownloadResponseHandler(Context context) {
        this.context = context;
        isShowLoading = false;
    }

    public DownloadResponseHandler(Context context, String destPath) {
        this.context = context;
        isShowLoading = false;
        this.destPath = destPath;
    }

    @Override
    public void onResponse(final Call call, final Response response) {
        responseStright(call, response);
    }

    public void onSuccess(int httpStatusCode, Map<String, List<String>> headerMap, File file, Object targetObj) {

    }

    @Override
    public void onFailure(Call call, IOException e) {
//        super.onFailure(call, e);
    }

    public DownloadResponseHandler setTargetObj(Object targetObj) {
        this.targetObj = targetObj;
        return this;
    }

    protected File getTemporaryFile(Context context) {
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

    private void responseStright(Call call, Response response) {
        try {
            if (response.code() == 200) {
                onFinish();
                File file = TextUtils.isEmpty(destPath) ? getTemporaryFile(context) : new File(destPath);
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    long total = response.body().contentLength();
                    Log.e(TAG, "total------>" + total);
                    long current = 0;
                    is = response.body().byteStream();
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        current += len;
                        fos.write(buf, 0, len);
                        Log.e(TAG, "current------>" + current);
                    }
                    fos.flush();
                    onSuccess(response.code(), response.headers()
                            .toMultimap(), file);
                    onSuccess(response.code(), response.headers()
                            .toMultimap(), file, targetObj);
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                    onFinish();
                    onFailure(response.code(), response.headers()
                            .toMultimap(), response.body().string(), e);
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, e.toString());
                    }
                }
            } else {
                onFinish();
                onFailure(response.code(), response.headers()
                        .toMultimap(), response.body().string(), null);
            }

        } catch (IOException e) {
            e.printStackTrace();
            onFailure(call, e);
        }
    }

}
