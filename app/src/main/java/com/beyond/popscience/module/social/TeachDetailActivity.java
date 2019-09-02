package com.beyond.popscience.module.social;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.sharesdk.ShareUtil;
import com.beyond.library.sharesdk.WebViewShare;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.SocialRestUsage;
import com.beyond.popscience.frame.pojo.TeachDetail;
import com.beyond.popscience.module.news.VideoPlayerActivity;
import com.beyond.popscience.module.social.adapter.TeachDetailAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 教学详情
 * Created by yao.cui on 2017/7/17.
 */

public class TeachDetailActivity extends BaseActivity {

    private static final String KEY_ID = "id";
    private static final int TASK_GET_DETAIL = 1811;

    @BindView(R.id.lvTeach)
    protected ListView lvTeach;
    @BindView(R.id.tv_title)
    protected TextView tvTitle;
    private TextView tvTeachTitle;
    private TeachDetailAdapter adapter;
    private String id;
    @Request
    private SocialRestUsage restUsage;
    private String titlesss;
    private String pic;

    public static void startActivity(Context context, String id) {
        Intent intent = new Intent(context, TeachDetailActivity.class);
        intent.putExtra(KEY_ID, id);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_teach_detail;
    }

    @Override
    public void initUI() {
        super.initUI();

        ToastUtil.showCenter(TeachDetailActivity.this, "恭喜您, + 1 科普绿币!");
        id = getIntent().getStringExtra(KEY_ID);
        tvTitle.setText("教学详情");
        adapter = new TeachDetailAdapter(this);
        lvTeach.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int count = lvTeach.getHeaderViewsCount();
                VideoPlayerActivity.startActivity(TeachDetailActivity.this,
                        adapter.getItem(position - count).getCoverPic(), adapter.getItem(position - count).getVedioUrl());
            }
        });

        addHeader();//在setadapter之前
        lvTeach.setAdapter(adapter);

        geTeach();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        if (taskId == TASK_GET_DETAIL && msg.getIsSuccess()) {
            TeachDetail detail = (TeachDetail) msg.getObj();
            if (detail != null && detail.getContentList() != null) {
                titlesss = detail.getTitle();
                adapter.getDataList().addAll(detail.getContentList());
                adapter.notifyDataSetChanged();
                pic = detail.getContentList().get(0).getCoverPic();
                tvTeachTitle.setText(detail.getTitle());
            }
        }
    }

    private void addHeader() {
        LinearLayout header = (LinearLayout) getLayoutInflater().inflate(R.layout.teach_detail_title, null, false);
        tvTeachTitle = (TextView) header.findViewById(R.id.tvTeachTitle);
        lvTeach.addHeaderView(header);

    }

    private void geTeach() {
        restUsage.getTeachDetail(TASK_GET_DETAIL, id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.shareLinLay)
    public void onViewClicked() {
        WebViewShare webViewShare = new WebViewShare();
        webViewShare.setTitle(titlesss);
        webViewShare.setImgUrl(pic);
        webViewShare.setLink("http://kpnew.appwzd.cn/kepu/share/communityTeach/id/" + id);
        ShareUtil.directShare(this, webViewShare);
    }
}
