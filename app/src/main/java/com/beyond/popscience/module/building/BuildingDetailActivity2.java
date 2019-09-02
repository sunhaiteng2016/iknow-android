package com.beyond.popscience.module.building;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.PhoneUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.BuildingRestUsage;
import com.beyond.popscience.frame.pojo.BuildingDetail;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.module.home.shopcart.CartMakeSureActivity;
import com.beyond.popscience.view.ListViewForScrollView;
import com.beyond.popscience.widget.GlideImageLoader;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 房屋详情 (*最新的房屋详情)
 * Created by linjinfa on 2017/10/14.
 * email 331710168@qq.com
 */
public class BuildingDetailActivity2 extends BaseActivity {

    /**
     *
     */
    private final static String EXTRA_BUILD_ID_KEY = "buildId";

    /**
     *
     */
    private final int REQUEST_DETAIL_TASK_ID = 101;
    @BindView(R.id.vp)
    ImageView vp;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_share)
    ImageView ivShare;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_express)
    TextView tvExpress;
    @BindView(R.id.tv_monthly_sales)
    TextView tvMonthlySales;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_choose_type)
    TextView tvChooseType;
    @BindView(R.id.rl_choose_type)
    RelativeLayout rlChooseType;
    @BindView(R.id.tv_parameter)
    TextView tvParameter;
    @BindView(R.id.rl_parameter)
    RelativeLayout rlParameter;
    @BindView(R.id.goods_des)
    TextView goodsDes;
    @BindView(R.id.listView)
    ListViewForScrollView listView;
    @BindView(R.id.sv)
    ScrollView sv;
    @BindView(R.id.iv_kefu)
    ImageView ivKefu;
    @BindView(R.id.ll_kefu)
    LinearLayout llKefu;
    @BindView(R.id.iv_collect)
    ImageView ivCollect;
    @BindView(R.id.ll_collect)
    LinearLayout llCollect;
    @BindView(R.id.ll_shopcar)
    LinearLayout llShopcar;
    @BindView(R.id.tv_addCart)
    TextView tvAddCart;

    @BindView(R.id.tv_now_pay)
    TextView tvNowPay;
    @BindView(R.id.lay)
    LinearLayout lay;
    @BindView(R.id.iv_phone)
    ImageView ivPhone;
    @BindView(R.id.iv_message)
    ImageView ivMessage;


    private DetailPicListAdapter detailPicListAdapter;
    private List<String> mImageList = new ArrayList<>();

    /**
     * @param context
     */
    public static void startActivity(Context context, String buildId) {
        Intent intent = new Intent(context, BuildingDetailActivity2.class);
        intent.putExtra(EXTRA_BUILD_ID_KEY, buildId);
        context.startActivity(intent);
    }


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
        return R.layout.activity_building2_new2;
    }

    @Override
    public void initUI() {
        super.initUI();
        sv.smoothScrollTo(0, 0);
        buildId = getIntent().getStringExtra(EXTRA_BUILD_ID_KEY);
        if (TextUtils.isEmpty(buildId)) {
            backNoAnim();
            return;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        requestDetail();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case REQUEST_DETAIL_TASK_ID://房屋详情
                if (msg.getIsSuccess()) {
                    buildingDetail = (BuildingDetail) msg.getObj();
                    //图片
                    List<String> imgUrlList = buildingDetail.getDetailPicList();
                    //配置
                    List<String> configList = buildingDetail.getConfigList();
                    if (null != imgUrlList && 0 != imgUrlList.size()) {
                        banner.setImages(imgUrlList);
                        banner.setImageLoader(new GlideImageLoader());
                        banner.start();
                    } else {
                        imgUrlList = new ArrayList<>();
                        imgUrlList.add("");
                        banner.setImages(imgUrlList);
                        banner.setImageLoader(new GlideImageLoader());
                        banner.start();
                    }

                    if (!TextUtils.isEmpty(buildingDetail.introduce)) {

                        goodsDes.setVisibility(View.VISIBLE);
                        goodsDes.setText(buildingDetail.introduce);

                    } else {
                        goodsDes.setVisibility(View.GONE);
                    }
                    if (!TextUtils.isEmpty(buildingDetail.title)) {
                        tvName.setText(buildingDetail.title);
                    }

                    tvAddress.setText(buildingDetail.address);
//                    if ("1".equals(goodsDetail.isCollection)) {
//                        iv_collect.setBackgroundResource(R.drawable.ic_collet_checked);
//                        isCollect = true;
//                    } else {
//                        iv_collect.setBackgroundResource(R.drawable.ic_collet_uncheck);
//                        isCollect = false;
////                    }
                    if (null != buildingDetail.getDetailPicList()) {
                        detailPicListAdapter = new DetailPicListAdapter();
                        listView.setAdapter(detailPicListAdapter);
                    }
                    tvPrice.setText("价格 ¥" + buildingDetail.price);
                }
                dismissProgressDialog();
                break;
        }
    }

    @OnClick(R.id.iv_message)
    void toShortMsg(View view) {
        //打开短信
        if (buildingDetail != null) {
            PhoneUtil.sendSms(this, buildingDetail.mobile, "");
        }
    }

    @OnClick(R.id.iv_phone)
    void toPhoneCall(View view) {
        //打电话
        if (buildingDetail != null) {
            PhoneUtil.callPhoneDial(this, buildingDetail.mobile);
        }
    }

    //返回
    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    //选择分类  加入购物车  立即购买
    @OnClick({R.id.rl_choose_type, R.id.tv_addCart, R.id.tv_now_pay})
    public void colorType(View view) {
        switch (view.getId()) {
            case R.id.rl_choose_type://选择分类

                break;
            case R.id.tv_addCart://加入购物车

                break;
            case R.id.tv_now_pay://立即购买

                if (null != buildingDetail) {
                    Intent intent = new Intent(BuildingDetailActivity2.this, CartMakeSureActivity.class);
                    intent.putExtra("bill_num", 1);
                    intent.putExtra("color", "蓝色");
                    intent.putExtra("product_id", buildId);
                    intent.putExtra("coverPic", buildingDetail.coverPic);
                    intent.putExtra("sendPic", buildingDetail.avatar);
                    intent.putExtra("goodsName", buildingDetail.classifyName);
                    intent.putExtra("store_name", buildingDetail.realName);
                    startActivity(intent);
                }
                break;
        }
    }


    /**
     * 请求详情
     */
    private void requestDetail() {
        showProgressDialog();
        buildingRestUsage.getBuildingDetail(REQUEST_DETAIL_TASK_ID, buildId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // TODO: add setContentView(...) invocation
//        ButterKnife.bind(this);
//    }

    class DetailPicListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return buildingDetail.getDetailPicList().size();
        }

        @Override
        public Object getItem(int position) {
            return buildingDetail.getDetailPicList().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (convertView == null) {
                convertView = LayoutInflater.from(BuildingDetailActivity2.this).inflate(R.layout.item_product_detail_pic_list, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ImageLoaderUtil.displayImage(BuildingDetailActivity2.this, buildingDetail.getDetailPicList().get(position), holder.iv_logo);
            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.iv_logo)
            ImageView iv_logo;

            public ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
