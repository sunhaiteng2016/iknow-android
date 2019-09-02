package com.beyond.popscience.physicalstore.adapters;


import android.support.annotation.Nullable;

import com.beyond.popscience.R;
import com.beyond.popscience.physicalstore.bean.CommentBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class CommentAdapter extends BaseQuickAdapter<CommentBean, BaseViewHolder> {

	public CommentAdapter(@Nullable List<CommentBean> data) {
		super(R.layout.item_comment, data);
	}

	@Override
	protected void convert(BaseViewHolder helper, CommentBean item) {

	}
}
