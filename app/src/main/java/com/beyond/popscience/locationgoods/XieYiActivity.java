package com.beyond.popscience.locationgoods;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.locationgoods.http.AddressRestUsage;

import org.apache.http.util.EncodingUtils;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class XieYiActivity extends BaseActivity {


    @BindView(R.id.go_back)
    RelativeLayout goBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_cancle)
    TextView tvCancle;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @Request
    AddressRestUsage addressRestUsage;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_xie_yi;
    }

    @Override
    public void initUI() {
        super.initUI();
        title.setText("入驻协议");
        tvContent.setText(getFromAssets("商户入驻协议.txt"));

    }

    //获取商家状态
    private void getShopStatus() {
        addressRestUsage.getShopStatus(10075);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.go_back, R.id.tv_cancle, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.go_back:
            case R.id.tv_cancle:
                finish();
                break;
            case R.id.tv_submit:
                Intent intent = new Intent(XieYiActivity.this, JoinUsActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case 10075:
                //查询商家状态
                if (msg.getIsSuccess()) {
                    String objStatus = (String) msg.getObj();
                    if (objStatus.equals("1")) {

                    } else {


                    }
                }
                break;
        }
    }

    //从assets 文件夹中获取文件并读取数据
    public String getFromAssets(String fileName) {
        String result = "";
        try {
            InputStream in = getResources().getAssets().open(fileName);
            int lenght = in.available();
            byte[] buffer = new byte[lenght];
            in.read(buffer);
            result = EncodingUtils.getString(buffer, "GBK");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
