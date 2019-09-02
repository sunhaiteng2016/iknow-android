package com.beyond.popscience.module.mservice.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.ToastUtil;
import com.beyond.library.view.pullrefresh.PullToRefreshBase;
import com.beyond.library.view.pullrefresh.PullToRefreshListView;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseFragment;
import com.beyond.popscience.frame.net.CommentRestUsage;
import com.beyond.popscience.frame.pojo.CommentResponse;
import com.beyond.popscience.frame.pojo.ServiceGoodsItem;
import com.beyond.popscience.frame.view.PublishedCommentView;
import com.beyond.popscience.module.mservice.GoodsDetailV2Activity;
import com.beyond.popscience.module.mservice.adapter.GoodsCommentAdapter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by linjinfa on 2017/10/12.
 * email 331710168@qq.com
 */
public class GoodsCommentFragment extends BaseFragment {

    /**
     * 请求评论列表
     */
    private final int REQUEST_COMMENT_LIST_TASK_ID = 101;

    /**
     * 发表评论
     */
    private final int REQUEST_SEND_COMMENT_TASK_ID = 102;

    /**
     *
     */
    private static final String EXTRA_SERVICE_GOODS_ITEM_KEY = "serviceGoodsItem";

    /**
     *
     */
    @BindView(R.id.publishedCommentView)
    protected PublishedCommentView publishedCommentView;
    /**
     *
     */
    @BindView(R.id.listView)
    protected PullToRefreshListView listView;

    private GoodsCommentAdapter goodsCommentAdapter;

    /**
     *
     */
    private ServiceGoodsItem serviceGoodsItem;

    /**
     *
     */
    @Request
    private CommentRestUsage commentRestUsage;

    private int page;

    /**
     *
     * @return
     */
    public static GoodsCommentFragment newInstance(ServiceGoodsItem serviceGoodsItem){
        GoodsCommentFragment goodsCommentFragment = new GoodsCommentFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_SERVICE_GOODS_ITEM_KEY, serviceGoodsItem);
        goodsCommentFragment.setArguments(bundle);

        return goodsCommentFragment;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_goods_comment;
    }

    @Override
    public void initUI() {
        super.initUI();
        serviceGoodsItem = (ServiceGoodsItem) getArguments().getSerializable(EXTRA_SERVICE_GOODS_ITEM_KEY);
        if(serviceGoodsItem == null){
            return ;
        }

        initListView();
        initPublishCommentView();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case REQUEST_COMMENT_LIST_TASK_ID: //评论列表
                CommentResponse commentResponse = null;
                if(msg.getIsSuccess()){
                    commentResponse = (CommentResponse) msg.getObj();
                    if(commentResponse!=null){
                        if(getActivity() instanceof GoodsDetailV2Activity){
                            ((GoodsDetailV2Activity)getActivity()).setCommentCountShow(commentResponse.getTotalcount());
                        }
                        if(commentResponse.getCommentList()!=null && commentResponse.getCommentList().size()!=0){
                            if(listView.isPullDownToRefresh()){
                                goodsCommentAdapter.getDataList().clear();
                            }
                            goodsCommentAdapter.getDataList().addAll(commentResponse.getCommentList());
                            goodsCommentAdapter.notifyDataSetChanged();
                        }
                    }
                }
                if(commentResponse == null || commentResponse.getCommentList() == null || commentResponse.getCommentList().size()==0 || goodsCommentAdapter.getCount() >= commentResponse.getTotal()){   //加载结束
                    listView.onLoadMoreCompleteAndNoData();
                }else{
                    listView.onLoadMoreComplete();
                }
                break;
            case REQUEST_SEND_COMMENT_TASK_ID:    //发表评论
                if(msg.getIsSuccess()){
                    publishedCommentView.hidden();
                    publishedCommentView.setText("");

                    listView.getRefreshableView().smoothScrollToPosition(0);
                    listView.setTopRefreshing();
                }
                dismissProgressDialog();
                break;
        }
    }

    /**
     * 初始化ListView
     */
    private void initListView(){
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                requestComment();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                requestComment();
            }
        });

        goodsCommentAdapter = new GoodsCommentAdapter(this);
        listView.getRefreshableView().setAdapter(goodsCommentAdapter);
        listView.setTopRefreshing();
    }

    /**
     * 请求评论列表
     */
    private void requestComment(){
        if(serviceGoodsItem != null){
            commentRestUsage.getComment(REQUEST_COMMENT_LIST_TASK_ID, page, serviceGoodsItem.getAppGoodsType(), serviceGoodsItem.getProductId());
        }
    }

    /**
     * 发表评论
     */
    private void requestPublishedComment(){
        if(serviceGoodsItem!=null){
            showProgressDialog();
            commentRestUsage.sendServiceComment(REQUEST_SEND_COMMENT_TASK_ID, serviceGoodsItem.getAppGoodsType(), serviceGoodsItem.getProductId(), publishedCommentView.getText());
        }
    }

    /**
     * 初始化 发表评论、回复
     */
    private void initPublishCommentView(){
        publishedCommentView.setOnPublishedOnClickListener(new PublishedCommentView.SimplePublishedClickListener() {
            @Override
            public void onOk() {
                if(TextUtils.isEmpty(publishedCommentView.getText().trim())){
                    ToastUtil.showCenter(getContext(), "请输入评论");
                    return ;
                }
                publishedCommentView.hiddenSoftInput();
                requestPublishedComment();
            }
        });
    }

    /**
     * 显示评论
     */
    @OnClick(R.id.bottomLinLay)
    public void showReplyPublishedCommentClick(View view){
        publishedCommentView.show();
    }

}
