package com.beyond.popscience.module.building;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.DateUtil;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.BuildingRestUsage;
import com.beyond.popscience.frame.pojo.BuildingDetail;
import com.beyond.popscience.frame.util.SPUtils;
import com.beyond.popscience.module.home.AddressPickTask;
import com.beyond.popscience.module.square.ClassifyActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;

/**
 * Created by danxiang.feng on 2017/10/14.
 */

public class PublishRentActivity extends BaseActivity {

    private final int REQ_PUBLISH_ID = 1301;

    private final int EXTRA_CLASSIFY = 101;

    private static final String BUILDING_DETAIL = "buildingDetail";

    private final String RMB_SYMBOL = "¥";

    private final String tradeItem[] = ClassifyBuildingActivity.TRADE_RENT_ITEM;

    @BindView(R.id.tv_title)
    protected TextView tv_title;
    @BindView(R.id.tradeLeftView)
    protected TextView tradeLeftView;
    @BindView(R.id.tradeRightView)
    protected TextView tradeRightView;
    @BindView(R.id.houseHolderView)
    protected TextView houseHolderView;
    @BindView(R.id.intermediaryView)
    protected TextView intermediaryView;
    @BindView(R.id.titleEditTxt)
    protected EditText titleEditTxt;
    @BindView(R.id.descriptEditTxt)
    protected EditText descriptEditTxt;
    @BindView(R.id.userNameEditTxt)
    protected EditText userNameEditTxt;
    @BindView(R.id.phoneEditTxt)
    protected EditText phoneEditTxt;
    @BindView(R.id.priceMinEditTxt)
    protected EditText priceMinEditTxt;
    @BindView(R.id.priceMaxEditTxt)
    protected EditText priceMaxEditTxt;
    @BindView(R.id.sizeMinEditView)
    protected EditText sizeMinEditView;
    @BindView(R.id.sizeMaxEditView)
    protected EditText sizeMaxEditView;
    @BindView(R.id.classifyTxtView)
    protected TextView classifyTxtView;
    @BindView(R.id.locationRequireEditView)
    protected EditText locationRequireEditView;
    @BindView(R.id.hourseTypeRequireEditView)
    protected EditText hourseTypeRequireEditView;
    @BindView(R.id.mianyi_iv)
    ImageView mianyiIv;
    @BindView(R.id.rl_ss)
    LinearLayout rlSs;
    @BindView(R.id.xiangxidizhiaddress)
    TextView xiangxidizhiaddress;
    @BindView(R.id.addressEditTxt)
    EditText addressEditTxt;

    @Request
    private BuildingRestUsage mRestUsage;

    private BuildingDetail request = new BuildingDetail();
    private BuildingDetail buildingDetail;

    private long lastClickTime = 0;   //上次点击的时间戳
    private String provincesss = "", citysss = "";

    public static void startActivity(Context context, BuildingDetail buildingDetail) {
        Intent intent = new Intent(context, PublishRentActivity.class);
        intent.putExtra(BUILDING_DETAIL, buildingDetail);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_publish_rent_layout;
    }

    @Override
    public void initUI() {
        super.initUI();
        provincesss = BeyondApplication.getInstance().getLocation().city;
        citysss = BeyondApplication.getInstance().getLocation().district;
        buildingDetail = (BuildingDetail) getIntent().getSerializableExtra(BUILDING_DETAIL);
        tv_title.setText("求租求购");

        initEditTxt();
        iniEditStatusView();
        xiangxidizhiaddress.setText(BeyondApplication.getInstance().getLocation().province + provincesss + citysss);
    }

    private void iniEditStatusView() {
        if (buildingDetail != null) {
            switchAuthType(buildingDetail.getSellTypeInt());
            switchAuthType(buildingDetail.getTradeInt());
            titleEditTxt.setText(buildingDetail.title);
            descriptEditTxt.setText(buildingDetail.introduce);
            userNameEditTxt.setText(buildingDetail.realName);
            phoneEditTxt.setText(buildingDetail.mobile);
            priceMinEditTxt.setText(buildingDetail.startPrice);
            priceMaxEditTxt.setText(buildingDetail.endPrice);
            sizeMinEditView.setText(buildingDetail.startArea);
            sizeMaxEditView.setText(buildingDetail.endArea);
            classifyTxtView.setText(buildingDetail.classifyName);
            locationRequireEditView.setText(buildingDetail.locationRequire);
            hourseTypeRequireEditView.setText(buildingDetail.hourseTypeRequire);
        } else {
            switchTradeType(1);
            switchAuthType(2);
        }
    }

