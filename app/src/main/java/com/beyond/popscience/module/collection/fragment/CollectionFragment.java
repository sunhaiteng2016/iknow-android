package com.beyond.popscience.module.collection.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.DensityUtil;
import com.beyond.library.util.ToastUtil;
import com.beyond.library.view.pullrefresh.PullToRefreshBase;
import com.beyond.library.view.pullrefresh.PullToRefreshSwipeMenuListView;
import com.beyond.library.view.swipemenulistview.SwipeMenu;
import com.beyond.library.view.swipemenulistview.SwipeMenuCreator;
import com.beyond.library.view.swipemenulistview.SwipeMenuItem;
import com.beyond.library.view.swipemenulistview.SwipeMenuListView;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseFragment;
import com.beyond.popscience.frame.net.CollectionRestUsage;
import com.beyond.popscience.frame.net.TownNoticeRestUsage;
import com.beyond.popscience.frame.net.TownRestUsage;
import com.beyond.popscience.frame.pojo.CollectionResponse;
import com.beyond.popscience.module.collection.CollectionActivity;
import com.beyond.popscience.module.home.AnnouncementActivity;
import com.beyond.popscience.module.home.adapter.NewsListAdapter;
import com.beyond.popscience.module.home.entity.News;
import com.beyond.popscience.module.news.NewsDetailActivity;
import com.beyond.popscience.module.social.NoticeDetailActivity;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Created by linjinfa on 2017/7/1.
 * email 331710168@qq.com
 */
public class CollectionFragment extends BaseFragment {

    /**
     *
     */
    private static final String EXTRA_TYPE_KEY = "type";

    /**
     * 新闻收藏列表
     */
    private final int REQUEST_COLLECTION_TASK_ID = 101;
    /**
     * 删除收藏
     */
    private final int REQUEST_DEL_NEWS_COLLECTION_TASK_ID = 102;

    /**
     *
     */
    @BindView(R.id.listView)
    protected PullToRefreshSwipeMenuListView listView;

    private NewsListAdapter newsListAdapter;

    @Request
    private CollectionRestUsage collectionRestUsage;

    @Request
    private TownRestUsage townRestUsage;

    @Request
    private TownNoticeRestUsage townNoticeRestUsage;

    private int page;

    /**
     *
     */
    private int type;
    private List<News> collectionList;

    /**
     *
     * @param type
     * @return
     */
    public static CollectionFragment newInstance(int type){
        CollectionFragment collectionFragment = new CollectionFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_TYPE_KEY, type);

        collectionFragment.setArguments(bundle);

