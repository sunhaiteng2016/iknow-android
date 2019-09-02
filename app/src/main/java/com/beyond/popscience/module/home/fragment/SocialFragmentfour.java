package com.beyond.popscience.module.home.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.DensityUtil;
import com.beyond.library.util.ToastUtil;
import com.beyond.library.view.GridViewNoScroll;
import com.beyond.library.view.pullrefresh.PullToRefreshBase;
import com.beyond.library.view.pullrefresh.PullToRefreshSwipeMenuListView;
import com.beyond.library.view.swipemenulistview.SwipeMenu;
import com.beyond.library.view.swipemenulistview.SwipeMenuCreator;
import com.beyond.library.view.swipemenulistview.SwipeMenuItem;
import com.beyond.library.view.swipemenulistview.SwipeMenuListView;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseFragment;
import com.beyond.popscience.frame.net.SocialRestUsage;
import com.beyond.popscience.frame.pojo.SocialInfo;
import com.beyond.popscience.frame.util.SPUtils;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.module.home.WelcomeActivity;
import com.beyond.popscience.module.home.adapter.OpenlistCityElv;
import com.beyond.popscience.module.home.adapter.SocialGridAdapter;
import com.beyond.popscience.module.home.adapter.SocialListAdapterTwo;
import com.beyond.popscience.module.home.entity.Address;
import com.beyond.popscience.module.home.entity.OpenCity;
import com.beyond.popscience.module.news.GlobalSearchActivity;
import com.beyond.popscience.module.social.SocialCircleContentV2Activity;
import com.beyond.popscience.module.social.SocialCircleSearchResultActivity;
import com.beyond.popscience.module.social.SocialDetailActivity;
import com.google.gson.Gson;
import com.okhttp.CallBackUtil;
import com.okhttp.OkhttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;
import okhttp3.Call;

/**
 * 我的社团
 */
public class SocialFragmentfour extends BaseFragment {

    /**
     * 我的社团 与 用户关联
     */
    private final String KEY_MY_COMMUNITY_LAST_TIMESTAMP = "myCommunityLastTimestamp_%s";
    /**
     * 首页推荐社团
     */
    private final int REQUEST_CAROUSEL_TASK_ID = 1001;
    /**
     * 我的社团
     */
    private final int REQUEST_MY_COMMUNITY_TASK_ID = 1002;
    /**
     * 退出社团
     */
    private final int REQUEST_QUIT_TASK_ID = 1003;
    /**
     * 选择地址
     */
    private final int REUQEST_TOWN_ADDRESS_CODE = 101;

    @BindView(R.id.tv_title)
    protected TextView titleTxtView;

    @BindView(R.id.leftTxtView)
    protected TextView leftTxtView;
    @BindView(R.id.tv_back)
    protected TextView tv_back;