    /**
     * 初始化相关 EditText
     */
    private void initEditTxt() {
        priceMinEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               /* if (!priceMinEditTxt.getText().toString().startsWith(RMB_SYMBOL)) {
                    if (priceMinEditTxt.getText().toString().contains(RMB_SYMBOL)) {
                        int selectionIndex = priceMinEditTxt.getSelectionStart();
                        priceMinEditTxt.setText(RMB_SYMBOL + priceMinEditTxt.getText().toString().replaceAll(RMB_SYMBOL, ""));
                        if (selectionIndex == 0) {
                            priceMinEditTxt.setSelection(priceMinEditTxt.getText().length());
                        }
                    } else {
                        priceMinEditTxt.setText(RMB_SYMBOL + priceMinEditTxt.getText().toString());
                        priceMinEditTxt.setSelection(priceMinEditTxt.getText().length());
                    }
                }*/
            }
        });
        priceMaxEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               /* if (!priceMaxEditTxt.getText().toString().startsWith(RMB_SYMBOL)) {
                    if (priceMaxEditTxt.getText().toString().contains(RMB_SYMBOL)) {
                        int selectionIndex = priceMaxEditTxt.getSelectionStart();
                        priceMaxEditTxt.setText(RMB_SYMBOL + priceMaxEditTxt.getText().toString().replaceAll(RMB_SYMBOL, ""));
                        if (selectionIndex == 0) {
                            priceMaxEditTxt.setSelection(priceMaxEditTxt.getText().length());
                        }
                    } else {
                        priceMaxEditTxt.setText(RMB_SYMBOL + priceMaxEditTxt.getText().toString());
                        priceMaxEditTxt.setSelection(priceMaxEditTxt.getText().length());
                    }
                }*/
            }
        });
    }

    private void switchAuthType(int sellType) {
        if (sellType == 1) {   //1：中介 2：房东
            intermediaryView.setTextColor(getResources().getColor(R.color.white));
            intermediaryView.setBackground(getResources().getDrawable(R.drawable.bg_blue_round1));
            houseHolderView.setTextColor(getResources().getColor(R.color.blue2));
            houseHolderView.setBackground(getResources().getDrawable(R.drawable.bg_blue_round3));
            request.sellType = "1";
        } else if (sellType == 2) {  //
            houseHolderView.setTextColor(getResources().getColor(R.color.white));
            houseHolderView.setBackground(getResources().getDrawable(R.drawable.bg_blue_round1));
            intermediaryView.setTextColor(getResources().getColor(R.color.blue2));
            intermediaryView.setBackground(getResources().getDrawable(R.drawable.bg_blue_round3));
            request.sellType = "2";
        }
    }

    private void switchTradeType(int tradeType) {
        if (tradeType == 1) {  //交易方式	1:求租 2:求售
            tradeLeftView.setTextColor(getResources().getColor(R.color.white));
            tradeLeftView.setBackground(getResources().getDrawable(R.drawable.bg_blue_round1));
            tradeRightView.setTextColor(getResources().getColor(R.color.blue2));
            tradeRightView.setBackground(getResources().getDrawable(R.drawable.bg_blue_round3));
            request.trade = "1";
        } else if (tradeType == 2) {
            tradeRightView.setTextColor(getResources().getColor(R.color.white));
            tradeRightView.setBackground(getResources().getDrawable(R.drawable.bg_blue_round1));
            tradeLeftView.setTextColor(getResources().getColor(R.color.blue2));
            tradeLeftView.setBackground(getResources().getDrawable(R.drawable.bg_blue_round3));
            request.trade = "2";
        }
    }


    @OnClick(R.id.intermediaryView)
    void intermediaryView() {  //中介
        switchAuthType(1);
    }

    @OnClick(R.id.houseHolderView)
        //房东
    void houseHolderView() {   //房东
        switchAuthType(2);
    }

    @OnClick(R.id.classifyLayout)
    void classifyLayout() {  //分类
        ClassifyActivity.startActivityForResult(this, ClassifyActivity.TYPE_SELECT_BUILDING, classifyTxtView.getText().toString(), EXTRA_CLASSIFY);
    }

    @OnClick(R.id.tradeLeftView)
    void tradeLeftView() {  //中介
        switchTradeType(1);
    }

    @OnClick(R.id.tradeRightView)
    void tradeRightView() {   //房东
        switchTradeType(2);
    }


    @OnClick(R.id.publishView)
    void publishView() {  //发布
        long currClickTime = DateUtil.getCurrentTime();
        if (currClickTime - lastClickTime < 1000) {
            return;
        }
        lastClickTime = currClickTime;

        if (TextUtils.isEmpty(request.trade)) {
            ToastUtil.showCenter(this, "请选择交易方式");
            return;
        }
        if (TextUtils.isEmpty(request.sellType)) {
            ToastUtil.showCenter(this, "请选择房源类型");
            return;
        }
        if (TextUtils.isEmpty(titleEditTxt.getText().toString())) {
            ToastUtil.showCenter(this, "请输入标题");
            return;
        }
        if (TextUtils.isEmpty(descriptEditTxt.getText().toString())) {
            try {
                ToastUtil.showCenter(this, "请详细描述您的需求");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        if (TextUtils.isEmpty(userNameEditTxt.getText().toString())) {
            ToastUtil.showCenter(this, "请输入姓名");
            return;
        }
        if (TextUtils.isEmpty(phoneEditTxt.getText().toString())) {
            ToastUtil.showCenter(this, "请输入联系方式");
            return;
        }
        if (isOnOff) {
            if (TextUtils.isEmpty(getPriceTxt(priceMinEditTxt.getText().toString()))) {
                ToastUtil.showCenter(this, "请输入最低价格");
                return;
            }
            if (TextUtils.isEmpty(getPriceTxt(priceMaxEditTxt.getText().toString()))) {
                ToastUtil.showCenter(this, "请输入最高价格");
                return;
            }
        }
        if (TextUtils.isEmpty(classifyTxtView.getText().toString())) {
            ToastUtil.showCenter(this, "请选择分类");
            return;
        }
        if (TextUtils.isEmpty(sizeMinEditView.getText().toString())) {
            ToastUtil.showCenter(this, "请输入最小面积");
            return;
        }
        if (TextUtils.isEmpty(sizeMaxEditView.getText().toString())) {
            ToastUtil.showCenter(this, "请输入最大面积");
            return;
        }
        if (TextUtils.isEmpty(xiangxidizhiaddress.getText().toString())) {
            ToastUtil.showCenter(this, "请输入位置要求");
            return;
        }
        if (TextUtils.isEmpty(hourseTypeRequireEditView.getText().toString())) {
            ToastUtil.showCenter(this, "请输入户型要求");
            return;
        }


        request.title = titleEditTxt.getText().toString();
        request.introduce = descriptEditTxt.getText().toString();
        request.realName = userNameEditTxt.getText().toString();
        request.mobile = phoneEditTxt.getText().toString();
        request.startPrice = getPriceTxt(priceMinEditTxt.getText().toString());
        request.endPrice = getPriceTxt(priceMaxEditTxt.getText().toString());
        request.startArea = sizeMinEditView.getText().toString();
        request.endArea = sizeMaxEditView.getText().toString();
        request.classifyName = classifyTxtView.getText().toString();
        // request.locationRequire = locationRequireEditView.getText().toString();
        request.locationRequire = addressEditTxt.getText().toString();
        request.hourseTypeRequire = hourseTypeRequireEditView.getText().toString();
        request.areaName = provincesss + "-" + citysss;
        showProgressDialog();
        String rentId = buildingDetail != null ? buildingDetail.rentId : null;
        mRestUsage.publishRent(REQ_PUBLISH_ID, rentId, request);
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        dismissProgressDialog();
        switch (taskId) {
            case REQ_PUBLISH_ID:
                if (msg.getIsSuccess()) {
                    if (buildingDetail != null) {
                        ToastUtil.showCenter(this, "修改成功");
                    } else {
                        ToastUtil.showCenter(this, "发布成功");
                        BuildingDetail buildingDetail = (BuildingDetail) msg.getObj();
                        if (buildingDetail != null && !TextUtils.isEmpty(buildingDetail.rentId)) {
                            //RentDetailActivity.startActivity(this,buildingDetail.rentId);
                            finish();
                        }
                    }
                    finish();
                }else{
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case EXTRA_CLASSIFY:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    String classify = data.getStringExtra(ClassifyActivity.EXTRA_CLASSIFY_KEY);
                    classifyTxtView.setText(classify);
                }
                break;
        }
    }

    /**
     * 获取商品定价
     *
     * @return
     */
    private String getPriceTxt(String price) {
        if (!TextUtils.isEmpty(price)) {
            if (price.equals("面议")) {
                return "-1";
            }
            return price.replaceAll(RMB_SYMBOL, "");
        }else{
            return "-1";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    //控制面议的开关
    public boolean isOnOff = true;

    @OnClick(R.id.rl_ss)
    public void onViewClicked() {
        if (isOnOff) {
            priceMinEditTxt.setText("");
            priceMaxEditTxt.setText("");
            priceMinEditTxt.setEnabled(false);
            priceMaxEditTxt.setEnabled(false);
            isOnOff = false;
            mianyiIv.setBackgroundResource(R.drawable.ok_check);
        } else {
            priceMinEditTxt.setText("");
            priceMaxEditTxt.setText("");
            priceMinEditTxt.setEnabled(true);
            priceMaxEditTxt.setEnabled(true);
            isOnOff = true;
            mianyiIv.setBackgroundResource(R.drawable.check_nor);
        }
    }


    @OnClick(R.id.xzxxdz)
    public void onViewClickedssss() {
        AddressPickTask task = new AddressPickTask(PublishRentActivity.this);
        task.setHideProvince(false);
        task.setHideCounty(false);
        task.setCallback(new AddressPickTask.Callback() {
            @Override
            public void onAddressInitFailed() {
                //showToast("数据初始化失败");
            }

            @Override
            public void onAddressPicked(Province province, City city, County county) {
                if (county == null) {
                } else {
                    //重新的刷新  列表的數據\
                    provincesss = city.getAreaName();
                    citysss = county.getAreaName();
                    xiangxidizhiaddress.setText(province.getAreaName() + city.getAreaName() + county.getAreaName());
                }
            }
        });

        String detailedArea = SPUtils.get(PublishRentActivity.this, "detailedArea", "") + "";
        String[] detailedAreas = detailedArea.split("-");
        if (detailedAreas.length > 1) {
            task.execute(detailedAreas[0], detailedAreas[1], detailedAreas[2]);
        } else {
            task.execute("浙江省", "杭州市", "仙居县");
        }
    }
}
