package com.beyond.popscience.physicalstore.fragments;


import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseFragment;
import com.beyond.popscience.physicalstore.adapters.CommentAdapter;
import com.beyond.popscience.physicalstore.bean.CommentBean;
import com.beyond.popscience.physicalstore.utils.BaseUtils;
import com.beyond.popscience.physicalstore.view.CustomLoadMoreView;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;

public class SecondFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener {
	private Context mContext;
	private ArrayList<CommentBean> cList = new ArrayList<>();
	private RecyclerView recycler;
	private CommentAdapter cAdapter;


	@Override
	public void onLoadMoreRequested() {
		recycler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (cAdapter.getItemCount() > 30) {
					cAdapter.loadMoreEnd();
				} else {
					cAdapter.addData(BaseUtils.getComment());
					cAdapter.loadMoreComplete();
				}
			}
		}, 2000);
	}

	@Override
	protected int getLayoutResID() {
		return R.layout.fragment_second;
	}

	@Override
	public void initUI() {
		super.initUI();
		mContext = getActivity();
		recycler = (RecyclerView) mRootView.findViewById(R.id.recycler);
		recycler.setLayoutManager(new LinearLayoutManager(mContext));
		cAdapter = new CommentAdapter(BaseUtils.getComment());
		View headerView = View.inflate(mContext, R.layout.item_comment_header, null);
		cAdapter.addHeaderView(headerView);
		cAdapter.setLoadMoreView(new CustomLoadMoreView());
		cAdapter.setOnLoadMoreListener(this, recycler);
		recycler.setAdapter(cAdapter);
	}


}
