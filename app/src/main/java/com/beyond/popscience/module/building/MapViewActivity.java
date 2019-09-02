package com.beyond.popscience.module.building;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.beyond.popscience.MapUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapViewActivity extends BaseActivity {


    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_dh)
    TextView tvDh;
    private AMap aMap;
    private String lon, lat;
    private MarkerOptions markerOption;
    private String mAddress;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_map_view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void initUI() {
        super.initUI();
        lon = getIntent().getStringExtra("lon");
        lat = getIntent().getStringExtra("lat");
        mAddress = getIntent().getStringExtra("mAddress");
        mapView.onCreate(mSavedInstanceState); // 此方法必须重写
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }

    private void setUpMap() {
        //  aMap.setOnMarkerClickListener(this);
        addMarkersToMap();// 往地图上添加marker
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mapView) {
            mapView.onDestroy();
        }
    }

    /**
     * 在地图上添加marker
     */
    private void addMarkersToMap() {
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        //将地图移动到定位点
        LatLng latlng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latlng));
        markerOption = new MarkerOptions().icon(BitmapDescriptorFactory
                .fromResource(R.drawable.maker))
                .position(latlng)
                .draggable(true);
        aMap.addMarker(markerOption);
    }

    @OnClick(R.id.tv_dh)
    public void onViewClicked() {

        final String[] address = new String[]{"高德地图", "百度地图"};
        new AlertDialog.Builder(this).setTitle("是否开始导航")
                .setSingleChoiceItems(address, 0,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    if (MapUtil.isGdMapInstalled()) {
                                        MapUtil.openGaoDeNavi(MapViewActivity.this, BeyondApplication.getInstance().getLocation().latitude, BeyondApplication.getInstance().getLocation().longitude, BeyondApplication.getInstance().getLocation().getAddressStr(), Double.parseDouble(lat), Double.parseDouble(lon), mAddress);
                                    } else {
                                        //这里必须要写逻辑，不然如果手机没安装该应用，程序会闪退，这里可以实现下载安装该地图应用
                                        Toast.makeText(MapViewActivity.this, "尚未安装高德地图", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                if (which == 1) {
                                    if (MapUtil.isBaiduMapInstalled()) {
                                        MapUtil.openBaiDuNavi(MapViewActivity.this,  BeyondApplication.getInstance().getLocation().latitude, BeyondApplication.getInstance().getLocation().longitude, null, Double.parseDouble(lat), Double.parseDouble(lon), mAddress);
                                    } else {
                                        Toast.makeText(MapViewActivity.this, "尚未安装百度地图", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton("取消", null).show();
    }

    @OnClick(R.id.ic_back_rl)
    public void onViewClickedss() {
        finish();
    }
}
