package com.beyond.popscience.locationgoods;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.beyond.popscience.R;
import com.beyond.popscience.SpecialLocalProductFragment;
import com.beyond.popscience.frame.base.BaseActivity;

public class ZczxActivity extends BaseActivity {

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_zczx;
    }

    @Override
    public void initUI() {
        super.initUI();
        getSupportFragmentManager().beginTransaction().add(R.id.fl, new SpecialLocalProductFragment()).commit();
    }
}
