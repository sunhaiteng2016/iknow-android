package com.beyond.popscience.module.building;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.PhoneUtil;
import com.beyond.library.util.ToastUtil;
import com.beyond.library.view.GridViewNoScroll;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.BuildingRestUsage;
import com.beyond.popscience.frame.pojo.BuildingDetail;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.module.building.adapter.BuildDetailImgListAdapter;
import com.beyond.popscience.module.building.adapter.InteriorConfigListAdapter;
import com.beyond.popscience.module.home.ChatActivity;
import com.beyond.popscience.module.home.fragment.Constant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 房屋详情
 * Created by linjinfa on 2017/10/14.
 * email 331710168@qq.com
 */
public class BuildingDetailActivity extends BaseActivity implements AMap.OnMarkerClickListener {

    /**
     *
     */
    private final static String EXTRA_BUILD_ID_KEY = "buildId";
    /**
     *
     */
    private final static String EXTRA_BUILD_ID_KEY_FLAG = "flag";
    /**
     *
     */
    private final int REQUEST_DETAIL_TASK_ID = 101;
    private String mflag;
    private MapView mMapView;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private boolean isFirstLoc = true;
    private RelativeLayout map_rl;
    private String lon, lat;
    private TextView tv1111;

    /**
     * @param context
     */
    public static void startActivity(Context context, String buildId, String flag) {
        Intent intent = new Intent(context, BuildingDetailActivity.class);
        intent.putExtra(EXTRA_BUILD_ID_KEY, buildId);
        intent.putExtra(EXTRA_BUILD_ID_KEY_FLAG, flag);
        context.startActivity(intent);
    }

    /**
     * @param context
     */
    public static void startActivity(Context context, String buildId) {
        Intent intent = new Intent(context, BuildingDetailActivity.class);
        intent.putExtra(EXTRA_BUILD_ID_KEY, buildId);
        context.startActivity(intent);
    }

    @BindView(R.id.tv_title)
    protected TextView titleTxtView;
    @BindView(R.id.tv_right)
    protected TextView tv_right;

    @BindView(R.id.listView)
    protected ListView listView;


    /**
     *
     */
    private TextView buildTitleTxtView;
    /**
     *
     */
    private TextView fangyuan_one;
    /**
     *
     */
    private TextView classifyNameTxtView;
    /**
     *
     */
    private TextView addressTxtView;
    /**
     *
     */
    private TextView huxingTxtView;
    /**
     *
     */
    private TextView areaTxtView;
    /**
     *
     */
    private TextView decorationTxtView;
    /**
     *
     */
    private TextView descTxtView;
    /**
     *
     */
    private TextView payTxtView;
    /**
     *
     */
    private TextView priceTxtView;

    private AMap aMap;

    private BuildDetailImgListAdapter buildDetailImgListAdapter;

    private InteriorConfigListAdapter interiorConfigListAdapter;

