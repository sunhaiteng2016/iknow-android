package com.beyond.popscience.module.home;


import com.beyond.popscience.AddressListFragment;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;

public class AddressListActivity extends BaseActivity {


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_address_list;
    }
    @Override
    public void initUI() {
        super.initUI();
        getSupportFragmentManager().beginTransaction().add(R.id.fl,new AddressListFragment()).commit();
    }
}