        return collectionFragment;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_collection;
    }

    @Override
    public void initUI() {
        super.initUI();

        type = getArguments().getInt(EXTRA_TYPE_KEY, 0);

        initListView();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId){
            case REQUEST_COLLECTION_TASK_ID:    //收藏列表
                CollectionResponse collectionResponse = null;
                if(msg.getIsSuccess()){
                    collectionResponse = (CollectionResponse) msg.getObj();
                    if(collectionResponse!=null){
                        if(collectionResponse.getCollectionList()!=null && collectionResponse.getCollectionList().size()!=0){
                            if(listView.isPullDownToRefresh()){
                                newsListAdapter.getDataList().clear();
                            }
                            collectionList=collectionResponse.getCollectionList();
                            newsListAdapter.getDataList().addAll(collectionResponse.getCollectionList());
                            newsListAdapter.notifyDataSetChanged();
                        }
                    }
                }
                if(collectionResponse == null || collectionResponse.getCollectionList() == null || collectionResponse.getCollectionList().size()==0 || newsListAdapter.getCount() >= collectionResponse.getTotalcount()){   //加载结束
                    listView.onLoadMoreCompleteAndNoData();
                }else{
                    listView.onLoadMoreComplete();
                }
                break;
            case REQUEST_DEL_NEWS_COLLECTION_TASK_ID:   //删除收藏
                if(msg.getIsSuccess()){
                    List<News> delNewsList = (List<News>) msg.getTargetObj();
                    if(delNewsList!=null && delNewsList.size()!=0){
                        newsListAdapter.getDataList().removeAll(delNewsList);
                        newsListAdapter.clearSelectedNews();
                        newsListAdapter.notifyDataSetChanged();

                        if(getActivity() instanceof CollectionActivity){
                            ((CollectionActivity)getActivity()).switchEditStatus(false);
                        }
                    }
                }
                dismissProgressDialog();
                break;
        }
    }

    /**
     * 初始化ListView
     */
    private void initListView(){
        listView.getRefreshableView().setMenuCreator(new SwipeMenuCreatorImpl(getContext().getApplicationContext()));
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.getRefreshableView().setCacheColorHint(Color.TRANSPARENT);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<SwipeMenuListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<SwipeMenuListView> refreshView) {
                page = 1;
                requestCollection();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<SwipeMenuListView> refreshView) {
                page++;
                requestCollection();
            }
        });
        listView.getRefreshableView().setOnSwipeMenuListener(new SwipeMenuListView.OnSwipeMenuListener() {
            @Override
            public boolean isEnableSwipeMenu(int position) {
                return !isEditStatus();
            }
        });
        listView.getRefreshableView().setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                if (index == 0) {
                    News news = newsListAdapter.getItem(position);
                    if(news!=null){
                        requestDelCollection(Arrays.asList(news));
                        newsListAdapter.getDataList().remove(news);
                        newsListAdapter.notifyDataSetChanged();
                    }
                    return true;
                }
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position = position - listView.getRefreshableView().getHeaderViewsCount();
                News news = newsListAdapter.getItem(position);
                if(isEditStatus()){ //编辑状态
                    newsListAdapter.toggle(news);
                    newsListAdapter.notifyDataSetChanged();
                }else{  //非编辑状态

                    if(isTown()){   //乡镇
                        news.appNewsType = News.TYPE_TOWN_NEWS;
                        NewsDetailActivity.startActivity(getActivity(), news);
                    }else if(isTownAnno()){ //乡镇公告
                        if (collectionList.get(position).type==0){
                            news.appNewsType = News.TYPE_TOWN_ANNOUNCEMENT;
                            AnnouncementActivity.startActivity(getActivity(), news);
                        }else{
                            //社团公告
                            NoticeDetailActivity.startActivity(getContext(), collectionList.get(position).newsId, collectionList.get(position).title, collectionList.get(position).publishTime);
                        }
                    }else{
                        NewsDetailActivity.startActivity(getActivity(), news);
                    }

                }
            }
        });

        newsListAdapter = new NewsListAdapter(this).setNeedEditStatus(true);
        listView.getRefreshableView().setAdapter(newsListAdapter);
        listView.setTopRefreshing();
    }

    /**
     * 乡镇
     * @return
     */
    private boolean isTown(){
        return type == 1;
    }

    /**
     * 乡镇公告
     * @return
     */
    private boolean isTownAnno(){
        return type == 2;
    }

    /**
     * 请求收藏列表
     */
    private void requestCollection(){
        if(isTown()){   //乡镇
            townRestUsage.getMyLikeNews(REQUEST_COLLECTION_TASK_ID, page);
        }else if(isTownAnno()){ //乡镇公告
            townNoticeRestUsage.getMyLikeNews(REQUEST_COLLECTION_TASK_ID, page);
        }else{  //普通新闻
            collectionRestUsage.getMyLikeNews(REQUEST_COLLECTION_TASK_ID, page);
        }
    }

    /**
     * 请求删除收藏
     */
    private void requestDelCollection(List<News> newsList){
        if(newsList!=null && newsList.size()!=0){
            String newsIdStr = "";
            for(int i=0;i<newsList.size();i++){
                News news = newsList.get(i);
                newsIdStr +=news.newsId;
                if(i != newsList.size()-1){
                    newsIdStr +=",";
                }
            }
            showProgressDialog();
            if(isTown()){   //乡镇
                townRestUsage.deleteNewCollection(REQUEST_DEL_NEWS_COLLECTION_TASK_ID, newsIdStr, newsList);
            }else if(isTownAnno()){ //乡镇公告
                townNoticeRestUsage.deleteNewCollection(REQUEST_DEL_NEWS_COLLECTION_TASK_ID, newsIdStr, newsList);
            }else{
                collectionRestUsage.deleteNewCollection(REQUEST_DEL_NEWS_COLLECTION_TASK_ID, newsIdStr, newsList);
            }
        }
    }

    /**
     * 关闭菜单
     */
    public void smoothCloseMenu(){
        if(listView!=null){
            listView.getRefreshableView().smoothCloseMenu();
        }
    }

    /**
     * 删除收藏
     */
    public void delCollection(){
        List<News> selectedNewsList = newsListAdapter.getSelectedNewsList();
        if(selectedNewsList==null || selectedNewsList.size()==0){
            ToastUtil.showCenter(getActivity(), "请选择要删除的新闻");
            return ;
        }
        requestDelCollection(selectedNewsList);
    }

    /**
     * 是否编辑状态
     * @return
     */
    public boolean isEditStatus(){
        if(getActivity() instanceof CollectionActivity){
            return ((CollectionActivity)getActivity()).isEditStatus();
        }
        return false;
    }

    /**
     * 切换编辑状态
     */
    public void switchEditStatus(){
        boolean isEdit = isEditStatus();
        if(isEdit){ //取消
            newsListAdapter.clearSelectedNews();
            newsListAdapter.notifyDataSetChanged();
        }
        if(getActivity() instanceof CollectionActivity){
            ((CollectionActivity)getActivity()).switchEditStatus(!isEdit);
        }
    }

    /**
     * 横向滑动菜单
     */
    class SwipeMenuCreatorImpl implements SwipeMenuCreator {

        private Context context;

        public SwipeMenuCreatorImpl(Context context) {
            this.context = context;
        }

        @Override
        public void create(SwipeMenu menu) {
            SwipeMenuItem deleteItem = new SwipeMenuItem(context);
            deleteItem.setBackground(new ColorDrawable(Color.RED));
            deleteItem.setIcon(R.drawable.xx_icon_12);
            deleteItem.setWidth(DensityUtil.dp2px(context, 90));
            menu.addMenuItem(deleteItem);
        }

    }

}
