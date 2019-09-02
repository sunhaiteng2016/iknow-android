package com.beyond.popscience.module.town;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.PopularMainActivity;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseFragment;
import com.beyond.popscience.frame.net.TownRestUsage;
import com.beyond.popscience.frame.pojo.AddressPicInfo;
import com.beyond.popscience.module.home.WelcomeActivity;
import com.beyond.popscience.module.home.adapter.OpenlistCityElv;
import com.beyond.popscience.module.home.entity.Address;
import com.beyond.popscience.module.home.entity.OpenCity;
import com.beyond.popscience.module.home.entity.secondPage;
import com.beyond.popscience.module.home.shopcart.NewFragmentActivity;
import com.beyond.popscience.module.news.GlobalSearchActivity;
import com.beyond.popscience.module.town.adapter.GvContentAdapter;
import com.beyond.popscience.module.town.adapter.TownCategoryAdapter;
import com.beyond.popscience.widget.AddressPopWindow;
import com.google.gson.Gson;
import com.okhttp.CallBackUtil;
import com.okhttp.OkhttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
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
 * 乡镇分类
 * Created by yao.cui on 2017/6/23.
 */

public class TownCategoryFragment extends BaseFragment {

    /**
     *
     */
    private final static String EXTRA_ADDRESS_KEY = "address";
    /**
     * 是否搜索列表
     */
    private final static String EXTRA_IS_SEARCH_KEY = "isSearch";
    /**
     *
     */
    private final static String EXTRA_AREA_TYPE_KEY = "areaType";
    /**
     * 请求地址图片
     */
    private final int REQUEST_PIC_TASK_ID = 101;
    @BindView(R.id.ic_back)
    ImageView icBack;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.searchEditTxt)
    EditText searchEditTxt;
    @BindView(R.id.searchBtn)
    Button searchBtn;
    @BindView(R.id.iv_mysj)
    ImageView ivMysj;
    Unbinder unbinder;
    private AddressPopWindow mAddressWindow;
    private List<Address> finalAddressList;

    /**
     * @param context
     */
    public static void startActivityForResult(Fragment context, Address address, int requestCode) {
        Intent intent = new Intent(context.getContext(), TownCategoryFragment.class);
        intent.putExtra(EXTRA_ADDRESS_KEY, address);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * @param context
     * @param address
     * @param areaType    选择的地址类型   0：默认，，，1：productArea
     * @param requestCode
     */
    public static void startActivityForResult(Fragment context, Address address, int areaType, int requestCode) {
        Intent intent = new Intent(context.getContext(), TownCategoryFragment.class);
        intent.putExtra(EXTRA_ADDRESS_KEY, address);
        intent.putExtra(EXTRA_AREA_TYPE_KEY, areaType);
        context.startActivityForResult(intent, requestCode);
    }

    public static void startActivityForResult(Activity activity, Address address, int requestCode) {
        Intent intent = new Intent(activity, TownCategoryFragment.class);
        intent.putExtra(EXTRA_ADDRESS_KEY, address);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * @param context
     * @param address
     * @param isSearch
     * @param requestCode
     */
    public static void startActivityForResult(Fragment context, Address address, boolean isSearch, int requestCode) {
        Intent intent = new Intent(context.getContext(), TownCategoryFragment.class);
        intent.putExtra(EXTRA_ADDRESS_KEY, address);
        intent.putExtra(EXTRA_IS_SEARCH_KEY, isSearch);
        context.startActivityForResult(intent, requestCode);
    }

    @BindView(R.id.addressNameTxtView)
    protected TextView addressNameTxtView;
    @BindView(R.id.lvCategory)
    protected ListView mLvCategory;
    @BindView(R.id.gvContent)
    protected GridView mGvContent;

    @BindView(R.id.shengshiqu)
    protected RelativeLayout shengshiqu;
    @BindView(R.id.xiangcun)
    protected TextView xiangcun;
    @BindView(R.id.ll_chengshi_list)
    protected LinearLayout ll_chengshi_list;
    @BindView(R.id.rl_mysj)
    protected RelativeLayout rl_mysj;
    private TownCategoryAdapter mCategoryAdapter;
    private GvContentAdapter mContentAdapter;
    @BindView(R.id.returren)
    protected ImageButton returren;
    @Request
    private TownRestUsage townRestUsage;
    private Address currAddress;
    /**
     * 是否搜索
     */
    private boolean isSearch = false;
    List<Address> addressList = new ArrayList<>();
    private int areaType = 0;  //0：默认   1：productArea
    public String City;


    @Override
    public void initUI() {
        super.initUI();
/*
        currAddress = (Address) getIntent().getSerializableExtra(EXTRA_ADDRESS_KEY);
        isSearch = getIntent().getBooleanExtra(EXTRA_IS_SEARCH_KEY, isSearch);
        areaType = getIntent().getIntExtra(EXTRA_AREA_TYPE_KEY, 0);*/
        addressNameTxtView.setText(WelcomeActivity.seletedAdress);
       returren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               ToastUtil.show(getActivity(),"ni");
            }
        });

        if (isSearch) {
            addressNameTxtView.setText("全部");
            addressNameTxtView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  /*  Intent data = new Intent();
                    setResult(Activity.RESULT_OK, data);
                    finish();*/
                }
            });
        } else {
            if (currAddress != null) {
                String name = currAddress.getName();
                if (currAddress.getParentAddress() != null && currAddress.getId() == currAddress.getParentAddress().getId()) {
                    name = currAddress.getParentAddress().getName();
                }
            }
        }


        mCategoryAdapter = new TownCategoryAdapter(this);
        mLvCategory.setAdapter(mCategoryAdapter);

        mContentAdapter = new GvContentAdapter(this);
        mGvContent.setAdapter(mContentAdapter);

        mLvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Address address = mCategoryAdapter.getItem(position);

                if (finalAddressList == null || finalAddressList.size() < 0) {
                    return;
                }
                Address address = finalAddressList.get(position);
                if (address != null) {
                    mCategoryAdapter.setCurrentChecked(position);
                    mCategoryAdapter.notifyDataSetChanged();

                    mContentAdapter.getDataList().clear();
                    if (address.getChild() != null && address.getChild().size() != 0) {
                        mContentAdapter.getDataList().addAll(address.getChild());
                    }

                    //第一个 直接添加 ParentAddress
                    Address parentAddress = new Address();
                    parentAddress.setId(address.getId());
                    parentAddress.setName(TextUtils.isEmpty(address.getName()) ? "" : address.getName());

                    mContentAdapter.getDataList().add(0, parentAddress);

                    mContentAdapter.notifyDataSetChanged();
                }

            }
        });

        mGvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Address parentAddress = mCategoryAdapter.getSelectedAddress();
                Address childAddress = mContentAdapter.getItem(position);
                Address address = Address.assembledAddress(parentAddress, childAddress);
                //传递到Townfragment的
                NewFragmentActivity mactivity = (NewFragmentActivity) getActivity();
                mactivity.switchFragmentmy(address);

            }
        });
        showOpenCityDialog();


