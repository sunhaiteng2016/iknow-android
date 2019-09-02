package com.beyond.popscience.module.home;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.beyond.library.network.net.bean.BaseResponse;
import com.beyond.library.network.net.httpclient.NewCustomResponseHandler;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.base.AppBaseRestUsageV1;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.module.home.entity.ShuaXin;
import com.beyond.popscience.module.home.entity.UserDate;
import com.beyond.popscience.module.home.fragment.Constant;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserDetailsActivity extends BaseActivity {

    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.hy_name)
    TextView hyName;
    @BindView(R.id.hy_content)
    TextView hyContent;
    @BindView(R.id.del_friend)
    TextView delFriend;
    @BindView(R.id.add_friends)
    TextView addFriends;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.zl_sex)
    TextView zlSex;
    @BindView(R.id.zl_address)
    TextView zlAddress;
    @BindView(R.id.zl_age)
    TextView zlAge;
    @BindView(R.id.zl_profession)
    TextView zlProfession;
    @BindView(R.id.zl_interest)
    TextView zlInterest;
    @BindView(R.id.tv_remark)
    TextView tvRemark;
    @BindView(R.id.ll_remark)
    LinearLayout llRemark;
    @BindView(R.id.submit)
    TextView submit;
    @BindView(R.id.go_back)
    RelativeLayout goBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    private UserDate.DataBean mdata;
    private int isMyFriends;
    private int friendUserId;
    private int friendId;
    private String flag;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_user_details;
    }

    @Override
    public void initUI() {
        super.initUI();
        title.setText("用户资料");
        friendId = getIntent().getIntExtra("userId", 0);
        flag = getIntent().getStringExtra("flag");
        if (null != flag) {
            if (flag.equals("1")) {
                llRemark.setVisibility(View.GONE);
            }
        }
        submit.setText("打招呼");
        getDatass();
    }

    private void getDatass() {
        //获取用户资料
        HashMap map = new HashMap();
        AppBaseRestUsageV1 appBaseRestUsageV1 = new AppBaseRestUsageV1();
        appBaseRestUsageV1.get(BeyondApplication.BaseUrl + "/im/getOthorUser/" + friendId + "/" + UserInfoUtil.getInstance().getUserInfo().getUserId(), map, new NewCustomResponseHandler() {
            @Override
            public void onSuccess(int httpStatusCode, Map headerMap, String responseStr) {
                super.onSuccess(httpStatusCode, headerMap, responseStr);
                UserDate userDate = JSON.parseObject(responseStr, UserDate.class);
                if (userDate.getCode() == 0) {
                    mdata = userDate.getData();
                    isMyFriends = mdata.getType();
                    if (isMyFriends == 2) {
                        addFriends.setVisibility(View.GONE);
                        delFriend.setVisibility(View.VISIBLE);
                    } else {
                        addFriends.setVisibility(View.VISIBLE);
                        delFriend.setVisibility(View.GONE);
                    }
                    hyName.setText(mdata.getNickname() != null ? mdata.getNickname() : "");
                    zlAddress.setText(mdata.getAddress() != null ? mdata.getAddress() : "");
                    tvRemark.setText(mdata.getRemakname());
                    zlAge.setText(mdata.getMobile());
                    int sex = mdata.getSex();
                    if (1 == sex) {
                        zlSex.setText("男");
                    } else {
                        zlSex.setText("女");
                    }
                    //是我自己 我就不打招呼
                    if (Integer.parseInt(UserInfoUtil.getInstance().getUserInfo().getUserId()) == mdata.getUserid()) {
                        submit.setVisibility(View.GONE);
                        addFriends.setVisibility(View.GONE);
                        llRemark.setVisibility(View.GONE);
                    } else {
                        friendUserId = mdata.getUserid();
                    }
                    Glide.with(UserDetailsActivity.this).load(mdata.getHeadImg()).into(img);
                }
            }

            @Override
            public void onFailure(int statusCode, Map headerMap, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headerMap, responseString, throwable);
            }
        });
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        getDatass();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.submit, R.id.add_friends, R.id.del_friend, R.id.ll_remark})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_friends:
                showAddFriends();
                break;
            case R.id.del_friend:
                delFriends();
                break;
            case R.id.ll_remark:
                Intent intent1 = new Intent(UserDetailsActivity.this, SetRemarkActivity.class);
                intent1.putExtra("userid", mdata.getUserid());
                intent1.putExtra("friendsId", friendUserId);
                intent1.putExtra("imgurl", mdata.getHeadImg());
                startActivity(intent1);
                break;
            case R.id.submit:
                //打招呼
                Intent intent = new Intent(UserDetailsActivity.this, ChatActivity.class);
                intent.putExtra(Constant.EXTRA_USER_ID, friendId + "");
                startActivity(intent);
                break;
        }
    }

    /**
     * 展示验证消息
     */
    private void showAddFriends() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View mView = LayoutInflater.from(this).inflate(R.layout.dialog_verify, null);
        final EditText verify = mView.findViewById(R.id.ed_verify);
        verify.setText("我是");
        builder.setView(mView);
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                //申请好友
                applyFriends(verify.getText().toString().trim());
            }
        });

        builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 添加好友
     *
     * @param verify
     */
    private void applyFriends(String verify) {
        AppBaseRestUsageV1 appBaseRestUsageV1 = new AppBaseRestUsageV1();
        HashMap<String, String> map = new HashMap();
        map.put("userid", UserInfoUtil.getInstance().getUserInfo().getUserId());
        map.put("touserid", friendUserId + "");
        map.put("message", verify);
        appBaseRestUsageV1.post(BeyondApplication.BaseUrl + "/im/addFriend", map, new NewCustomResponseHandler() {
            @Override
            public void onFailure(int statusCode, Map headerMap, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headerMap, responseString, throwable);
            }

            @Override
            public void onSuccess(int httpStatusCode, Map headerMap, String responseStr) {
                super.onSuccess(httpStatusCode, headerMap, responseStr);
                BaseResponse sss = JSON.parseObject(responseStr, BaseResponse.class);
                if (sss.getCode() == 0) {
                    ToastUtil.show(UserDetailsActivity.this, "添加成功！");
                    EventBus.getDefault().post(new ShuaXin());
                    Intent intent = new Intent(UserDetailsActivity.this, AddFriendsActivity.class);
                    startActivity(intent);
                    addFriends.setText("等待验证");
                    finish();
                } else {
                    ToastUtil.show(UserDetailsActivity.this, sss.getMessage());
                }
            }
        });
    }

    @OnClick(R.id.go_back)
    public void onViewClicked() {
        finish();
    }

    /**
     * 删除好友
     */
    private void delFriends() {
        HashMap<String, String> map = new HashMap<>();
        map.put("userid", UserInfoUtil.getInstance().getUserInfo().getUserId());
        map.put("touserid", friendId + "");
        AppBaseRestUsageV1 appBaseRestUsageV1 = new AppBaseRestUsageV1();
        appBaseRestUsageV1.post(BeyondApplication.BaseUrl + "/im/deleteMyFriend", map, new NewCustomResponseHandler() {
            @Override
            public void onFailure(int statusCode, Map headerMap, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headerMap, responseString, throwable);
            }

            @Override
            public void onSuccess(int httpStatusCode, Map headerMap, String responseStr) {
                super.onSuccess(httpStatusCode, headerMap, responseStr);
                BaseResponse sss = JSON.parseObject(responseStr, BaseResponse.class);
                if (sss.getCode() == 0) {
                    ToastUtil.show(UserDetailsActivity.this, "删除成功！");
                    EventBus.getDefault().post(new ShuaXin());
                    finish();
                }
            }
        });
    }

    @OnClick(R.id.ll_phone)
    public void onViewClickedss() {
        if (null != mdata.getMobile()) {
            Intent intentq = new Intent(Intent.ACTION_CALL);
            Uri data = Uri.parse("tel:" + mdata.getMobile());
            intentq.setData(data);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity(intentq);
        }
    }
}
