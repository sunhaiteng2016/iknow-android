package com.beyond.popscience.module.personal;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.CommonRestUsage;
import com.beyond.popscience.frame.util.ImageLoaderUtil;

import java.util.HashMap;

import butterknife.BindView;

/**
 * 关于我们
 * Created by YAO.CUI on 2017/6/12.
 */

public class HelpActivity extends BaseActivity {
    private final int TASK_HELP = 1901;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_help_1)
    ImageView ivHelp1;
    @BindView(R.id.iv_help_2)
    ImageView ivHelp2;
    @BindView(R.id.shadowView)
    View shadowView;

    @Request
    private CommonRestUsage mRestUsage;

    public static void startActivity(Context context){
        Intent intent = new Intent(context,HelpActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void initUI() {
        super.initUI();
        tvTitle.setText(getString(R.string.fragment_my_help));
        shadowView.setVisibility(View.GONE);
        getHelp();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId){
            case TASK_HELP:
                if (msg.getIsSuccess()){
                    HashMap<String,String> content = (HashMap<String, String>) msg.getObj();
                    if (content!= null){
                        String urlStrings = content.get("content");
                        if(!TextUtils.isEmpty(urlStrings)){
                            String[] urls = urlStrings.split(",");
                            if(urls!=null && urls.length>=1){
                                ImageLoaderUtil.display(this,urls[0],ivHelp1);
                                if(urls.length>=2){
                                    ImageLoaderUtil.display(this,urls[1],ivHelp2);
                                }
                            }
                        }
                    }
                }
                break;
        }
    }

    private void getHelp(){
        mRestUsage.help(TASK_HELP);
    }
    @Override
    protected int getLayoutResID() {
        return R.layout.activity_help;
    }

}
