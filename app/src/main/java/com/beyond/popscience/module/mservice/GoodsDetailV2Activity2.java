package com.beyond.popscience.module.mservice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.CommentRestUsage;
import com.beyond.popscience.frame.net.ServiceRestUsage;
import com.beyond.popscience.frame.net.SquareRestUsage;
import com.beyond.popscience.frame.pojo.GoodsDetail;
import com.beyond.popscience.frame.pojo.ServiceGoodsItem;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.module.building.BuildingDetailActivity;
import com.beyond.popscience.module.home.ChatActivity;
import com.beyond.popscience.module.home.fragment.Constant;
import com.beyond.popscience.module.home.shopcart.CartMakeSureActivity;
import com.beyond.popscience.module.mservice.adapter.GoodsDetailSlideAdapter;
import com.beyond.popscience.view.ListViewForScrollView;
import com.beyond.popscience.widget.GlideImageLoader;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 商品详情  (新的技能详情)
 * <p>
 * Created by yao.cui on 2017/6/24.
 */

public class GoodsDetailV2Activity2 extends BaseActivity {

    private final String[] TABS = new String[]{
            "详情描述", "查看评论"
    };

    private static final int TASK_GET_GOODS_DETAIL = 1402;//获取轮播图
    /**
     * 请求评论列表
     */
    private final int REQUEST_COMMENT_LIST_TASK_ID = 1403;

    private static final String KEY_PRODUCT = "product";
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
    @BindView(R.id.iv_message)
    TextView ivMessage;
    @BindView(R.id.iv_phone)
    TextView ivPhone;

    private GoodsDetail goodsDetail;
    private ServiceGoodsItem serviceGoodsItem;

    @Request
    private ServiceRestUsage mRestUsage;

    @Request
    private SquareRestUsage squareRestUsage;
    /**
     *
     */
    @Request
    private CommentRestUsage commentRestUsage;
    /**
     *
     */
    private GoodsDetailSlideAdapter slideAdapter;
    /**
     * 是否点击了编辑
     */
    private boolean isEdit;
    private DetailPicListAdapter detailPicListAdapter;

    public static void startActivity(Context context, ServiceGoodsItem serviceGoodsItem) {
        Intent intent = new Intent(context, GoodsDetailV2Activity2.class);
        intent.putExtra(KEY_PRODUCT, serviceGoodsItem);
        context.startActivity(intent);
    }

    public static void startActivityGoods(Context context, ServiceGoodsItem serviceGoodsItem) {
        if (serviceGoodsItem != null) {
            serviceGoodsItem.setAppGoodsType("5");
        }
        Intent intent = new Intent(context, GoodsDetailV2Activity2.class);
        intent.putExtra(KEY_PRODUCT, serviceGoodsItem);
        context.startActivity(intent);
    }

    /**
     * 任务详情
     *
     * @param context
     */
    public static void startActivityTask(Context context, String uId) {
        ServiceGoodsItem serviceGoodsItem = new ServiceGoodsItem();
        serviceGoodsItem.setAppGoodsType("2");
        serviceGoodsItem.setProductId(uId);

        Intent intent = new Intent(context, GoodsDetailV2Activity2.class);
        intent.putExtra(KEY_PRODUCT, serviceGoodsItem);
        context.startActivity(intent);
    }

    /**
     * 技能详情
     *
     * @param context
     */
    public static void startActivitySkill(Context context, String uId) {
        ServiceGoodsItem serviceGoodsItem = new ServiceGoodsItem();
        serviceGoodsItem.setAppGoodsType("1");
        serviceGoodsItem.setProductId(uId);

        Intent intent = new Intent(context, GoodsDetailV2Activity2.class);
        intent.putExtra(KEY_PRODUCT, serviceGoodsItem);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_building2_new2;
    }

    @Override
    public void initUI() {
        super.initUI();
        serviceGoodsItem = (ServiceGoodsItem) getIntent().getSerializableExtra(KEY_PRODUCT);
        if (serviceGoodsItem == null) {
            backNoAnim();
            return;
        }

        showProgressDialog();
        getGoodsDetail();

    }


    @Override
    protected void onResume() {
        super.onResume();
        getGoodsDetail();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);

