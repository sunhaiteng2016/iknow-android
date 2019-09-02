package com.beyond.popscience.module.home;

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
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.module.home.entity.ShuaXin;


import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设置备注
 */
public class SetRemarkActivity extends BaseActivity {


    @BindView(R.id.go_back)
    RelativeLayout goBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.releases)
    TextView releases;
    @BindView(R.id.ed_remark)
    EditText edRemark;
    private int friendsId;
    private String userid,imgurl;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_set_remark;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.go_back, R.id.releases})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.go_back:
                finish();
                break;
            case R.id.releases:
                setRemark();
                break;
        }
    }

    @Override
    public void initUI() {
        super.initUI();
        title.setText("备注");
        friendsId = getIntent().getIntExtra("friendsId", 0);
        userid = getIntent().getStringExtra("userid");
        imgurl = getIntent().getStringExtra("imgurl");
    }

    /**
     * 设置备注名
     */
    private void setRemark() {
        HashMap<String, String> map = new HashMap<>();
        map.put("userid", UserInfoUtil.getInstance().getUserInfo().getUserId()+"");
        map.put("friendid", friendsId + "");
        map.put("friendname", edRemark.getText().toString().trim());
        AppBaseRestUsageV1  appBaseRestUsageV1 = new AppBaseRestUsageV1();
        appBaseRestUsageV1.post(BeyondApplication.BaseUrl+"/im/updateFriendName",map,new NewCustomResponseHandler(){
            @Override
            public void onSuccess(int httpStatusCode, Map headerMap, String responseStr) {
                super.onSuccess(httpStatusCode, headerMap, responseStr);
                BaseResponse base = JSON.parseObject(responseStr, BaseResponse.class);
                if (base.getCode()==0){
                    ToastUtil.show(SetRemarkActivity.this,"设置成功！");
                    //刷新列表
                    EventBus.getDefault().post(new ShuaXin());
                    finish();
                }else {
                    ToastUtil.show(SetRemarkActivity.this,base.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Map headerMap, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headerMap, responseString, throwable);
            }
        });

    }
}
