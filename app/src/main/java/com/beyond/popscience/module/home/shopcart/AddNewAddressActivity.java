package com.beyond.popscience.module.home.shopcart;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.api.AddressListApi;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.pojo.ContactBen;
import com.beyond.popscience.utils.xutils.CityPickerPopWindow;
import com.beyond.popscience.widget.SwitchView;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 新增地址界面(或者编辑地址界面)
 */
public class AddNewAddressActivity extends BaseActivity implements View.OnClickListener, CityPickerPopWindow.CityPickListener {

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
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.edit_consignee)
    EditText editConsignee;
    @BindView(R.id.edit_phone)
    EditText editPhone;
    @BindView(R.id.ll_lian)
    LinearLayout llLian;
    @BindView(R.id.text_address)
    TextView textAddress;
    @BindView(R.id.ll_address)
    LinearLayout llAddress;
    @BindView(R.id.edit_detail)
    EditText editDetail;
    @BindView(R.id.layout_Detailed)
    LinearLayout layoutDetailed;
    @BindView(R.id.btn_home)
    RadioButton btnHome;
    @BindView(R.id.ll_home)
    LinearLayout llHome;
    @BindView(R.id.btn_company)
    RadioButton btnCompany;
    @BindView(R.id.ll_company)
    LinearLayout llCompany;
    @BindView(R.id.switchButton)
    SwitchView switchButton;
    @BindView(R.id.ll_def_address)
    LinearLayout llDefAddress;
    @BindView(R.id.root_layout)
    LinearLayout rootLayout;
    @BindView(R.id.edit_street)
    EditText editStreet;
    @BindView(R.id.layout_street)
    LinearLayout layoutStreet;

    private String title = "新增收货地址";
    private Intent intent;
    private int addressType = 1;//1 家 2 公司
    private boolean isEdit = false;//默认不是编辑地址,是添加地址
    private String province, city, district;
    private int type;//1 新增  2 编辑
    @Request
    private AddressListApi addressListApi;//添加地址
    private int isDefault = 1;//1 默认地址 2 ,不是默认
    private static final int ADDRESS_TASKID = 1003;//添加地址请求id
    private static final int ADDRESS_UPDATE = 1004;//修改
    private String contactName;//联系人(编辑传过来的)
    private String contactPhone;//手机号
    private String address;//地址(省市区)
    private String street;//街道
    private String addressDetail;//详细地址
    private int status;//地址的状态 stasus 1 是默认地址 其他都是普通地址
    private int addressId;//地址id
