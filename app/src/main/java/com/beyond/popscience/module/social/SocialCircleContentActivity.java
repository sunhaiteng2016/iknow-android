package com.beyond.popscience.module.social;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.beyond.library.util.DensityUtil;
import com.beyond.library.view.pullrefresh.PullToRefreshBase;
import com.beyond.library.view.pullrefresh.PullToRefreshListView;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.pojo.SocialInfo;
import com.beyond.popscience.frame.util.Util;
import com.beyond.popscience.module.social.adapter.SocialCircleContentAdapter;

import butterknife.BindView;

/**
 * 圈子内容
 * Created by linjinfa on 2017/6/22.
 * email 331710168@qq.com
 */
public class SocialCircleContentActivity extends BaseActivity {

    private final static String EXTRA_SOCIAL_INFO_KEY = "socialInfo";

    /**
     *
     */
    @BindView(R.id.topReLay)
    protected RelativeLayout topReLay;
    @BindView(R.id.listView)
    protected PullToRefreshListView listView;
    @BindView(R.id.topPicImgView)
    protected ImageView topPicImgView;
    @BindView(R.id.topBgView)
    protected View topBgView;
    private View headerView;
    private ImageView headerPicImgView;
    private SocialInfo socialInfo;
    private SocialCircleContentAdapter socialCircleContentAdapter;

    /**
     *
     * @param context
     * @param socialInfo
     */
    public static void startActivity(Context context, SocialInfo socialInfo){
        Intent intent = new Intent(context, SocialCircleContentActivity.class);
        intent.putExtra(EXTRA_SOCIAL_INFO_KEY, socialInfo);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_social_circle_content;
    }

    @Override
    public void initUI() {
        socialInfo = (SocialInfo) getIntent().getSerializableExtra(EXTRA_SOCIAL_INFO_KEY);
        if(socialInfo == null){
            backNoAnim();
            return ;
        }

        initListView();
    }

    /**
     * 初始化ListView
     */
    private void initListView(){
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                topBgView.setAlpha(calAlpha(Math.abs(headerView.getTop())));
            }
        });

        addHeader();
        socialCircleContentAdapter = new SocialCircleContentAdapter(this);
        for(int i=0;i<100;i++){
            socialCircleContentAdapter.getDataList().add(new SocialInfo());
        }
        listView.setAdapter(socialCircleContentAdapter);
    }

    /**
     * 初始化header
     */
    private void addHeader(){
        headerView = View.inflate(this, R.layout.adapter_social_circle_content_header, null);
        headerPicImgView = (ImageView) headerView.findViewById(R.id.headerPicImgView);

        int imgHeight = Util.getImageHeight(DensityUtil.getScreenWidth(this));
        topPicImgView.getLayoutParams().height = imgHeight;
        headerPicImgView.getLayoutParams().height = imgHeight;

        listView.setZoomView(topPicImgView);
        listView.setZoomViewHeight(imgHeight);

        TabLayout tabLayout = (TabLayout) headerView.findViewById(R.id.tabLayout);
        LinearLayout linearLayout = (LinearLayout) tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout.setDividerDrawable(ActivityCompat.getDrawable(this, R.drawable.layout_divider_vertical));
        linearLayout.setDividerPadding(20);

        tabLayout.addTab(tabLayout.newTab().setText("圈子"));
        tabLayout.addTab(tabLayout.newTab().setText("公告"));
        tabLayout.addTab(tabLayout.newTab().setText("教学"));
        tabLayout.addTab(tabLayout.newTab().setText("成员"));

        listView.addHeaderView(headerView, null, false);
    }

    /**
     * 计算alpha  y=ax+b
     * @param x
     * @return
     */
    private float calAlpha(int x){
        float b = 0;
        float a = 1.0f / (headerView.getMeasuredHeight() - topReLay.getMeasuredHeight());
        float y = a * x + b;
        return y;
    }

}
