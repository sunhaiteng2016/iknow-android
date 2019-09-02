package com.beyond.popscience.module.home;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.network.net.bean.BaseResponse;
import com.beyond.library.sharesdk.ShareUtil;
import com.beyond.library.sharesdk.WebViewShare;
import com.beyond.library.util.ToastUtil;
import com.beyond.library.view.pullrefresh.PullToRefreshBase;
import com.beyond.library.view.pullrefresh.PullToRefreshListView;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.NewsRestUsage;
import com.beyond.popscience.frame.net.TownNoticeRestUsage;
import com.beyond.popscience.frame.pojo.NewsDetailObj;
import com.beyond.popscience.frame.pojo.NewsRecommendObj;
import com.beyond.popscience.frame.pojo.NewsRecommendResponse;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.SPUtils;
import com.beyond.popscience.frame.util.VKConstants;
import com.beyond.popscience.frame.util.VideoDownloader;
import com.beyond.popscience.locationgoods.dIalog.AdjSelPopWindow;
import com.beyond.popscience.locationgoods.http.NotificationRestUsage;
import com.beyond.popscience.module.home.entity.News;
import com.beyond.popscience.module.news.NewsDetailActivity;
import com.beyond.popscience.module.news.adapter.NewsDetailListAdapter;
import com.beyond.popscience.module.news.adapter.TextSizeListAdapter;
import com.beyond.popscience.utils.sun.util.OpenFileUtil;
import com.tencent.smtt.sdk.TbsReaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 公告详情
 * Created by linjinfa on 2017/6/25.
 * email 331710168@qq.com
 */
public class AnnouncementActivity extends BaseActivity {

    /**
     * 请求新闻详情
     */
    private final int REQUEST_NEWS_DETAIL_TASK_ID = 101;
    /**
     * 新闻收藏
     */
    private final int REQUEST_NEWS_COLLECTION_TASK_ID = 102;
    /**
     * 删除收藏
     */
    private final int REQUEST_DEL_NEWS_COLLECTION_TASK_ID = 104;

    /**
     * 获取推荐新闻
     */
    private final int REQUEST_RECOMMEND_NEWS_TASK_ID = 105;
    /**
     *
     */
    private final static String EXTRA_NEWS_KEY = "news";

    /**
     *
     */
    @BindView(R.id.tv_title)
    protected TextView titleTxtView;
    /**
     *
     */
    @BindView(R.id.rightImgView)
    protected ImageView rightImgView;

    /**
     *
     */
    @BindView(R.id.listView)
    protected PullToRefreshListView listView;
    /**
     *
     */
    @BindView(R.id.emptyReLay)
    protected RelativeLayout emptyReLay;
    /**
     *
     */
    @BindView(R.id.collectedImgView)
    protected ImageView collectedImgView;
    /**
     *
     */
    private TextView editorTxtView;
    /**
     *
     */
    private TextView newsTitleTxtView;

    /**
     *
     */
    private NewsDetailListAdapter newsDetailListAdapter;

    @Request
    private TownNoticeRestUsage townNoticeRestUsage;

    @Request
    private NewsRestUsage mRestUsage;

    private News news;
    /**
     *
     */
    private NewsDetailObj newsDetailObj;

    View recommendNews1, recommendNews2, recommendNews3;
    private ListView luokuan_lv;
    private TextView adj;
    private TextView footerPublishTimeTxtView;
    private ListView adj_list;
    private LinearLayout ll_yulan;

    @Override
    protected void onResume() {
        super.onResume();
        requestNewRecommend();
    }

    /**
     *
     */
    public static void startActivity(Context context, News news) {
        Intent intent = new Intent(context, AnnouncementActivity.class);
        intent.putExtra(EXTRA_NEWS_KEY, news);
        context.startActivity(intent);
    }

    /**
     * @param fragment
     */
    public static void startActivity(Fragment fragment, News news) {
        Intent intent = new Intent(fragment.getActivity(), AnnouncementActivity.class);
        intent.putExtra(EXTRA_NEWS_KEY, news);
        fragment.startActivity(intent);
    }