//    private String province;//省
//    private String city;//市
//    private String area;//区


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_add_new_address;
    }

    @Override
    public void initUI() {
        super.initUI();
        intent = getIntent();

        if (!TextUtils.isEmpty(intent.getStringExtra("title"))) {
            title = intent.getStringExtra("title");
        } else {
            title = "新增收货地址";
        }

        if (null != getIntent()) {
            type = getIntent().getIntExtra("type", 0);
            tvRight.setVisibility(View.VISIBLE);
            if (1 == type) {
                tvRight.setText("保存");
            } else if (2 == type) {
                tvRight.setText("完成");
                contactName = getIntent().getStringExtra("contactName");
                contactPhone = getIntent().getStringExtra("contactPhone");
                address = getIntent().getStringExtra("address");
                street = getIntent().getStringExtra("street");
                addressDetail = getIntent().getStringExtra("addressDetail");
                status = getIntent().getIntExtra("status",0);
                addressId = getIntent().getIntExtra("addressId",0);
                province = getIntent().getStringExtra("province");
                city = getIntent().getStringExtra("city");
                district = getIntent().getStringExtra("area");
                editConsignee.setText(contactName);
                editPhone.setText(contactPhone);
                textAddress.setText(address);
                editStreet.setText(street);
                editDetail.setText(addressDetail);
                if (1 == status){
                    switchButton.setOpened(true);
                    isDefault = 1;
                }else {
                    switchButton.setOpened(false);
                    isDefault = 2;
                }
            }
        }
        tvTitle.setText(title);
        rightImgView.setVisibility(View.GONE);
        rightImgView.setImageResource(R.drawable.icon_dots);

        switchButton.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(View view) {
                switchButton.setOpened(true);
                isDefault = 1;
            }

            @Override
            public void toggleToOff(View view) {
                switchButton.setOpened(false);
                isDefault = 2;

            }
        });

        llHome.setOnClickListener(this);
        llCompany.setOnClickListener(this);
        llLian.setOnClickListener(this);
        llAddress.setOnClickListener(this);
        tvRight.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_home:
                btnHome.setChecked(true);
                btnCompany.setChecked(false);
                addressType = 1;//家
                break;
            case R.id.ll_company:
                btnHome.setChecked(false);
                btnCompany.setChecked(true);
                addressType = 2;//公司
                break;
            case R.id.ll_lian://添加联系人

                //判断有没有权限
                if (AndPermission.hasPermission(this, Manifest.permission.READ_CONTACTS) &&
                        AndPermission.hasPermission(this, Manifest.permission.WRITE_CONTACTS)) {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, 1);
                } else {
                    AndPermission.with(AddNewAddressActivity.this)
                            .requestCode(100)
                            .permission(Manifest.permission.READ_CONTACTS)
                            .permission(Manifest.permission.WRITE_CONTACTS)
                            .rationale(new RationaleListener() {
                                @Override
                                public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                                    AndPermission.rationaleDialog(AddNewAddressActivity.this, rationale).show();
                                }
                            })
                            .callback(listener)
                            .start();
                    if (Build.VERSION.SDK_INT >= 23) {
                        String[] mPermissionList = new String[]{android.Manifest.permission.READ_CONTACTS, android.Manifest.permission.WRITE_CONTACTS};
                        ActivityCompat.requestPermissions(this, mPermissionList, 1234);
                    }
                }
                break;
            case R.id.ll_address://选择地址
                //判断软键盘是否打开，为true则关闭，否则不处理
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                boolean isOpen = imm.isActive();
                if (isOpen) {
                    closrKeyboard();
                }

                //城市选择弹出框
                CityPickerPopWindow mPopWindow = new CityPickerPopWindow(this);
                mPopWindow.showPopupWindow(rootLayout);
                mPopWindow.setCityPickListener(this);
                break;
            case R.id.tv_right://保存按钮 即添加地址
                if (TextUtils.isEmpty(editConsignee.getText().toString().trim())) {
                    ToastUtil.show(AddNewAddressActivity.this, "收货人收货姓名不能为空!");
                    return;
                }
                if (TextUtils.isEmpty(editPhone.getText().toString().trim())) {
                    ToastUtil.show(AddNewAddressActivity.this, "手机号不能为空!");
                    return;
                }
                if (TextUtils.isEmpty(textAddress.getText().toString().trim())) {
                    ToastUtil.show(AddNewAddressActivity.this, "所在地区不能为空!");
                    return;
                }
                if (TextUtils.isEmpty(editDetail.getText().toString().trim())) {
                    ToastUtil.show(AddNewAddressActivity.this, "详细地址不能为空!");
                    return;
                }

                if (TextUtils.isEmpty(editStreet.getText().toString().trim())) {
                    ToastUtil.show(AddNewAddressActivity.this, "街道地址不能为空!");
                    return;
                }
                if (1 == type) {//新增
                    addressListApi.addAddress(ADDRESS_TASKID, editConsignee.getText().toString().trim(), editPhone.getText().toString().trim()
                            , province, city, district, editStreet.getText().toString().trim(), editDetail.getText().toString().trim(), isDefault + "");
                }else if (2 == type){//修改
                    addressListApi.updateAddress(ADDRESS_UPDATE,addressId + "",editConsignee.getText().toString().trim(),editPhone.getText().toString().trim(),
                            province,city,district,editStreet.getText().toString().trim(),editDetail.getText().toString().trim(),isDefault+"");

                }
                break;

        }
    }


    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case ADDRESS_TASKID://添加地址
                if (msg.getIsSuccess()) {
                    ToastUtil.show(AddNewAddressActivity.this, "" + msg.getMsg());
                    finish();
                }
                break;
            case ADDRESS_UPDATE:
                if (msg.getIsSuccess()) {
                    ToastUtil.show(AddNewAddressActivity.this, "" + msg.getMsg());
                    finish();
                }
                break;

        }
    }

    private PermissionListener listener = new PermissionListener() {

        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。

            // 这里的requestCode就是申请时设置的requestCode。
            // 和onActivityResult()的requestCode一样，用来区分多个不同的请求。
            if (requestCode == 100) {
                // TODO ...
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            switch (requestCode) {
                case 100:
//                    AndPermission.defaultSettingDialog(AddNewAddressActivity.this, 100).show();
                    break;
            }

        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (1): {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        Uri contactData = data.getData();
                        @SuppressWarnings("deprecation")
                        Cursor c = AddNewAddressActivity.this.managedQuery(contactData, null, null, null, null);
                        c.moveToFirst();
                        ContactBen contactPhone = getContactPhone(c, AddNewAddressActivity.this);
                        if (contactPhone == null) {
                            contactPhone = new ContactBen();
                        }
                        editConsignee.setText("" + contactPhone.name);

                        String phone = contactPhone.phone;
                        if (phone != null) {
                            if (phone.contains(" ")) {
                                phone = phone.replaceAll(" ", "");
                            }
                        }
                        editPhone.setText("" + phone);
                    }
                }
                break;
            }
        }
    }

    private ContactBen getContactPhone(Cursor cursor, Context context) {
        ContactBen vo = new ContactBen();
        int phoneColumn = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
        int phoneNum = 0;
        try {
            phoneNum = cursor.getInt(phoneColumn);
        } catch (Exception e) {
            return null;
        }

        // String phoneResult = "";
        if (phoneNum > 0) {
            // 获得联系人的ID号
            int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            String contactId = cursor.getString(idColumn);

            vo.name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            // 获得联系人的电话号码的cursor;
            Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);

            if (phones.moveToFirst()) {
                // 遍历所有的电话号码
                for (; !phones.isAfterLast(); phones.moveToNext()) {
                    int index = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int typeindex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
                    int phone_type = phones.getInt(typeindex);
                    String phoneNumber = phones.getString(index);
                    switch (phone_type) {
                        case 2:
                            vo.phone = phoneNumber;
                            break;
                    }
                }
                if (!phones.isClosed()) {
                    phones.close();
                }
            }
        }
        return vo;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void pickValue(String value) {
        textAddress.setText(value);
        province = value.split("-")[0];
        city = value.split("-")[1];
        district = value.split("-")[2];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
