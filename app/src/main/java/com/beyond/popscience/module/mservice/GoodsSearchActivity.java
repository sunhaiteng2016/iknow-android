package com.beyond.popscience.module.mservice;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.view.pullrefresh.PullToRefreshBase;
import com.beyond.library.view.pullrefresh.PullToRefreshListView;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.ServiceRestUsage;
import com.beyond.popscience.frame.pojo.ServiceGoodsItem;
import com.beyond.popscience.frame.pojo.ServiceGoodsList;
import com.beyond.popscience.module.mservice.adapter.GoodsAdapter;
import com.beyond.popscience.widget.ServiceSortView;

import java.util.List;

import butterknife.BindView;

/**
 * 商品搜索
 * Created by yao.cui on 2017/6/24.
 */

public class GoodsSearchActivity extends BaseActivity implements ServiceSortView.ISortListener {
    private static final String KEY_WORD = "keyword";
    private final int TASK_SEARCH_GOODS=1501;//搜索商品

    @BindView(R.id.sortview)
    protected ServiceSortView sortview;
    @BindView(R.id.lvGoods)
    protected PullToRefreshListView lvGoods;
    @BindView(R.id.searchEditTxt)
    protected EditText searchEditTxt;

    private GoodsAdapter adapter;
    private List<ServiceGoodsItem> goodsList;

    private int currentPage = 1;
    private String keyword;
    private ServiceGoodsList list;

    private String byDis="0";//根据距离暂无效果 0：近的在前 1:远的在前
    private String byTime="0";//根据时间0:时间早的在前 1：时间晚的在前
    private String byMoney="0";//根据金额 0:价格低的在前 1：价格高的在前
    private String byAll="0";//根据金额 0:价格低的在前 1：价格高的在前

    @Request
    private ServiceRestUsage restUsage;

    public static void startActivity(Activity context,String keyWord){
        Intent intent=  new Intent(context,GoodsSearchActivity.class);
        intent.putExtra(KEY_WORD,keyWord);
        context.startActivity(intent);
    }

    @Override
    public void initUI() {
        super.initUI();

        keyword = getIntent().getStringExtra(KEY_WORD);
        searchEditTxt.setText(keyword);

        sortview.setSortListener(this);
        initList();
        searchGoods();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId){
            case TASK_SEARCH_GOODS:
                lvGoods.onRefreshComplete();
                if (msg.getIsSuccess()&& msg.getObj()!= null){
                    list = (ServiceGoodsList) msg.getObj();
                    if (lvGoods.isPullDownToRefresh()){
                        adapter.getDataList().clear();
                        currentPage = 2;
                    } else {
                        ++currentPage;
                    }

                    adapter.getDataList().addAll(list.getProductList());
                    adapter.notifyDataSetChanged();
                }
                break;

        }
    }

    private void initList(){
        adapter = new GoodsAdapter(this);
        lvGoods.setMode(PullToRefreshBase.Mode.BOTH);
        lvGoods.setAdapter(adapter);

        lvGoods.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                currentPage = 1;
                searchGoods();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (currentPage <=list.getTotalpage()){
                    searchGoods();
                } else {
                    lvGoods.onLoadMoreCompleteAndNoData();
                }
            }
        });
        lvGoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position = position - lvGoods.getRefreshableView().getHeaderViewsCount();
//                GoodsDetailActivity.startActivity(GoodsSearchActivity.this,adapter.getDataList().get(position).getProductId());

//                GoodsDetailV2Activity.startActivityGoods(GoodsSearchActivity.this, adapter.getDataList().get(position));
                GoodsDetailV2Activity2.startActivityGoods(GoodsSearchActivity.this, adapter.getDataList().get(position));
            }
        });

    }

    /**
     * 搜索商品
     */
    private void searchGoods(){
        restUsage.searchProduct(TASK_SEARCH_GOODS,currentPage,keyword,byDis,byMoney,byTime,byAll);
    }

    @Override
    public void sortItemClick(ServiceSortView.Type type) {
        if (type== ServiceSortView.Type.TOGETHER){
            byAll = "0";
            byMoney="";
            byDis="";
            byTime="";
        } else if (type == ServiceSortView.Type.PRICE_DOWN){
            byAll = "";
            byMoney="0";
            byDis="";
            byTime="";
        } else if (type == ServiceSortView.Type.PRICE_UP){
            byAll = "";
            byMoney="1";
            byDis="";
            byTime="";
        } else if (type == ServiceSortView.Type.TIME){
            byAll = "";
            byMoney="";
            byDis="";
            byTime="0";
        } else if (type == ServiceSortView.Type.TIME_UP){
            byAll = "";
            byMoney="";
            byDis="";
            byTime="1";
        } else if(type == ServiceSortView.Type.DISTANCE){
            byAll = "";
            byMoney="";
            byDis="0";
            byTime="";
        } else if(type == ServiceSortView.Type.DISTANCE_UP){
            byAll = "";
            byMoney="";
            byDis="1";
            byTime="";
        }

        lvGoods.setTopRefreshing();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_goods_search;
    }
}
