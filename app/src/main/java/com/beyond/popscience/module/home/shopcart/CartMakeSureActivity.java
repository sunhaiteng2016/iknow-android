package com.beyond.popscience.module.home.shopcart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.L;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.BuildConfig;
import com.beyond.popscience.R;
import com.beyond.popscience.api.CreatOrderApi;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.pojo.ComfinOrderInfoBean;
import com.beyond.popscience.frame.pojo.CreadOrderGoodsDetail;
import com.beyond.popscience.frame.pojo.CreatOrderBean;
import com.beyond.popscience.frame.pojo.CreatOrdersMy;
import com.beyond.popscience.frame.pojo.DataBean;
import com.beyond.popscience.frame.pojo.GoodsDetail;
import com.beyond.popscience.frame.pojo.OrderInfoBean;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.module.home.adapter.FragmentOneItemAdapter;
import com.beyond.popscience.utils.adapter.BaseRVAdapter;
import com.beyond.popscience.utils.adapter.BaseViewHolder;
import com.beyond.popscience.widget.CircleImageView;
import com.beyond.popscience.widget.MyListView;
import com.beyond.popscience.widget.MyRecyclerView;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 确定订单界面    item_comfirmorder_item.xml
 */
public class CartMakeSureActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.leftTxtView)
    TextView leftTxtView;
    @BindView(R.id.rightImgView)
    ImageView rightImgView;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.shadowView)
    View shadowView;
    @BindView(R.id.topReLay)
    RelativeLayout topReLay;
    @BindView(R.id.tv_name_ms)
    TextView tvNameMs;
    @BindView(R.id.tv_phoen_ms)
    TextView tvPhoenMs;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.ll_address)
    LinearLayout llAddress;
    @BindView(R.id.tv_address_empty)
    TextView tvAddressEmpty;
    @BindView(R.id.ll_addresss)
    LinearLayout llAddresss;
    @BindView(R.id.imageView2)
    ImageView imageView2;
    @BindView(R.id.recycler_goods)
    MyRecyclerView recyclerGoods;
    @BindView(R.id.tv_yuhuijuan)
    TextView tvYuhuijuan;
    @BindView(R.id.ll_yhq)
    LinearLayout llYhq;
    @BindView(R.id.tv_lebei)
    TextView tvLebei;
    @BindView(R.id.tv_lebeifee)
    TextView tvLebeifee;
    @BindView(R.id.cb_isuselebei)
    CheckBox cbIsuselebei;
    @BindView(R.id.cb_zt)
    CheckBox cbZt;
    @BindView(R.id.cb_transfee)
    CheckBox cbTransfee;
    @BindView(R.id.ll_ziti)
    LinearLayout llZiti;
    @BindView(R.id.tv_ledian)
    TextView tvLedian;
    @BindView(R.id.cb_isuseledian)
    CheckBox cbIsuseledian;
    @BindView(R.id.tv_addressbottom)
    TextView tvAddressbottom;
    @BindView(R.id.tv_allmoney_pay)
    TextView tvAllmoneyPay;
    @BindView(R.id.tv_sign)
    TextView tvSign;

    private int  bill_num;//购买数量
    private String product_id;//商品id
    @Request
    private CreatOrderApi creatOrderApi;
    private final static int CREATE_ORDER_TASKID = 1001;//生成订单任务id
    private List<CreadOrderGoodsDetail> mDataList = new ArrayList<>();
    private BaseRVAdapter mBaseAdapter;
    private final int REQUEST_OK = 100;
    private String order_des;//订单留言
    private String order_code;//订单号
    private String address;//地址
    private String coverPic;//商品封面
    private String goodsName;//商品的名字
    private String sendPic;//商店的图片
    private String store_name;//商店的名字
    private List<ComfinOrderInfoBean.StoreListBean> storeListBeen = new ArrayList<>();
    DecimalFormat df = new DecimalFormat("######0.00");

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_cart_make_sure;
    }

    @Override
    public void initUI() {
        super.initUI();
        tvTitle.setText("确认订单");
        rightImgView.setVisibility(View.GONE);
        rightImgView.setImageResource(R.drawable.icon_dots);
        if (null != getIntent()){
            bill_num = getIntent().getIntExtra("bill_num",0);
            product_id = getIntent().getStringExtra("product_id");
            if (0 != bill_num && !TextUtils.isEmpty(product_id)){
                showProgressDialog();
                getDatas(bill_num,product_id);
            }
            coverPic = getIntent().getStringExtra("coverPic");
            goodsName = getIntent().getStringExtra("goodsName");
            sendPic = getIntent().getStringExtra("sendPic");
            store_name = getIntent().getStringExtra("store_name");
            mDataList.add(new CreadOrderGoodsDetail(product_id,bill_num,coverPic));
        }
//        setBaseAdapter();
        recyclerGoods.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

    }

    //生成订单
    private void getDatas(int bill_num0,String product_id0){
        if (!TextUtils.isEmpty(UserInfoUtil.getInstance().getUserInfo().getUserId())) {
            creatOrderApi.creatOrder(CREATE_ORDER_TASKID ,UserInfoUtil.getInstance().getUserInfo().getUserId(),product_id0,bill_num0 + "");
        }
    }

    @OnClick(R.id.ll_address)
    public void setLlAddress(){//选择收货地址
        Intent intent = new Intent(CartMakeSureActivity.this,AddressActivity.class);
        intent.putExtra("title","编辑地址");
        startActivityForResult(intent,REQUEST_OK);
    }

    @OnClick(R.id.tv_sign)
    public void setTvSign(){//提交订单
        Intent intent = new Intent(CartMakeSureActivity.this,ChoosePayTypeActivity.class);
        intent.putExtra("order_des",order_des);
        intent.putExtra("order_code",order_code);
        intent.putExtra("address",address);
        intent.putExtra("num",storeListBeen.get(0).getNums());
        startActivity(intent);
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId){
            case CREATE_ORDER_TASKID://生成订单
                dismissProgressDialog();
                if (msg.getIsSuccess()){
                    CreatOrdersMy creatOrderBean = (CreatOrdersMy) msg.getObj();
                    Log.e("====确认订单页数据===", "" + creatOrderBean.toString());
                    if (null != creatOrderBean) {
                        if (!TextUtils.isEmpty(creatOrderBean.getOrderInfo().getOrderAddress())) {
                            tvAddress.setText("收货地址: " + creatOrderBean.getOrderInfo().getOrderAddress());
                        }
                        if (!TextUtils.isEmpty(creatOrderBean.getOrderInfo().getOrderUser())) {
                            tvNameMs.setText("收件人: " + creatOrderBean.getOrderInfo().getOrderUser());
                        }
                        if (!TextUtils.isEmpty(creatOrderBean.getOrderInfo().getOrderPhone())){
                            tvPhoenMs.setText(creatOrderBean.getOrderInfo().getOrderPhone());
                        }
                        if (0 != creatOrderBean.getOrderInfo().getBillPrice()){
                            tvAllmoneyPay.setText("¥ " + creatOrderBean.getOrderInfo().getBillPrice());
                        }
                        order_des = creatOrderBean.getOrderInfo().getOrderDes();
                        order_code = creatOrderBean.getOrderInfo().getOrderCode();
                        address = creatOrderBean.getOrderInfo().getOrderAddress();
                        ComfinOrderInfoBean.StoreListBean.GoodsInfoBean goodsInfoBean = new ComfinOrderInfoBean.StoreListBean.GoodsInfoBean();
                        goodsInfoBean.setLogo_pic(coverPic);
                        goodsInfoBean.setGoods_id(product_id + "");
                        goodsInfoBean.setNum(bill_num + "");
                        goodsInfoBean.setGoods_name(goodsName);
                        goodsInfoBean.setPrice(creatOrderBean.getOrderInfo().getBillPrice()+ "");

                        ComfinOrderInfoBean.StoreListBean storeBean = new ComfinOrderInfoBean.StoreListBean();
                        storeBean.setLogo_pic(sendPic);
                        storeBean.setNums(bill_num + "");
                        storeBean.setStore_name(store_name);
                        storeBean.setPrices(creatOrderBean.getOrderInfo().getBillPrice()+"");
                        List<ComfinOrderInfoBean.StoreListBean.GoodsInfoBean> mGoodsInfoList = new ArrayList<>();
                        mGoodsInfoList.add(goodsInfoBean);
                        storeBean.setGoods_info(mGoodsInfoList);
                        storeListBeen.add(storeBean);

                        if (0 != storeListBeen.size()) {
                            setBaseAdapter();
                        }
                    }


                }else {

                    if ("1".equals(msg.getCode())){//选择编辑默认地址
                        if (!TextUtils.isEmpty(msg.getMsg())) {
                            ToastUtil.show(CartMakeSureActivity.this,"请编辑默认地址!");
                        }
                    }else {
                        if (!TextUtils.isEmpty(msg.getMsg())) {
                            ToastUtil.show(CartMakeSureActivity.this,msg.getMsg());
                        }
                    }
                }
                break;
        }
    }

    private void setBaseAdapter(){
        mBaseAdapter = new BaseRVAdapter(CartMakeSureActivity.this,storeListBeen) {
            @Override
            public int getLayoutId(int viewType) {
                return R.layout.item_comfirmorder_item;
            }

            @Override
            public void onBind(BaseViewHolder holder, int position) {
                if (null != storeListBeen) {
                    if (storeListBeen.get(position).getLogo_pic().contains("http://")) {
                        Glide.with(CartMakeSureActivity.this).load(storeListBeen.get(position).getLogo_pic()).into(holder.getImageView(R.id.iv_storeLogo));
                    } else {
                        Glide.with(CartMakeSureActivity.this).load(BuildConfig.BASE_URL + storeListBeen.get(position).getLogo_pic()).into(holder.getImageView(R.id.iv_storeLogo));
                    }

                    if (!TextUtils.isEmpty(storeListBeen.get(position).getStore_name())) {
                        holder.setTextView(R.id.tv_storeName, storeListBeen.get(position).getStore_name());
                    }

//                    if (!TextUtils.equals(storeListBeen.get(position).getExp(), "0")) {
//                        holder.setTextView(R.id.tv_itemfee, "邮费 ¥" + storeListBeen.get(position).getExp());
//                    } else {
//                        holder.setTextView(R.id.tv_itemfee, "免邮");
//                    }
                    double totalprice = Double.valueOf(storeListBeen.get(position).getPrices()) * Double.valueOf(storeListBeen.get(position).getNums());
                    holder.setTextView(R.id.tv_itemPrice, "¥" + df.format(totalprice));
                    tvAllmoneyPay.setText("¥" + df.format(totalprice));
                    holder.setTextView(R.id.tv_itemNum, "共" + storeListBeen.get(position).getNums() + "件商品");
                    MyListView my_list_view = holder.getView(R.id.my_list_view_order);
                    my_list_view.setAdapter(new FragmentOneItemAdapter(CartMakeSureActivity.this, storeListBeen.get(position).getGoods_info()));
                    if (!TextUtils.isEmpty(holder.getTextView(R.id.et_itemMessage).getText().toString().trim())) {
                        order_des = holder.getTextView(R.id.et_itemMessage).getText().toString().trim();
                    }
                }
            }

        };
        recyclerGoods.setAdapter(mBaseAdapter);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_OK:
            if (null != data){
                String receive_name = data.getStringExtra("receive_name");
                String address2 = data.getStringExtra("address");
                tvAddress.setText(address2);
                tvNameMs.setText(receive_name);
                String phone = data.getStringExtra("phone");
                tvPhoenMs.setText(phone);
                order_des = "";
                address = address2;
                getDatas(bill_num,product_id);
            }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void updateAdapter(int position,int num) {
        storeListBeen.get(position).setNums(num+"");
        storeListBeen.get(position).getGoods_info().get(0).setNum(num+"");
        mBaseAdapter.notifyDataSetChanged();
    }
}
