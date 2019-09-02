package com.beyond.popscience.module.home;


import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.okhttp.CallBackUtil;
import com.okhttp.OkhttpUtil;

import org.json.JSONObject;

import okhttp3.Call;

public class DingDanDeltesActivity extends BaseActivity {

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ding_dan_deltes);
    }*/

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_ding_dan_deltes;
    }

    @Override
    public void initUI() {
        super.initUI();
        JSONObject obj = new JSONObject();
        OkhttpUtil.okHttpPostJson("", obj.toString(),new CallBackUtil.CallBackString(){

            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {

            }
        });
    }
}
