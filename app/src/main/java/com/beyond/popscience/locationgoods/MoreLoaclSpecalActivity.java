package com.beyond.popscience.locationgoods;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.utils.CreatLayoutUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoreLoaclSpecalActivity extends BaseActivity {


    @BindView(R.id.go_back)
    RelativeLayout goBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.leftListView)
    RecyclerView leftListView;
    @BindView(R.id.rightListView)
    RecyclerView rightListView;
    private List<String> lists= new ArrayList<>();
    private CommonAdapter<String> leftAdapter;
    private CommonAdapter<String> rightAdapter;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_more_loacl_specal;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.go_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void initUI() {
        super.initUI();
        initListView();
    }

    private void initListView() {
        CreatLayoutUtils.creatLinearLayout(this,leftListView);
        lists.add("");
        lists.add("");
        lists.add("");
        lists.add("");
        lists.add("");
        lists.add("");
        lists.add("");
        lists.add("");
        leftAdapter = new CommonAdapter<String>(this,R.layout.item_left_list,lists){

            @Override
            protected void convert(ViewHolder holder, String s, int position) {
            }
        };
        leftListView.setAdapter(leftAdapter);

        CreatLayoutUtils.cretGridViewLayout(this,rightListView,2);
        rightAdapter = new CommonAdapter<String>(this,R.layout.item_right_list,lists){

            @Override
            protected void convert(ViewHolder holder, String s, int position) {

            }
        };
        rightListView.setAdapter(rightAdapter);
    }
}
