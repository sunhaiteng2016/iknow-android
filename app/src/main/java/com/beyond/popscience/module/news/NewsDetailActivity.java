package com.beyond.popscience.module.news;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.network.net.bean.BaseResponse;
import com.beyond.library.sharesdk.PlatformListFakeActivity;
import com.beyond.library.sharesdk.ShareUtil;
import com.beyond.library.sharesdk.WebViewShare;
import com.beyond.library.util.ToastUtil;
import com.beyond.library.view.ListViewNoScroll;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.CollectionRestUsage;
import com.beyond.popscience.frame.net.CommentRestUsage;
import com.beyond.popscience.frame.net.NewsRestUsage;
import com.beyond.popscience.frame.net.SearchRestUsage;
import com.beyond.popscience.frame.net.TownRestUsage;
import com.beyond.popscience.frame.pojo.NewsDetailAnswerObj;
import com.beyond.popscience.frame.pojo.NewsDetailContent;
import com.beyond.popscience.frame.pojo.NewsDetailObj;
import com.beyond.popscience.frame.pojo.NewsDetailQuestion;
import com.beyond.popscience.frame.pojo.NewsDetailQuestionObj;
import com.beyond.popscience.frame.pojo.NewsRecommendObj;
import com.beyond.popscience.frame.pojo.NewsRecommendResponse;
import com.beyond.popscience.frame.thirdsdk.ThirdSDKManagerV2;
import com.beyond.popscience.frame.util.D;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.SPUtils;
import com.beyond.popscience.frame.view.PublishedCommentView;
import com.beyond.popscience.module.home.entity.News;
import com.beyond.popscience.module.mservice.X5WebViewActivity;
import com.beyond.popscience.module.news.adapter.LinearAdapter;
import com.beyond.popscience.module.news.adapter.NewsQuestionListAdapter;
import com.beyond.popscience.module.news.adapter.TextSizeListAdapter;
import com.beyond.popscience.module.news.task.InitTTSTask;
import com.beyond.popscience.widget.MyListView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class NewsDetailActivity extends BaseActivity {
    @BindView(R.id.newsListView)
    RecyclerView newsListView;
    @BindView(R.id.iv_head_view)
    ImageView ivHeadView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.adj)
    TextView adj;
    @BindView(R.id.adj_list)
    MyListView adjList;
    @BindView(R.id.ll_yulan)
    LinearLayout llYulan;
    @BindView(R.id.luokuan_lv)
    MyListView luokuanLv;
    @BindView(R.id.tv_sz)
    ImageView tvSz;
    @BindView(R.id.linkTxtView)
    TextView linkTxtView;
    @BindView(R.id.lineView)
    View lineView;
    @BindView(R.id.ly)
    TextView ly;
    @BindView(R.id.footerAuthorTxtView)
    TextView footerAuthorTxtView;
    @BindView(R.id.footerPublishTimeTxtView)
    TextView footerPublishTimeTxtView;
    @BindView(R.id.editorTxtView)
    TextView editorTxtView;
    @BindView(R.id.treadImgView)
    ImageView treadImgView;
    @BindView(R.id.treadTxtView)
    TextView treadTxtView;
    @BindView(R.id.treadLinear)
    LinearLayout treadLinear;
    @BindView(R.id.praiseImgView)
    ImageView praiseImgView;
    @BindView(R.id.praiseTxtView)
    TextView praiseTxtView;
    @BindView(R.id.praiseLinear)
    LinearLayout praiseLinear;
    @BindView(R.id.bottomLinLay)
    LinearLayout bottomLinLay;
    @BindView(R.id.questionListView)
    ListViewNoScroll questionListView;
    @BindView(R.id.submitQuestionTxtView)
    TextView submitQuestionTxtView;
    @BindView(R.id.questionLinLay)
    LinearLayout questionLinLay;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.img_card_view)
    CardView imgCardView;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvTip)
    TextView tvTip;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvViewCount)
    TextView tvViewCount;
    @BindView(R.id.tvTop)
    TextView tvTop;
    @BindView(R.id.deliver)
    View deliver;

    @BindView(R.id.ll_recommend_news)
    LinearLayout llRecommendNews;
    @BindView(R.id.viewpager)
    NestedScrollView viewpager;
    @BindView(R.id.editCommentImgView)
    ImageView editCommentImgView;
    @BindView(R.id.publishCommentTxtView)
    TextView publishCommentTxtView;
    @BindView(R.id.collectedImgView)
    ImageView collectedImgView;
    @BindView(R.id.noCollectedImgView)
    ImageView noCollectedImgView;
    @BindView(R.id.fl1)
    FrameLayout fl1;
    @BindView(R.id.collectionTxtView)
    TextView collectionTxtView;
    @BindView(R.id.collectionLinLay)
    LinearLayout collectionLinLay;
    @BindView(R.id.iv1111)
    ImageView iv1111;
    @BindView(R.id.commentTxtView)
    TextView commentTxtView;
    @BindView(R.id.lookCommentLinLay)
    LinearLayout lookCommentLinLay;
    @BindView(R.id.shareTxtView)
    TextView shareTxtView;
    @BindView(R.id.shareLinLay)
    LinearLayout shareLinLay;
    @BindView(R.id.ivVoice)
    ImageView ivVoice;
    @BindView(R.id.publishedCommentView)
    PublishedCommentView publishedCommentView;
    @BindView(R.id.coordinator)
    CoordinatorLayout coordinator;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @Request
    private SearchRestUsage searchRestUsage;
    @Request
    private CommentRestUsage commentRestUsage;
    @Request
    private CollectionRestUsage collectionRestUsage;
    @Request
    private TownRestUsage townRestUsage;
    @Request
    private NewsRestUsage mRestUsage;
    private final static String EXTRA_NEWS_KEY = "news";
    private final static String EXTRA_NEWS_TYPE = "news_type";
    private News news;
    private List<NewsDetailContent> list = new ArrayList<>();
    private NewsQuestionListAdapter newsQuestionListAdapter;
    private NewsDetailObj newsDatailObj;
    View recommendNews1, recommendNews2, recommendNews3;
    private boolean isCollectionRequesting;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_test_list;
    }

    private LinearAdapter adapter;
    /**
     * 请求新闻详情
     */
    private final int REQUEST_NEWS_DETAIL_TASK_ID = 101;
    /**
     * 新闻收藏
     */
    private final int REQUEST_NEWS_COLLECTION_TASK_ID = 102;
    /**
     * 发表评论
     */
    private final int REQUEST_NEWS_PUBLISHED_COMMENT_TASK_ID = 103;
    /**
     * 删除收藏
     */
    private final int REQUEST_DEL_NEWS_COLLECTION_TASK_ID = 104;
    /**
     * 初始化TTS
     */
    private final int REQUEST_INIT_TTS_TASK_ID = 105;
    /**
     * 点赞/取消点赞
     */
    private final int REQUEST_PRAISE_TASK_ID = 106;
    /**
     * 不喜欢/取消不喜欢
     */
    private final int REQUEST_UN_LIKE_TASK_ID = 107;

    /**
     * 获取推荐新闻
     */
    private final int REQUEST_RECOMMEND_NEWS_TASK_ID = 108;
    /**
     * 获取教学互动题目
     */
    private final int REQUEST_GET_QUESTION_TASK_ID = 109;
    /**
     * 提交答案
     */
    private final int REQUEST_SUBMIT_ANSWER_TASK_ID = 110;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    public static void startActivity(Context context, News news) {
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra(EXTRA_NEWS_KEY, news);
        context.startActivity(intent);
    }

    /**
     * @param fragment
     */
    public static void startActivity(Fragment fragment, News news) {
        Intent intent = new Intent(fragment.getActivity(), NewsDetailActivity.class);
        intent.putExtra(EXTRA_NEWS_KEY, news);
        fragment.startActivity(intent);
    }

    public static void startActivity(Fragment fragment, News news, boolean isTownNews) {
        Intent intent = new Intent(fragment.getActivity(), NewsDetailActivity.class);
        intent.putExtra(EXTRA_NEWS_KEY, news);
        intent.putExtra(EXTRA_NEWS_TYPE, isTownNews);
        fragment.startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestNewRecommend();
    }

    //推荐阅读
    private void requestNewRecommend() {
        mRestUsage.getRecommendNewsNoCache(REQUEST_RECOMMEND_NEWS_TASK_ID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final TextSizeListAdapter textSizeListAdapter = new TextSizeListAdapter(this);
        final AlertDialog ad = new AlertDialog.Builder(this)
                .setSingleChoiceItems(textSizeListAdapter, -1, null)
                .setTitle("正文字号")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SPUtils.put(NewsDetailActivity.this, "txtSize", textSizeListAdapter.selectedTxtSize());
                        adapter.setTxtSize(textSizeListAdapter.selectedTxtSize());
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
        return true;
    }

    @Override
    public void initUI() {
        super.initUI();
        news = (News) getIntent().getSerializableExtra(EXTRA_NEWS_KEY);
        ToastUtil.showCenter(NewsDetailActivity.this, "恭喜您, + 1 科普绿币!");
        if (news == null || TextUtils.isEmpty(news.newsId)) {
            backNoAnim();
            return;
        }
        initListview();
        addFootView();
        initPublishedView();
        NewsDetailObj newsDetailObj = new NewsDetailObj();
        newsDetailObj.setTitle(news.title);
        newsDetailObj.setAuchor(news.auchor);
        newsDetailObj.setPublishTime(news.publishTime);
        newsDetailObj.setTopPic(news.getFirstPic());

        requestNewsDetail();
        requestGetQuestion();

    }

    /**
     * 发表评论View
     */
    private void initPublishedView() {
        publishedCommentView.setOnPublishedOnClickListener(new PublishedCommentView.SimplePublishedClickListener() {
            @Override
            public void onOk() {
                if (TextUtils.isEmpty(publishedCommentView.getText().trim())) {
                    ToastUtil.showCenter(NewsDetailActivity.this, "请输入评论");
                    return;
                }
                publishedCommentView.showSoftInput();
                requestPublishedComment();
            }
        });
    }

    private void addFootView() {
        questionLinLay.setVisibility(View.GONE);
        newsQuestionListAdapter = new NewsQuestionListAdapter(this);
        questionListView.setAdapter(newsQuestionListAdapter);

        submitQuestionTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newsQuestionListAdapter.getCount() != newsQuestionListAdapter.getSelectedNewsDetailQuestionMap().size()) {
                    ToastUtil.showCenter(NewsDetailActivity.this, "请选择答案");
                    return;
                }

                List<HashMap<String, String>> answerList = new ArrayList<HashMap<String, String>>();
                for (Map.Entry<NewsDetailQuestion, List<String>> entry : newsQuestionListAdapter.getSelectedNewsDetailQuestionMap().entrySet()) {
                    NewsDetailQuestion newsDetailQuestion = entry.getKey();
                    List<String> optionsKeyList = entry.getValue();
                    if (optionsKeyList == null || optionsKeyList.size() == 0) {
                        ToastUtil.showCenter(NewsDetailActivity.this, "请选择答案");
                        return;
                    }

                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < optionsKeyList.size(); i++) {
                        sb.append(optionsKeyList.get(i));
                        if (i != optionsKeyList.size() - 1) {
                            sb.append(",");
                        }
                    }

                    HashMap<String, String> answerMap = new HashMap<String, String>();
                    answerMap.put("answer", sb.toString());
                    answerMap.put("qid", newsDetailQuestion.getQid());
                    answerList.add(answerMap);
                }
                requestSubmitAnswer(answerList);
            }
        });

        // 如果是不乡镇新闻，则显示推荐新闻
        if (news.isDefaultNews()) {
            llRecommendNews.setVisibility(View.VISIBLE);
        } else {
            llRecommendNews.setVisibility(View.GONE);
        }
        praiseLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPraise();
            }
        });
        treadLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestUnLike();
            }
        });

        recommendNews1 = findViewById(R.id.recommendNewsOne);
        recommendNews2 = findViewById(R.id.recommendNewsTwo);
        recommendNews3 = findViewById(R.id.recommendNewsThree);

    }

    private void initListview() {
        adapter = new LinearAdapter(this, list);
        newsListView.setLayoutManager(new GridLayoutManager(this, 1));
        newsListView.setAdapter(adapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            viewpager.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    getPositionAndOffset(i1, i3);
                }
            });
        }
    }

    /**
     * 记录RecyclerView当前位置
     *
     * @param i2
     * @param i3
     */
    private void getPositionAndOffset(int i2, int i3) {
        //获取与该view的顶部的偏移量
        //得到该View的数组位置
        SPUtils.put(this, String.format(getNewsDetailIdKey(), news.newsId), i2);
        SPUtils.put(this, String.format(getNewsDetailTopIdKey(), news.newsId), i3);
    }

    /**
     * 乡镇 缓存新闻详情滚动的位置
     */
    private final String KEY_NEWS_DETAIL_TOWN_ID = "keyNewsDetail_town_%s";
    /**
     * 乡镇
     */
    private final String KEY_NEWS_DETAIL_TOWN_TOP_ID = "keyNewsDetail_town_%s_top";
    /**
     * 缓存新闻详情滚动的位置
     */
    private final String KEY_NEWS_DETAIL_ID = "keyNewsDetail_%s";
    /**
     *
     */
    private final String KEY_NEWS_DETAIL_TOP_ID = "keyNewsDetail_%s_top";

    /**
     * @return
     */
    private String getNewsDetailIdKey() {
        return news.isTownNews() ? KEY_NEWS_DETAIL_TOWN_ID : KEY_NEWS_DETAIL_ID;
    }

    /**
     * @return
     */
    private String getNewsDetailTopIdKey() {
        return news.isTownNews() ? KEY_NEWS_DETAIL_TOWN_TOP_ID : KEY_NEWS_DETAIL_TOP_ID;
    }

    /**
     * 请求新闻详情
     */
    private void requestNewsDetail() {
        if (news.isTownNews()) {
            townRestUsage.getNewsDetail(REQUEST_NEWS_DETAIL_TASK_ID, news.newsId);
        } else {
            searchRestUsage.getNewsDetail(REQUEST_NEWS_DETAIL_TASK_ID, news.newsId);
        }
    }

    /**
     * 请求新闻题目
     */
    private void requestGetQuestion() {
        if (news.isDefaultNews()) { //默认新闻
            searchRestUsage.getQuestion(REQUEST_GET_QUESTION_TASK_ID, news.newsId);
        }
    }

    /**
     * 提交答案
     *
     * @param answerList
     */
    private void requestSubmitAnswer(List<HashMap<String, String>> answerList) {
        if (answerList == null || answerList.size() == 0) {
            return;
        }
        showProgressDialog();
        searchRestUsage.submitAnswer(REQUEST_SUBMIT_ANSWER_TASK_ID, answerList);
    }

    /**
     * 请求点赞
     */
    private void requestPraise() {
        if (newsDatailObj != null) {
            showProgressDialog();
            if (news.isTownNews()) {  //乡镇
                townRestUsage.praise(REQUEST_PRAISE_TASK_ID, news.newsId, !newsDatailObj.isVote());
            } else {
                searchRestUsage.praise(REQUEST_PRAISE_TASK_ID, news.newsId, !newsDatailObj.isVote());
            }
        }
    }

    /**
     * 请求不喜欢
     */
    private void requestUnLike() {
        if (newsDatailObj != null) {
            showProgressDialog();
            if (news.isTownNews()) {  //乡镇
                townRestUsage.unLike(REQUEST_UN_LIKE_TASK_ID, news.newsId, !newsDatailObj.isDislike());
            } else {
                searchRestUsage.unLike(REQUEST_UN_LIKE_TASK_ID, news.newsId, !newsDatailObj.isDislike());
            }
        }
    }

    /**
     * 发表评论
     */
    private void requestPublishedComment() {
        showProgressDialog();
        if (news.isTownNews()) {  //乡镇
            townRestUsage.sendComment(REQUEST_NEWS_PUBLISHED_COMMENT_TASK_ID, news.newsId, publishedCommentView.getText());
        } else {
            commentRestUsage.sendComment(REQUEST_NEWS_PUBLISHED_COMMENT_TASK_ID, news.newsId, publishedCommentView.getText());
        }
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case REQUEST_NEWS_DETAIL_TASK_ID:
                if (msg.getIsSuccess()) {
                    BaseResponse<NewsDetailObj> baseResponese = msg.getBaseResponse();
                    newsDatailObj = baseResponese.getData();
                    toolbar.setTitle(newsDatailObj.getTitle());
                    setSupportActionBar(toolbar);
                    toolbar.inflateMenu(R.menu.menu_scrolling);
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                    initFooter(newsDatailObj);
                    initHeader(newsDatailObj);
                    initBottomView();
                    if (newsDatailObj.getContent() != null) {
                        boolean isFirst = adapter.getItemCount() == 0;
                        newsListView.stopScroll();
                        adapter.setThumbUrl(newsDatailObj.getTopPic());
                        adapter.setVideoName(newsDatailObj.getTitle());
                        list.clear();
                        list.addAll(newsDatailObj.getContent());
                        adapter.notifyDataSetChanged();
                        Integer targetPos = (Integer) SPUtils.get(this, String.format(getNewsDetailIdKey(), news.newsId), 0);
                        Integer offsetPos = (Integer) SPUtils.get(this, String.format(getNewsDetailTopIdKey(), news.newsId), 0);
                        if (targetPos != null) {
                            // scrollToPosition(targetPos, offsetPos);
                        }
                    }
                }
                break;
            case REQUEST_SUBMIT_ANSWER_TASK_ID: //提交答案
                if (msg.getIsSuccess()) {
                    BaseResponse<NewsDetailAnswerObj> baseResponse = msg.getBaseResponse();
                    if (baseResponse.getData() != null) {
                        NewsDetailAnswerObj newsDetailAnswerObj = baseResponse.getData();
                        if (newsDetailAnswerObj != null) {
                            String tip = "恭喜你，+ " + newsDetailAnswerObj.getScore() + " 绿币";
                            initUI();
                            if (newsDetailAnswerObj.getScoreFloat() <= 0) {
                                tip = "抱歉，本次未能获得绿币！";
                            }
                            D.show(this, null, tip, "确定", null, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                        }
                    }
                }
                dismissProgressDialog();
                break;
            case REQUEST_GET_QUESTION_TASK_ID:  //互动题目
                questionLinLay.setVisibility(View.GONE);
                if (msg.getIsSuccess()) {
                    BaseResponse<NewsDetailQuestionObj> baseResponse = msg.getBaseResponse();
                    if (baseResponse.getData() != null) {
                        NewsDetailQuestionObj newsDetailQuestionObj = baseResponse.getData();
                        if (newsDetailQuestionObj != null && newsDetailQuestionObj.getQuestionList() != null && newsDetailQuestionObj.getQuestionList().size() > 0) {
                            newsQuestionListAdapter.getDataList().clear();
                            newsQuestionListAdapter.getDataList().addAll(newsDetailQuestionObj.getQuestionList());
                            newsQuestionListAdapter.notifyDataSetChanged();
                            questionLinLay.setVisibility(View.VISIBLE);
                        }
                    }
                }
                break;
            case REQUEST_PRAISE_TASK_ID:  //点赞/取消点赞
                if (msg.getIsSuccess()) {
                    newsDatailObj.setVote(!newsDatailObj.isVote());
                    newsDatailObj.setVoteNum(String.valueOf(newsDatailObj.isVote() ? newsDatailObj.getVoteNumLong() + 1 : newsDatailObj.getVoteNumLong() - 1));
                    ToastUtil.showCenter(NewsDetailActivity.this, "恭喜您, + 1 科普绿币!");
                    initFooter(newsDatailObj);
                }
                dismissProgressDialog();
                break;
            case REQUEST_UN_LIKE_TASK_ID:  //不喜欢/取消不喜欢
                if (msg.getIsSuccess()) {
                    newsDatailObj.setDislike(!newsDatailObj.isDislike());
                    newsDatailObj.setDislikeNum(String.valueOf(newsDatailObj.isDislike() ? newsDatailObj.getDislikeNumLong() + 1 : newsDatailObj.getDislikeNumLong() - 1));
                    initFooter(newsDatailObj);
                }
                dismissProgressDialog();
                break;

            case REQUEST_RECOMMEND_NEWS_TASK_ID://推荐阅读
                if (msg.getIsSuccess()) {
                    NewsRecommendResponse newsRecommendResponse = (NewsRecommendResponse) msg.getObj();
                    if (newsRecommendResponse != null) {
                        // 如果不是乡镇新闻，才设置推荐新闻
                        if (!getIntent().getBooleanExtra(EXTRA_NEWS_TYPE, false)) {
                            setRecommedNews(newsRecommendResponse.getNewsList());
                        }
                    }
                }
                dismissProgressDialog();
                break;
            case REQUEST_NEWS_PUBLISHED_COMMENT_TASK_ID:    //发表评论
                if (msg.getIsSuccess()) {
                    newsDatailObj.setCommentCount(String.valueOf(newsDatailObj.getCommentCountLong() + 1));
                    initBottomView();

                    publishedCommentView.hidden();
                    publishedCommentView.setText("");
                    ToastUtil.showCenter(NewsDetailActivity.this, "恭喜您, + 1 科普绿币!");
                    CommentListActivity.startActivity(NewsDetailActivity.this, news);
                }
                dismissProgressDialog();
                break;

            case REQUEST_NEWS_COLLECTION_TASK_ID:   //新闻收藏
                if (msg.getIsSuccess()) {
                    ToastUtil.showCenter(this, "收藏成功");
                    if (newsDatailObj != null) {
                        newsDatailObj.setCollected(true);
                        newsDatailObj.setLikeCount(String.valueOf(newsDatailObj.getLikeCountLong() + 1));
                        initBottomView();
                    }
                } else {
                    ToastUtil.showCenter(this, "收藏失败");
                }
                isCollectionRequesting = false;
                break;
            case REQUEST_DEL_NEWS_COLLECTION_TASK_ID:   //删除新闻收藏
                if (msg.getIsSuccess()) {
                    ToastUtil.showCenter(this, "取消收藏成功");
                    if (newsDatailObj != null) {
                        newsDatailObj.setCollected(false);
                        newsDatailObj.setLikeCount(String.valueOf(newsDatailObj.getLikeCountLong() - 1));
                        initBottomView();
                    }
                } else {
                    ToastUtil.showCenter(this, "取消收藏失败");
                }
                isCollectionRequesting = false;
                break;

            case REQUEST_INIT_TTS_TASK_ID:  //初始化TTS
                speak();
                dismissProgressDialog();
                break;
        }
    }

    /**
     * 初始化header数据
     */
    private void initHeader(final NewsDetailObj newsDetailObj) {
        Glide.with(this).load(newsDetailObj.getTopPic()).into(ivHeadView);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    /**
     * 初始化footer数据
     */
    private void initFooter(final NewsDetailObj newsDetailObj) {
        if (newsDetailObj != null) {
            footerAuthorTxtView.setText(newsDetailObj.getAuchor());
            footerPublishTimeTxtView.setText(newsDetailObj.getPublishTime());
            editorTxtView.setText(newsDetailObj.getEditor());

            praiseTxtView.setText(TextUtils.isEmpty(newsDetailObj.getVoteNum()) ? "0" : newsDetailObj.getVoteNum());
            treadTxtView.setText(TextUtils.isEmpty(newsDetailObj.getDislikeNum()) ? "0" : newsDetailObj.getDislikeNum());

            if (newsDetailObj.isVote()) { //已经点赞
                praiseImgView.setImageResource(R.drawable.icon_like_red);
            } else {
                praiseImgView.setImageResource(R.drawable.icon_like);
            }

            praiseLinear.setSelected(newsDetailObj.isVote());
            praiseTxtView.setSelected(newsDetailObj.isVote());

            if (newsDetailObj.isDislike()) { //已经不喜欢
                treadImgView.setImageResource(R.drawable.icon_unlike_blue);
            } else {
                treadImgView.setImageResource(R.drawable.icon_unlike);
            }

            treadLinear.setSelected(newsDetailObj.isDislike());
            treadTxtView.setSelected(newsDetailObj.isDislike());

            if (null != newsDetailObj.getLinkName() && !TextUtils.isEmpty(newsDetailObj.getLinkName())) {
                linkTxtView.setVisibility(View.VISIBLE);
                tvSz.setVisibility(View.VISIBLE);
                linkTxtView.setText(newsDetailObj.getLinkName());
                linkTxtView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        X5WebViewActivity.startActivity(NewsDetailActivity.this, newsDetailObj.getLink(), "");
                    }
                });
            } else {
                linkTxtView.setVisibility(View.GONE);
                tvSz.setVisibility(View.GONE);
            }
        }
    }

    //设置推荐阅读
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
                startActivity(NewsDetailActivity.this, news);
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
                startActivity(NewsDetailActivity.this, news);
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
                startActivity(NewsDetailActivity.this, news);
                finish();
            }
        });
    }

    /**
     * 初始化底部view
     */
    private void initBottomView() {
        if (newsDatailObj != null) {
            String likeCount = newsDatailObj.getLikeCount();
            collectionTxtView.setText(newsDatailObj.getLikeCount().equals("0") ? "" : likeCount);
            String commentCount = newsDatailObj.getCommentCount();
            commentTxtView.setText(newsDatailObj.getCommentCount().equals("0") ? "" : commentCount);
            if (newsDatailObj.isCollected()) {    //已经被收藏
                showCollectionRotation(noCollectedImgView, collectedImgView);
            }
        }
    }

    /**
     * 收藏旋转动画
     *
     * @param fontView
     * @param backView
     */
    private void showCollectionRotation(View fontView, View backView) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ObjectAnimator.ofFloat(fontView, "alpha", 1, 0), ObjectAnimator.ofFloat(fontView, "rotationY", -180, 0));
        animatorSet.playTogether(ObjectAnimator.ofFloat(backView, "alpha", 0, 1), ObjectAnimator.ofFloat(backView, "rotationY", 0, -180));
        animatorSet.setDuration(300);
        animatorSet.start();
    }

    /**
     * 显示发表评论View
     */
    @OnClick({R.id.editCommentImgView, R.id.publishCommentTxtView})
    public void showPublishedCommentClick() {
        publishedCommentView.show();
    }

    /**
     * 收藏
     *
     * @param view
     */
    @OnClick(R.id.collectionLinLay)
    public void collectionClick(View view) {
        if (newsDatailObj == null) {
            return;
        }
        if (isCollectionRequesting) {
            return;
        }
        isCollectionRequesting = true;
        if (newsDatailObj.isCollected()) { //已经收藏
            requestDelCollection();
            showCollectionRotation(collectedImgView, noCollectedImgView);
        } else {  //未收藏   这期不做
            requestCollection();
            showCollectionRotation(noCollectedImgView, collectedImgView);
        }
    }

    @OnClick(R.id.ivVoice)
    public void clickVoice() {
        if (ivVoice.isSelected()) {//正在播放
            ThirdSDKManagerV2.getInstance().ttsStop();
        } else {
            soundClick();
        }
        ivVoice.setSelected(!ivVoice.isSelected());
    }


    /**
     * 播放
     */
    private void soundClick() {
        if (ThirdSDKManagerV2.getInstance().isTTSSInited()) {
            speak();
        } else {
            requestInitTTS();
        }
    }

    /**
     * 朗读
     */
    private void speak() {
        if (newsDatailObj.getContent() != null) {
            if (ThirdSDKManagerV2.getInstance().isTTSStart()) {
                ThirdSDKManagerV2.getInstance().ttsStop();
            } else {
                List<NewsDetailContent> newsDetailContentList = newsDatailObj.getContent();
                StringBuffer sb = new StringBuffer();
                for (NewsDetailContent newsDetailContent : newsDetailContentList) {
                    if (newsDetailContent.isTxt() && !TextUtils.isEmpty(newsDetailContent.getContentNews())) {
                        sb.append(newsDetailContent.getContentNews().replaceAll("<br/>", ""));
                    }
                }
                if (sb.length() > 0) {
                    ThirdSDKManagerV2.getInstance().ttsSpeak(sb.toString());
                }
            }
        }
    }

    /**
     * 初始化TTS
     */
    private void requestInitTTS() {
        showProgressDialog();
        execuTask(new InitTTSTask(REQUEST_INIT_TTS_TASK_ID));
    }

    /**
     * 请求收藏
     */
    private void requestCollection() {
        if (news.isTownNews()) {  //乡镇
            townRestUsage.newCollection(REQUEST_NEWS_COLLECTION_TASK_ID, news.newsId);
        } else {
            collectionRestUsage.newCollection(REQUEST_NEWS_COLLECTION_TASK_ID, news.newsId);
        }
    }

    /**
     * 请求删除收藏
     */
    private void requestDelCollection() {
        if (news.isTownNews()) {  //乡镇
            townRestUsage.deleteNewCollection(REQUEST_DEL_NEWS_COLLECTION_TASK_ID, news.newsId, null);
        } else {
            collectionRestUsage.deleteNewCollection(REQUEST_DEL_NEWS_COLLECTION_TASK_ID, news.newsId, null);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ThirdSDKManagerV2.getInstance().destroyTTSVoice();
    }

    /**
     * 查看评论
     *
     * @param view
     */
    @OnClick(R.id.lookCommentLinLay)
    public void lookCommentClick(View view) {
        CommentListActivity.startActivity(this, news);
    }


    /**
     * 分享
     *
     * @param view
     */
    @OnClick(R.id.shareLinLay)
    public void shareClick(View view) {
        if (newsDatailObj != null) {
            WebViewShare webViewShare = new WebViewShare();
            webViewShare.setTitle(newsDatailObj.getTitle());
            webViewShare.setImgUrl(newsDatailObj.getTopPic());
            webViewShare.setNewId(news.newsId);
            String url = "http://kpnew.appwzd.cn/kepu/share/news/id/" + news.newsId;
            if (news.appNewsType == News.TYPE_TOWN_NEWS) {//乡镇新闻
                url = "http://kpnew.appwzd.cn/kepu/share/townNews/id/" + news.newsId;
            } else if (news.appNewsType == News.TYPE_TOWN_ANNOUNCEMENT) {
                url = "http://kpnew.appwzd.cn/kepu/share/townNotice/id/" + news.newsId;
            }

            webViewShare.setLink(url);

            ShareUtil.directShare(this, webViewShare, new PlatformListFakeActivity.OnResultListener() {
                @Override
                public void onSuccess(Platform platform) {
                    ToastUtil.showCenter(NewsDetailActivity.this, "恭喜您, + 1 科普绿币!");
                    if (platform instanceof Wechat || platform instanceof WechatMoments ||
                            platform instanceof QQ || platform instanceof QZone) {
                        if (news.isDefaultNews()) {
                            searchRestUsage.share(11, "1", news.newsId);
                        } else if (news.isTownNews()) {
                            searchRestUsage.share(11, "2", news.newsId);
                        }
                    }
                }

                @Override
                public void onError(Platform platform) {
                    Log.e("share", "onError" + platform.getName());
                }

                @Override
                public void onCancel(Platform platform) {
                    Log.e("share", "onError" + platform.getName());
                }
            });
        }
    }

}
