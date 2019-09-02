package com.beyond.popscience.locationgoods;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.locationgoods.bean.MAddressListBean;
import com.beyond.popscience.locationgoods.http.AddressRestUsage;
import com.beyond.popscience.utils.CreatLayoutUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 收货地址
 */
public class ReceivingAddressActivity extends BaseActivity {


    @BindView(R.id.go_back)
    RelativeLayout goBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.listView)
    RecyclerView listView;
    @BindView(R.id.add_address)
    TextView addAddress;
    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.phone_tv)
    TextView phoneTv;
    @BindView(R.id.detail_address_tv)
    TextView detailAddressTv;
    @BindView(R.id.ll_default_address)
    LinearLayout llDefaultAddress;
    @BindView(R.id.rlss)
    RelativeLayout rlss;
    @BindView(R.id.rl_edit_address)
    RelativeLayout rlEditAddress;
    @BindView(R.id.ll1)
    LinearLayout ll1;
    @BindView(R.id.rl1)
    RelativeLayout rl1;

    private List<MAddressListBean> lists = new ArrayList<>();
    private CommonAdapter<MAddressListBean> adapter;

    @Request
    AddressRestUsage addressRestUsage;
    private MAddressListBean mDate;
    private String flag;


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_receiving_address;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @Override
    public void initUI() {
        super.initUI();
        initListView();
        title.setText("收货地址");
        flag = getIntent().getStringExtra("flag");
        //获取收货地址
        new Thread() {
            @Override
            public void run() {
                super.run();
                addressRestUsage.getAddressList(10001, UserInfoUtil.getInstance().getUserInfo().getUserId());
            }
        }.start();
    }

    private void initListView() {
        CreatLayoutUtils.creatLinearLayout(this, listView);

        adapter = new CommonAdapter<MAddressListBean>(this, R.layout.item_add_address, lists) {

            @Override
            protected void convert(ViewHolder holder, MAddressListBean s, final int position) {
                holder.setText(R.id.name, s.getName()+",").setText(R.id.phone, s.getPhoneNumber())
                        .setText(R.id.detail_address, s.getProvince()+" "+s.getCity()+" "+s.getRegion()+" "+s.getDetailAddress());

                if (s.getDefaultStatus() == 1) {
                    holder.setBackgroundRes(R.id.tv_check, R.drawable.chenck);
                } else {
                    holder.setBackgroundRes(R.id.tv_check, R.drawable.uncheck);
                }

                holder.setOnClickListener(R.id.ed_edit, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ReceivingAddressActivity.this, AddAddressActivity.class);
                        intent.putExtra("data", lists.get(position));
                        intent.putExtra("flag", "edit");
                        startActivity(intent);
                    }
                });
            }
        };
        listView.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (null == flag) return;
                if (flag.equals("1")) {
                    Intent intent = new Intent();
                    MAddressListBean mAddressListBean = lists.get(position);
                    intent.putExtra("address", mAddressListBean);
                    setResult(101, intent);
                    finish();
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }


    @OnClick(R.id.add_address)
    public void onViewClickeds() {
        Intent intent = new Intent(this, AddAddressActivity.class);
        intent.putExtra("flag", "add");
        startActivity(intent);
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case 10001:
                if (msg.getIsSuccess()) {
                    List<MAddressListBean> listBeans = (List<MAddressListBean>) msg.getObj();
                    if (listBeans.size() > 0) {
                        ll1.setVisibility(View.VISIBLE);
                        rl1.setVisibility(View.GONE);
                        lists.clear();
                        //赛选出来 默认地址
                        for (MAddressListBean bean : listBeans) {
                            if (bean.getDefaultStatus() == 1) {
                                //设置数据
                                nameTv.setText(bean.getName());
                                phoneTv.setText(bean.getPhoneNumber());
                                detailAddressTv.setText(bean.getDetailAddress());
                                mDate = bean;
                                break;
                            }
                        }
                        if (mDate == null) {
                            //没有默认地址
                            llDefaultAddress.setVisibility(View.GONE);
                        }
                        lists.addAll(listBeans);
                        adapter.notifyDataSetChanged();
                    } else {
                        ll1.setVisibility(View.GONE);
                        rl1.setVisibility(View.VISIBLE);
                    }

                }
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        addressRestUsage.getAddressList(10001, UserInfoUtil.getInstance().getUserInfo().getUserId());
    }

    @OnClick(R.id.rl_edit_address)
    public void onViewClicked() {
        if (null == mDate) return;
        //编辑数据
        Intent intent = new Intent(ReceivingAddressActivity.this, AddAddressActivity.class);
        intent.putExtra("data", mDate);
        intent.putExtra("flag", "edit");
        startActivity(intent);
    }

    @OnClick(R.id.go_back)
    public void onViewClickedss() {
        finish();
    }

    @OnClick(R.id.ll_default_address)
    public void onViewClickedssss() {
        if (null == flag) return;
        if (flag.equals("1")) {
            Intent intent = new Intent();
            intent.putExtra("address", mDate);
            setResult(101, intent);
            finish();
        }
    }
}
