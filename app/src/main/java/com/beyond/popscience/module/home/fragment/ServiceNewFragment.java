package com.beyond.popscience.module.home.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.DensityUtil;
import com.beyond.library.util.ToastUtil;
import com.beyond.library.view.pullrefresh.PullToRefreshBase;
import com.beyond.library.view.pullrefresh.PullToRefreshRecycleView;
import com.beyond.popscience.R;
import com.beyond.popscience.api.ProductApi;
import com.beyond.popscience.frame.base.BaseFragment;
import com.beyond.popscience.frame.pojo.ServiceGoodsList;
import com.beyond.popscience.module.home.adapter.ServiceAdapter;
import com.beyond.popscience.widget.GridSpacingItemDecoration;

import butterknife.BindView;

/**
 * Describe：新的 服务页面
 * Date：2018/3/7
 * Time: 15:22
 * Author: Bin.Peng
 */

public class ServiceNewFragment extends BaseFragment {
    @BindView(R.id.rv_service)
    PullToRefreshRecycleView rv_service;

    private ServiceAdapter serviceAdapter;
    private int page = 1;
    private ServiceGoodsList serviceGoodsList;

    @Request
    private ProductApi productApi;
    //private List<String> mListDatas = new ArrayList<>();

    //商品列表
    private final int PRODUCT_LIST_ID = 1;

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_new_service;
    }

    public static ServiceNewFragment getInstance() {
        ServiceNewFragment fragment = new ServiceNewFragment();
        return fragment;
    }

    @Override
    public void initUI() {
        super.initUI();
        initRecycleView();
        getProductsList();

        rv_service.setTopRefreshing();
    }

    //获取商品列表
    private void getProductsList() {
        productApi.getProductsList(PRODUCT_LIST_ID, page);
    }

    private void initRecycleView() {
        serviceAdapter = new ServiceAdapter(getActivity());
        rv_service.setMode(PullToRefreshBase.Mode.BOTH);
        rv_service.getRefreshableView().setLayoutManager(new GridLayoutManager(getContext(), 2));
        //rv_service.getRefreshableView().addItemDecoration(new GridSpacingItemDecoration(1, DensityUtil.dp2px(getActivity(), 1), false));
        rv_service.getRefreshableView().setAdapter(serviceAdapter);

        rv_service.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                page = 1;
                getProductsList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                page++;
                getProductsList();
            }
        });
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case PRODUCT_LIST_ID:
                rv_service.onRefreshComplete();
                if (msg.getIsSuccess()) {
                    //是否下拉刷新
                    if (rv_service.isPullDownToRefresh()) {
                        serviceAdapter.getDataList().clear();
                    }
                    serviceGoodsList = (ServiceGoodsList) msg.getObj();
                    if (serviceGoodsList != null && serviceGoodsList.getProductList() != null) {
                        serviceAdapter.getDataList().addAll(serviceGoodsList.getProductList());
                        serviceAdapter.notifyDataSetChanged();
                    }
                } else {
                    ToastUtil.show(getContext(), msg.getMsg());
                }
                break;
        }
    }

    //写adapter的方法
//    private void setServiceAdapter() {
//        BaseRVAdapter mBaseAdapter = new BaseRVAdapter(getActivity(), serviceGoodsList.getProductList()) {
//            @Override
//            public int getLayoutId(int viewType) {
//                return R.layout.item_big_img;
//            }
//
//            @Override
//            public void onBind(BaseViewHolder holder, int position) {
//
//            }
//
//        };
//    }

//    private void getDatas() {
//        Map<String, Object> params = new HashMap<>();
//        params.put("page", page);
//        XUtil.Post(UrlConfig.GET_PRODUCTS + page, params, new MyCallBack<String>() {
//            @Override
//            public void onSuccess(String result) {
//                super.onSuccess(result);
//            }
//
//            @Override
//            public void onFinished() {
//                super.onFinished();
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                super.onError(ex, isOnCallback);
//            }
//        });
//    }
}
