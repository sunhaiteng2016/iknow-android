package com.beyond.popscience.module.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.beyond.library.network.net.bean.BaseResponse;
import com.beyond.library.network.net.httpclient.NewCustomResponseHandler;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.base.AppBaseRestUsageV1;
import com.beyond.popscience.module.home.fragment.Constant;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetGroupNameActivity extends BaseActivity {


    @BindView(R.id.go_back)
    RelativeLayout goBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.group_name_submit)
    TextView groupNameSubmit;
    @BindView(R.id.ed_ss)
    EditText edSs;
    private String groupId;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_set_group_name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.go_back, R.id.group_name_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.go_back:
                finish();
                break;
            case R.id.group_name_submit:
                if (edSs.getText().toString().equals("")) {
                    ToastUtil.show(SetGroupNameActivity.this, "请输入群组名称");
                } else {
                    setDatas();
                }
                break;
        }
    }

    @Override
    public void initUI() {
        super.initUI();
        groupId = getIntent().getStringExtra("groupId");
        title.setText("设置群组名称");

    }

    private void setDatas() {
        AppBaseRestUsageV1 appBaseRestUsageV1 = new AppBaseRestUsageV1();
        HashMap<String, String> map = new HashMap();
        map.put("groupId", groupId);
        map.put("groupName", edSs.getText().toString());
        appBaseRestUsageV1.post(BeyondApplication.BaseUrl + "/im/updateGroupName", map, new NewCustomResponseHandler() {
            @Override
            public void onSuccess(int httpStatusCode, Map headerMap, String responseStr) {
                super.onSuccess(httpStatusCode, headerMap, responseStr);
                BaseResponse baseRespnse = JSON.parseObject(responseStr, BaseResponse.class);
                if (baseRespnse.getCode() == 0) {
                    ToastUtil.show(SetGroupNameActivity.this, "修改成功");
                    EventBus.getDefault().post(new ChatGroup(edSs.getText().toString()));
                    finish();
                } else {
                    ToastUtil.show(SetGroupNameActivity.this, "修改失败");
                }
            }

            @Override
            public void onFailure(int statusCode, Map headerMap, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headerMap, responseString, throwable);
            }
        });
    }
}
