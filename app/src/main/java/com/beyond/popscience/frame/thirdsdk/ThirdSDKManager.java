package com.beyond.popscience.frame.thirdsdk;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.thirdsdk.gtpush.GeTuiPushSDK;
import com.beyond.popscience.frame.thirdsdk.location.ALocationSDK;
import com.beyond.popscience.frame.thirdsdk.oss.OssSDK;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 第三方管理
 * Created by linjinfa on 2017/5/5.
 * email 331710168@qq.com
 */
public class ThirdSDKManager {

    /**
     * 个推
     */
    private GeTuiPushSDK geTuiPushSDK;
    /**
     *
     */
    private Application application;

    private String getuiDeviceid;   //个推设备ID
    /**
     * 高德sdk
     */
    private ALocationSDK aLocationSDK;
    /**
     *
     */
    private HashMap<Context, OssSDK> cacheOssSDKHashMap =  new HashMap<>();

    private ThirdSDKManager() {
    }

    /**
     * 单例
     *
     * @return
     */
    public static ThirdSDKManager getInstance() {
        return ThirdSDKManager.Singleton.INSTANCE.getInstance();
    }

    /**
     * 初始化 在 Application 中
     *
     * @param application
     */
    public void init(Application application, boolean isDebug) {
        this.application = application;
    }

    /**
     * 初始化相关第三方sdk  在 Activity 中
     *
     * @param activity
     */
    public void init(Activity activity) {
        this.application = activity.getApplication();

        initPush(activity);
    }

    /**
     * 单独 初始化 push
     */
    private void initPush(Activity activity) {
        //个推
        geTuiPushSDK = new GeTuiPushSDK();
        geTuiPushSDK.init(activity);
    }

    /**
     * 开始定位
     */
    public void startLocation(Activity activity, ILocationListener locationListener){
        if(aLocationSDK == null){
            aLocationSDK = new ALocationSDK();
        }
        aLocationSDK.startLocation(activity, locationListener);
    }

    /**
     * 授权
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (geTuiPushSDK != null) {
            geTuiPushSDK.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * 授权
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(Activity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (aLocationSDK != null) {
            aLocationSDK.onRequestPermissionsResult(activity, requestCode, permissions, grantResults);
        }
    }

    public String getGetuiDeviceid() {
        return getuiDeviceid;
    }

    public void setGetuiDeviceid(String getuiDeviceid) {
        this.getuiDeviceid = getuiDeviceid;
    }

    /**
     * 上传图片
     * @param pathList
     */
    public void uploadImage(List<String> pathList, IUploadCallback iUploadCallback){
        new OssSDK(BeyondApplication.getInstance()).uploadImage(pathList, iUploadCallback);
    }

    /**
     * 上传图片
     * @param pathList
     */
    public void uploadImage(Context context, List<String> pathList, IUploadCallback iUploadCallback){
        OssSDK ossSDK = new OssSDK(BeyondApplication.getInstance());
        cacheOssSDKHashMap.put(context, ossSDK);

        ossSDK.uploadImage(pathList, iUploadCallback);
    }

    /**
     * 停止上传图片
     */
    public void stopUploadImage(Context context){
        OssSDK ossSDK = cacheOssSDKHashMap.remove(context);
        if(ossSDK!=null){
            ossSDK.setiUploadCallback(null);
        }
    }

    /**
     * 销毁
     */
    public void destroy() {
        destroyLocation();
        cacheOssSDKHashMap.clear();
    }

    /**
     * 销毁定位相关
     */
    public void destroyLocation(){
        if(aLocationSDK!=null){
            aLocationSDK.destroy();
        }
        aLocationSDK = null;
    }

    /**
     * 单例
     */
    private enum Singleton {
        INSTANCE;

        private ThirdSDKManager singleton;

        Singleton() {
            singleton = new ThirdSDKManager();
        }

        public ThirdSDKManager getInstance() {
            return singleton;
        }
    }

    public void registerNewMessageCallBack(IPushCalback calback) {
        PushHandlerManager.getInstance().registerCallBack(calback);
    }

    public void unRegisterNewMessageCallBack(IPushCalback calback) {
        PushHandlerManager.getInstance().unRegisterCallBack(calback);
    }

    /**
     * 上传callback
     */
    public interface IUploadCallback{
        /**
         * 上传成功
         */
        void onSuccess(Map<String, String> successMap, List<String> failureList);

        /**
         * 上传失败
         */
        void onFailure(List<String> failureList, String errCode, String errMsg);
    }

    /**
     * 上传Callback
     */
    public static class UploadCallback implements IUploadCallback{

        private Object targetObj;

        public UploadCallback() {
        }

        public UploadCallback(Object targetObj) {
            this.targetObj = targetObj;
        }

        @Override
        public void onSuccess(Map<String, String> successMap, List<String> failureList) {
            onSuccess(successMap, failureList, targetObj);
        }

        public void onSuccess(Map<String, String> successMap, List<String> failureList, Object targetObj) {

        }

        @Override
        public void onFailure(List<String> failureList, String errCode, String errMsg) {
            onFailure(failureList, errCode, errMsg, targetObj);
        }

        public void onFailure(List<String> failureList, String errCode, String errMsg, Object targetObj) {

        }

    }

    /**
     *
     */
    public interface IPushCalback {
        /**
         * 收到新消息
         */
        void newMessage();
    }

    /**
     * 定位Listener
     */
    public interface ILocationListener{
        void onSuccess(Location location);
        void onFailure(String errCode, String errInfo, String errDetail);
    }

    /**
     * 定位信息
     */
    public static class Location{
        /**
         * 定位类型
         */
        public int locationType;
        /**
         * 经度
         */
        public double longitude;
        /**
         * 纬度
         */
        public double latitude;
        /**
         * 精度
         */
        public float accuracy;
        /**
         * 提供者
         */
        public String provider;
        /**
         * 速度
         */
        public float speed;
        /**
         * 角度
         */
        public float bearing;
        /**
         * 获取当前提供定位服务的卫星个数
         */
        public int satellites;
        /**
         * 国家
         */
        public String country;
        /**
         * 省
         */
        public String province;
        /**
         * 城市
         */
        public String city;
        /**
         * 城区
         */
        public String district;
        /**
         * 街道
         */
        public String street;
        /**
         * 街道门牌号
         */
        public String streetNum;
        /**
         * 城市编码
         */
        public String cityCode;
        /**
         * 地区编码
         */
        public String adCode;
        /**
         * 获取当前定位点的AOI信息
         */
        public String aoiName;
        /**
         * 获取当前室内定位的建筑物Id
         */
        public String buildingId;
        /**
         * 获取当前室内定位的楼层
         */
        public String floor;
        /**
         * 地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息
         */
        public String address;

        /**
         * 获取地址
         * @return
         */
        public String getAddressStr(){
            StringBuffer sb = new StringBuffer();
            if(!TextUtils.isEmpty(province)){
                sb.append(province);
            }
            if(!TextUtils.isEmpty(city)){
                sb.append(city);
            }
            if(!TextUtils.isEmpty(district)){
                sb.append(district);
            }
            if(!TextUtils.isEmpty(street)){
                sb.append(street);
            }
            return sb.toString();
        }

    }

}
