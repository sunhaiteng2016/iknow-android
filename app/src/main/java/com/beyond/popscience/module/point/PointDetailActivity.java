package com.beyond.popscience.module.point;


import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.view.pullrefresh.PullToRefreshBase;
import com.beyond.library.view.pullrefresh.PullToRefreshListView;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.CommonRestUsage;
import com.beyond.popscience.frame.net.PointRestUsage;
import com.beyond.popscience.frame.pojo.CommentResponse;
import com.beyond.popscience.frame.pojo.PointDetailResult;
import com.beyond.popscience.frame.pojo.UserInfo;
import com.beyond.popscience.frame.util.D;
import com.beyond.popscience.frame.util.SPUtils;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.module.point.adapter.DetailAdapter;

import java.text.DecimalFormat;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 积分明细  item_point_detail
 */
public class PointDetailActivity extends BaseActivity {

    private static final int TASK_DETAIL_LIST = 800005;
    /**
     * 获取阅读竞赛绿币
     */
    private final int REQUEST_SCORE_TASK_ID = 104;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.my_point_detail)
    PullToRefreshListView listView;
    TextView tvPoint;
    TextView tvMoney;

    private String score;//绿币数
    private String money;//可提现金额
    private DetailAdapter adapter;
    private PointDetailResult pointDetailResult;
    private int page = 1;

    @Request
    PointRestUsage restUsage;
    private TextView tv_right;
    @Request
    private CommonRestUsage commonRestUsage;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_point_detail;
    }

    @Override
    public void initUI() {
        super.initUI();
        tvTitle.setText("绿币明细");
        View headView = getLayoutInflater().inflate(R.layout.point_details_item_header, null);
        tvPoint = (TextView) headView.findViewById(R.id.point_tv);
        tvMoney = (TextView) headView.findViewById(R.id.tv_exchange_money);
        listView.addHeaderView(headView);
        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_right.setText("审核记录");
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(PointDetailActivity.this,ShenHeActivity.class);
                startActivity(intent);
            }
        });
        //总绿币数
        score = SPUtils.get(PointDetailActivity.this, "score", "") + "";
        //tvPoint.setText(score);
        DecimalFormat df = new DecimalFormat("0.00");
        money = df.format(Double.valueOf(score) / 800.00);
        tvMoney.setText("可兑换" + money + "元现金");
        initListView();
        getPointDetails();
        requestScore();
    }
    /**
     * 请求绿币
     */
    private void requestScore() {
        commonRestUsage.getScore(REQUEST_SCORE_TASK_ID);
    }
    /**
     * 初始化ListView
     */
    private void initListView() {
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                getPointDetails();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                getPointDetails();
            }
        });

        adapter = new DetailAdapter(this);
        listView.getRefreshableView().setAdapter(adapter);
        listView.setRefreshing();
    }

    @OnClick({R.id.tv_withdraw, R.id.tv_exchange})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_exchange:
                finish();
                break;
            case R.id.tv_withdraw:
                if (Float.valueOf(money) >=30) {
                    Intent i = new Intent(this, DepositMoneyActivity.class);
                    i.putExtra("money", money);
                    i.putExtra("stexchang", "800.00");
                    startActivity(i);
                } else {
                    D.show(this, "温馨提示", "提现金额不足30元，暂不支持提现", null, "确定", null);
                }
                break;

        }
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case TASK_DETAIL_LIST:
                if (msg.getIsSuccess()) {
                    pointDetailResult = (PointDetailResult) msg.getObj();
                    if (pointDetailResult != null) {
                        if (pointDetailResult.getList() != null && pointDetailResult.getList().size() != 0) {
                            if (listView.isPullDownToRefresh()) {
                                adapter.getDataList().clear();
                            }
                            adapter.getDataList().addAll(pointDetailResult.getList());
                            adapter.notifyDataSetChanged();
                            setHeaderData(pointDetailResult);
                        }
                    }
                }
                if (pointDetailResult == null || pointDetailResult.getList() == null
                        || pointDetailResult.getList().size() == 0
                        || pointDetailResult.getCurrentPage() >= pointDetailResult.getTotalPage()) {   //加载结束
                    listView.onLoadMoreCompleteAndNoData();
                } else {
                    listView.onLoadMoreComplete();
                    setHeaderData(pointDetailResult);
                }
                break;

            case REQUEST_SCORE_TASK_ID: //请求绿币
                Log.e("==我的绿币===", msg.getIsSuccess().toString());
                if (msg.getIsSuccess()) {
                    HashMap<String, String> map = (HashMap<String, String>) msg.getObj();
                    if (map != null) {
                        score = map.get("score");
                        if (!TextUtils.isEmpty(score)) {
                            tvPoint.setText(score);
                            UserInfo userInfo = UserInfoUtil.getInstance().getUserInfo();
                            userInfo.setScore(score);
                            UserInfoUtil.getInstance().saveUserInfo(userInfo);
                            SPUtils.put(PointDetailActivity.this,"score",score);
//                            tempScore = score;
                        } else {
//                            myIntegralTxtView.setText("0");
                            tvPoint.setText(SPUtils.get(PointDetailActivity.this,"score","") + "");
                        }
                    } else {
//                        myIntegralTxtView.setText("0");
                    }
                } else {
//                    myIntegralTxtView.setText("0");
                }

                break;
        }
    }

    private void setHeaderData(PointDetailResult pointDetailResult) {
        DecimalFormat df = new DecimalFormat("0.00");
        String sss=pointDetailResult.getStexchang();
        money = df.format(Double.valueOf(score) / Double.valueOf(sss));
        tvMoney.setText("可兑换" + money + "元现金");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void getPointDetails() {
        restUsage.detailList(TASK_DETAIL_LIST, UserInfoUtil.getInstance().getUserInfo().getUserId(), page);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        requestScore();
    }
}
