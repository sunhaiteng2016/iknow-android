package com.beyond.popscience.locationgoods;


import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.locationgoods.http.NotificationRestUsage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationDetailActivity extends BaseActivity {

    @BindView(R.id.go_back)
    RelativeLayout goBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_content1)
    TextView tvContent1;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.tv_content22)
    TextView tvContent22;
    private String titles, detail, time;
    private int id;

    @Request
    NotificationRestUsage notificationRestUsage;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_notification_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.go_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void initUI() {
        super.initUI();
        titles = getIntent().getStringExtra("title");
        detail = getIntent().getStringExtra("detail");
        time = getIntent().getStringExtra("time");
        id = getIntent().getIntExtra("id", 0);
        tv.setText("尊敬的" + UserInfoUtil.getInstance().getUserInfo().getNickName() + ":");
        title.setText(titles);
        tvContent.setText(detail);
        tvContent1.setText(time);
        tvContent22.setText(titles);
        title.setText("通知详情");
        notificationRestUsage.setIsRead(1008611, id + "");
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case 1008611:
                if (msg.getIsSuccess()) {

                }
                break;
        }
    }
}
