package com.beyond.popscience.module.point;


import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.DensityUtil;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.LoginActivity;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.PointRestUsage;
import com.beyond.popscience.frame.pojo.ExchangeBean;
import com.beyond.popscience.frame.pojo.GoodsExchangeBean;
import com.beyond.popscience.frame.pojo.ProductSucBean;
import com.beyond.popscience.frame.pojo.RecordDetail;
import com.beyond.popscience.frame.pojo.UserInfo;
import com.beyond.popscience.frame.pojo.point.IndexDataBean;
import com.beyond.popscience.frame.pojo.point.IndexGoods;
import com.beyond.popscience.frame.pojo.point.SignCheckBean;
import com.beyond.popscience.frame.util.SPUtils;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.module.personal.XfjlActivity;
import com.beyond.popscience.module.personal.Zzjl;
import com.beyond.popscience.module.point.adapter.GoodsAdapter;
import com.beyond.popscience.module.point.adapter.HeadAdapter;
import com.beyond.popscience.view.DiscreteScrollView;
import com.beyond.popscience.view.InfiniteScrollAdapter;
import com.beyond.popscience.view.Orientation;
import com.beyond.popscience.view.ScaleTransformer;
import com.beyond.popscience.view.ShowSignSuccessView;
import com.beyond.popscience.widget.GridSpacingItemDecoration;
import com.okhttp.CallBackUtil;
import com.okhttp.OkhttpUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

import static android.view.View.GONE;

public class PonitShopActivity extends BaseActivity implements DiscreteScrollView.OnItemChangedListener<RecyclerView.ViewHolder>, HeadAdapter.OnItemClickListener {

    private static final int TASK_RECORD_LIST = 800001;
    private static final int INDEX_CODE = 800002;
    private static final int SIGN_CHECK = 800003;
    private static final int SIGN = 800004;
    private static final int TASK_INSERTRANKING_LIST = 800005;
    private static final int TASK_PRODUCT_SUC = 800006;
    @BindView(R.id.tv_title)
    protected TextView titleTxtView;
    @BindView(R.id.forecast_city_picker)
    DiscreteScrollView forecast_city_picker;
    @BindView(R.id.goods_rv)
    RecyclerView rvGoods;
    @BindView(R.id.lb_detail)
    LinearLayout lbDetail;
    @BindView(R.id.lb_exchange_record)
    LinearLayout lbExchangeRecord;
    @BindView(R.id.lb_get_ways)
    LinearLayout lbGetWays;
    @BindView(R.id.lb_no)
    TextView lbNo;
    @BindView(R.id.lx_sign)
    TextView lxSign;
    @BindView(R.id.oneDay)
    TextView oneDay;
    @BindView(R.id.twoDay)
    TextView twoDay;
    @BindView(R.id.threeDay)
    TextView threeDay;
    @BindView(R.id.fourDay)
    TextView fourDay;
    @BindView(R.id.fiveDay)
    TextView fiveDay;
    @BindView(R.id.sixDay)
    TextView sixDay;
    @BindView(R.id.sevenDay)
    TextView sevenDay;
    @BindView(R.id.no_data)
    TextView noData;

    @Request
    private PointRestUsage restUsage;
    private UserInfo userInfo;

