package com.beyond.library.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.util.List;

/**
 * 打电话、发短信
 * Created by linjinfa 331710168@qq.com on 2015/1/28.
 */
public class PhoneUtil {

    /**
     * 跳转到打电话界面
     * @param context
     * @param phoneNum
     */
    public static void callPhoneDial(Context context, String phoneNum){
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNum));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 直接打电话
     * @param context
     * @param phoneNum
     */
    @SuppressLint("MissingPermission")
    public static void callPhone(Context context, String phoneNum){
        try {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送短信
     * @param context
     * @param phoneNum
     */
    public static boolean sendSms(Context context, String phoneNum){
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("smsto:"+phoneNum));
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 发送短信
     * @param context
     * @param phoneNum
     */
    public static boolean sendSms(Context context, String phoneNum, String message){
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("smsto:" + phoneNum));
            intent.putExtra("sms_body", message);
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 群发短信
     * @param context
     */
    public static boolean sendSmsMutilPhone(Context context, List<String> phoneNumList, String message){
        if(phoneNumList == null || phoneNumList.size()==0){
            return false;
        }

        StringBuilder sb = new StringBuilder();
        int count = phoneNumList.size();
        for(int i = 0; i < count; i++){
            sb.append(phoneNumList.get(i));
            if(i != count-1){
                sb.append(";");
            }
        }
        boolean result = sendSms(context, sb.toString(), message);
        return result;
    }
}
