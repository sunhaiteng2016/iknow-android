package com.beyond.popscience.frame.thirdsdk;

import android.content.Context;
import android.support.annotation.NonNull;

import com.beyond.popscience.frame.thirdsdk.baidu.TTSSdk;

/**
 * 第三方管理
 * Created by linjinfa on 2017/5/5.
 * email 331710168@qq.com
 */
public class ThirdSDKManagerV2 {

    private TTSSdk ttsSdk;

    private ThirdSDKManagerV2() {
    }

    /**
     * 单例
     *
     * @return
     */
    public static ThirdSDKManagerV2 getInstance() {
        return ThirdSDKManagerV2.Singleton.INSTANCE.getInstance();
    }

    /**
     * 初始化语音合成
     */
    public void initTTSVoice(Context context){
        if(ttsSdk == null){
            ttsSdk = new TTSSdk();
            ttsSdk.init(context);
        }
    }

    /**
     * 初始化语音合成
     */
    public void destroyTTSVoice(){
        if(ttsSdk != null){
            ttsSdk.destroy();
        }
        ttsSdk = null;
    }

    /**
     *
     * @param txt
     */
    public void ttsSpeak(String txt){
        if(ttsSdk != null){
            ttsSdk.speak(txt);
        }
    }

    /**
     *
     */
    public void ttsStop(){
        if(ttsSdk != null){
            ttsSdk.stop();
        }
    }

    /**
     *
     * @return
     */
    public boolean isTTSStart(){
        if(ttsSdk!=null){
            return ttsSdk.isStart();
        }
        return false;
    }

    /**
     * 是否已经初始化过
     * @return
     */
    public boolean isTTSSInited(){
        return ttsSdk != null;
    }

    /**
     * 授权
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }

    /**
     * 单例
     */
    private enum Singleton {
        INSTANCE;

        private ThirdSDKManagerV2 singleton;

        Singleton() {
            singleton = new ThirdSDKManagerV2();
        }

        public ThirdSDKManagerV2 getInstance() {
            return singleton;
        }
    }

}
