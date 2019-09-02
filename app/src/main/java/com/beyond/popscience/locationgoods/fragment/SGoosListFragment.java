package com.beyond.popscience.locationgoods.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseFragment;
import com.beyond.popscience.locationgoods.bean.ProductDetail;
import com.beyond.popscience.module.news.ShowPhotoActivity;
import com.beyond.popscience.utils.CreatLayoutUtils;
import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SGoosListFragment extends BaseFragment {
    @BindView(R.id.listView)
    RecyclerView listView;
    Unbinder unbinder;
    @BindView(R.id.tv_production_place)
    TextView tvProductionPlace;
    @BindView(R.id.tv_brand)
    TextView tvBrand;
    @BindView(R.id.rlv_attr)
    RecyclerView rlvAttr;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private CommonAdapter<Describe> adapter;
    List<Describe> lists = new ArrayList<>();
    private List<String> attrList = new ArrayList<>();
    private CommonAdapter<String> attrAdapter;
    List<String> imgList = new ArrayList<>();

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_address_three;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void initUI() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initDateListView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXX(ProductDetail messageEvent) {
        //设置数据
        String description = messageEvent.getProduct().getDescription();
        JSONArray jsonAry = JSON.parseArray(description);
        lists.clear();
        imgList.clear();
        for (int i = 0; i < jsonAry.size(); i++) {
            JSONObject obj = jsonAry.getJSONObject(i);
            String text = obj.getString("text");
            String image = obj.getString("image");
            Describe describe = new Describe();
            describe.img = image;
            describe.title = text;
            lists.add(describe);
            imgList.add(describe.img);
        }
        adapter.notifyDataSetChanged();
        //设置相关数据
        tvProductionPlace.setText(messageEvent.getProduct().getPlace());
        tvBrand.setText(messageEvent.getProduct().getBrand());
        //找到相关属性
        List<ProductDetail.ProductBean.SkuListBean> skuList = messageEvent.getProduct().getSkuList();
        if (null != skuList && skuList.size() > 0) {
            attrList.clear();
            //有数据
            for (ProductDetail.ProductBean.SkuListBean bean : skuList) {
                attrList.add(bean.getSp1());
            }
            attrAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void initDateListView() {
        CreatLayoutUtils.creatLinearLayout(getActivity(), listView);
        adapter = new CommonAdapter<Describe>(getActivity(), R.layout.item_goods_list_data, lists) {

            @Override
            protected void convert(ViewHolder holder, Describe s, int position) {
                holder.setText(R.id.describe_content, s.title);
                Glide.with(getActivity()).load(s.img).into((ImageView) holder.getView(R.id.img));
            }
        };
        listView.setAdapter(adapter);


        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                ShowPhotoActivity.startActivity(getActivity(), imgList, position);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        CreatLayoutUtils.creatLinearLayout(getActivity(), rlvAttr);
        attrAdapter = new CommonAdapter<String>(getActivity(), R.layout.item_only_textview_three_ssss, attrList) {

            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.tv, s);
            }
        };
        rlvAttr.setAdapter(attrAdapter);
    }

    public class Describe {
        public String title;
        public String img;
    }

}
