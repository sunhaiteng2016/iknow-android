package com.beyond.popscience.module.town.fragment;

import android.view.View;
import android.widget.AdapterView;

import com.beyond.library.view.pullrefresh.PullToRefreshListView;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseFragment;
import com.beyond.popscience.frame.pojo.TopicItem;
import com.beyond.popscience.module.town.TopicDetailActivity;
import com.beyond.popscience.module.town.adapter.TopicListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnItemClick;

/**
 * Created by lenovo on 2017/6/23.
 */

public class TopicFragment extends BaseFragment {
    @BindView(R.id.lvTopic)
    protected PullToRefreshListView mLvTopic;
    private TopicListAdapter mAdapter;
    private List<TopicItem> mTopics;

    public static TopicFragment getInstance(){
        return new TopicFragment();
    }

    @Override
    public void initUI() {
        super.initUI();

        initList();
    }

    private void initList(){
        mTopics = new ArrayList<>();
        mAdapter = new TopicListAdapter(this);
        for (int i = 0; i < 20;i++){
            TopicItem item = new TopicItem();
            mTopics.add(item);
        }
        mAdapter.getDataList().addAll(mTopics);
        mLvTopic.setAdapter(mAdapter);

        mLvTopic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TopicDetailActivity.startActivity(TopicFragment.this,mAdapter.getItem(position));
            }
        });

    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_topic;
    }
}