    @Request
    private BuildingRestUsage buildingRestUsage;
    /**
     *
     */
    private String buildId;
    /**
     *
     */
    private BuildingDetail buildingDetail;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_building_detail;
    }

    MyLocationStyle myLocationStyle;

    @Override
    public void initUI() {
        super.initUI();

        buildId = getIntent().getStringExtra(EXTRA_BUILD_ID_KEY);
        mflag = getIntent().getStringExtra(EXTRA_BUILD_ID_KEY_FLAG);

        if (mflag != null && mflag.equals("1")) {
            tv_right.setVisibility(View.GONE);
        } else {
            tv_right.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(buildId)) {
            backNoAnim();
            return;
        }

        titleTxtView.setText("房屋详情");

        initListView();

        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(mSavedInstanceState);
        if (aMap == null) {
            aMap = mMapView.getMap();
            UiSettings settings = aMap.getUiSettings();
            // 是否显示定位按钮
            settings.setMyLocationButtonEnabled(true);
            aMap.setMyLocationEnabled(false);//显示定位层并且可以触发定位,默认是flas
            aMap.setOnMarkerClickListener(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        requestDetail();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case REQUEST_DETAIL_TASK_ID:
                if (msg.getIsSuccess()) {
                    buildingDetail = (BuildingDetail) msg.getObj();
                    //图片
                    List<String> imgUrlList = buildingDetail.getDetailPicList();
                    buildDetailImgListAdapter.getDataList().clear();
                    if (imgUrlList != null) {
                        if (!TextUtils.isEmpty(buildingDetail.coverPic)) {
                            imgUrlList.add(0, buildingDetail.coverPic);
                        }
                        buildDetailImgListAdapter.getDataList().addAll(imgUrlList);
                    }
                    buildDetailImgListAdapter.notifyDataSetChanged();

                    //配置
                    List<String> configList = buildingDetail.getConfigList();
                    interiorConfigListAdapter.getDataList().clear();
                    if (configList != null) {
                        interiorConfigListAdapter.getDataList().addAll(configList);
                    }
                    interiorConfigListAdapter.notifyDataSetChanged();

                    initHeaderData();
                }
                dismissProgressDialog();
                break;
        }
    }

    /**
     *
     */
    private void initListView() {
        addHeaderView();

        buildDetailImgListAdapter = new BuildDetailImgListAdapter(this);
        listView.setAdapter(buildDetailImgListAdapter);
    }

    /**
     * 添加headerview
     */
    private void addHeaderView() {
        View view = View.inflate(this, R.layout.header_building_detail, null);
        listView.addHeaderView(view, null, false);

        buildTitleTxtView = (TextView) view.findViewById(R.id.buildTitleTxtView);
        fangyuan_one = (TextView) view.findViewById(R.id.fangyuan_one);
        classifyNameTxtView = (TextView) view.findViewById(R.id.classifyNameTxtView);
        addressTxtView = (TextView) view.findViewById(R.id.addressTxtView);
        huxingTxtView = (TextView) view.findViewById(R.id.huxingTxtView);
        areaTxtView = (TextView) view.findViewById(R.id.areaTxtView);
        decorationTxtView = (TextView) view.findViewById(R.id.decorationTxtView);
        descTxtView = (TextView) view.findViewById(R.id.descTxtView);
        payTxtView = (TextView) view.findViewById(R.id.payTxtView);
        priceTxtView = (TextView) view.findViewById(R.id.priceTxtView);
        map_rl = (RelativeLayout) view.findViewById(R.id.map_rl);
        tv1111 = (TextView) view.findViewById(R.id.tv1111);

        GridViewNoScroll interiorConfigGridView = (GridViewNoScroll) view.findViewById(R.id.InteriorConfigGridView);
        interiorConfigListAdapter = new InteriorConfigListAdapter(this);

        interiorConfigGridView.setAdapter(interiorConfigListAdapter);
    }

    private void initHeaderData() {
        if (buildingDetail != null) {
            buildTitleTxtView.setText(buildingDetail.title);
            classifyNameTxtView.setText(buildingDetail.classifyName);
            fangyuan_one.setText("1".equals(buildingDetail.sellType) ? "中介" : "房东");
            String trade = "";
            if ("1".equals(buildingDetail.trade)) {
                trade = "出租";
                if (buildingDetail.price.equals("-1.00")) {
                    priceTxtView.setText("面议");
                } else {
                    priceTxtView.setText("¥" + buildingDetail.price + "/月");
                }
            } else if ("2".equals(buildingDetail.trade)) {
                trade = "出售";
                if (buildingDetail.price.equals("-1.00")) {
                    priceTxtView.setText("面议");
                } else {
                    priceTxtView.setText("¥" + buildingDetail.price + "元");
                }
            }
            payTxtView.setText(trade);
            addressTxtView.setText(buildingDetail.address);
            String houseType = "";
            if (!TextUtils.isEmpty(buildingDetail.houseType)) {
                String houseTypes[] = buildingDetail.houseType.split(",");
                if (houseTypes.length > 0) {
                    houseType += houseTypes[0] + "厅";
                }
                if (houseTypes.length > 1) {
                    houseType += houseTypes[1] + "室";
                }
                if (houseTypes.length > 2) {
                    houseType += houseTypes[2] + "卫";
                }
            }
            huxingTxtView.setText(houseType);
            areaTxtView.setText(buildingDetail.size + "m²");
            decorationTxtView.setText(buildingDetail.decorate);
            descTxtView.setText(buildingDetail.introduce);
            if (mflag != null && mflag.equals("1")) {
            } else {
                //如果是自己的商品则展示编辑按钮
                if (TextUtils.equals(buildingDetail.
                        userId, UserInfoUtil.getInstance().getUserInfo().getUserId())) {
                    tv_right.setText("编辑");
                    tv_right.setVisibility(View.VISIBLE);
                }
            }
            //经纬度展示
            lon = buildingDetail.lon;
            lat = buildingDetail.lat;
            //将地图移动到定位点
            if (null != lon && null != lat) {
                aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                LatLng latlng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(latlng));
                MarkerOptions markerOption = new MarkerOptions().icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.maker))
                        .position(latlng)
                        .draggable(true);
                aMap.addMarker(markerOption);

                tv1111.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //经纬度传递到下边
                        Intent intent = new Intent(BuildingDetailActivity.this, MapViewActivity.class);
                        intent.putExtra("lon", lon);
                        intent.putExtra("lat", lat);
                        intent.putExtra("mAddress", buildingDetail.address);
                        startActivity(intent);
                    }
                });
            }
        }
    }

    /**
     * 请求详情
     */
    private void requestDetail() {
        showProgressDialog();
        buildingRestUsage.getBuildingDetail(REQUEST_DETAIL_TASK_ID, buildId);
    }

    @OnClick(R.id.llCall)
    void toPhoneCall(View view) {
        //打电话
        if (buildingDetail != null) {
            PhoneUtil.callPhoneDial(this, buildingDetail.mobile);
        }
    }

    @OnClick(R.id.llShotMsg)
    void toShortMsg(View view) {
        //打开短信
        if (buildingDetail != null) {
            PhoneUtil.sendSms(this, buildingDetail.mobile, "");
        }
    }

    @OnClick(R.id.tv_right)
    void rightEdit() {
        PublishBuildingActivity.startActivity(this, buildingDetail);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mMapView) {
            mMapView.onDestroy();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        Intent intent = new Intent(BuildingDetailActivity.this, MapViewActivity.class);
        intent.putExtra("lon", lon);
        intent.putExtra("lat", lat);
        intent.putExtra("mAddress", buildingDetail.address);
        startActivity(intent);
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
    
    @OnClick(R.id.lxmj)
    public void onViewClicked() {
        if (buildingDetail.userId.equals("-1")) {
            ToastUtil.showCenter(this, "该内容为后台发布，请通过电话联系");
        } else {
            Intent intent = new Intent(BuildingDetailActivity.this, ChatActivity.class);
            intent.putExtra(Constant.EXTRA_USER_ID, buildingDetail.userId + "");
            startActivity(intent);
        }
    }
}