//=================================================================================================================
        //请求是否开通城市服务
        JSONObject objs = new JSONObject();
        try {
            objs.put("address", WelcomeActivity.seletedAdress);
            OkhttpUtil.okHttpPostJson(BeyondApplication.BaseUrl + "/villages/isOpen", objs.toString(), new CallBackUtil.CallBackString() {
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
                        boolean data = obj.getBoolean("data");
                        if (data == true) {
                        } else {
                            getopenlist();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (areaType == 1) {
            addressList = BeyondApplication.getInstance().getAreaAddressList();
        } else {
            //请求具体的 镇村
            //addressList = BeyondApplication.getInstance().getCacheAddressList();
            final JSONObject obj = new JSONObject();
            try {
                obj.put("address", WelcomeActivity.seletedAdress);
                OkhttpUtil.okHttpPostJson(BeyondApplication.BaseUrl + "/address/getCountryList", obj.toString(), new CallBackUtil.CallBackString() {

                    @Override
                    public void onFailure(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("", "");
                        Gson gson = new Gson();
                        CunBean cunbean = gson.fromJson(response, CunBean.class);
                        if (cunbean.getData() == null || cunbean.getData().size() <= 0) {
                            rl_mysj.setVisibility(View.VISIBLE);
                            return;
                        }

                        List<CunBean.DataBean> mlist = cunbean.getData();
                        rl_mysj.setVisibility(View.GONE);
                        for (int i = 0; i < mlist.size(); i++) {
                            //去给 addresslist 赋值  这个是当前页面的数据源
                            Address a = new Address();
                            a.setId(mlist.get(i).getId());
                            a.setName(mlist.get(i).getName());
                            List<Address> childlist = new ArrayList<Address>();
                            List<CunBean.DataBean.ChildBean> jsonchildlist = mlist.get(i).getChild();
                            for (int j = 0; j < jsonchildlist.size(); j++) {

                                Address a1 = new Address();
                                a1.setId(jsonchildlist.get(j).getId());
                                a1.setName(jsonchildlist.get(j).getName());
                                a1.setPic(jsonchildlist.get(j).getPic());
                                childlist.add(a1);
                            }
                            a.setChild(childlist);
                            addressList.add(a);

                        }
                        if (mCategoryAdapter.getDataList().size() > 0) {
                            mCategoryAdapter.getDataList().clear();
                        }
                        mCategoryAdapter.getDataList().addAll(addressList);

                        //刷新 默认村 数据
                        Address address = addressList.get(0);
                        if (address != null) {
                            mCategoryAdapter.setCurrentChecked(0);
                            mCategoryAdapter.notifyDataSetChanged();

                            mContentAdapter.getDataList().clear();
                            if (address.getChild() != null && address.getChild().size() != 0) {
                                mContentAdapter.getDataList().addAll(address.getChild());
                            }

                            //第一个 直接添加 ParentAddress
                            Address parentAddress = new Address();
                            parentAddress.setId(address.getId());
                            parentAddress.setName(TextUtils.isEmpty(address.getName()) ? "" : address.getName());

                            mContentAdapter.getDataList().add(0, parentAddress);

                            mContentAdapter.notifyDataSetChanged();
                        }

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
            requestPic();
        }

        mCategoryAdapter.getDataList().addAll(addressList);
        mCategoryAdapter.notifyDataSetChanged();

        //
        int position = 0;
        if (currAddress != null) {
            if (currAddress.getParentAddress() != null) {
                int count = mCategoryAdapter.getCount();
                for (int i = 0; i < count; i++) {
                    Address address = mCategoryAdapter.getItem(i);
                    if (address != null) {
                        if (address.getId() == currAddress.getParentAddress().getId()) {
                            position = i;
                            break;
                        }
                    }
                }
            }
        }
        mLvCategory.getOnItemClickListener().onItemClick(null, null, position, position);


        //选择 城市乡村的功能
        finalAddressList = addressList;
        shengshiqu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //功能开放的城市
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
                            ArrayList<Province> data = new ArrayList<>();
                            data.addAll(JSON.parseArray(newjson, Province.class));
                            AddressPicker picker = new AddressPicker(getActivity(), data);
                            picker.setShadowVisible(true);
                            picker.setHideProvince(true);
                            picker.setTextSize(16);
                            picker.setPadding(10);
                            picker.setSelectedItem("", "台州市", "仙居县");
                            picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
                                @Override
                                public void onAddressPicked(Province province, City city, County county) {
                                    //  showToast("province : " + province + ", city: " + city + ", county: " + county);
                                    addressNameTxtView.setText(city.getAreaName() + "-" + county.getAreaName());
                                    WelcomeActivity.seletedAdress = city.getAreaName() + "-" + county.getAreaName();
                                    City = city.getAreaName() + "-" + county.getAreaName();

                                    //请求具体的 乡镇村
                                    JSONObject obj = new JSONObject();
                                    try {
                                        obj.put("address", City);
                                        OkhttpUtil.okHttpPostJson(BeyondApplication.BaseUrl + "/address/getCountryList", obj.toString(), new CallBackString() {
                                            @Override
                                            public void onFailure(Call call, Exception e) {

                                            }

                                            @Override
                                            public void onResponse(String response) {
                                                Log.e("", "");
                                                Gson gson = new Gson();
                                                secondPage page = gson.fromJson(response, secondPage.class);
                                                if (mCategoryAdapter.getDataList().size() > 0) {
                                                    mCategoryAdapter.getDataList().clear();
                                                }
                                                if (finalAddressList.size() > 0) {
                                                    finalAddressList.clear();
                                                }
                                                if (page.getData() == null) {
                                                    rl_mysj.setVisibility(View.VISIBLE);
                                                    return;
                                                }
                                                rl_mysj.setVisibility(View.GONE);
                                                for (int i = 0; i < page.getData().size(); i++) {
                                                    Address a = new Address();
                                                    a.setId(page.getData().get(i).getId());
                                                    a.setName(page.getData().get(i).getName());
                                           /* finalAddressList.get(i).setId(page.getData().get(i).getId());
                                            finalAddressList.get(i).setName(page.getData().get(i).getName());
                                            List<Address> childs = finalAddressList.get(i).getChild();*/
                                                    ArrayList<Address> mlist = new ArrayList<Address>();
                                                    a.setChild(mlist);
                                                    List<secondPage.DataBean.ChildBean> mchids = page.getData().get(i).getChild();
                                                    for (int j = 0; j < mchids.size(); j++) {
                                              /*  childs.get(j).setName(mchids.get(j).getName());
                                                childs.get(j).setId(mchids.get(j).getId());
                                                childs.get(j).setPic(mchids.get(j).getPic());*/
                                                        Address b = new Address();
                                                        b.setId(mchids.get(j).getId());
                                                        b.setName(mchids.get(j).getName());
                                                        b.setPic(mchids.get(j).getPic());
                                                        mlist.add(b);
                                                    }
                                                    finalAddressList.add(a);
                                                }
                                       /* mCategoryAdapter = new TownCategoryAdapter(TownCategoryActivity.this);
                                        mLvCategory.setAdapter(mCategoryAdapter);

                                        mContentAdapter = new GvContentAdapter(TownCategoryActivity.this);
                                        mGvContent.setAdapter(mContentAdapter);*/

                                                List<Address> ssss = mCategoryAdapter.getDataList();
                                                mCategoryAdapter.getDataList().addAll(finalAddressList);
                                                mCategoryAdapter.notifyDataSetChanged();


                                                //刷新 默认村 数据
                                                Address address = finalAddressList.get(0);
                                                if (address != null) {
                                                    mCategoryAdapter.setCurrentChecked(0);
                                                    mCategoryAdapter.notifyDataSetChanged();

                                                    mContentAdapter.getDataList().clear();
                                                    if (address.getChild() != null && address.getChild().size() != 0) {
                                                        mContentAdapter.getDataList().addAll(address.getChild());
                                                    }

                                                    //第一个 直接添加 ParentAddress
                                                    Address parentAddress = new Address();
                                                    parentAddress.setId(address.getId());
                                                    parentAddress.setName(TextUtils.isEmpty(address.getName()) ? "" : address.getName());

                                                    mContentAdapter.getDataList().add(0, parentAddress);

                                                    mContentAdapter.notifyDataSetChanged();
                                                }
                                            }
                                        });

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                            picker.show();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


            }
        });


    }

    private ExpandableListView elv_opencity;
    public AlertDialog mdilaog;
    private List<OpenCity.DataBean> mlisyt = new ArrayList<>();
    private OpenlistCityElv madapter;

    private void showOpenCityDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View cityview = LayoutInflater.from(getActivity()).inflate(R.layout.item_is_open_service, null);
        elv_opencity = (ExpandableListView) cityview.findViewById(R.id.elv_opencity);
        madapter = new OpenlistCityElv(getActivity(), mlisyt);
        elv_opencity.setAdapter(madapter);
        builder.setView(cityview);
        builder.setTitle("该城市没有开通服务");
        builder.setCancelable(false);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        if (mdilaog == null) {
            mdilaog = builder.create();
        }
        elv_opencity.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                WelcomeActivity.seletedAdress = mlisyt.get(i).getAreaName() + "-" + mlisyt.get(i).getChild().get(i1).getAreaName();
                PopularMainActivity actituty = (PopularMainActivity) getActivity();
                actituty.switchFragment(1);
                mdilaog.dismiss();
                return false;
            }
        });
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
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case REQUEST_PIC_TASK_ID:   //请求地址图片
                if (msg.getIsSuccess()) {
                    HashMap<String, List<AddressPicInfo>> allAddressPicInfoMap = (HashMap<String, List<AddressPicInfo>>) msg.getObj();
                    if (allAddressPicInfoMap != null && allAddressPicInfoMap.size() != 0) {
                        List<Address> addressList = BeyondApplication.getInstance().getCacheAddressList();
                        if (addressList != null && addressList.size() != 0) {
                            for (Address parentAddress : addressList) {
                                if (parentAddress.getChild() != null && parentAddress.getChild().size() != 0) {
                                    List<AddressPicInfo> addressPicInfoList = allAddressPicInfoMap.get(String.valueOf(parentAddress.getId()));
                                    if (addressPicInfoList != null && addressPicInfoList.size() != 0) {
                                        for (Address childAddress : parentAddress.getChild()) {
                                            for (AddressPicInfo addressPicInfo : addressPicInfoList) {
                                                if (childAddress.getId() == addressPicInfo.getVillageId()) {
                                                    childAddress.setPic(addressPicInfo.getPic());
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            mContentAdapter.notifyDataSetChanged();
                        }
                    }
                }
                dismissProgressDialog();
                break;
        }
    }

    /**
     * 请求地址图片
     */
    private void requestPic() {
        townRestUsage.getPicture(REQUEST_PIC_TASK_ID);
    }

    /**
     * 地址搜索
     */
    @OnClick(R.id.searchEditTxt)
    public void addressSearchClick() {
        GlobalSearchActivity.startActivityTownAddress(getActivity(), areaType);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_town_category;
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

}
