package com.beyond.popscience.locationgoods;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.locationgoods.bean.MAddressListBean;
import com.beyond.popscience.locationgoods.http.AddressRestUsage;
import com.beyond.popscience.module.home.AddressPickTask;
import com.beyond.popscience.module.home.WelcomeActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;

public class AddAddressActivity extends BaseActivity {

    public int EDIT_ADDRESS_CODE = 1;

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.name_tv)
    EditText nameTv;
    @BindView(R.id.phone_ed)
    EditText phoneEd;
    @BindView(R.id.address_tv_sel)
    TextView addressTvSel;
    @BindView(R.id.detail_address_ed)
    EditText detailAddressEd;
    @BindView(R.id.check_tv)
    TextView checkTv;
    @BindView(R.id.submit)
    TextView submit;
    @BindView(R.id.del_tv)
    TextView delTv;
    private String name, phone, address, detailAddress;
    public boolean isSelCity = false;
    private String mProvince, mCity, mCounty;
    private String postCode;


    @Request
    AddressRestUsage addressRestUsage;
    private String flag;
    private MAddressListBean mAddressListBean;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_add_address;
    }

    @Override
    public void initUI() {
        super.initUI();
        title.setText("新增收货地址");
        flag = getIntent().getStringExtra("flag");
        if (flag.equals("edit")) {
            mAddressListBean = (MAddressListBean) getIntent().getSerializableExtra("data");
            setData(mAddressListBean);
        }
    }

    //设置数据
    private void setData(MAddressListBean data) {
        nameTv.setText(data.getName() == null ? "" : data.getName());
        phoneEd.setText(data.getPhoneNumber() == null ? "" : data.getPhoneNumber());
        addressTvSel.setText(data.getProvince() == null ? "" : data.getProvince() + data.getCity() == null ? "" : data.getCity() + data.getRegion() == null ? "" : data.getRegion());
        detailAddressEd.setText(data.getDetailAddress() == null ? "" : data.getDetailAddress());
        int status = data.getDefaultStatus();
        if (status == 1) {
            checkTv.setBackgroundResource(R.drawable.chenck);
        } else {
            checkTv.setBackgroundResource(R.drawable.uncheck);
        }
        delTv.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.go_back, R.id.submit, R.id.address_tv_sel, R.id.check_tv, R.id.del_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.go_back:
                finish();
                break;
            case R.id.address_tv_sel:
                //城市选择
                selCity();
                break;
            case R.id.del_tv:
                //删除地址
                addressRestUsage.delAddress(10003, mAddressListBean.getId() + "");
                break;
            case R.id.submit:
                getInput();
                if (checkInput()) {
                    upDateAddress();
                }
                break;
            case R.id.check_tv:
                if (isSelCity) {
                    isSelCity = false;
                    checkTv.setBackgroundResource(R.drawable.uncheck);
                } else {
                    isSelCity = true;
                    checkTv.setBackgroundResource(R.drawable.chenck);
                }
                break;
        }
    }

    //上传地址
    private void upDateAddress() {

        HashMap<String, String> map = new HashMap<>();
        map.put("city", mCity);
        map.put("province", mProvince);
        map.put("region", mCounty);
        if (isSelCity) {
            map.put("defaultStatus", "1");//defaultStatus (integer, optional): 是否为默认 0非默认 1默认 ,
        } else {
            map.put("defaultStatus", "0");//defaultStatus (integer, optional): 是否为默认 0非默认 1默认 ,
        }

        map.put("detailAddress", detailAddress);
        map.put("name", name);
        map.put("phoneNumber", phone);
        map.put("postCode", postCode);
        map.put("userId", UserInfoUtil.getInstance().getUserInfo().getUserId());
        if (flag.equals("edit")) {
            map.put("id", mAddressListBean.getId() + "");
            addressRestUsage.updataAddress(10002, map);
        } else {
            addressRestUsage.addAddress(10001, map);
        }
    }

    private boolean checkInput() {
        if (TextUtils.isEmpty(name)) {
            ToastUtil.showCenter(this, "请输入收货人");
            return false;
        }
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showCenter(this, "请输入手机号码");
            return false;
        }
        if (TextUtils.isEmpty(address)) {
            ToastUtil.showCenter(this, "请选择收货地址");
            return false;
        }
        if (TextUtils.isEmpty(detailAddress)) {
            ToastUtil.showCenter(this, "请输入详细地址");
            return false;
        }
        return true;
    }

    private void selCity() {
        AddressPickTask task = new AddressPickTask(this);
        task.setHideProvince(false);
        task.setHideCounty(false);
        task.setCallback(new AddressPickTask.Callback() {
            @Override
            public void onAddressInitFailed() {

            }

            @Override
            public void onAddressPicked(Province province, City city, County county) {
                if (county == null) {
                } else {
                    mProvince = province.getAreaName();
                    mCity = city.getAreaName();
                    mCounty = county.getAreaName();
                    postCode = county.getAreaId();
                    addressTvSel.setText(province.getAreaName() + city.getAreaName() + county.getAreaName());
                }
            }
        });
        task.execute(WelcomeActivity.seletedAdress_two.split("-")[0], WelcomeActivity.seletedAdress_two.split("-")[1], WelcomeActivity.seletedAdress_two.split("-")[2]);
    }

    private void getInput() {
        name = nameTv.getText().toString().trim();
        phone = phoneEd.getText().toString().trim();
        address = addressTvSel.getText().toString().trim();
        detailAddress = detailAddressEd.getText().toString().trim();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case 10001:
                ToastUtil.showCenter(this, msg.getMsg());
                if (msg.getIsSuccess()) {
                    finish();
                }
                break;
            case 10002:
                ToastUtil.showCenter(this, msg.getMsg());
                if (msg.getIsSuccess()) {
                    finish();
                }
                break;
            case 10003:
                if (msg.getIsSuccess()) {
                    ToastUtil.showCenter(this, "删除成功！");
                    finish();
                } else {
                    ToastUtil.showCenter(this, "删除失败！");
                }
                break;
        }
    }
}
