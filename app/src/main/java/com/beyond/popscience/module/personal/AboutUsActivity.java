package com.beyond.popscience.module.personal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.ClipBoardUtil;
import com.beyond.library.util.PhoneUtil;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.CommonRestUsage;
import com.beyond.popscience.frame.util.D;
import com.beyond.popscience.frame.util.ImageUtils;
import com.beyond.popscience.frame.util.Util;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 关于我们
 * Created by YAO.CUI on 2017/6/12.
 */

public class AboutUsActivity extends BaseActivity {
    private final int TASK_ABOUT_US = 1601;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.phoneTxtView)
    TextView phoneTxtView;
    @BindView(R.id.qqTxtView)
    TextView qqTxtView;

    @BindView(R.id.tvIntroduction)
    TextView tvIntroduction;
    @BindView(R.id.versionTxtView)
    TextView versionTxtView;

    @Request
    private CommonRestUsage mRestUsage;

    public static void startActivity(Context context){
        Intent intent = new Intent(context,AboutUsActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void initUI() {
        super.initUI();
        tvTitle.setText(getString(R.string.about_us));

        versionTxtView.setText("V"+ Util.getVersionName(this));
        getAboutUs();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId){
            case TASK_ABOUT_US:
                if (msg.getIsSuccess()){
                    HashMap<String,String> content = (HashMap<String, String>) msg.getObj();
                    if (content!= null){
                        String str = content.get("content");
                        if(!TextUtils.isEmpty(str)){
                            tvIntroduction.setText(Html.fromHtml(str));
                        }
                    }
                }
                break;
        }
    }

    private void getAboutUs(){
        mRestUsage.aboutUs(TASK_ABOUT_US);
    }
    @Override
    protected int getLayoutResID() {
        return R.layout.activity_about_us;
    }

    /**
     *
     * @param view
     */
    @OnClick(R.id.phoneTxtView)
    public void phoneClick(View view){
        D.show(this, null, new String[]{"拨打电话"}, true, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PhoneUtil.callPhoneDial(AboutUsActivity.this, phoneTxtView.getText().toString());
            }
        });
    }

    /**
     * qq
     * @param view
     */
    @OnClick(R.id.qqTxtView)
    public void qqClick(View view){
        ClipBoardUtil.copy(qqTxtView.getText().toString(), this);
        ToastUtil.showCenter(this, "复制成功");
    }

    /**
     * 公众号
     * @param view
     */
    @OnClick(R.id.weChatTxtView)
    public void weChatClick(View view){
        ClipBoardUtil.copy("kpzgwzd", this);
        ToastUtil.showCenter(this, "复制成功");
    }

    /**
     * 二维码
     * @param view
     */
    @OnClick(R.id.qrCodeImgView)
    public void qrCodeClick(View view){
        D.show(this, null, new String[]{"保存图片"}, true, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showProgressDialog();
                ImageUtils.saveBitmapToCameraByViewThread(AboutUsActivity.this, BitmapFactory.decodeResource(getResources(), R.drawable.icon_about_qr_code) , new Runnable(){
                    @Override
                    public void run() {
                        dismissProgressDialog();
                    }
                });
            }
        });
    }

}
