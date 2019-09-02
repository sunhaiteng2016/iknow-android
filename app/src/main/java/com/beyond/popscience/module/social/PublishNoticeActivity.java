package com.beyond.popscience.module.social;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.DateUtil;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.SocialRestUsage;
import com.beyond.popscience.frame.pojo.PublishNoticeResult;
import com.beyond.popscience.frame.pojo.PushMsg;
import com.beyond.popscience.frame.util.NotifactionUtil;
import com.just.library.IFileUploadChooser;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 圈子-- 发布公告
 * Created by yao.cui on 2017/7/17.
 */

public class PublishNoticeActivity  extends BaseActivity{
    private static final int TASK_PUBLISH_NOTICE = 1901;
    private static final String KEY_ID = "id";

    @BindView(R.id.tv_title)
    protected TextView tvTitle;
    @BindView(R.id.tv_right)
    protected TextView tvRight;

    @BindView(R.id.etTitle)
    protected EditText etTitle;
    @BindView(R.id.etName)
    protected EditText etName;
    @BindView(R.id.etContent)
    protected EditText etContent;
    @Request
    private SocialRestUsage restUsage;
    private String id;

    public static void startActivity(Context context,String id){
        Intent intent = new Intent(context,PublishNoticeActivity.class);
        intent.putExtra(KEY_ID,id);
        context.startActivity(intent);
    }

    @Override
    public void initUI() {
        super.initUI();
        tvTitle.setText("发布公告");
        tvRight.setText("确定");
        tvRight.setVisibility(View.VISIBLE);

        id = getIntent().getStringExtra(KEY_ID);
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        dismissProgressDialog();
        if (taskId == TASK_PUBLISH_NOTICE && msg.getIsSuccess()){
            ToastUtil.showCenter(this,"发布成功");
            PublishNoticeResult result = (PublishNoticeResult) msg.getObj();
            NoticeDetailActivity.startActivity(this,result.getNoticeId(),etName.getText().toString(),
                    DateUtil.toPattern(DateUtil.getNowString(),DateUtil.DATEMONTHCZDAY));
            finish();
        }
    }

    @OnClick(R.id.tv_right)
    public void confirm(View view){
        if (TextUtils.isEmpty(etTitle.getText())){
            ToastUtil.showCenter(this,"请输入标题");
        } else if (TextUtils.isEmpty(etName.getText())){
            ToastUtil.showCenter(this,"请输入发布人姓名");
        } else if(TextUtils.isEmpty(etContent.getText())){
            ToastUtil.showCenter(this,"请输入内容");
        } else{
            showProgressDialog();
            restUsage.publishNotice(TASK_PUBLISH_NOTICE,id,etTitle.getText().toString(),
                    etName.getText().toString(),etContent.getText().toString());
        }
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_publish_notice;
    }
}
