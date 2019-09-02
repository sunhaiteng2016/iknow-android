package com.beyond.popscience.module.town;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.beyond.library.network.enity.MSG;
import com.beyond.library.view.pullrefresh.PullToRefreshBase;
import com.beyond.library.view.pullrefresh.PullToRefreshListView;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.module.home.entity.Address;
import com.beyond.popscience.module.news.GlobalSearchActivity;
import com.beyond.popscience.module.town.adapter.SearchTownAddressAdapter;
import com.beyond.popscience.module.town.task.SearchAddressTask;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 地址搜索结果
 * Created by linjinfa on 2017/6/9.
 * email 331710168@qq.com
 */
public class TownAddressSearchResultActivity extends BaseActivity {

    /**
     * 搜索
     */
    private final int REQUEST_SEARCH_ADDRESS_TASK_ID = 101;
    /**
     *
     */
    private final static String EXTRA_KEY_WORD_KEY = "keyWord";

    private final static String EXTRA_AREA_TYPE_KEY = "areaType";

    @BindView(R.id.listView)
    protected PullToRefreshListView listView;
    @BindView(R.id.emptyTxtView)
    protected TextView emptyTxtView;
    @BindView(R.id.searchLabelTxtView)
    protected TextView searchLabelTxtView;
    /**
     *
     */
    private SearchTownAddressAdapter addressAdapter;
    /**
     *
     */
    private String keyWord;
    /**
     * 地址类型  0：远乡镇老地址  1：浙江市 区 地址
     */
    private int areaType = 0;

    /**
     *
     * @param context
     */
    public static void startActivity(Context context, String keyWord, int areaType){
        Intent intent = new Intent(context, TownAddressSearchResultActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        intent.putExtra(EXTRA_KEY_WORD_KEY, keyWord);
        intent.putExtra(EXTRA_AREA_TYPE_KEY, areaType);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_town_address_search_result;
    }

    @Override
    public void initUI() {
        keyWord = getIntent().getStringExtra(EXTRA_KEY_WORD_KEY);
        areaType = getIntent().getIntExtra(EXTRA_AREA_TYPE_KEY,0);
        searchLabelTxtView.setText(keyWord);

        initListView();
        requestSearchAddress();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId){
            case REQUEST_SEARCH_ADDRESS_TASK_ID:    //搜索地址
                List<Address> addressList = (List<Address>) msg.getObj();

                addressAdapter.getDataList().clear();
                addressAdapter.getDataList().addAll(addressList);
                addressAdapter.notifyDataSetChanged();

                switchListViewEmpty();

                dismissProgressDialog();
                break;
        }
    }

    /**
     * 搜索地址
     */
    private void requestSearchAddress(){
        showProgressDialog();
        execuTask(new SearchAddressTask(REQUEST_SEARCH_ADDRESS_TASK_ID, keyWord,areaType));
    }

    /**
     * 初始化
     */
    private void initListView(){
        listView.setMode(PullToRefreshBase.Mode.DISABLED);
        listView.getRefreshableView().setCacheColorHint(Color.TRANSPARENT);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position = position - listView.getRefreshableView().getHeaderViewsCount();
                Address address = addressAdapter.getItem(position);

                BeyondApplication.getInstance().popActivity(GlobalSearchActivity.class, TownCategoryActivity.class);

                Intent data = new Intent();
                data.putExtra("address", address);
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        });

        addressAdapter = new SearchTownAddressAdapter(this);
        listView.getRefreshableView().setAdapter(addressAdapter);
    }

    /**
     * 切换 listview 与 emptyview
     */
    private void switchListViewEmpty(){
        if(addressAdapter.getCount() == 0){
            listView.setVisibility(View.GONE);
            emptyTxtView.setVisibility(View.VISIBLE);
            emptyTxtView.setText("未搜索到 "+keyWord+" 相关资讯");
        }else{
            listView.setVisibility(View.VISIBLE);
            emptyTxtView.setVisibility(View.GONE);
        }
    }

    /**
     * 搜索
     */
    @OnClick(R.id.searchLabelTxtView)
    public void searchClick(){
        finish();
    }

}
