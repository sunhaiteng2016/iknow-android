package com.beyond.popscience.module.point;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.PointRestUsage;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class FeedbackActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.deposit_success_button)
    Button depositSuccessButton;
    @BindView(R.id.produce_idea)
    CheckedTextView produceIdea;
    @BindView(R.id.function_idea)
    CheckedTextView functionIdea;
    @BindView(R.id.content_edit)
    EditText contentEdit;
    @BindView(R.id.contact_edit)
    EditText contactEdit;
    private String content;
    private String contactWays;
    @Request
    private PointRestUsage pointRestUsage;
    private static final int FEEDBACK = 8100;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_idea_feedback_layout;
    }

    @Override
    public void initUI() {
        super.initUI();
        tvTitle.setText("意见反馈");
    }

    @OnClick({R.id.ib_back, R.id.deposit_success_button})
    public void onCheckClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.deposit_success_button:
                String content = contentEdit.getText().toString();
                String contact = contactEdit.getText().toString();
                if(TextUtils.isEmpty(content)){
                    Toast.makeText(this,"请输入反馈内容",Toast.LENGTH_LONG).show();
                    return;
                }
                Map<String,Object> map = new HashMap<>();
                map.put("comment",content+" "+contact);
                pointRestUsage.feedBack(FEEDBACK,map);
                break;
        }
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        if (msg.getIsSuccess()) {
            switch (taskId) {
                case FEEDBACK:
                    if (msg.getMsg() != null) {
                        com.beyond.library.util.ToastUtil.show(this, msg.getMsg());
                        ToastUtil.showCenter(this,"恭喜您, + 1 科普绿币!");
                        startActivity(new Intent(FeedbackActivity.this, FeedbackSuccessActivity.class));
                        finish();
                    }
                    break;
            }
        } else {
            com.beyond.library.util.ToastUtil.show(this,"反馈失败,请重试");
        }
    }

    @OnClick({R.id.produce_idea, R.id.function_idea})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.produce_idea:
                produceIdea.setChecked(true);
                functionIdea.setChecked(false);
                break;
            case R.id.function_idea:
                produceIdea.setChecked(false);
                functionIdea.setChecked(true);
                break;
        }
    }

}
