package com.beyond.popscience.module.personal;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.CommonRestUsage;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 反馈
 * Created by yao.cui on 2017/6/12.
 */

public class FeedbackActivity extends BaseActivity {

    private final int TASK_FEEDBACK = 1701;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.etFeedback)
    EditText etFeedback;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;

    @Request
    private CommonRestUsage mRestUsage;

    public static void startActivity(Context context){
        Intent intent = new Intent(context,FeedbackActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void initUI() {
        super.initUI();
        tvTitle.setText(getString(R.string.feedback));
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        if (taskId == TASK_FEEDBACK&&msg.getIsSuccess()){
            ToastUtil.showCenter(this,"谢谢反馈");
        }
    }

    @OnClick(R.id.tvSubmit)
    public void submit(View view){
        if (!TextUtils.isEmpty(etFeedback.getText())){
            mRestUsage.postFeedback(TASK_FEEDBACK,etFeedback.getText().toString());
        } else {
            ToastUtil.showCenter(this,"请填写反馈内容");
        }
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_feedback;
    }
}
