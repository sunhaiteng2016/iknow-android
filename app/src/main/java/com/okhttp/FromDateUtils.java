package com.okhttp;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FromDateUtils {
    public static void FromPost(final String url, final Map<String, String> params, MyCallBackSS myCallBackSS) {
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();
            MediaType MutilPart_Form_Data = MediaType.parse("multipart/form-data; charset=utf-8");
            RequestBody bodyParams = RequestBody.create(MutilPart_Form_Data, JSON.toJSONString(params));
            MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("userid", "", bodyParams);
            RequestBody requestBody = requestBodyBuilder.build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            Response response = client.newCall(request).execute();
            myCallBackSS.onSuccess(response.body().toString());
        } catch (IOException e) {
            e.printStackTrace();
            myCallBackSS.onFiled();
        }
    }

    public interface MyCallBackSS {
        void onSuccess(String ss);

        void onFiled();
    }
}
