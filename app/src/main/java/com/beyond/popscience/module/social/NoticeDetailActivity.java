package com.beyond.popscience.module.social;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.sharesdk.ShareUtil;
import com.beyond.library.sharesdk.WebViewShare;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.SocialRestUsage;
import com.beyond.popscience.frame.pojo.NoticeDetail;
import com.beyond.popscience.locationgoods.http.NotificationRestUsage;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 圈子 公告详情
 * Created by yao.cui on 2017/7/17.
 */

public class NoticeDetailActivity extends BaseActivity {

    private static final int TASK_GET_NOTICE = 1111;
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_ID = "id";
    private static final String KEY_DATE = "date";


    @BindView(R.id.tv_title)
    protected TextView tvTitle;
    @BindView(R.id.tvNoticeTitle)
    protected TextView tvNoticeTitle;
    @BindView(R.id.tvNoticeContent)
    protected TextView tvContent;
    @BindView(R.id.tvAuthor)
    protected TextView tvAuthor;
    @BindView(R.id.tvDate)
    protected TextView tvDate;

    @BindView(R.id.collectedImgView)
    protected ImageView collectedImgView;
    @Request
    private SocialRestUsage restUsage;

    private String id;

    private String author;
    private String date;
    private String MyLike;
    private String title;

    public static void startActivity(Context context, String id, String author, String date) {
        Intent intent = new Intent(context, NoticeDetailActivity.class);
        intent.putExtra(KEY_ID, id);
        intent.putExtra(KEY_AUTHOR, author);
        intent.putExtra(KEY_DATE, date);
        context.startActivity(intent);
    }

    @Request
    NotificationRestUsage notificationRestUsage;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_notice_detail;
    }

    @Override
    public void initUI() {
        super.initUI();
        tvTitle.setText("公告详情");
//        ToastUtil.showCenter(NoticeDetailActivity.this,"恭喜您, + 1 科普绿币!");
        id = getIntent().getStringExtra(KEY_ID);
        author = getIntent().getStringExtra(KEY_AUTHOR);
        date = getIntent().getStringExtra(KEY_DATE);
        tvAuthor.setText(author);
        tvDate.setText(date);
        getNotice();
        showProgressDialog();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        dismissProgressDialog();
        if (taskId == TASK_GET_NOTICE && msg.getIsSuccess()) {
            NoticeDetail detail = (NoticeDetail) msg.getObj();
            MyLike = detail.getMyLike();
            title = detail.getTitle();
            if (MyLike.equals("0")) {    //已经被收藏
                collectedImgView.setImageResource(R.drawable.icon_collection);
            } else {
                collectedImgView.setImageResource(R.drawable.icon_collection_press);
            }
            if (detail != null) {
                tvNoticeTitle.setText(detail.getTitle());
                tvContent.setText(detail.getContent());
                tvAuthor.setText(detail.getAuthor());

            }
        }
        switch (taskId) {
            case 10086:   //新闻收藏
                if (msg.getIsSuccess()) {
                    ToastUtil.showCenter(this, "收藏成功");
                    initBottomView();
                } else {
                    ToastUtil.showCenter(this, "收藏失败");
                }
                dismissProgressDialog();
                break;
            case 10087:   //删除新闻收藏
                if (msg.getIsSuccess()) {
                    ToastUtil.showCenter(this, "取消收藏成功");
                    initBottomView();
                } else {
                    ToastUtil.showCenter(this, "取消收藏失败");
                }
                dismissProgressDialog();
                break;
        }
    }

    /**
     * 初始化底部view
     */
    private void initBottomView() {
        if (MyLike.equals("0")) {    //已经被收藏
            collectedImgView.setImageResource(R.drawable.icon_collection_press);
            MyLike = "1";
        } else {
            collectedImgView.setImageResource(R.drawable.icon_collection);
            MyLike = "0";
        }
    }

    /**
     * 获取公告
     */
    private void getNotice() {
        restUsage.getNoticeDetail(TASK_GET_NOTICE, id);
    }

    /**
     * 收藏
     */
    @OnClick(R.id.collectionLinLay)
    public void collectionClick() {
        if (MyLike.equals("0")) { //未收藏
            requestCollection();
        } else {  //已收藏   这期不做
            requestDelCollection();
        }
    }

    private void requestDelCollection() {
        restUsage.deleteNewCollection(10087, id, "1", null);
    }

    private void requestCollection() {
        restUsage.newCollection(10086, id, "1");
    }

    /**
     * 分享
     */
    @OnClick(R.id.shareLinLay)
    public void shareClick() {
        WebViewShare webViewShare = new WebViewShare();
        webViewShare.setTitle(title);
        webViewShare.setImgUrl("");
        webViewShare.setLink("http://kpnew.appwzd.cn/kepu/share/communityNotice/id/" + id);
        ShareUtil.directShare(this, webViewShare);
    }

}
