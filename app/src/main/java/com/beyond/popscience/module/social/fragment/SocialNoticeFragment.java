package com.beyond.popscience.module.social.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.sharesdk.ShareUtil;
import com.beyond.library.sharesdk.WebViewShare;
import com.beyond.library.view.pullrefresh.PullToRefreshBase;
import com.beyond.library.view.pullrefresh.PullToRefreshRecycleView;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseFragment;
import com.beyond.popscience.frame.base.CustomRecyclerBaseAdapter;
import com.beyond.popscience.frame.net.SocialRestUsage;
import com.beyond.popscience.frame.pojo.NoticeDetail;
import com.beyond.popscience.frame.pojo.NoticeListObj;
import com.beyond.popscience.module.social.NoticeDetailActivity;
import com.beyond.popscience.module.social.SocialCircleContentV2Activity;
import com.beyond.popscience.module.social.adapter.NoticeListAdapter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 圈子 公告列表
 * Created by yao.cui on 2017/7/17.
 */

public class SocialNoticeFragment extends BaseFragment {
    private static final String KEY_ID = "id";
    private static final int TASK_GET_NOTICE_LIST = 1121;

    @BindView(R.id.recyclerView)
    protected PullToRefreshRecycleView recyclerView;
    private NoticeListAdapter adapter;
    @Request
    private SocialRestUsage restUsage;
    private int mCurrentPage = 1;
    private NoticeListObj noticeList;
    private String id;

    public static SocialNoticeFragment newInstance(String id){
        SocialNoticeFragment fragment = new SocialNoticeFragment();
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

        adapter = new NoticeListAdapter(this);

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
                getNotices();
            }

            @Override
            public void onPullUpToRefresh(final PullToRefreshBase<RecyclerView> refreshView) {
                mCurrentPage++;
                getNotices();
            }
        });

        adapter.setOnItemClickListener(new CustomRecyclerBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, int position, long id) {
                NoticeDetail detail = adapter.getDataList().get(position);
                NoticeDetailActivity.startActivity(getContext(),detail.getNoticeId(),detail.getAuthor(),detail.getPublishTime());
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

            noticeList = (NoticeListObj) msg.getObj();

            if (noticeList!= null && noticeList.getNoticeList()!=null){

                adapter.getDataList().addAll(noticeList.getNoticeList());
                adapter.notifyDataSetChanged();
            }

        }

        recyclerView.onRefreshComplete();
    }

    private void getNotices(){
        restUsage.getNoticeList(TASK_GET_NOTICE_LIST,id,mCurrentPage);
    }


}
