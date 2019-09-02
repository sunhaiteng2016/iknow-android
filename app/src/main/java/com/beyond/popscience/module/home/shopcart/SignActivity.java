package com.beyond.popscience.module.home.shopcart;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.PointRestUsage;
import com.beyond.popscience.frame.pojo.UserInfo;
import com.beyond.popscience.frame.pojo.point.SignCheckBean;
import com.beyond.popscience.frame.util.SPUtils;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.view.ShowSignSuccessView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignActivity extends BaseActivity {
    //检查签到

    private static final int SIGN_CHECK = 800003;
    private static final int SIGN = 800004;
    @BindView(R.id.iv_chacha)
    ImageView ivChacha;
    @BindView(R.id.ll_sign_back)
    LinearLayout llSignBack;
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
    @BindView(R.id.ll_sign_ll)
    LinearLayout llSignLl;
    @BindView(R.id.back_iv)
    ImageView backIv;
    private UserInfo userInfo;
    private List<TextView> signViewList;
    private SignCheckBean signCheckBean;
    private int keys;
    private boolean isduanqian = false;
    @Request
    private PointRestUsage pointRestUsage;
    private List<SignCheckBean> signCheckResultBeanList;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_sign;
    }

    @Override
    public void initUI() {
        super.initUI();
        signViewList = new ArrayList<>();
        userInfo = UserInfoUtil.getInstance().getUserInfo();
        addAllSignView();
        requestCheckBeforeSign();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        if (taskId == SIGN_CHECK) {
            if (msg.getIsSuccess()) {
                //这个是检查签到
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
            }
        }

        if (taskId == SIGN) {
            if (msg.getIsSuccess()) {
                String mmsg = msg.getMsg();
                if (mmsg.equals("1")) {//1:签到成功  0:签到失败
                    ShowSignSuccessView showSignSuccessView = null;
                    dismissProgressDialog();
                    if (isduanqian) {
                        ShowSignSuccessView showSignSuccessViews = new ShowSignSuccessView(SignActivity.this, "+" + 1 + "绿币");
                        displayCurrentDays(1);
                        showSignSuccessViews.showPopupWindow();
                        final ShowSignSuccessView finalShowSignSuccessView = showSignSuccessViews;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finalShowSignSuccessView.dismiss();
                            }
                        }, 3000);
                        //requestIndexData();
                        requestCheckBeforeSign();
                        //本地加上
                        String mscore = (String) SPUtils.get(SignActivity.this, "score", "");
                        double socree;
                        if (signCheckBean == null) {
                            socree = Double.parseDouble(mscore) + 1.0;
                        } else if (Integer.parseInt(signCheckBean.getDays()) < 7) {
                            socree = Double.parseDouble(mscore) + keys;
                        } else {
                            socree = Double.parseDouble(mscore) + 7;
                        }
                        SPUtils.put(SignActivity.this, "score", socree + "");
                        return;
                    }

                    if (signCheckBean == null) {
                        showSignSuccessView = new ShowSignSuccessView(SignActivity.this, "+" + 1 + "绿币");
                        displayCurrentDays(1);
                    } else if (Integer.parseInt(signCheckBean.getScore()) < 7) {
                        showSignSuccessView = new ShowSignSuccessView(SignActivity.this, "+" + keys + "绿币");
                        displayCurrentDays(keys);
                    } else {
                        showSignSuccessView = new ShowSignSuccessView(SignActivity.this, "+" + 7 + "绿币");
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
                    String mscore = (String) SPUtils.get(SignActivity.this, "score", "");
                    double socree;
                    if (signCheckBean == null) {
                        socree = Double.parseDouble(mscore) + 1.0;
                    } else {
                        socree = Double.parseDouble(mscore) + Double.parseDouble(signCheckBean.getScore());
                    }
                    SPUtils.put(SignActivity.this, "score", socree + "");
                    // requestIndexData();
                    requestCheckBeforeSign();
                } else {
                    ToastUtil.show(SignActivity.this, "今日已签,明天继续!");
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    /**
     * 签到前的检查
     */
    private void requestCheckBeforeSign() {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userId", userInfo.getUserId());
        pointRestUsage.signCheck(SIGN_CHECK, paramMap);
    }

    public  Handler handler  = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            finish();
        }
    };

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
            //这个地方自动 隐藏
            handler.sendEmptyMessageDelayed(100,2000);
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
            handler.sendEmptyMessageDelayed(100,2000);
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
     * 签到接口
     */
    private void requestSign(int keys, int days) {
        showProgressDialog();
//        if (days > 0) keys = 7;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("keysta", keys);
        // paramMap.put("days", days);
        paramMap.put("userId", userInfo.getUserId());
        pointRestUsage.sign(SIGN, paramMap);
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
     * 点击签到.成功后,对应的当天要显示为已签到
     *
     * @param postion
     */
    private void displayCurrentDays(int postion) {
        signViewList.get(postion - 1).setBackground(getResources().getDrawable(R.drawable.bg_circle_gray));
        signViewList.get(postion - 1).setTextColor(getResources().getColor(R.color.white));
        signViewList.get(postion - 1).setText("已签到");
        handler.sendEmptyMessageDelayed(100,2000);
        signViewList.get(postion - 1).setTextSize(12);
        signViewList.get(postion - 1).setClickable(false);
    }

    @OnClick(R.id.back_iv)
    public void onViewClicked() {
        finish();
    }
}