        switch (taskId) {
            case TASK_GET_GOODS_DETAIL:
                if (msg.getIsSuccess()) {
                    goodsDetail = (GoodsDetail) msg.getObj();
                    Log.e("===任务详情===", goodsDetail.toString());
                    //图片
                    List<String> imgUrlList = goodsDetail.getDetailPicList();

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

                    if (!TextUtils.isEmpty(goodsDetail.introduce)) {

                        goodsDes.setVisibility(View.VISIBLE);
                        goodsDes.setText(goodsDetail.introduce);

                    } else {
                        goodsDes.setVisibility(View.GONE);
                    }
                    if (!TextUtils.isEmpty(goodsDetail.title)) {
                        tvName.setText(goodsDetail.title);
                    }

                    tvAddress.setText(goodsDetail.address);
//                    if ("1".equals(goodsDetail.isCollection)) {
//                        iv_collect.setBackgroundResource(R.drawable.ic_collet_checked);
//                        isCollect = true;
//                    } else {
//                        iv_collect.setBackgroundResource(R.drawable.ic_collet_uncheck);
//                        isCollect = false;
////                    }
                    if (null != goodsDetail.getDetailPicList()) {
                        detailPicListAdapter = new DetailPicListAdapter();
                        listView.setAdapter(detailPicListAdapter);
                    }
                    tvPrice.setText("价格 ¥" + goodsDetail.price);
                } else {

                }
                dismissProgressDialog();

                break;

        }
    }


    @OnClick(R.id.iv_message)
    void toShortMsg(View view) {
        //打开短信
        if (goodsDetail != null) {
            PhoneUtil.sendSms(this, goodsDetail.mobile, "");
        }
    }

    @OnClick(R.id.iv_phone)
    void toPhoneCall(View view) {
        //打电话
        if (goodsDetail != null) {
            PhoneUtil.callPhoneDial(this, goodsDetail.mobile);
        }
    }

//    @OnClick(R.id.tv_right)
//    void toEditGoods(View view) {
//        isEdit = true;
//        if ("1".equals(serviceGoodsItem.getAppGoodsType())) { //技能
//            Bundle bundle = new Bundle();
//            bundle.putInt("type", 1);
//            bundle.putSerializable("goodsDetail", goodsDetail);
//            CheckAuthTool.startActivity(this, SkillPublishActivity.class, bundle);
//        } else if ("2".equals(serviceGoodsItem.getAppGoodsType())) {   //任务
//            Bundle bundle = new Bundle();
//            bundle.putInt("type", 2);
//            bundle.putSerializable("goodsDetail", goodsDetail);
//            CheckAuthTool.startActivity(this, SkillPublishActivity.class, bundle);
//        } else {  //商品
//            PublishGoodsActivity.startActivityEdit(this, goodsDetail);
//        }
//    }

//    @OnClick(R.id.emptyReLay)
//    public void reloadClick(View view) {
//        showProgressDialog();
//        getGoodsDetail();
//    }

    /**
     * 请求评论列表
     */
    private void requestComment() {
        commentRestUsage.getComment(REQUEST_COMMENT_LIST_TASK_ID, 1, serviceGoodsItem.getAppGoodsType(), serviceGoodsItem.getProductId());
    }

    /**
     * 获取商品详情
     */
    private void getGoodsDetail() {
        if ("1".equals(serviceGoodsItem.getAppGoodsType())) { //技能
            squareRestUsage.getSkillDetail(TASK_GET_GOODS_DETAIL, serviceGoodsItem.getProductId());
        } else if ("2".equals(serviceGoodsItem.getAppGoodsType())) {   //任务
            squareRestUsage.getTaskDetail(TASK_GET_GOODS_DETAIL, serviceGoodsItem.getProductId());
        } else {  //商品
            mRestUsage.getGoodsDetail(TASK_GET_GOODS_DETAIL, serviceGoodsItem.getProductId());
        }
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

                if (null != goodsDetail) {
                    Intent intent = new Intent(GoodsDetailV2Activity2.this, CartMakeSureActivity.class);
                    intent.putExtra("bill_num", 1);
                    intent.putExtra("color", "蓝色");
                    intent.putExtra("product_id", serviceGoodsItem.getProductId());
                    intent.putExtra("coverPic", goodsDetail.coverPic);
                    intent.putExtra("sendPic", goodsDetail.avatar);
                    intent.putExtra("goodsName", goodsDetail.classifyName);
                    intent.putExtra("store_name", goodsDetail.realName);
                    startActivity(intent);
                }
                break;
        }
    }


    @OnClick(R.id.iv_back)
    void setIvBack() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.lxmj)
    public void onViewClicked() {
        if (goodsDetail.userId.equals("-1")) {
            ToastUtil.showCenter(this, "该内容为后台发布，请通过电话联系");
        } else {
            Intent intent = new Intent(GoodsDetailV2Activity2.this, ChatActivity.class);
            intent.putExtra(Constant.EXTRA_USER_ID, goodsDetail.userId + "");
            startActivity(intent);
        }


    }


    class DetailPicListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return goodsDetail.getDetailPicList().size();
        }

        @Override
        public Object getItem(int position) {
            return goodsDetail.getDetailPicList().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (convertView == null) {
                convertView = LayoutInflater.from(GoodsDetailV2Activity2.this).inflate(R.layout.item_product_detail_pic_list, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ImageLoaderUtil.displayImage(GoodsDetailV2Activity2.this, goodsDetail.getDetailPicList().get(position), holder.iv_logo);
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
