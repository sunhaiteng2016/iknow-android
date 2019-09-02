package com.beyond.popscience.module.news;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.network.net.bean.BaseResponse;
import com.beyond.library.view.pullrefresh.PullToRefreshBase;
import com.beyond.library.view.pullrefresh.PullToRefreshListView;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.SearchRestUsage;
import com.beyond.popscience.frame.net.TownRestUsage;
import com.beyond.popscience.frame.pojo.NewsResponse;
import com.beyond.popscience.module.home.AnnouncementActivity;
import com.beyond.popscience.module.home.adapter.NewsListAdapter;
import com.beyond.popscience.module.home.entity.News;

import butterknife.BindView;

/**
 * 搜索结果
 * Created by linjinfa on 2017/6/9.
 * email 331710168@qq.com
 */
public class GlobalSearchResultActivity extends BaseActivity {

    /**
     *
     */
    private final static String EXTRA_SEARCH_TYPE_KEY = "searchType";
    /**
     * 搜索
     */
    private final int SEARCH_TASK_ID = 101;
    /**
     *
     */
    private final static String EXTRA_KEY_WORD_KEY = "keyWord";

    @BindView(R.id.tv_title)
    protected TextView titleTxtView;
    @BindView(R.id.listView)
    protected PullToRefreshListView listView;
    @BindView(R.id.emptyTxtView)
    protected TextView emptyTxtView;
    /**
     *
     */
    private NewsListAdapter newsListAdapter;
    /**
     *
     */
    private String keyWord;
    /**
     *
     */
    private int page = 1;

    @Request
    private TownRestUsage townRestUsage;
    @Request
    private SearchRestUsage searchRestUsage;

    /**
     *
     */
    private int type = GlobalSearchActivity.TYPE_DEFAULT;

    /**
     *
     * @param context
     */
    public static void startActivity(Context context, String keyWord){
        Intent intent = new Intent(context, GlobalSearchResultActivity.class);
        intent.putExtra(EXTRA_KEY_WORD_KEY, keyWord);
        context.startActivity(intent);
    }

    /**
     *
     * @param context
     */
    public static void startActivityTown(Context context, String keyWord){
        Intent intent = new Intent(context, GlobalSearchResultActivity.class);
        intent.putExtra(EXTRA_KEY_WORD_KEY, keyWord);
        intent.putExtra(EXTRA_SEARCH_TYPE_KEY, GlobalSearchActivity.TYPE_TOWN);
        context.startActivity(intent);
    }

    /**
     * 公告搜索
     * @param context
     */
    public static void startActivityTownAnno(Context context, String keyWord){
        Intent intent = new Intent(context, GlobalSearchResultActivity.class);
        intent.putExtra(EXTRA_KEY_WORD_KEY, keyWord);
        intent.putExtra(EXTRA_SEARCH_TYPE_KEY, GlobalSearchActivity.TYPE_TOWN_ANNO);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_global_search_result;
    }

    @Override
    public void initUI() {
        type = getIntent().getIntExtra(EXTRA_SEARCH_TYPE_KEY, GlobalSearchActivity.TYPE_DEFAULT);

        keyWord = getIntent().getStringExtra(EXTRA_KEY_WORD_KEY);
        titleTxtView.setText("搜索结果");

        initListView();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch(taskId){
            case SEARCH_TASK_ID:    //搜索
                NewsResponse newsResponse = null;
                if(msg.getIsSuccess()){
                    BaseResponse<NewsResponse> baseResponse = msg.getBaseResponse();
                    if(baseResponse!=null && baseResponse.getData()!=null){
                        newsResponse = baseResponse.getData();
                        if(newsResponse.getNewsList()!=null && newsResponse.getNewsList().size()!=0){
                            if(listView.isPullDownToRefresh()){
                                newsListAdapter.getDataList().clear();
                            }
                            newsListAdapter.getDataList().addAll(newsResponse.getNewsList());
                            newsListAdapter.notifyDataSetChanged();
                        }
                    }
                }
                if(newsResponse == null || newsResponse.getNewsList() == null || newsResponse.getNewsList().size()==0 || newsListAdapter.getCount() >= newsResponse.getTotalcount()){   //加载结束
                    listView.onLoadMoreCompleteAndNoData();
                }else{
                    listView.onLoadMoreComplete();
                }
                switchListViewEmpty();
                break;
        }
    }

    /**
     * 初始化
     */
    private void initListView(){
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.getRefreshableView().setCacheColorHint(Color.TRANSPARENT);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position = position - listView.getRefreshableView().getHeaderViewsCount();
                News news = newsListAdapter.getItem(position);

                if(isSearchTown() || isSearchTownAnno()){ //乡镇 & 公告
                    if(news.searchType == 1){    //	1:新闻 2：公告
                        news.appNewsType = News.TYPE_TOWN_NEWS;
                        NewsDetailActivity.startActivity(GlobalSearchResultActivity.this, news);
                    }else if(news.searchType == 2){ //	1:新闻 2：公告
                        news.appNewsType = News.TYPE_TOWN_ANNOUNCEMENT;
                        AnnouncementActivity.startActivity(GlobalSearchResultActivity.this, news);
                    }
                }else{
                    NewsDetailActivity.startActivity(GlobalSearchResultActivity.this, news);
                }
            }
        });

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                requestSearch();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                requestSearch();
            }
        });

        newsListAdapter = new NewsListAdapter(this);
        listView.getRefreshableView().setAdapter(newsListAdapter);

        listView.setRefreshing();
    }

    /**
     * 切换 listview 与 emptyview
     */
    private void switchListViewEmpty(){
        if(newsListAdapter.getCount() == 0){
            listView.setVisibility(View.GONE);
            emptyTxtView.setVisibility(View.VISIBLE);
            emptyTxtView.setText("未搜索到 "+keyWord+" 相关资讯");
        }else{
            listView.setVisibility(View.VISIBLE);
            emptyTxtView.setVisibility(View.GONE);
        }
    }

    /**
     * 搜索乡镇
     * @return
     */
    private boolean isSearchTown(){
        return type == GlobalSearchActivity.TYPE_TOWN;
    }

    /**
     * 搜索乡镇公告
     * @return
     */
    private boolean isSearchTownAnno(){
        return type == GlobalSearchActivity.TYPE_TOWN_ANNO;
    }

    /**
     * 搜索
     */
    private void requestSearch(){
        if(isSearchTown() || isSearchTownAnno()){ //乡镇 & 公告
            townRestUsage.search(SEARCH_TASK_ID, page, keyWord);
        }else{
            searchRestUsage.search(SEARCH_TASK_ID, page, keyWord);
        }
    }

}