    @BindView(R.id.listView)
    protected PullToRefreshSwipeMenuListView listView;
    Unbinder unbinder;
    private SocialListAdapterTwo socialListAdapter;
    private SocialGridAdapter socialGridAdapter;
    @Request
    private SocialRestUsage socialRestUsage;
    private GridViewNoScroll gridView;
    public boolean data;
    /**
     * 当前的地址
     */
    private Address currAddress;
    /**
     * 解决 放在 HeaderView 中的 GridView 出现无法点击的 bug     暂时没找到真正的原因
     */
    private DataSetObserver gridViewDataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            gridView.setVisibility(socialGridAdapter.getCount() == 0 ? View.GONE : View.VISIBLE);
        }
    };
    private String titles, newsId, pics;

    public static SocialFragmentfour getInstance() {
        SocialFragmentfour fragment = new SocialFragmentfour();
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REUQEST_TOWN_ADDRESS_CODE: //选择地址
                if (resultCode == Activity.RESULT_OK && data != null) {
                    Address address = (Address) data.getSerializableExtra("address");
                    if (address != null) {
                        this.currAddress = address;
                        setAddressTxt();
                    }
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        socialGridAdapter.unregisterDataSetObserver(gridViewDataSetObserver);
        super.onDestroy();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_social_three;
    }

    @Override
    public void initUI() {
        super.initUI();
        titleTxtView.setText("我的社团");
        if (getArguments() != null) {
            titles = getArguments().getString("titles");
            newsId = getArguments().getString("link");
            pics = getArguments().getString("pics");
        }
        currAddress = new Address();
        currAddress.setName("仙居");
        String[] ss = WelcomeActivity.seletedAdress.split("-");
        leftTxtView.setText(ss[1]);
        //setAddressTxt()
        initListView();
        getisopencity();
    }

    private ExpandableListView elv_opencity;
    private AlertDialog mdilaog;
    private List<OpenCity.DataBean> mlisyt = new ArrayList<>();
    private OpenlistCityElv madapter;

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case REQUEST_CAROUSEL_TASK_ID:  //首页推荐社团
                if (msg.getIsSuccess()) {
                    List<SocialInfo> socialInfoList = (List<SocialInfo>) msg.getObj();
                    if (socialInfoList != null && socialInfoList.size() != 0) {
                        initHeaderView(socialInfoList);
                    } else {
                        initHeaderView(socialInfoList);
                    }
                }
                break;
            case REQUEST_MY_COMMUNITY_TASK_ID:  //首页推荐社团
                if (msg.getIsSuccess()) {
                    List<SocialInfo> socialInfoList = (List<SocialInfo>) msg.getObj();
                    if (socialInfoList != null && socialInfoList.size() != 0) {
                        socialListAdapter.getDataList().clear();
                        socialListAdapter.getDataList().addAll(socialInfoList);
                        socialListAdapter.notifyDataSetChanged();
                    }
                }
                String currTimeStamp = BeyondApplication.getInstance().getCurrSystemTimeStampStr();
                if (!TextUtils.isEmpty(currTimeStamp)) {
                    SPUtils.put(getContext(), getMyCommunityLastTimestampKey(), currTimeStamp);
                }
                listView.onRefreshComplete();
                break;
            case REQUEST_QUIT_TASK_ID:  //退出社团
                if (msg.getIsSuccess()) {
                    SocialInfo socialInfo = (SocialInfo) msg.getTargetObj();
                    if (socialInfo != null) {
                        socialListAdapter.getDataList().remove(socialInfo);
                        socialListAdapter.notifyDataSetChanged();
                    }
                    ToastUtil.showCenter(getActivity(), "退出成功");
                }
                dismissProgressDialog();
                break;
        }
    }

    /**
     * 设置地址显示
     */
    private void setAddressTxt() {
        if (currAddress != null) {
            String name = currAddress.getName();
            if (currAddress.getParentAddress() != null && currAddress.getId() == currAddress.getParentAddress().getId()) {
                name = currAddress.getParentAddress().getName();
            }
            leftTxtView.setText(name);
        } else {
            leftTxtView.setText(null);
        }
    }

    /**
     * 初始化ListView
     */
    private void initListView() {
        listView.getRefreshableView().setMenuCreator(new SwipeMenuCreatorImpl());
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        listView.getRefreshableView().setCacheColorHint(Color.TRANSPARENT);
        listView.getRefreshableView().setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                if (index == 0) {
                    SocialInfo socialInfo = socialListAdapter.getItem(position);
                    if (socialInfo != null) {
                        requestQuit(socialInfo);
                    }
                    return true;
                }
                return false;
            }
        });
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<SwipeMenuListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<SwipeMenuListView> refreshView) {
                request();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<SwipeMenuListView> refreshView) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position = position - listView.getRefreshableView().getHeaderViewsCount();

                SocialInfo socialInfo = socialListAdapter.getItem(position);
                if (socialInfo != null) {
                    if (socialInfo.isCheck()) {
                        socialListAdapter.getItem(position).setCheck(false);
                    } else {
                        socialListAdapter.getItem(position).setCheck(true);
                    }
                }
                socialListAdapter.notifyDataSetChanged();
            }
        });

        socialListAdapter = new SocialListAdapterTwo(this);
        addHeaderView();
    }

    /**
     * 添加headerview
     */
    private void addHeaderView() {
        View headerView = View.inflate(getContext(), R.layout.adapter_social_list_header, null);
        headerView.setVisibility(View.GONE);
        TextView allSocialTxtView = (TextView) headerView.findViewById(R.id.allSocialTxtView);
        TextView mySocialTxtView = (TextView) headerView.findViewById(R.id.mySocialTxtView);
        gridView = (GridViewNoScroll) headerView.findViewById(R.id.gridView);
        gridView.setVisibility(View.GONE);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SocialInfo socialInfo = socialGridAdapter.getItem(position);
                if (socialInfo != null) {
                    if (socialInfo.getState() == 2) { //	0:未加入 1:申请中 2：已加入
                        SocialCircleContentV2Activity.startActivity(getActivity(), socialInfo);
                    } else {
                        SocialDetailActivity.startActivity(getActivity(), socialInfo);
                    }
                }
            }
        });

        socialGridAdapter = new SocialGridAdapter(this);
        socialGridAdapter.registerDataSetObserver(gridViewDataSetObserver);
        gridView.setAdapter(socialGridAdapter);

        //我的圈子
        mySocialTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //所有圈子
        allSocialTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data) {
                    SocialCircleSearchResultActivity.startAllSocialCircleActivity(getActivity());
                } else {
                    mdilaog.show();
                }
            }
        });
        listView.addHeaderView(headerView);
        listView.setAdapter(socialListAdapter);
    }

    /**
     * 初始化headerview数据
     */
    private void initHeaderView(List<SocialInfo> socialInfoList) {
        socialGridAdapter.getDataList().clear();
        socialGridAdapter.getDataList().addAll(socialInfoList);
        socialGridAdapter.notifyDataSetChanged();
    }

    /**
     *
     */
    private void request() {
        if (data) {
            requestCarousel();
        }
        requestMyCommunity();
    }

    /**
     * 请求推荐
     */
    private void requestCarousel() {
        socialRestUsage.getCarousel(REQUEST_CAROUSEL_TASK_ID);
    }

    /**
     * 请求我的社团
     */
    private void requestMyCommunity() {
        String timeStamp = (String) SPUtils.get(getContext(), getMyCommunityLastTimestampKey(), "");
        socialRestUsage.myCommunity(REQUEST_MY_COMMUNITY_TASK_ID, timeStamp);
    }

    /**
     * 请求退出社团
     */
    private void requestQuit(SocialInfo socialInfo) {
        showProgressDialog();
        socialRestUsage.quit(REQUEST_QUIT_TASK_ID, socialInfo.getCommunityId(), socialInfo);
    }

    /**
     * @return
     */
    private String getMyCommunityLastTimestampKey() {
        String userId = UserInfoUtil.getInstance().getUserInfo().getUserId();
        return String.format(KEY_MY_COMMUNITY_LAST_TIMESTAMP, userId == null ? "" : userId);
    }

    @OnClick({R.id.leftTxtView})
    public void toAddressSelect() {
        //TownCategoryActivity.startActivityForResult(this, currAddress,1, REUQEST_TOWN_ADDRESS_CODE);
        JSONObject obj = new JSONObject();
        OkhttpUtil.okHttpPostJson(BeyondApplication.BaseUrl + "/villages/queryOpenArea", obj.toString(), new CallBackUtil.CallBackString() {

            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject onj = new JSONObject(response);
                    JSONArray date = onj.getJSONArray("data");
                    String ss = date.toString();
                    String ssss = ss.replace("child", "counties");
                    String s = ssss.replace("id", "areaId");
                    String newjson = "[{\"areaId\": \"130000\",\"areaName\": \"河北省\",\"cities\":" + s + "}]";
                    final ArrayList<Province> datas = new ArrayList<>();
                    datas.addAll(JSON.parseArray(newjson, Province.class));
                    AddressPicker picker = new AddressPicker(getActivity(), datas);
                    picker.setShadowVisible(true);
                    picker.setHideProvince(true);
                    picker.setTextSize(16);
                    picker.setPadding(10);
                    picker.setSelectedItem("", "台州市", "仙居县");
                    picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
                        @Override
                        public void onAddressPicked(Province province, City city, County county) {
                            WelcomeActivity.seletedAdress = city.getAreaName() + "-" + county.getAreaName();
                            //重新的刷新  列表的數據
                            leftTxtView.setText(county.getAreaName());
                            data = true;
                            request();
                        }
                    });
                    picker.show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 搜索
     */
    @OnClick(R.id.rightImgView)
    public void searchClick(View view) {
        if (data) {
            GlobalSearchActivity.startActivitySocial(getContext());
        } else {
            mdilaog.show();
        }
    }

    public void getisopencity() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("address", WelcomeActivity.seletedAdress);
            OkhttpUtil.okHttpPostJson(BeyondApplication.BaseUrl + "/villages/isOpen", obj.toString(), new CallBackUtil.CallBackString() {
                @Override
                public void onFailure(Call call, Exception e) {
                    Log.e("s", "------------------------------------------------------------");
                    e.printStackTrace();
                }

                @Override
                public void onResponse(String response) {
                    //{"code":0,"message":"当前选择城市已经开通服务权限","data":true}
                    try {
                        JSONObject obj = new JSONObject(response);
                        data = obj.getBoolean("data");
                        if (data) {
                            request();
                        } else {
                            getopenlist();
                            requestMyCommunity();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getopenlist() {
        JSONObject obj = new JSONObject();
        OkhttpUtil.okHttpPostJson(BeyondApplication.BaseUrl + "/villages/queryOpenArea", obj.toString(), new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                mlisyt.clear();
                OpenCity opencity = gson.fromJson(response, OpenCity.class);
                if (opencity.getData().size() > 0) {
                    mlisyt.addAll(opencity.getData());
                    madapter.notifyDataSetChanged();
                    if (!mdilaog.isShowing()) {
                        mdilaog.show();
                        mdilaog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getActivity().getResources().getColor(R.color.blue));
                        mdilaog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getActivity().getResources().getColor(R.color.blue));
                    }
                }
            }
        });
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

    @OnClick(R.id.tv_back)
    public void onViewClicked() {
        getActivity().finish();
    }

    @OnClick(R.id.tv_submit)
    public void onViewClickedsss() {
        //遍历 那几个被选中了
        for (SocialInfo socialInfo : socialListAdapter.getDataList()) {
            if (socialInfo.isCheck()) {
                //发布说说
                socialRestUsage.publishArticleTwo(10086, socialInfo.getCommunityId(), titles, pics, newsId);
            }
        }
        ToastUtil.showCenter(getActivity(),"转发成功");
        getActivity().finish();
    }

    /**
     * 横向滑动菜单
     */
    class SwipeMenuCreatorImpl implements SwipeMenuCreator {

        @Override
        public void create(SwipeMenu menu) {
            SwipeMenuItem deleteItem = new SwipeMenuItem(getContext());
            deleteItem.setBackground(new ColorDrawable(Color.RED));
            deleteItem.setWidth(DensityUtil.dp2px(getContext(), 70));
            deleteItem.setTitle("退出社团");
            deleteItem.setTitleColor(Color.WHITE);
            deleteItem.setTitleSize(15);
            menu.addMenuItem(deleteItem);
        }

    }

}