    @Request
    NotificationRestUsage notificationRestUsage;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_announcement;
    }

    @Override
    public void initUI() {
        super.initUI();
//        ToastUtil.showCenter(AnnouncementActivity.this,"恭喜您, + 1 科普绿币!");
        news = (News) getIntent().getSerializableExtra(EXTRA_NEWS_KEY);
        if (news == null || TextUtils.isEmpty(news.newsId)) {
            backNoAnim();
            return;
        }
        titleTxtView.setText("公告详情");
        rightImgView.setVisibility(View.VISIBLE);
        rightImgView.setImageResource(R.drawable.icon_dots);
        initListView();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case REQUEST_NEWS_DETAIL_TASK_ID:   //新闻详情
                if (msg.getIsSuccess()) {
                    BaseResponse<NewsDetailObj> baseResponse = msg.getBaseResponse();
                    if (baseResponse.getData() != null) {
                        switchListEmptyView(false);
                        newsDetailObj = baseResponse.getData();
                        initHeader(newsDetailObj);
                        initFooter(newsDetailObj);
                        initBottomView();

                        if (newsDetailObj.getContent() != null) {
                            newsDetailListAdapter.setThumbUrl(newsDetailObj.getTopPic());
                            newsDetailListAdapter.getDataList().clear();
                            newsDetailListAdapter.getDataList().addAll(newsDetailObj.getContent());
                            newsDetailListAdapter.notifyDataSetChanged();
                        }
                    } else {
                        switchListEmptyView(true);
                    }
                } else {
                    switchListEmptyView(true);
                }
                dismissProgressDialog();
                break;
            case REQUEST_NEWS_COLLECTION_TASK_ID:   //新闻收藏
                if (msg.getIsSuccess()) {
                    ToastUtil.showCenter(this, "收藏成功");
                    if (newsDetailObj != null) {
                        newsDetailObj.setCollected(true);
                        newsDetailObj.setLikeCount(String.valueOf(newsDetailObj.getLikeCountLong() + 1));
                        initBottomView();
                    }
                } else {
                    ToastUtil.showCenter(this, "收藏失败");
                }
                dismissProgressDialog();
                break;
            case REQUEST_DEL_NEWS_COLLECTION_TASK_ID:   //删除新闻收藏
                if (msg.getIsSuccess()) {
                    ToastUtil.showCenter(this, "取消收藏成功");
                    if (newsDetailObj != null) {
                        newsDetailObj.setCollected(false);
                        newsDetailObj.setLikeCount(String.valueOf(newsDetailObj.getLikeCountLong() - 1));
                        initBottomView();
                    }
                } else {
                    ToastUtil.showCenter(this, "取消收藏失败");
                }
                dismissProgressDialog();
                break;
            case REQUEST_RECOMMEND_NEWS_TASK_ID:
                if (msg.getIsSuccess()) {
                    NewsRecommendResponse newsRecommendResponse = (NewsRecommendResponse) msg.getObj();
                    if (newsRecommendResponse != null) {
                        setRecommedNews(newsRecommendResponse.getNewsList());
                    }
                }
                dismissProgressDialog();
                break;
        }
    }

    /**
     * 初始化ListView
     */
    private void initListView() {
        listView.setMode(PullToRefreshBase.Mode.DISABLED);

        addHeader();
        addFooter();

        newsDetailListAdapter = new NewsDetailListAdapter(this);
        listView.getRefreshableView().setAdapter(newsDetailListAdapter);
        requestNewsDetail();
    }

    /**
     * 初始化header
     */
    private void addHeader() {
        View headerView = View.inflate(this, R.layout.adapter_news_detail_header2, null);
        newsTitleTxtView = (TextView) headerView.findViewById(R.id.newsTitleTxtView);

        listView.addHeaderView(headerView, null, false);
    }

    /**
     * 初始化header数据
     */
    private void initHeader(final NewsDetailObj newsDetailObj) {
        if (newsDetailObj != null) {
            newsTitleTxtView.setText(newsDetailObj.getTitle());
        }
    }

    /**
     * 添加footer
     */
    private void addFooter() {
        View footerView = View.inflate(this, R.layout.adapter_news_detail_footer, null);
        editorTxtView = (TextView) footerView.findViewById(R.id.editorTxtView);
        LinearLayout bottomLinLay = (LinearLayout) footerView.findViewById(R.id.bottomLinLay);
        bottomLinLay.setVisibility(View.GONE);
//        praiseTxtView = (TextView) footerView.findViewById(R.id.praiseTxtView);
//        treadTxtView = (TextView) footerView.findViewById(R.id.treadTxtView);
//        praiseImgView = (ImageView) footerView.findViewById(R.id.praiseImgView);
//        treadImgView = (ImageView) footerView.findViewById(R.id.treadImgView);
//        //点赞
//        praiseLiner = (LinearLayout) footerView.findViewById(R.id.praiseLinear);
//        praiseLiner.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                requestPraise();
//            }
//        });
//        //不喜欢
//        treadLinear = (LinearLayout) footerView.findViewById(R.id.treadLinear);
//        treadLinear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                requestUnLike();
//            }
//        });
        //新闻推荐
        recommendNews1 = footerView.findViewById(R.id.recommendNewsOne);
        recommendNews2 = footerView.findViewById(R.id.recommendNewsTwo);
        recommendNews3 = footerView.findViewById(R.id.recommendNewsThree);
        ll_yulan = (LinearLayout) footerView.findViewById(R.id.ll_yulan);


        luokuan_lv = (ListView) footerView.findViewById(R.id.luokuan_lv);
        adj_list = (ListView) footerView.findViewById(R.id.adj_list);
        adj = (TextView) footerView.findViewById(R.id.adj);
        footerPublishTimeTxtView = (TextView) footerView.findViewById(R.id.footerPublishTimeTxtView);

        listView.getRefreshableView().addFooterView(footerView, null, false);
    }

    /**
     * 初始化footer数据
     */
    private void initFooter(final NewsDetailObj newsDetailObj) {
        if (newsDetailObj != null) {
            editorTxtView.setText(newsDetailObj.getAuchor());
//            praiseTxtView.setText(TextUtils.isEmpty(newsDetailObj.getVoteNum())?"0":newsDetailObj.getVoteNum());
//            treadTxtView.setText(TextUtils.isEmpty(newsDetailObj.getDislikeNum())?"0":newsDetailObj.getDislikeNum());
//
//            if(newsDetailObj.isVote()){ //已经点赞
//                praiseImgView.setImageResource(R.drawable.icon_like_red);
//
//            }else{
//                praiseImgView.setImageResource(R.drawable.icon_like);
//            }
//
//            praiseLiner.setSelected(newsDetailObj.isVote());
//            praiseTxtView.setSelected(newsDetailObj.isVote());
//
//            if(newsDetailObj.isDislike()){ //已经不喜欢
//                treadImgView.setImageResource(R.drawable.icon_unlike_blue);
//            }else{
//                treadImgView.setImageResource(R.drawable.icon_unlike);
//            }
//
//            treadLinear.setSelected(newsDetailObj.isDislike());
//            treadTxtView.setSelected(newsDetailObj.isDislike());


            final TbsReaderView mTbsReaderView = new TbsReaderView(AnnouncementActivity.this, new TbsReaderView.ReaderCallback() {
                @Override
                public void onCallBackAction(Integer integer, Object o, Object o1) {

                }
            });
            ll_yulan.addView(mTbsReaderView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            //添加落款
            luokuan_lv.setAdapter(new LkAdapter(AnnouncementActivity.this, newsDetailObj.getSignOff()));
            footerPublishTimeTxtView.setText(newsDetailObj.getPublishTime());
            final List<NewsDetailObj.adj> fileList = newsDetailObj.getFileList();
            if (null != fileList && fileList.size() > 0) {
                List<String> list = new ArrayList<>();
                for (NewsDetailObj.adj adj1 : fileList) {
                    list.add(adj1.getFilename());
                }
                adj_list.setAdapter(new AdjAdapter(AnnouncementActivity.this, list));

                adj_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //  downloadClick(newsDetailObj.getFileaddress(), newsDetailObj.getFilename());
                        downloadClick(fileList.get(i).getFileaddress(), fileList.get(i).getFilename());
                    }
                });
            }
        }
    }


    /**
     * 下载
     */
    private void downloadClick(final String url, final String fileName) {
        if (TextUtils.isEmpty(url) || !checkPermission()) {
            return;
        }
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AnnouncementActivity.this);
        builder.setTitle("提示");
        builder.setMessage("本次下载将会消耗流量\n请在Wi-Fi条件下载");
        builder.setCancelable(false);

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                VideoDownloader.get().download(url, fileName, VKConstants.SYSTEM_CAMERA_IMG_VIDEO, new VideoDownloader.OnDownloadListener() {
                    @Override
                    public void onDownloadStart() {

                    }

                    @Override
                    public void onDownloadSuccess(String url, final String filePath) {
                        //ToastUtil.show(AnnouncementActivity.this,"文件存储地址："+filePath);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //弹窗
                                AdjSelPopWindow popWindow = new AdjSelPopWindow(AnnouncementActivity.this, filePath);
                                popWindow.show(adj_list);

                            }
                        });
                    }

                    @Override
                    public void onDownloading(int progress) {

                    }

                    @Override
                    public void onDownloadFailed() {
                        //ToastUtil.show(AnnouncementActivity.this,"下载失败");
                    }
                });
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //>=6.0检查运行时权限
            int grantCode = PermissionChecker.checkSelfPermission(AnnouncementActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE");
            if (grantCode != PackageManager.PERMISSION_GRANTED) { //未授权
                if (AnnouncementActivity.this instanceof Activity) {
                    ActivityCompat.requestPermissions((Activity) AnnouncementActivity.this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 101);
                }
                return false;
            }
        }
        return true;
    }

    private void setRecommedNews(final List<NewsRecommendObj> newsRecommendList) {

        TextView tvTitle1 = (TextView) recommendNews1.findViewById(R.id.tvTitle);
        TextView tvTitle2 = (TextView) recommendNews2.findViewById(R.id.tvTitle);
        TextView tvTitle3 = (TextView) recommendNews3.findViewById(R.id.tvTitle);
        tvTitle1.setText(newsRecommendList.get(0).getTitle());
        tvTitle2.setText(newsRecommendList.get(1).getTitle());
        tvTitle3.setText(newsRecommendList.get(2).getTitle());

        ImageView imageView1 = (ImageView) recommendNews1.findViewById(R.id.img);
        ImageView imageView2 = (ImageView) recommendNews2.findViewById(R.id.img);
        ImageView imageView3 = (ImageView) recommendNews3.findViewById(R.id.img);
        ImageLoaderUtil.displayImage(this, newsRecommendList.get(0).getPics(), imageView1);
        ImageLoaderUtil.displayImage(this, newsRecommendList.get(1).getPics(), imageView2);
        ImageLoaderUtil.displayImage(this, newsRecommendList.get(2).getPics(), imageView3);

        TextView tvTip1 = (TextView) recommendNews1.findViewById(R.id.tvTip);
        TextView tvTip2 = (TextView) recommendNews2.findViewById(R.id.tvTip);
        TextView tvTip3 = (TextView) recommendNews3.findViewById(R.id.tvTip);
        tvTip1.setText(newsRecommendList.get(0).getAuchor());
        tvTip2.setText(newsRecommendList.get(1).getAuchor());
        tvTip3.setText(newsRecommendList.get(2).getAuchor());

        TextView tvDate1 = (TextView) recommendNews1.findViewById(R.id.tvDate);
        TextView tvDate2 = (TextView) recommendNews2.findViewById(R.id.tvDate);
        TextView tvDate3 = (TextView) recommendNews3.findViewById(R.id.tvDate);
        tvDate1.setText(newsRecommendList.get(0).getPublishTime());
        tvDate2.setText(newsRecommendList.get(1).getPublishTime());
        tvDate3.setText(newsRecommendList.get(2).getPublishTime());


        recommendNews1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                news.newsId = newsRecommendList.get(0).getNewsId();
                news.newsStyle = Integer.valueOf(newsRecommendList.get(0).getNewsStyle());
                //推荐的都是默认新闻
                news.appNewsType = 0;
                NewsDetailActivity.startActivity(AnnouncementActivity.this, news);
                finish();
            }
        });
        recommendNews2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                news.newsId = newsRecommendList.get(1).getNewsId();
                news.newsStyle = Integer.valueOf(newsRecommendList.get(1).getNewsStyle());
                //推荐的都是默认新闻
                news.appNewsType = 0;
                NewsDetailActivity.startActivity(AnnouncementActivity.this, news);
                finish();
            }
        });
        recommendNews3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                news.newsId = newsRecommendList.get(2).getNewsId();
                news.newsStyle = Integer.valueOf(newsRecommendList.get(2).getNewsStyle());
                //推荐的都是默认新闻
                news.appNewsType = 0;
                NewsDetailActivity.startActivity(AnnouncementActivity.this, news);
                finish();
            }
        });

    }

    /**
     * 切换 listview  emptyview
     */
    private void switchListEmptyView(boolean isShowEmpty) {
        if (isShowEmpty) {
            emptyReLay.setVisibility(View.VISIBLE);
        } else {
            emptyReLay.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化底部view
     */
    private void initBottomView() {
        if (newsDetailObj != null) {
            if (newsDetailObj.isCollected()) {    //已经被收藏
                collectedImgView.setImageResource(R.drawable.icon_collection_press);
            } else {
                collectedImgView.setImageResource(R.drawable.icon_collection);
            }
        }
    }

    /**
     * 请求新闻详情
     */
    private void requestNewsDetail() {
        showProgressDialog();
        townNoticeRestUsage.getNewsDetail(REQUEST_NEWS_DETAIL_TASK_ID, news.newsId);
    }

    /**
     * 请求收藏
     */
    private void requestCollection() {
        showProgressDialog();
        townNoticeRestUsage.newCollection(REQUEST_NEWS_COLLECTION_TASK_ID, news.newsId);
    }

    /**
     * 请求删除收藏
     */
    private void requestDelCollection() {
        showProgressDialog();
        townNoticeRestUsage.deleteNewCollection(REQUEST_DEL_NEWS_COLLECTION_TASK_ID, news.newsId, null);
    }

    private void requestNewRecommend() {
        mRestUsage.getRecommendNewsNoCache(REQUEST_RECOMMEND_NEWS_TASK_ID);
    }

    /**
     *
     */
    @OnClick(R.id.emptyReLay)
    public void emptyClick() {
        requestNewsDetail();
    }

    /**
     *
     */
    @OnClick(R.id.rightImgView)
    public void moreClick() {
        final TextSizeListAdapter textSizeListAdapter = new TextSizeListAdapter(this);
        final AlertDialog ad = new AlertDialog.Builder(this)
                .setSingleChoiceItems(textSizeListAdapter, -1, null)
                .setTitle("正文字号")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SPUtils.put(AnnouncementActivity.this, "txtSize", textSizeListAdapter.selectedTxtSize());
                        newsDetailListAdapter.setTxtSize(textSizeListAdapter.selectedTxtSize());
                    }
                })
                .create();

        ad.getListView().setCacheColorHint(Color.TRANSPARENT);
        ad.getListView().setSelector(new ColorDrawable(Color.TRANSPARENT));
        ad.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position = position - ad.getListView().getHeaderViewsCount();
                textSizeListAdapter.setTxtSizeByPos(position);
            }
        });

        ad.show();
    }

    /**
     * 收藏
     */
    @OnClick(R.id.collectionLinLay)
    public void collectionClick() {
        if (newsDetailObj == null) {
            return;
        }
        if (newsDetailObj.isCollected()) { //已经收藏
            requestDelCollection();
        } else {  //未收藏   这期不做
            requestCollection();
        }
    }

    /**
     * 分享
     */
    @OnClick(R.id.shareLinLay)
    public void shareClick() {
        if (newsDetailObj != null) {
            WebViewShare webViewShare = new WebViewShare();
            webViewShare.setTitle(newsDetailObj.getTitle());
            webViewShare.setImgUrl(newsDetailObj.getTopPic());
            webViewShare.setLink("http://kpnew.appwzd.cn/kepu/share/townNotice/id/" + news.newsId);
            ShareUtil.directShare(this, webViewShare);
        }
    }

}
