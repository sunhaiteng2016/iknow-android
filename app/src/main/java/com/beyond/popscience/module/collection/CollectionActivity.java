package com.beyond.popscience.module.collection;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.view.CustomCommonTabLayout;
import com.beyond.popscience.module.collection.fragment.CollectionFragment;
import com.beyond.popscience.widget.TabEntity;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;

/**
 * 我的收藏
 * Created by linjinfa on 2017/6/10.
 * email 331710168@qq.com
 */
public class CollectionActivity extends BaseActivity implements View.OnClickListener {

    /**
     *
     */
    private final TabEntity[] TABS = {
            new TabEntity("科普资讯", 0, 0),
            new TabEntity("乡镇信息", 0, 0),
            new TabEntity("公告通知", 0, 0)
    };

    /**
     *
     */
    private final String EDIT_TXT = "编辑";
    /**
     *
     */
    @BindView(R.id.tv_title)
    protected TextView titleTxtView;
    /**
     *
     */
    @BindView(R.id.leftTxtView)
    protected TextView leftTxtView;
    /**
     *
     */
    @BindView(R.id.tv_right)
    protected TextView rightTxtView;
    /**
     *
     */
    @BindView(R.id.ib_back)
    protected ImageButton backImgBtn;

    @BindView(R.id.tabLayout)
    protected CustomCommonTabLayout tabLayout;
    /**
     *
     */
    private CollectionFragment currFragment;

    /**
     *
     */
    public static void startActivity(Context context){
        Intent intent = new Intent(context, CollectionActivity.class);
        context.startActivity(intent);
    }

    /**
     *
     */
    public static void startActivity(Fragment fragment){
        Intent intent = new Intent(fragment.getActivity(), CollectionActivity.class);
        fragment.startActivity(intent);
    }


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_collection;
    }

    @Override
    public void initUI() {
        rightTxtView.setOnClickListener(this);
        leftTxtView.setOnClickListener(this);

        titleTxtView.setText("我的收藏");

        switchEditStatus(false);
        initTab();
    }

    /**
     *
     */
    private void initTab(){
        tabLayout.setTabData(new ArrayList<CustomTabEntity>(Arrays.asList(TABS)));
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int i) {
                switchFragment(i);
            }

            @Override
            public void onTabReselect(int i) {

            }
        });
        tabLayout.setCurrentTab(0);
        switchFragment(0);
    }

    /**
     * 切换Fragment
     * @param index
     */
    private void switchFragment(int index){
        currFragment = CollectionFragment.newInstance(index);
        replaceFragment(R.id.tabContent, currFragment, false);
    }

    /**
     * 切换编辑状态
     */
    public void switchEditStatus(boolean isEdit){
        rightTxtView.setVisibility(View.VISIBLE);
        if(isEdit){
            backImgBtn.setVisibility(View.GONE);
            leftTxtView.setVisibility(View.VISIBLE);
            leftTxtView.setText("删除");
            rightTxtView.setText("取消");
        }else{
            backImgBtn.setVisibility(View.VISIBLE);
            leftTxtView.setVisibility(View.GONE);
            rightTxtView.setText(EDIT_TXT);
        }
        if(currFragment!=null){
            currFragment.smoothCloseMenu();
        }
    }

    /**
     * 是否编辑状态
     * @return
     */
    public boolean isEditStatus(){
        return !EDIT_TXT.equals(rightTxtView.getText().toString());
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tv_right: //编辑/取消
                if(currFragment!=null){
                    currFragment.switchEditStatus();
                }
                break;
            case R.id.leftTxtView:  //删除
                if(currFragment!=null){
                    currFragment.delCollection();
                }
                break;
        }
    }

}
