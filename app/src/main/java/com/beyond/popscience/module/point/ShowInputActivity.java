package com.beyond.popscience.module.point;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;

public class ShowInputActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_input_layout);
    }

    @Override
    protected int getLayoutResID() {
        return 0;
    }
}