    private InfiniteScrollAdapter infiniteAdapter;
    private HeadAdapter headAdapter;
    private List<String> headUrlList;
    private IndexDataBean indexResultBean;//首页接口返回结果实体类
    private List<SignCheckBean> signCheckResultBeanList;//签到前检查结果实体类
    private SignCheckBean signCheckBean;
    private List<GoodsExchangeBean> goodsExchangeBeanList;
    private List<TextView> signViewList;
    private int keys;
    private List<RecordDetail> recordDetails;
    //绿币数，需传到绿币明细界面
    private String score = "0.0";
    private SlideFromBottomPopup popup;
    private boolean isduanqian = false;
    private Integer ajxSignData;
    private Integer sqlSignData;
    private Integer keySta;
    private int page = 1;
    private Zzjl zzjl;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_ponit_shop;
    }

    @Override
    public void initUI() {
        super.initUI();
        signViewList = new ArrayList<>();
        addAllSignView();
        titleTxtView.setText("科普绿币");
        userInfo = UserInfoUtil.getInstance().getUserInfo();
        rvGoods.setNestedScrollingEnabled(false);
        iniHeadView();
        showProgressDialog("加载中");
        requestIndexData();
        requestCheckBeforeSign();
        recordDetails = new ArrayList<>();
    }

    /**
     * 将7个签到的View添加到集合中
     */
    private void addAllSignView() {
        signViewList.add(oneDay);
        signViewList.add(twoDay);
        signViewList.add(threeDay);
        signViewList.add(fourDay);
        signViewList.add(fiveDay);
        signViewList.add(sixDay);
        signViewList.add(sevenDay);
    }

    /**
     * dayno  已经签到过的数目
     *
     * @param dayNo
     */
    private void showHasSigneDays(int dayNo) {
        if (dayNo == 0) {
            signViewList.get(0).setBackground(getResources().getDrawable(R.drawable.bg_circle_blue));
            signViewList.get(0).setTextColor(getResources().getColor(R.color.white));
            signViewList.get(0).setText("签到");
            signViewList.get(0).setTextSize(12);
            signViewList.get(0).setClickable(true);
            clickFalse(0);
            return;
        } else if (dayNo == 7 &&
//                !signCheckBean.getSqlsigndate().equals(signCheckBean.getAjxsigndate()) &&
                Integer.valueOf(signCheckBean.getAjxsigndate()) - Integer.valueOf(signCheckBean.getSqlsigndate()) == 1) {
            signViewList.get(dayNo - 1).setBackground(getResources().getDrawable(R.drawable.bg_circle_blue));
            signViewList.get(dayNo - 1).setTextColor(getResources().getColor(R.color.white));
            signViewList.get(dayNo - 1).setText("签到");
            signViewList.get(dayNo - 1).setTextSize(12);
            signViewList.get(dayNo - 1).setClickable(true);
//            lxSign.setText(signCheckBean.getDays());
            clickFalse(dayNo - 1);
            for (int i = 0; i < dayNo - 1; i++) {
                signViewList.get(i).setBackground(getResources().getDrawable(R.drawable.bg_circle_red));
                signViewList.get(i).setTextColor(getResources().getColor(R.color.red));
                signViewList.get(i).setClickable(false);
            }

            return;
        }
        for (int i = 0; i <= dayNo - 1; i++) {
            signViewList.get(i).setBackground(getResources().getDrawable(R.drawable.bg_circle_red));
            signViewList.get(i).setTextColor(getResources().getColor(R.color.red));
            signViewList.get(i).setClickable(false);
        }
        clickFalse(dayNo - 1);

        TextView temTextView = null;
        if (signCheckBean.getAjxsigndate() == null) {
            temTextView = signViewList.get(dayNo - 1);
            temTextView.setBackground(getResources().getDrawable(R.drawable.bg_soild_gray));
            temTextView.setTextColor(getResources().getColor(R.color.white));
            temTextView.setText("已签到");
            temTextView.setTextSize(12);
            temTextView.setClickable(false);
            return;
        }

        int sqlDate = Integer.parseInt(signCheckBean.getSqlsigndate());
        int ajxDate = Integer.parseInt(signCheckBean.getAjxsigndate());

        if (sqlDate == ajxDate) {
            temTextView = signViewList.get(dayNo - 1);
            temTextView.setBackground(getResources().getDrawable(R.drawable.bg_soild_gray));
            temTextView.setTextColor(getResources().getColor(R.color.white));
            temTextView.setText("已签到");
            temTextView.setTextSize(12);
            temTextView.setClickable(false);
        } else {
            temTextView = signViewList.get(dayNo - 1);
            temTextView.setBackground(getResources().getDrawable(R.drawable.bg_circle_blue));
            temTextView.setTextColor(getResources().getColor(R.color.white));
            temTextView.setText("签到");
            temTextView.setTextSize(12);
            temTextView.setClickable(true);
        }

    }

    private void clickFalse(int dayNo) {
        for (int i = dayNo + 1; i < 7; i++) {
            signViewList.get(i).setClickable(false);
        }
    }

    /**
     * 点击签到.成功后,对应的当天要显示为已签到
     *
     * @param postion
     */
    private void displayCurrentDays(int postion) {
        signViewList.get(postion - 1).setBackground(getResources().getDrawable(R.drawable.bg_circle_gray));
        signViewList.get(postion - 1).setTextColor(getResources().getColor(R.color.white));
        signViewList.get(postion - 1).setText("已签到");
        signViewList.get(postion - 1).setTextSize(12);
        signViewList.get(postion - 1).setClickable(false);
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        dismissProgressDialog();
        if (msg != null) {
            if (msg.getIsSuccess()) {
                switch (taskId) {
                    case TASK_PRODUCT_SUC:
                        //兑换成功后的操作
                        ProductSucBean bean = (ProductSucBean) msg.getObj();
                        if (bean.getOrde() != null) {
                            Intent intent = new Intent(this, ExchageSuccessActivity.class);
                            intent.putExtra("productSucBean", bean);
                            startActivity(intent);
                        }
                        break;
                    case TASK_INSERTRANKING_LIST:
                        popup.dismiss();
                        ExchangeBean successId = (ExchangeBean) msg.getObj();
                        productsuc(successId.getSuccessid());
                        break;
                    case TASK_RECORD_LIST:
                        if (msg.getObj() != null) {
                            recordDetails = (List<RecordDetail>) msg.getObj();
                        }
                        showRecordListPopup();
                        break;
                    case INDEX_CODE:
                        dismissProgressDialog();
                        indexResultBean = (IndexDataBean) msg.getObj();
                        showLBInfo();
                        makeIndexGoodsData();
                        break;
                    case SIGN_CHECK:
                        try {
                            signCheckResultBeanList = (List<SignCheckBean>) msg.getObj();
                            if (signCheckResultBeanList == null || signCheckResultBeanList.size() == 0) {
                                showHasSigneDays(0);
                                lxSign.setText("您已连续签到0天");
                                return;
                            }
                            signCheckBean = signCheckResultBeanList.get(0);
                            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            String ss = signCheckResultBeanList.get(0).getSigndate();//上次签到的时间
                            Date currdates = new Date(Long.parseLong(ss));
                            String t1 = formatter.format(currdates);

                            long currenttime = System.currentTimeMillis();
                            Date currdate = new Date(currenttime);
                            String t2 = formatter.format(currdate);
                            String v1 = t1.substring(0, 10);
                            String v2 = t2.substring(0, 10);

                            DateFormat formatters = new SimpleDateFormat("yyyy-MM-dd");
                            Date befortime = formatters.parse(v1);
                            Date curtime = formatters.parse(v2);
                            long befortimes = befortime.getTime();
                            long curtimes = curtime.getTime();


                            if (curtimes - befortimes > 1000 * 60 * 60 * 24L) {
                                //断签
                                isduanqian = true;
                                showHasSigneDays(1);
                                return;
                            }
                            isduanqian = false;
                            int keysta = Integer.valueOf(signCheckBean.getKeysta());


                            lxSign.setText("您已连续签到" + signCheckBean.getDays() + "天");

                            SimpleDateFormat sdf = new SimpleDateFormat("dd", Locale.CHINA);
                            //keysta 代表第几个控件已经签了
                            if (sdf.format(new Date()).equals(signCheckBean.getSqlsigndate()) && keysta != 7)
                                showHasSigneDays(keysta);
                            else if (keysta == 7) {
                                //正好7天
                                if (Integer.parseInt(signCheckBean.getSqlsigndate()) == Integer.parseInt(signCheckBean.getAjxsigndate())) {
                                    showHasSigneDays(7);
                                } else {
                                    showHasSigneDays(1);
                                }
                            } else {
                                if (Integer.parseInt(signCheckBean.getSqlsigndate()) == Integer.parseInt(signCheckBean.getAjxsigndate())) {
                                    showHasSigneDays(keysta);
                                } else {
                                    showHasSigneDays(keysta + 1);
                                }

                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        break;
                    case SIGN:

                        String mmsg = msg.getMsg();
                        if (mmsg.equals("1")) {//1:签到成功  0:签到失败
                            ShowSignSuccessView showSignSuccessView = null;
                            if (isduanqian) {
                                ShowSignSuccessView showSignSuccessViews = new ShowSignSuccessView(this, "+" + 1 + "绿币");
                                displayCurrentDays(1);
                                showSignSuccessViews.showPopupWindow();
                                final ShowSignSuccessView finalShowSignSuccessView = showSignSuccessViews;
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finalShowSignSuccessView.dismiss();
                                    }
                                }, 3000);
                                requestIndexData();
                                requestCheckBeforeSign();
                                //本地加上
                                String mscore = (String) SPUtils.get(PonitShopActivity.this, "score", "");
                                double socree;
                                if (signCheckBean == null) {
                                    socree = Double.parseDouble(mscore) + 1.0;
                                } else if (Integer.parseInt(signCheckBean.getDays()) < 7) {
                                    socree = Double.parseDouble(mscore) + keys;
                                } else {
                                    socree = Double.parseDouble(mscore) + 7;
                                }
                                SPUtils.put(PonitShopActivity.this, "score", socree + "");
                                return;
                            }

                            if (signCheckBean == null) {
                                showSignSuccessView = new ShowSignSuccessView(this, "+" + 1 + "绿币");
                                displayCurrentDays(1);
                            } else if (Integer.parseInt(signCheckBean.getScore()) < 7) {
                                showSignSuccessView = new ShowSignSuccessView(this, "+" + keys + "绿币");
                                displayCurrentDays(keys);
                            } else {
                                showSignSuccessView = new ShowSignSuccessView(this, "+" + 7 + "绿币");
                                displayCurrentDays(keys);
                            }
                            showSignSuccessView.showPopupWindow();
                            final ShowSignSuccessView finalShowSignSuccessView = showSignSuccessView;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finalShowSignSuccessView.dismiss();
                                }
                            }, 3000);
                            String mscore = (String) SPUtils.get(PonitShopActivity.this, "score", "");
                            double socree;
                            if (signCheckBean == null) {
                                socree = Double.parseDouble(mscore) + 1.0;
                            } else {
                                socree = Double.parseDouble(mscore) + Double.parseDouble(signCheckBean.getScore());
                            }
                            SPUtils.put(PonitShopActivity.this, "score", socree + "");
                            requestIndexData();
                            requestCheckBeforeSign();
                        } else {
                            ToastUtil.show(this, "今日已签,明天继续!");
                        }
                        break;
                }
            } else {
                dismissProgressDialog();
                //  ToastUtil.show(this, "连接服务器出错,请稍后重试");
            }
        } else {
            dismissProgressDialog();
            // ToastUtil.show(this, "连接服务器出错,请稍后重试");
        }
    }

    /**
     * 初始化头部轮播图
     */
    private void iniHeadView() {
        headUrlList = new ArrayList<>();
        headUrlList.add("http://h.hiphotos.baidu.com/image/pic/item/08f790529822720e23a3d3b777cb0a46f21fab09.jpg");
        forecast_city_picker.setOrientation(com.beyond.popscience.view.Orientation.HORIZONTAL);
        forecast_city_picker.addOnItemChangedListener(this);
//        forecast_city_picker.setItemTransitionTimeMillis(DiscreteScrollViewOptions.getTransitionTime());
        forecast_city_picker.setItemTransformer(new ScaleTransformer.Builder().setMinScale(0.93f).build());
        headAdapter = new HeadAdapter(this, headUrlList);
        infiniteAdapter = InfiniteScrollAdapter.wrap(headAdapter);
        headAdapter.setOnItemClickListener(this);
        forecast_city_picker.setAdapter(infiniteAdapter);
    }


    /**
     * 组装商品列表数据源
     */
    private void makeIndexGoodsData() {
        if (indexResultBean.getList() == null || indexResultBean.getList().size() == 0) {
            rvGoods.setVisibility(GONE);
            noData.setVisibility(View.VISIBLE);
            return;
        }
        if (rvGoods.VISIBLE == GONE) {
            rvGoods.setVisibility(View.VISIBLE);
        }
        initGoodsListAdapter();
    }

    /**
     * 初始化兑换商品列表适配器
     */
    private void initGoodsListAdapter() {
        GoodsAdapter adapter = new GoodsAdapter(this);
        adapter.getDataList().addAll(indexResultBean.getList());
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        rvGoods.setLayoutManager(manager);
        rvGoods.addItemDecoration(new GridSpacingItemDecoration(2, DensityUtil.dp2px(this, 10), false));
        rvGoods.setAdapter(adapter);
        adapter.setOnGoodsClickListener(new GoodsAdapter.OnGoodsClickListener() {
            @Override
            public void onItemClick(View view, IndexGoods info) {
                switch (view.getId()) {
                    case R.id.index_exchange:
                        showPopup(info.getId());
                        break;
                    case R.id.ll_item:
                        Intent intent = new Intent(PonitShopActivity.this, PointGoodsDetailActivity.class);
                        intent.putExtra("id", info.getId()); //传入商品id
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    private void showPopup(final String id) {
        popup = new SlideFromBottomPopup(this);
        popup.setOnItemClickListener(new SlideFromBottomPopup.OnItemClickListener() {
            @Override
            public void onItemClick(View v, String name, String phone, String dizi, String address) {
                switch (v.getId()) {
                    case R.id.write_address_sure:
                        if (name.isEmpty()) {
                            ToastUtil.show(PonitShopActivity.this, "请输入收货人姓名");
                        } else if (phone.isEmpty()) {
                            ToastUtil.show(PonitShopActivity.this, "请输入收货人号码");
                        } else if (dizi.isEmpty()) {
                            ToastUtil.show(PonitShopActivity.this, "请输入地址");
                        } else if (address.isEmpty()) {
                            ToastUtil.show(PonitShopActivity.this, "请输入街道门牌等详细地址");
                        } else {
                            insertrankingList(name, phone, dizi, address, id);
                        }
                        break;
                }
            }
        });
        popup.showPopupWindow();
    }

    private void insertrankingList(String name, String phone, String dizi, String address, String proid) {
        restUsage.insertrankingList(TASK_INSERTRANKING_LIST,
                UserInfoUtil.getInstance().getUserInfo().getUserId(),
                proid,
                name,
                phone,
                dizi,
                address);
    }

    /**
     * 兑换成功接口
     *
     * @param successid 兑换成功id
     */
    private void productsuc(String successid) {
        restUsage.productSuc(TASK_PRODUCT_SUC, successid);
    }

    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * 展示相关数据
     */
    private void showLBInfo() {
        if (indexResultBean.getStu() == null) {
            lbNo.setText("绿币" + SPUtils.get(PonitShopActivity.this, "score", "") + "");
            // lbNo.setText("绿币" + score+ "");
            return;
        }
       /* score = indexResultBean.getStu().getScore();
        lbNo.setText("绿币" + score+ "");*/
        lbNo.setText("绿币" + SPUtils.get(PonitShopActivity.this, "score", "") + "");
    }


    @OnClick({R.id.lb_detail, R.id.lb_exchange_record, R.id.lb_get_ways})
    public void onHorClicked(View view) {
        switch (view.getId()) {
            case R.id.lb_detail:
                Intent intent = new Intent(this, PointDetailActivity.class);
                intent.putExtra("score", score);
                startActivity(intent);
                break;
            case R.id.lb_exchange_record:
                XfjlActivity.startActivity(this);
                break;
            case R.id.lb_get_ways:
                Intent i = new Intent(this, MakePointActivity.class);
                if (signCheckBean != null) {
                    sqlSignData = Integer.valueOf(signCheckBean.getSqlsigndate());
                    ajxSignData = Integer.valueOf(signCheckBean.getAjxsigndate());
                    keySta = Integer.valueOf(signCheckBean.getKeysta());
                }
                //获取数据为空或者断签，则签到+1积分
                if (signCheckBean == null || ajxSignData - sqlSignData > 1) {
                    i.putExtra("sign_days", "1");
                } else if (ajxSignData - sqlSignData == 1) { //连续签到，当天未签到，积分+keysta + 1
                    i.putExtra("sign_days", keySta + 1 + "");
                } else if (ajxSignData == sqlSignData) { //连续签到，当天已签到
                    i.putExtra("sign_days", signCheckBean.getKeysta());
                } else if (Integer.parseInt(signCheckBean.getDays()) >= 1) { // 签到大于7天，积分+7
                    i.putExtra("sign_days", "7");
                }
                startActivity(i);
                break;
        }
    }

    private void showRecordListPopup() {
        if (recordDetails.size() == 0)
            ToastUtil.show(this, "暂无兑换记录");
        else {
            ShowRecordListPopup popup = new ShowRecordListPopup(this, recordDetails);
            popup.showPopupWindow();
        }
    }

    @OnClick({R.id.oneDay, R.id.twoDay, R.id.threeDay, R.id.fourDay, R.id.fiveDay, R.id.sixDay, R.id.sevenDay})
    public void onViewClicked(View view) {
        int days = 0;
        if (signCheckBean != null) {
            if ("7".equals(signCheckBean.getKeysta()))
//                    && signCheckBean.getSqlsigndate().equals(signCheckBean.getAjxsigndate()))
                days = Integer.parseInt(signCheckBean.getDays()) + 1;
            else days = Integer.parseInt(signCheckBean.getDays());
        }
        switch (view.getId()) {
            case R.id.oneDay:
                keys = 1;
                requestSign(keys, days);
                break;
            case R.id.twoDay:
                keys = 2;
                requestSign(keys, days);
                break;
            case R.id.threeDay:
                keys = 3;
                requestSign(keys, days);
                break;
            case R.id.fourDay:
                keys = 4;
                requestSign(keys, days);
                break;
            case R.id.fiveDay:
                keys = 5;
                requestSign(keys, days);
                break;
            case R.id.sixDay:
                keys = 6;
                requestSign(keys, days);
                break;
            case R.id.sevenDay:
                keys = 7;
                requestSign(keys, days);
                break;
        }
    }

    /**
     * 请求兑换记录接口
     */
    private void requestRecordList() {
        // restUsage.recordList(TASK_RECORD_LIST, "1");
        String obj = " {\"page\": \"" + page + "\"}";
        HashMap<String, String> map = new HashMap<>();
        map.put("baseParams", UserInfoUtil.getInstance().getUserInfo().getToken());
        OkhttpUtil.okHttpPostJson(BeyondApplication.BaseUrl + "/user/jiaoyijilu", obj, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                try {
                    zzjl = JSON.parseObject(response, Zzjl.class);
                    if (zzjl.getCode() == 0) {
                        List<Zzjl.DataBean> mdata = zzjl.getData();
                        if (mdata.size() > 0) {
                            ShowRecordListPopupTwo popup = new ShowRecordListPopupTwo(PonitShopActivity.this, mdata);
                            popup.showPopupWindow();
                        } else {
                            ToastUtil.showCenter(PonitShopActivity.this, "暂无交易记录！");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 请求积分商城首页接口
     */
    private void requestIndexData() {
        restUsage.index(INDEX_CODE, userInfo.getUserId());
    }

    /**
     * 签到前的检查
     */
    private void requestCheckBeforeSign() {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userId", userInfo.getUserId());
        restUsage.signCheck(SIGN_CHECK, paramMap);
    }

    /**
     * 签到接口
     */
    private void requestSign(int keys, int days) {
        showProgressDialog();
//        if (days > 0) keys = 7;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("keysta", keys);
        // paramMap.put("days", days);
        paramMap.put("userId", userInfo.getUserId());
        restUsage.sign(SIGN, paramMap);
    }

    @Override
    public void clickItem(int position) {
        Intent intent = new Intent(this, IKnowDetailActivity.class);
        startActivity(intent);
    }
}