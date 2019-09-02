package com.beyond.popscience.module.point;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;

import butterknife.OnClick;

public class FeedbackSuccessActivity extends BaseActivity {


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_idea_feedback_success_layout;
    }

    @OnClick(R.id.lyt_dismiss)
    public void onClick() {
        finish();
    }
}
