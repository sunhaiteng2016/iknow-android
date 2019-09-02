package com.beyond.popscience.module.social;

import android.content.Context;
import android.content.Intent;
import android.widget.ListView;
import android.widget.TextView;

import com.beyond.library.view.pullrefresh.PullToRefreshBase;
import com.beyond.library.view.pullrefresh.PullToRefreshListView;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.pojo.Comment;
import com.beyond.popscience.module.social.adapter.CommentNoticeAdapter;

import butterknife.BindView;

/**
 * 评论通知
 * Created by linjinfa on 2017/6/24.
 * email 331710168@qq.com
 */
public class CommentNoticeActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    protected TextView titleTxtView;

    @BindView(R.id.listView)
    protected PullToRefreshListView listView;
    private CommentNoticeAdapter commentNoticeAdapter;

    /**
     *
     * @param context
     */
    public static void startActivity(Context context){
        Intent intent = new Intent(context, CommentNoticeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_comment_notice;
    }

    @Override
    public void initUI() {
        super.initUI();

        titleTxtView.setText("评论通知");
        initListView();
    }

    /**
     * 初始化ListView
     */
    private void initListView(){
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });

        commentNoticeAdapter = new CommentNoticeAdapter(this);
        for(int i=0;i<40;i++){
            commentNoticeAdapter.getDataList().add(new Comment());
        }
        listView.getRefreshableView().setAdapter(commentNoticeAdapter);
    }

}
