package com.beyond.popscience.locationgoods.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.popscience.MapUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseFragment;
import com.beyond.popscience.locationgoods.bean.ProductDetail;
import com.beyond.popscience.locationgoods.http.AddressRestUsage;
import com.beyond.popscience.module.home.ChatActivity;
import com.beyond.popscience.module.home.fragment.Constant;
import com.beyond.popscience.module.news.ShowPhotoActivity;
import com.beyond.popscience.utils.CreatLayoutUtils;
import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 商家详情
 */
public class SShopDateilFragment extends BaseFragment {
    @BindView(R.id.listView)
    RecyclerView listView;
    Unbinder unbinder;
    @BindView(R.id.sc)
    NestedScrollView sc;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_take)
    TextView tvTake;
    @BindView(R.id.tv_yysj)
    TextView tvYysj;
    @BindView(R.id.tv_ptps)
    TextView tvPtps;
    @BindView(R.id.tv_nhjs)
    TextView tvNhjs;
    @BindView(R.id.iv_img_two)
    ImageView ivImgTwo;
    private CommonAdapter<String> adapter;
    private String id;
    List<String> lists = new ArrayList<>();
    @Request
    AddressRestUsage addressRestUsage;
    private ProductDetail obj;

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_address_four;
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
        super.initUI();
        if (null != getArguments()) {
            id = getArguments().getString("id");
        }
        initDateListView();
        getDates();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void getDates() {
        addressRestUsage.getProductDateis(1008611, id);
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case 1008611:
                if (msg.getIsSuccess()) {
                    obj = (ProductDetail) msg.getObj();
                    setData(obj);
                }
                break;
        }
    }

    private void setData(ProductDetail obj) {
        ProductDetail.ShopBean shop = obj.getShop();
        tvName.setText(shop.getShopName());
        tvAddress.setText(shop.getShopAddress());
        tvPhone.setText(shop.getShopTel());
        tvNhjs.setText(shop.getShopDetail());
        tvYysj.setText("营业时间:"+shop.getOpenTime());
        if (shop.getShopAuth() == 0) {
            ivImgTwo.setVisibility(View.GONE);
        } else {
            ivImgTwo.setVisibility(View.VISIBLE);
        }
        //农户实景
        String imageList = shop.getShopImgList();
        String[] imagesss = imageList.split(",");
        if (lists.size() > 0) {
            lists.clear();
        }
        for (String sss : imagesss) {
            lists.add(sss);
        }
        adapter.notifyDataSetChanged();
    }

    private void initDateListView() {
        CreatLayoutUtils.cretGridViewLayout(getActivity(), listView, 3);
        adapter = new CommonAdapter<String>(getActivity(), R.layout.item_shop_data_iv, lists) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                Glide.with(getActivity()).load(s).into((ImageView) holder.getView(R.id.iv_ss));
            }
        };
        listView.setAdapter(adapter);
        listView.setFocusable(false);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                ShowPhotoActivity.startActivity(getActivity(), lists, position);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        sc.post(new Runnable() {

            @Override
            public void run() {
                sc.scrollTo(0, 20);
            }
        });
    }

    @OnClick({R.id.tv_address, R.id.tv_phone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_address:
                final String[] address = new String[]{"高德地图", "百度地图"};
                new AlertDialog.Builder(getActivity()).setTitle("是否开始导航")
                        .setSingleChoiceItems(address, 0,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (which == 0) {
                                            if (MapUtil.isGdMapInstalled()) {
                                                MapUtil.openGaoDeNavi(getActivity(), BeyondApplication.getInstance().getLocation().latitude, BeyondApplication.getInstance().getLocation().longitude, BeyondApplication.getInstance().getLocation().street, obj.getShop().getShopAddress());
                                            } else {
                                                //这里必须要写逻辑，不然如果手机没安装该应用，程序会闪退，这里可以实现下载安装该地图应用
                                                Toast.makeText(getActivity(), "尚未安装高德地图", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        if (which == 1) {
                                            if (MapUtil.isBaiduMapInstalled()) {
                                                MapUtil.openBaiDuNavi(getActivity(), BeyondApplication.getInstance().getLocation().latitude, BeyondApplication.getInstance().getLocation().longitude, BeyondApplication.getInstance().getLocation().getAddressStr(), obj.getProduct().getArea()+obj.getShop().getShopAddress());
                                            } else {
                                                Toast.makeText(getActivity(), "尚未安装百度地图", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        dialog.dismiss();
                                    }
                                })
                        .setNegativeButton("取消", null).show();
                break;
            case R.id.tv_phone:
                if (null != obj.getShop().getShopTel()) {
                    Intent intentq = new Intent(Intent.ACTION_CALL);
                    Uri data = Uri.parse("tel:" + obj.getShop().getShopTel());
                    intentq.setData(data);
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(intentq);
                }
                break;
        }
    }


    @OnClick(R.id.tv_take)
    public void onViewClicked() {
        Intent intent1 = new Intent(getActivity(), ChatActivity.class);
        intent1.putExtra(Constant.EXTRA_USER_ID, obj.getShop().getUserId() + "");
        startActivity(intent1);
    }
}
