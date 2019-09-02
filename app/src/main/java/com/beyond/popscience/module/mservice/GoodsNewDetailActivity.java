package com.beyond.popscience.module.mservice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
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
import com.beyond.popscience.api.CreatOrderApi;
import com.beyond.popscience.api.ProductApi;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.pojo.GoodsDetailNew;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.module.home.ChatActivity;
import com.beyond.popscience.module.home.UserDetailsActivity;
import com.beyond.popscience.module.home.fragment.Constant;
import com.beyond.popscience.module.home.shopcart.CartMakeSureActivity;
import com.beyond.popscience.view.ListViewForScrollView;
import com.beyond.popscience.widget.GlideImageLoader;
import com.beyond.popscience.window.GoodsParameterWindow;
import com.beyond.popscience.window.GoodsSelectColorWindow;
import com.flyco.animation.BounceEnter.BounceBottomEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe： 商品详情
 * Date：2018/3/7
 * Time: 16:30
 * Author: Bin.Peng
 */

public class GoodsNewDetailActivity extends BaseActivity {
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_price)
    TextView tv_price;
    @BindView(R.id.tv_express)
    TextView tv_express;
    @BindView(R.id.tv_monthly_sales)
    TextView tv_monthly_sales;
    @BindView(R.id.tv_address)
    TextView tv_address;

    @BindView(R.id.tv_choose_type)
    TextView tv_choose_type;
    @BindView(R.id.tv_parameter)
    TextView tv_parameter;

    @BindView(R.id.sv)
    ScrollView sv;
    @BindView(R.id.listView)
    ListViewForScrollView listView;

    @BindView(R.id.iv_collect)
    ImageView iv_collect;
    @BindView(R.id.vp)
    ImageView vp;

    private static final String KEY_PRODUCT_ID = "KEY_PRODUCT_ID";
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.rl_choose_type)
    RelativeLayout rlChooseType;
    @BindView(R.id.rl_parameter)
    RelativeLayout rlParameter;
    @BindView(R.id.iv_kefu)
    ImageView ivKefu;
    @BindView(R.id.ll_kefu)
    LinearLayout llKefu;
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
    @BindView(R.id.goods_des)
    TextView goodsDes;
    @BindView(R.id.iv_phone)
    TextView ivPhone;
    @BindView(R.id.iv_message)
    TextView ivMessage;

    //商品参数
    private GoodsParameterWindow goodsParameterWindow;
    //商品颜色
    private GoodsSelectColorWindow goodsSelectColorWindow;
    //是否选择分类
    private boolean isChooseType = false;
    //是否删除
    private boolean isCollect = false;//默认没有收藏

    @Request
    private ProductApi productApi;
    @Request
    private CreatOrderApi creatOrderApi;
    private final int PRODUCT_DETAIL = 1;
    private final int PRODUCT_COLLECT = PRODUCT_DETAIL + 1;
    private final int PRODUCT_DELETE_COLLECT = PRODUCT_COLLECT + 1;

    private String product_id;
    private GoodsDetailNew goodsDetail;
    private DetailPicListAdapter detailPicListAdapter;
    private List<String> deaiIamgeList = new ArrayList<>();

    public static final void startActivity(Context context, String productId) {
        Intent intent = new Intent(context, GoodsNewDetailActivity.class);
        intent.putExtra(KEY_PRODUCT_ID, productId);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_goods_detail_new;
    }

    @Override
    public void initUI() {
        super.initUI();
        sv.smoothScrollTo(0, 0);
        goodsParameterWindow = new GoodsParameterWindow(this);

        product_id = getIntent().getStringExtra(KEY_PRODUCT_ID);
        getProductDetail();
    }

    //详情
    private void getProductDetail() {
        productApi.getProductsDetail(PRODUCT_DETAIL, product_id);
    }

    //删除收藏
    private void getProductsDeleteCollect() {
        productApi.getProductsDeleteCollet(PRODUCT_DELETE_COLLECT, product_id);
    }

    //收藏
    private void getProductsCollect() {
        productApi.getProductsCollet(PRODUCT_COLLECT, product_id);
    }


    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case PRODUCT_DETAIL: //详情
                if (msg.getIsSuccess()) {
                    goodsDetail = (GoodsDetailNew) msg.getObj();

                    if (null != goodsDetail.data) {
                        for (int i = 0; i < goodsDetail.data.goods.size(); i++) {
                            deaiIamgeList.add(goodsDetail.data.goods.get(i).getGoodsDescImg());
                        }
                        goodsSelectColorWindow = new GoodsSelectColorWindow(this, deaiIamgeList);
                        banner.setImages(deaiIamgeList);
                        banner.setImageLoader(new GlideImageLoader());
                        banner.start();
                        if (null != goodsDetail.data.goods && !TextUtils.isEmpty(goodsDetail.data.goods.get(0).getGoodsDesc())) {
                            StringBuffer sBuffer = new StringBuffer();
                            for (int i = 0; i < goodsDetail.data.goods.size(); i++) {
                                sBuffer.append(goodsDetail.data.goods.get(i).getGoodsDesc());
                            }
                            //goodsDes.setVisibility(View.VISIBLE);
                            goodsDes.setText(sBuffer.toString());
                        } else {
                            goodsDes.setVisibility(View.GONE);
                        }
//                    ImageLoaderUtil.displayImage(this, goodsDetail.avatar, vp);
                        tv_name.setText(goodsDetail.data.title);
//                    tv_express.setText("快递不知道字段");
//                    tv_monthly_sales.setText("月销不知道字段");
                        tv_address.setText(goodsDetail.data.address);
                        if ("1".equals(goodsDetail.isCollection)) {
                            iv_collect.setBackgroundResource(R.drawable.ic_collet_checked);
                            isCollect = true;
                        } else {
                            iv_collect.setBackgroundResource(R.drawable.ic_collet_uncheck);
                            isCollect = false;
                        }
                        if (null != goodsDetail.data.goods) {
                            detailPicListAdapter = new DetailPicListAdapter();
                            listView.setAdapter(detailPicListAdapter);
                        }
                        tv_price.setText("价格 ¥" + goodsDetail.data.price);
                    }
                    //根据分类ID 选择分类
//                    if (goodsDetail.classfyId.equals("5")) {
//                        tv_choose_type.setText("农产品买卖");
//                    } else if (goodsDetail.classfyId.equals("6")) {
//                        tv_choose_type.setText("房屋租赁");
//                    } else if (goodsDetail.classfyId.equals("7")) {
//                        tv_choose_type.setText("招工招聘");
//                    } else if (goodsDetail.classfyId.equals("8")) {
//                        tv_choose_type.setText("闲置物品");
//                    } else if (goodsDetail.classfyId.equals("9")) {
//                        tv_choose_type.setText("二手生活");
//                    } else {
//                        tv_choose_type.setText("找技能");
//                    }
                } else {
                    ToastUtil.show(this, msg.getMsg());
                }
                break;
            case PRODUCT_COLLECT: //收藏
                if (msg.getIsSuccess()) {
                    isCollect = true;//收藏成功
                    ToastUtil.show(this, msg.getMsg());
                    iv_collect.setBackgroundResource(R.drawable.ic_collet_checked);
                } else {
                    isCollect = false;
                    ToastUtil.show(this, msg.getMsg());
                }
                break;
            case PRODUCT_DELETE_COLLECT: //删除收藏
                if (msg.getIsSuccess()) {
                    isCollect = false;
                    ToastUtil.show(this, msg.getMsg());
                    iv_collect.setBackgroundResource(R.drawable.ic_collet_uncheck);
                } else {
                    isCollect = true;
                    ToastUtil.show(this, msg.getMsg());
                }
                break;
        }
    }

    //返回
    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.iv_message)
    void toShortMsg(View view) {
        //打开短信
        if (goodsDetail != null) {
            PhoneUtil.sendSms(this, goodsDetail.data.mobile, "");
        }
    }

    @OnClick(R.id.iv_phone)
    void toPhoneCall(View view) {
        //打电话
        if (goodsDetail != null) {
            PhoneUtil.callPhoneDial(this, goodsDetail.data.mobile);
        }
    }

    //分享
    @OnClick(R.id.iv_share)
    public void share() {
        ToastUtil.show(this, "分享啥");
    }

    //选择分类  加入购物车  立即购买
    @OnClick({R.id.rl_choose_type, R.id.tv_addCart, R.id.tv_now_pay})
    public void colorType(View view) {
        switch (view.getId()) {
            case R.id.rl_choose_type://选择分类
                goodsSelectColorWindow.anchorView(view)
                        .offset(0, 0)
                        .gravity(Gravity.BOTTOM)
                        .showAnim(new BounceBottomEnter())
                        .dismissAnim(new SlideBottomExit())
                        .dimEnabled(true)
                        .show();
                break;
            case R.id.tv_addCart://加入购物车
                goodsSelectColorWindow.anchorView(view)
                        .offset(0, 0)
                        .gravity(Gravity.BOTTOM)
                        .showAnim(new BounceBottomEnter())
                        .dismissAnim(new SlideBottomExit())
                        .dimEnabled(true)
                        .show();

                //确定完, 添加至购物车
                goodsSelectColorWindow.setGetChooseNum(new GoodsSelectColorWindow.GetChooseNum() {
                    @Override
                    public void getNum(int num, String chooseColor) {
                        // TODO: 2018/3/12  拿到数据添加至购物车  ShopCarActivity
                        ToastUtil.show(GoodsNewDetailActivity.this, "添加购物车");
                        return;
                    }
                });
                break;
            case R.id.tv_now_pay://立即购买
//                if (isChooseType) {
//                    if (null != goodsDetail) {
//                        Intent intent = new Intent(this, CartMakeSureActivity.class);
//                        intent.putExtra("coverPic", goodsDetail.data.coverPic);
//                        intent.putExtra("product_id", goodsDetail.data.productId);
//                        intent.putExtra("goodsName",goodsDetail.data.classfyName);
//                        startActivity(intent);
//                    }
//                } else {
//                    goodsSelectColorWindow.anchorView(view)
//                            .offset(0, 0)
//                            .gravity(Gravity.BOTTOM)
//                            .showAnim(new BounceBottomEnter())
//                            .dismissAnim(new SlideBottomExit())
//                            .dimEnabled(true)
//                            .show();
//                    goodsSelectColorWindow.setGetChooseNum(new GoodsSelectColorWindow.GetChooseNum() {
//                        @Override
//                        public void getNum(int num, String chooseColor) {
//                            Intent intent = new Intent(GoodsNewDetailActivity.this, CartMakeSureActivity.class);
//                            intent.putExtra("bill_num", num);
//                            intent.putExtra("color", chooseColor);
//                            intent.putExtra("product_id", goodsDetail.data.productId);
//                            intent.putExtra("coverPic", goodsDetail.data.coverPic);
//                            intent.putExtra("goodsName",goodsDetail.data.classfyName);
//                            startActivity(intent);
//                        }
//                    });
//                }

                /*if (null != goodsDetail) {
                    Intent intent = new Intent(GoodsNewDetailActivity.this, CartMakeSureActivity.class);
                    intent.putExtra("bill_num", 1);
                    intent.putExtra("color", "蓝色");
                    intent.putExtra("product_id", goodsDetail.data.productId);
                    intent.putExtra("coverPic", goodsDetail.data.coverPic);
                    intent.putExtra("sendPic", goodsDetail.data.avatar);
                    intent.putExtra("goodsName", goodsDetail.data.classfyName);
                    intent.putExtra("store_name", goodsDetail.data.realName);
                    startActivity(intent);
                }*/

                Intent intent = new Intent(GoodsNewDetailActivity.this, ChatActivity.class);
                intent.putExtra(Constant.EXTRA_USER_ID, goodsDetail.data.userId + "");
                startActivity(intent);
                break;
        }
    }

    //选择参数
    @OnClick(R.id.rl_parameter)
    public void parameter(View view) {
        goodsParameterWindow.anchorView(view)
                .offset(0, 0)
                .gravity(Gravity.BOTTOM)
                .showAnim(new BounceBottomEnter())
                .dismissAnim(new SlideBottomExit())
                .dimEnabled(true)
                .show();
    }

    //收藏
    @OnClick(R.id.ll_collect)
    public void collect() {
        if (isCollect) {//去取消收藏
            getProductsDeleteCollect();
        } else {
            getProductsCollect();
        }
    }

    //跳转到购物车
    @OnClick(R.id.ll_shopcar)
    public void shopCar() {
        Intent intent = new Intent(this, ShopCarActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    class DetailPicListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return goodsDetail.data.goods.size();
        }

        @Override
        public Object getItem(int position) {
            return goodsDetail.data.goods.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (convertView == null) {
                convertView = LayoutInflater.from(GoodsNewDetailActivity.this).inflate(R.layout.item_product_detail_pic_list_desc, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_desc.setText(goodsDetail.data.goods.get(position).getGoodsDesc());
            ImageLoaderUtil.displayImage(GoodsNewDetailActivity.this, goodsDetail.data.goods.get(position).getGoodsDescImg(), holder.iv_logo);
            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.iv_logo)
            ImageView iv_logo;
            @BindView(R.id.tv_desc)
            TextView tv_desc;

            public ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
