package com.beyond.popscience.frame.thirdsdk.location;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.beyond.library.util.L;
import com.beyond.popscience.frame.thirdsdk.ThirdSDKManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 高德
 * Created by linjinfa on 2017/6/23.
 * email 331710168@qq.com
 */
public class ALocationSDK {

    /**
     *
     */
    private final int WRITE_COARSE_LOCATION_REQUEST_CODE = 910;

    /**
     * 需要的权限
     */
    private final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };

    /**
     *
     */
    private AMapLocationClient locationClient;
    private AMapLocationClientOption locationOption;

    /**
     * 开始定位
     *
     * @param activity
     */
    public void startLocation(Activity activity, final ThirdSDKManager.ILocationListener locationListener) {

        List<String> permissionsList = new ArrayList<String>();

        for (String permissionStr : PERMISSIONS) {
            if (permissionGrantedNeedRequest(activity, permissionStr)) {
                permissionsList.add(permissionStr);
            }
        }

        if (permissionsList.size() > 0) {
            ActivityCompat.requestPermissions(activity, permissionsList.toArray(new String[permissionsList.size()]), WRITE_COARSE_LOCATION_REQUEST_CODE);//自定义的code
            return;
        }

        if (locationClient == null) {
            locationClient = new AMapLocationClient(activity.getApplicationContext());

            locationOption = new AMapLocationClientOption();
            locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            locationClient.setLocationOption(locationOption);

            locationClient.setLocationListener(new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation aMapLocation) {
                    showLocationStr(aMapLocation);
                    if (locationListener != null) {
                        if (aMapLocation == null) {
                            locationListener.onFailure("-110", "定位失败", null);
                        } else if (aMapLocation.getErrorCode() != 0) {
                            locationListener.onFailure(String.valueOf(aMapLocation.getErrorCode()), aMapLocation.getErrorInfo(), aMapLocation.getLocationDetail());
                        } else {  //成功
                            ThirdSDKManager.Location location = new ThirdSDKManager.Location();

                            location.locationType = aMapLocation.getLocationType();
                            location.longitude = aMapLocation.getLongitude();
                            location.latitude = aMapLocation.getLatitude();
                            location.accuracy = aMapLocation.getAccuracy();
                            location.provider = aMapLocation.getProvider();
                            location.speed = aMapLocation.getSpeed();
                            location.bearing = aMapLocation.getBearing();
                            location.satellites = aMapLocation.getExtras() != null ? aMapLocation.getExtras().getInt("satellites", 0) : 0;
                            location.country = aMapLocation.getCountry();
                            location.province = aMapLocation.getProvince();
                            location.city = aMapLocation.getCity();
                            location.district = aMapLocation.getDistrict();
                            location.address = aMapLocation.getAddress();
                            location.street = aMapLocation.getStreet();
                            location.streetNum = aMapLocation.getStreetNum();
                            location.cityCode = aMapLocation.getCityCode();
                            location.adCode = aMapLocation.getAdCode();
                            location.aoiName = aMapLocation.getAoiName();
                            location.buildingId = aMapLocation.getBuildingId();
                            location.floor = aMapLocation.getFloor();

                            locationListener.onSuccess(location);
                        }
                    }
                }
            });
        }
        if (!locationClient.isStarted()) {
            locationClient.startLocation();
        }
    }

    /**
     * 销毁
     */
    public void destroy() {
        if (locationClient != null) {
            locationClient.stopLocation();
            locationClient.onDestroy();
        }
        locationClient = null;
        locationOption = null;
    }

    /**
     * 根据定位结果返回定位信息的字符串
     */
    private synchronized void showLocationStr(AMapLocation location) {
        if (null == location) {
            return;
        }
        StringBuffer sb = new StringBuffer();
        //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
        if (location.getErrorCode() == 0) {
            sb.append("定位成功" + "\n");
            sb.append("定位类型: " + location.getLocationType() + "\n");
            sb.append("经    度    : " + location.getLongitude() + "\n");
            sb.append("纬    度    : " + location.getLatitude() + "\n");
            sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
            sb.append("提供者    : " + location.getProvider() + "\n");

            if (location.getProvider().equalsIgnoreCase(
                    android.location.LocationManager.GPS_PROVIDER)) {
                // 以下信息只有提供者是GPS时才会有
                sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
                sb.append("角    度    : " + location.getBearing() + "\n");
                // 获取当前提供定位服务的卫星个数
                sb.append("星    数    : "
                        + location.getExtras().getInt("satellites", 0) + "\n");
            } else {
                // 供者是GPS时是没有以下信息的
                sb.append("国    家    : " + location.getCountry() + "\n");
                sb.append("省            : " + location.getProvince() + "\n");
                sb.append("市            : " + location.getCity() + "\n");
                sb.append("区            : " + location.getDistrict() + "\n");
                sb.append("地    址    : " + location.getAddress() + "\n");
            }
        } else {
            //定位失败
            sb.append("定位为失败" + "\n");
            sb.append("错误码:" + location.getErrorCode() + "\n");
            sb.append("错误信息:" + location.getErrorInfo() + "\n");
            sb.append("错误描述:" + location.getLocationDetail() + "\n");
        }

        L.v("showLocationStr=======> " + sb.toString());
    }

    /**
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(Activity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == WRITE_COARSE_LOCATION_REQUEST_CODE) {
            if (!verifyPermissions(grantResults)) {
                showMissingPermissionDialog(activity);
            }
        }
    }

    /**
     * 显示提示信息
     *
     * @since 2.5.0
     */
    private void showMissingPermissionDialog(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("当前应用缺少必要权限。\n\n请点击\"设置\"-\"权限\"-打开所需权限。");

        // 拒绝, 退出应用
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.setPositiveButton("设置",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings(activity);
                        dialog.dismiss();
                    }
                });

        builder.setCancelable(false);

        builder.show();
    }

    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    private void startAppSettings(Activity activity) {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + activity.getPackageName()));
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检测是否说有的权限都已经授权
     *
     * @param grantResults
     * @return
     * @since 2.5.0
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否需要申请权限
     *
     * @param activity
     * @param permission
     * @return
     */
    private boolean permissionGrantedNeedRequest(Activity activity, String permission) {
        int grantCode = PermissionChecker.checkSelfPermission(activity, permission);
        if (grantCode != PackageManager.PERMISSION_GRANTED || ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            return true;
        }
        return false;
    }

}
