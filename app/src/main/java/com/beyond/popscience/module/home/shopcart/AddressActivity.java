package com.beyond.popscience.module.home.shopcart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.view.pullrefresh.PullToRefreshBase;
import com.beyond.library.view.pullrefresh.PullToRefreshRecycleView;
import com.beyond.popscience.R;
import com.beyond.popscience.api.AddressListApi;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.pojo.AddressListBean;
import com.beyond.popscience.frame.pojo.DataBean;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.utils.adapter.BaseRVAdapter;
import com.beyond.popscience.utils.adapter.BaseViewHolder;
import com.vincent.filepicker.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 收货地址  item_address
 */
public class AddressActivity extends BaseActivity {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.leftTxtView)
    TextView leftTxtView;
    @BindView(R.id.rightImgView)
    ImageView rightImgView;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.shadowView)
    View shadowView;
    @BindView(R.id.topReLay)
    RelativeLayout topReLay;
    @BindView(R.id.pull_recycle_view)
    PullToRefreshRecycleView pull_recycle_view;
    @BindView(R.id.img_address_null)
    ImageView imgAddressNull;
    @BindView(R.id.rl_address_null)
    RelativeLayout rlAddressNull;
    @BindView(R.id.tv_add)
    TextView tvAdd;
    @BindView(R.id.tv_msg)
    TextView tvMsg;
    @BindView(R.id.empty_layout)
    LinearLayout emptyLayout;

    @Request
    private AddressListApi addressListApi;
    private final int TASK_SEND_CODE = 1005;//请求码
    private final int TASKID_SET_DEFULT = 1006;//设置默认地址
    private final int TASKID_CANCLE_DEFULT = 1007;//取消默认地址
    private final int TASKID_DELETE_ADDRESS = 1008;//删除地址
    private BaseRVAdapter mBaseAdapter;
    private List<DataBean> mDataList = new ArrayList<>();
    private final int REQUEST_OK = 100;//返回结果吗

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_address;
    }

    @Override
    public void initUI() {
        super.initUI();
        tvTitle.setText("地址列表");
        rightImgView.setVisibility(View.GONE);
        rightImgView.setImageResource(R.drawable.icon_dots);

//        initContent();
        initRefreshListView();
        pull_recycle_view.setTopRefreshing();
    }

    private void initRefreshListView() {
        setListAdapter();
        pull_recycle_view.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pull_recycle_view.getRefreshableView().setLayoutManager(new LinearLayoutManager(this));
        pull_recycle_view.getRefreshableView().setAdapter(mBaseAdapter);

        pull_recycle_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                if (!TextUtils.isEmpty(UserInfoUtil.getInstance().getUserInfo().getUserId())) {
                   initContent();
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {

            }
        });
    }

    //设置数据
    private void setListAdapter() {
        mBaseAdapter = new BaseRVAdapter(this, mDataList) {
            @Override
            public int getLayoutId(int viewType) {
                return R.layout.item_address;
            }

            @Override
            public void onBind(BaseViewHolder holder, final int position) {

                TextView tv_name = holder.getTextView(R.id.tv_name);//姓名
                TextView tv_phone = holder.getTextView(R.id.tv_phone);//电话
                TextView tv_ismoren = holder.getTextView(R.id.tv_ismoren);//是否是默认的地址
                TextView tv_address = holder.getTextView(R.id.tv_address);
//                ImageView img_moren = holder.getImageView(R.id.img_moren);//默认地址的选择
                TextView tv_edit = holder.getTextView(R.id.tv_edit);//编辑
                TextView tv_delete = holder.getTextView(R.id.tv_delete);//删除
                if (0 != mDataList.size()) {

                    tv_address.setText(mDataList.get(position).getProvince() + mDataList.get(position).getCity() +
                            mDataList.get(position).getArea() + mDataList.get(position).getStreet() + mDataList.get(position).getAddressDetail());
                    tv_phone.setText(mDataList.get(position).getContactPhone());
                    tv_name.setText(mDataList.get(position).getContactName());
                    if (1 ==  mDataList.get(position).getStatus()) {
                        tv_ismoren.setVisibility(View.VISIBLE);
                        holder.setImageResource(R.id.img_moren, R.drawable.ic_home_selected);
                    } else {
                        tv_ismoren.setVisibility(View.GONE);
                        holder.setImageResource(R.id.img_moren, R.drawable.ic_home_unselected);
                    }
                    holder.getView(R.id.ll_ismoren).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (1 == mDataList.get(position).getStatus()) {
                                ToastUtil.getInstance(AddressActivity.this).showToast("已是默认地址");
                                addressListApi.updateAddress(TASKID_CANCLE_DEFULT,mDataList.get(position).
                                                getAddressId() + "",mDataList.get(position).getContactName(),mDataList.get(position).getContactPhone()
                                        ,mDataList.get(position).getProvince(),mDataList.get(position).getCity(),mDataList.get(position).getArea(),
                                        mDataList.get(position).getStreet(),mDataList.get(position).getAddressDetail(),
                                        2 + "");// stasus 1 是默认地址 其他都是普通地址

                            } else {
                                // TODO: 2018/3/9  设置默认地址
                                addressListApi.updateAddress(TASKID_SET_DEFULT,mDataList.get(position).
                                getAddressId() + "",mDataList.get(position).getContactName(),mDataList.get(position).getContactPhone()
                                ,mDataList.get(position).getProvince(),mDataList.get(position).getCity(),mDataList.get(position).getArea(),
                                        mDataList.get(position).getStreet(),mDataList.get(position).getAddressDetail(),
                                        1 + "");// stasus 1 是默认地址 其他都是普通地址
                            }
                        }
                    });

                    tv_edit.setOnClickListener(new View.OnClickListener() {//编辑地址
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(AddressActivity.this, AddNewAddressActivity.class);
                            intent.putExtra("title", "编辑地址");
                            intent.putExtra("type", 2);//1新增 2编辑
                            intent.putExtra("contactName",mDataList.get(position).getContactName());
                            intent.putExtra("contactPhone",mDataList.get(position).getContactPhone());
                            String address = mDataList.get(position).getProvince() + mDataList.get(position).getCity() + mDataList.get(position).getArea();
                            intent.putExtra("address",address);
                            intent.putExtra("street",mDataList.get(position).getStreet());
                            intent.putExtra("addressDetail",mDataList.get(position).getAddressDetail());
                            intent.putExtra("status",mDataList.get(position).getStatus());
                            intent.putExtra("addressId",mDataList.get(position).getAddressId());
                            intent.putExtra("province",mDataList.get(position).getProvince());
                            intent.putExtra("city",mDataList.get(position).getCity());
                            intent.putExtra("area",mDataList.get(position).getArea());
                            startActivity(intent);
                        }
                    });

                    tv_delete.setOnClickListener(new View.OnClickListener() {//删除地址
                        @Override
                        public void onClick(View v) {
                            addressListApi.dedeleteAddress(TASKID_DELETE_ADDRESS,mDataList.get(position).getAddressId() + "");
                        }
                    });
                }

            }


        };

        mBaseAdapter.addItemClickListener(new BaseRVAdapter.OnItemClickListener() {
            @Override
            public void itemSelect(int position) {
                // TODO: 2018/3/13 点击返回到确认订单界面
                Intent intent = new Intent(AddressActivity.this,CartMakeSureActivity.class);
                intent.putExtra("receive_name",mDataList.get(position).getContactName());
                intent.putExtra("address",mDataList.get(position).getProvince() + mDataList.get(position).getCity() + mDataList.get(position).getArea()
                + mDataList.get(position).getStreet());
                intent.putExtra("phone",mDataList.get(position).getContactPhone());
                setResult(REQUEST_OK,intent);
                finish();
            }
        });
    }

    //初始化数据
    private void initContent() {
        if (!TextUtils.isEmpty(UserInfoUtil.getInstance().getUserInfo().getUserId())) {
            showProgressDialog();
            addressListApi.getAddressList(TASK_SEND_CODE, UserInfoUtil.getInstance().getUserInfo().getUserId());
        }
    }

    @OnClick(R.id.tv_add)
    public void setTvAdd() {//跳转至编辑地址界面
        Intent intent = new Intent(AddressActivity.this, AddNewAddressActivity.class);
        intent.putExtra("title", "新增地址");
        intent.putExtra("type", 1);//1新增 2编辑
        startActivity(intent);
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        Log.e("===MSG: ",msg.getMsg() + msg.getObj());
        switch (taskId) {
            case TASK_SEND_CODE://获取地址列表的数据
                pull_recycle_view.onRefreshComplete();
                if (msg.getIsSuccess()) {
                    emptyLayout.setVisibility(View.GONE);
                    pull_recycle_view.setVisibility(View.VISIBLE);
                    if (pull_recycle_view.isPullDownToRefresh() && mDataList.size() != 0) {
                        mDataList.clear();
                    }
                    List<DataBean> dlist = (List<DataBean>) msg.getObj();
                    mDataList.addAll(dlist);
                    mBaseAdapter.notifyDataSetChanged();
                }else {
                    pull_recycle_view.setVisibility(View.INVISIBLE);
                    emptyLayout.setVisibility(View.VISIBLE);
                    tvMsg.setText("暂无数据");
                }
                dismissProgressDialog();
                break;
            case TASKID_SET_DEFULT://设置默认地址
                if (msg.getIsSuccess()){
                      ToastUtil.getInstance(AddressActivity.this).showToast(msg.getMsg());
                    if (!TextUtils.isEmpty(UserInfoUtil.getInstance().getUserInfo().getUserId())) {
                        initContent();
                        mBaseAdapter.notifyDataSetChanged();
                    }
                }else {
                    if (!TextUtils.isEmpty(msg.getMsg())) {
                        ToastUtil.getInstance(AddressActivity.this).showToast(msg.getMsg());
                    }
                }
                break;
            case TASKID_CANCLE_DEFULT://取消默认地址
                if (msg.getIsSuccess()){
                    ToastUtil.getInstance(AddressActivity.this).showToast(msg.getMsg());
                    if (!TextUtils.isEmpty(UserInfoUtil.getInstance().getUserInfo().getUserId())) {
                        initContent();
                        mBaseAdapter.notifyDataSetChanged();
                    }
                }else {
                    if (!TextUtils.isEmpty(msg.getMsg())) {
                        ToastUtil.getInstance(AddressActivity.this).showToast(msg.getMsg());
                    }
                }
                break;
            case TASKID_DELETE_ADDRESS://删除地址
                if (msg.getIsSuccess()){
                    ToastUtil.getInstance(AddressActivity.this).showToast(msg.getMsg());
                    if (!TextUtils.isEmpty(UserInfoUtil.getInstance().getUserInfo().getUserId())) {
                        initContent();
                        mBaseAdapter.notifyDataSetChanged();
                    }
                }else {
                    if (!TextUtils.isEmpty(msg.getMsg())) {
                        ToastUtil.getInstance(AddressActivity.this).showToast(msg.getMsg());
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initContent();
    }
}
