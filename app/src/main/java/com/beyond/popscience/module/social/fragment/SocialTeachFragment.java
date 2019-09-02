package com.beyond.popscience.module.social.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.view.pullrefresh.PullToRefreshBase;
import com.beyond.library.view.pullrefresh.PullToRefreshListView;
import com.beyond.library.view.pullrefresh.PullToRefreshRecycleView;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseFragment;
import com.beyond.popscience.frame.base.CustomRecyclerBaseAdapter;
import com.beyond.popscience.frame.net.SocialRestUsage;
import com.beyond.popscience.frame.pojo.NoticeDetail;
import com.beyond.popscience.frame.pojo.TeachListObj;
import com.beyond.popscience.module.social.NoticeDetailActivity;
import com.beyond.popscience.module.social.SocialCircleContentV2Activity;
import com.beyond.popscience.module.social.TeachDetailActivity;
import com.beyond.popscience.module.social.adapter.NoticeListAdapter;
import com.beyond.popscience.module.social.adapter.TeachListAdapter;

import butterknife.BindView;

/**
 * 圈子教学
 * Created by yao.cui on 2017/7/17.
 */

public class SocialTeachFragment extends BaseFragment{
    private static final String KEY_ID = "id";
    private static final int TASK_GET_TEACH_LIST=1211;

    @BindView(R.id.recyclerView)
    protected PullToRefreshRecycleView recyclerView;
    private TeachListAdapter adapter;
    @Request
    private SocialRestUsage restUsage;
    private int mCurrentPage = 1;
    private TeachListObj teachList;
    private String id;

    public static SocialTeachFragment newInstance(String id){
        SocialTeachFragment fragment = new SocialTeachFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ID,id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_pull_list;
    }

    @Override
    public void initUI() {
        super.initUI();
        id = getArguments().getString(KEY_ID);

        initRecyclerView();
    }

    /**
     * 初始化 RecyclerView
     */
    private void initRecyclerView(){
        recyclerView.setMode(PullToRefreshBase.Mode.BOTH);
        recyclerView.getRefreshableView().setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        adapter = new TeachListAdapter(this);

        recyclerView.getRefreshableView().setAdapter(adapter);
        //
        recyclerView.setOnReadyForPullListener(new PullToRefreshRecycleView.OnReadyForPullListener() {
                @Override
                public boolean isDisablePullStart() {
                    if(getActivity() instanceof SocialCircleContentV2Activity){
                        return !((SocialCircleContentV2Activity)getActivity()).isMDScrollBottom();
                    }
                    return false;
                }

                @Override
                public boolean isDisablePullEnd() {
                    if(getActivity() instanceof SocialCircleContentV2Activity){
                        return !((SocialCircleContentV2Activity)getActivity()).isMDScrollTop();
                    }
                    return false;
                }
        });
        //
        recyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                mCurrentPage=1;
                getTeachList();
            }

            @Override
            public void onPullUpToRefresh(final PullToRefreshBase<RecyclerView> refreshView) {
                mCurrentPage++;
                getTeachList();
            }
        });

        adapter.setOnItemClickListener(new CustomRecyclerBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, int position, long id) {
                TeachDetailActivity.startActivity(getContext(),adapter.getDataList().get(position).getTeachId());
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.setTopRefreshing();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        if (msg.getIsSuccess()){
            if (recyclerView.isPullDownToRefresh()){
                adapter.getDataList().clear();
            }
            teachList = (TeachListObj) msg.getObj();

            if (teachList!= null && teachList.getTeachList()!= null){
                adapter.getDataList().addAll(teachList.getTeachList());
                adapter.notifyDataSetChanged();
            }
        }

        recyclerView.onRefreshComplete();
    }

    private void getTeachList(){
        restUsage.getTeachList(TASK_GET_TEACH_LIST,id,mCurrentPage);
    }
}
