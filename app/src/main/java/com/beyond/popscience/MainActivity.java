package com.beyond.popscience;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.beyond.library.network.NetWorkInject;
import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.sharesdk.ShareUtil;
import com.beyond.library.sharesdk.WebViewShare;
import com.beyond.library.util.L;
import com.beyond.library.view.pullrefresh.PullToRefreshBase;
import com.beyond.library.view.pullrefresh.PullToRefreshListView;
import com.beyond.library.view.pullrefresh.PullToRefreshRecycleView;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.base.BaseNoTitleActivity;
import com.beyond.popscience.frame.base.CustomBaseAdapter;
import com.beyond.popscience.frame.base.CustomRecyclerBaseAdapter;
import com.beyond.popscience.frame.net.TestRestUsage;
import com.beyond.popscience.frame.thirdsdk.ThirdSDKManager;
import com.beyond.popscience.module.collection.CollectionActivity;

import static com.beyond.library.network.task.UIOnMainThread.runOnUiThread;

/**
 *
 */
public class MainActivity extends BaseActivity {

    @Request
    private TestRestUsage testRestUsage;

    public static void startActivity(Context context){
        Intent intent = new Intent(context,MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    public void initUI() {
        super.initUI();

        String dsadsad = getIntent().getStringExtra("dsadsad");
        
        final PullToRefreshListView listView = (PullToRefreshListView) findViewById(R.id.listView);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                listView.setTopRefreshing();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
        HHAdapter hhAdapter = new HHAdapter(this);
        for(int i=0;i<200;i++){
            hhAdapter.getDataList().add("林金发"+i);
        }
        listView.setAdapter(hhAdapter);

        PullToRefreshRecycleView recyclerView = (PullToRefreshRecycleView) findViewById(R.id.recyclerView);
        recyclerView.setMode(PullToRefreshBase.Mode.BOTH);
        recyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {

            }
        });
        recyclerView.getRefreshableView().setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        TestAdapter testAdapter = new TestAdapter(this);
        for(int i=0;i<200;i++){
            testAdapter.getDataList().add("林金发"+i);
        }

        recyclerView.getRefreshableView().setAdapter(testAdapter);
    }

    class HHAdapter extends CustomBaseAdapter<String> {

        public HHAdapter(Activity context) {
            super(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(MainActivity.this);
            textView.setMinHeight(50);
            textView.setText(dataList.get(position));
            return textView;
        }

    }

    class TestAdapter extends CustomRecyclerBaseAdapter<String> {

        public TestAdapter(Activity context) {
            super(context);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView textView = new TextView(MainActivity.this);
            textView.setMinHeight(50);

            return new TestViewHolder(textView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((TestViewHolder)holder).setData(dataList.get(position));
        }

        class TestViewHolder extends RecyclerView.ViewHolder{

            public TestViewHolder(View itemView) {
                super(itemView);
            }

            public void setData(String txt){
                ((TextView)itemView).setText(txt);
            }
        }
    }

    /**
     * 分享
     * @param view
     */
    public void shareClick(View view){
        WebViewShare webViewShare = new WebViewShare();
        webViewShare.setTitle("我是标题");
        webViewShare.setLink("http://www.baidu.com/");
        ShareUtil.directShare(this, webViewShare);
    }

    /**
     * 直接分享
     * @param view
     */
    public void directShareClick(View view){
//        GlobalSearchActivity.startActivity(this);
//        NewsDetailActivity.startActivity(this);
        CollectionActivity.startActivity(this);
    }

    /**
     * 请求
     * @param view
     */
    public void networkClick(View view){
        NetWorkInject.init(this);
        testRestUsage.test(1);
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        L.v("refreshUI=====> "+msg+"    "+msg.getBaseResponse());

        L.v("refreshUI22=====> 数据是否来自缓存："+msg.isFromCache()+"    "+msg.getBaseResponse());
    }
}
