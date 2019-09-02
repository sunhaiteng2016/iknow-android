package com.beyond.popscience.module.personal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.PermissionChecker;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.AccountRestUsage;
import com.beyond.popscience.frame.pojo.UserInfo;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.ImageUtils;
import com.beyond.popscience.frame.util.SPUtils;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.module.home.entity.Address;
import com.beyond.popscience.module.home.entity.Grxx;
import com.beyond.popscience.widget.AddressPopWindow;
import com.bumptech.glide.Glide;
import com.okhttp.CallBackUtil;
import com.okhttp.OkhttpUtil;
import com.wildma.pictureselector.PictureSelector;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

/**
 * 个人资料
 * Created by linjinfa on 2017/6/11.
 * email 331710168@qq.com
 */
public class PersonalActivity extends BaseActivity implements AddressPopWindow.IAddressChangeListener {
    /**
     * 更新用户信息
     */
    private final int REQUEST_UPDATE_USER_INFO_TASK_ID = 101;
    /**
     * 更新头像
     */
    private final int TASK_UPDATE_AVATAR = 102;


    private final int REQUEST_PERMISSION = 101;

    /**
     *
     */
    @BindView(R.id.tv_title)
    protected TextView titleTxtView;

    @BindView(R.id.ivAvatar)
    protected CircleImageView ivAvatar;

    @BindView(R.id.tvNickName)
    protected TextView tvNickName;

    @BindView(R.id.tvSex)
    protected TextView tvSex;

    @BindView(R.id.tvBirthday)
    protected TextView tvBirthday;

    @BindView(R.id.tvCareer)
    protected TextView tvCareer;

    @BindView(R.id.tvTure)
    protected TextView tvTure;
    @BindView(R.id.phone_tv)
    protected TextView phoneTv;
    @BindView(R.id.tvAddress)
    protected TextView tvAddress;
    @BindView(R.id.set_pwd)
    LinearLayout setPwd;
    @BindView(R.id.xiugai_pwd)
    LinearLayout xiugaiPwd;
    @BindView(R.id.qr_code)
    LinearLayout qrCode;
    @BindView(R.id.tv_yqm)
    TextView tvYqm;

    private UserInfo userInfo;

    private String addressId;

    private String[] mSelectImgItems;

    private AddressPopWindow mAddressWindow;

    @Request
    private AccountRestUsage accountRestUsage;

    private CountDownTimer mTimer;
    /*头像路径*/
    private String avatarPath;

    private Map<String, String> mapTure = new HashMap<>();
    private Map<String, String> mapCareer = new HashMap<>();
    private static String md5pay;
    private int isshop;


    /**
     * @param fragment
     */
    public static void startActivity(Fragment fragment) {
        Intent intent = new Intent(fragment.getActivity(), PersonalActivity.class);
        fragment.startActivity(intent);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_personal;
    }

    @Override
    public void initUI() {
        titleTxtView.setText("个人资料");
        getisPwd();
        userInfo = UserInfoUtil.getInstance().getUserInfo();
        ImageLoaderUtil.display(this, userInfo.getAvatar(), ivAvatar);

        mSelectImgItems = new String[]{getString(R.string.from_photo),
                getString(R.string.from_camera), getString(R.string.cancel)};

        initValues();
        tvNickName.setText(userInfo.getNickName());
        tvSex.setText(userInfo.isMan() ? "男" : "女");
        tvBirthday.setText(userInfo.getDisplayBirthday());
        tvCareer.setText(mapCareer.get(userInfo.getCareer()));
        tvTure.setText(mapTure.get(userInfo.getEducation()));
        addressId = userInfo.getArea();
        tvAddress.setText(SPUtils.get(PersonalActivity.this, "detailedArea", "") + "");
        phoneTv.setText(SPUtils.get(PersonalActivity.this, "Mobile", "") + "");
        tvYqm.setText(userInfo.getUserId());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                String picturePath = data.getStringExtra(PictureSelector.PICTURE_PATH);
                Log.e("", "");
                avatarPath = picturePath;
                handleImg(avatarPath);
            }
        }
        if (requestCode == PersonalEditActivity.TYPE_EDIT_NICKNAME) {
            userInfo = UserInfoUtil.getInstance().getUserInfo();
            tvNickName.setText(userInfo.getNickName());
        }
        if (requestCode == PersonalEditActivity.TYPE_EDIT_SEX) {
            userInfo = UserInfoUtil.getInstance().getUserInfo();
            tvSex.setText(userInfo.isMan() ? "男" : "女");
        }
        if (requestCode == ImageUtils.GET_IMAGE_BY_CAMERA) {
            if (ImageUtils.imageUriFromCamera != null) {
                ImageUtils.cropImage(PersonalActivity.this, ImageUtils.imageUriFromCamera);
            }
        } else if (requestCode == ImageUtils.GET_IMAGE_FROM_PHONE) {
            if (data != null && data.getData() != null) {
                ImageUtils.cropImage(PersonalActivity.this, data.getData());
            }
        } else if (requestCode == ImageUtils.CROP_IMAGE) {
            if (ImageUtils.cropImageUri != null) {
                showProgressDialog();
                avatarPath = ImageUtils.cropImageUri.getPath();
                final File file = new File(avatarPath);
                if (file.exists() && file.length() > 0) {//因为某些鬼畜的原因需要加上这个判断 QAQ
                    handleImg(avatarPath);
                } else if (mTimer == null) {
                    /**
                     * 小米手机 拍照完成之后上传图片会报错
                     * java.net.ProtocolException: expected 370 bytes but received 8192
                     *
                     * 加此处代码在图片写入sdcard 完成之后再执行上传操作
                     *
                     */
                    mTimer = new CountDownTimer(10 * 1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (file.exists() && file.length() > 0) {
                                handleImg(avatarPath);

                                if (mTimer != null) {
                                    mTimer.cancel();
                                    mTimer = null;
                                }
                            }
                        }

                        @Override
                        public void onFinish() {
                            dismissProgressDialog();
                            ToastUtil.showCenter(PersonalActivity.this, "修改失败");
                            mTimer = null;
                        }
                    };

                    mTimer.start();
                }

            }
        }
    }

    private void initValues() {
        mapTure.put("1", "博士");
        mapTure.put("2", "硕士");
        mapTure.put("3", "本科");
        mapTure.put("4", "大专");
        mapTure.put("5", "高中");
        mapTure.put("6", "高中以下");

        mapCareer.put("1", "公务员");
        mapCareer.put("2", "教师");
        mapCareer.put("3", "医务人员");
        mapCareer.put("4", "科研人员");
        mapCareer.put("5", "学生");
        mapCareer.put("6", "农民");
        mapCareer.put("7", "工人");
        mapCareer.put("8", "企业主");
        mapCareer.put("9", "企业管理人员");
        mapCareer.put("10", "金融服务业");
        mapCareer.put("11", "律师");
        mapCareer.put("12", "技术人员");
        mapCareer.put("13", "自由职业");

    }


    /**
     * 处理图片并上传
     *
     * @param imgPath
     */
    private void handleImg(String imgPath) {
        String type = imgPath.endsWith(".png") ? "png" : "jpg";
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
        Map<String, String> params = new HashMap<>();
        params.put("avatar", bitmap2StrByBase64(bitmap, type));
        bitmap.recycle();
        accountRestUsage.updateUserInfo(TASK_UPDATE_AVATAR, params);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if ((grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                ImageUtils.openCameraImage(PersonalActivity.this);
            } else {
                displayFrameworkBugMessageAndExit();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    protected void displayFrameworkBugMessageAndExit() {
        String msg = getString(R.string.camera_permission_error);
        // camera error
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(msg);
        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }

        });

        builder.show();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        if (taskId == REQUEST_UPDATE_USER_INFO_TASK_ID) {
            if (msg.getIsSuccess()) {
                ToastUtil.showCenter(this, "修改成功");
                userInfo.setAddress("");
                userInfo.setArea(addressId);
                UserInfoUtil.getInstance().saveUserInfo(userInfo);
            }
        } else if (taskId == TASK_UPDATE_AVATAR) {
            dismissProgressDialog();
            if (msg.getIsSuccess() && msg.getObj() != null) {
                Map<String, String> urlMap = (Map<String, String>) msg.getObj();
                ImageLoaderUtil.display(this, urlMap.get("url"), ivAvatar);

                //修改本地存储的user
                UserInfo info = UserInfoUtil.getInstance().getUserInfo();
                info.setAvatar(urlMap.get("url"));
                UserInfoUtil.getInstance().saveUserInfo(info);
                ToastUtil.showCenter(this, "修改成功");

                Glide.with(PersonalActivity.this).load(info.getAvatar()).into(ivAvatar);
            } else {
                ToastUtil.showCenter(this, "修改失败");
            }

        }
    }

    @Override
    public void onAddressChange(Address city, Address zone) {
        tvAddress.setText(city.getName() + " " + zone.getName());
        addressId = zone.getId() + "";

        Map<String, String> params = new HashMap<>();
        params.put("area", addressId);
        params.put("address", "");
        accountRestUsage.updateUserInfo(REQUEST_UPDATE_USER_INFO_TASK_ID, params);
    }

    /**
     * 修改昵称
     */
    @OnClick(R.id.llChangeNickName)
    public void userNameChange() {
        PersonalEditActivity.startActivityNickName(this);
    }

    /**
     * 文化程度
     */
    @OnClick(R.id.cultureLine)
    public void cultureClick() {
        PersonalEditActivity.startActivityCulture(this);
    }

    /**
     * 个人职业
     */
    @OnClick(R.id.careerLine)
    public void careerClick() {
        PersonalEditActivity.startActivityCareer(this);
    }

    /**
     * 出生年月
     */
    @OnClick(R.id.birthdayLine)
    public void birthdayClick() {
        PersonalEditActivity.startActivityBirthday(this);
    }

    /**
     * 联系方式
     */
    @OnClick(R.id.contactReLay)
    public void contactClick() {
        //PersonalEditActivity.startActivityContact(this);
    }


    /**
     * 性别
     */
    @OnClick(R.id.sexLine)
    public void sexClick() {
        PersonalEditActivity.startActivitySex(this);
    }

    /**
     * 密码修改
     */
    @OnClick(R.id.rlChangePwd)
    public void changePwdClick() {
        ChangePwdActivity.startActivity(this);
    }

    /**
     * 支付密码修改
     */
    @OnClick(R.id.set_pwd)
    public void setPwdClick() {
        if (null != md5pay) {
            if (md5pay.equals("true")) {
                SetPwdActivity.startActivity(this, "3");
            } else {
                SetPwdActivity.startActivity(this, "1");
            }
        }
    }

    /**
     * 修改支付密码
     */
    @OnClick(R.id.xiugai_pwd)
    public void changepayPwdClick() {
        SetPwdActivity.startActivity(this, "2");
    }

    @OnClick(R.id.rlAvatar)
    public void changeAvatar() {
        // D.show(this, "设置图片", mSelectImgItems, headimgListener);
        PictureSelector
                .create(PersonalActivity.this, PictureSelector.SELECT_REQUEST_CODE)
                .selectPicture(true, 200, 200, 1, 1);
    }

    /**
     * 头像设置确定对话框
     */
    private DialogInterface.OnClickListener headimgListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == 0) {
                ImageUtils.openLocalImage(PersonalActivity.this);
            } else if (which == 1) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //>=6.0检查运行时权限
                    int grantCode = PermissionChecker.checkSelfPermission(PersonalActivity.this, "android.permission.CAMERA");
                    if (grantCode == PackageManager.PERMISSION_GRANTED) { //已授权
                        ImageUtils.openCameraImage(PersonalActivity.this);
                    } else {
                        ActivityCompat.requestPermissions(PersonalActivity.this, new String[]{"android.permission.CAMERA"}, REQUEST_PERMISSION);
                    }
                } else {
                    ImageUtils.openCameraImage(PersonalActivity.this);
                }

            } else if (which == 2) {
            }
            dialog.dismiss();
        }
    };

    /**
     * 通过Base32将Bitmap转换成Base64字符串
     *
     * @param bit
     * @return
     */
    public String bitmap2StrByBase64(Bitmap bit, String type) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 40, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        return "data:image/" + type + ";base64," + Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    //是不是支付密码
    public void getisPwd() {
        JSONObject obj = new JSONObject();
        Map<String, String> map = new HashMap<>();
        map.put("baseParams", UserInfoUtil.getInstance().getUserInfo().getToken());
        OkhttpUtil.okHttpPostJson(BeyondApplication.BaseUrl + "/user/queryMeScore", obj.toString(), map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                try {
                    Grxx grxx = JSON.parseObject(response, Grxx.class);
                    if (grxx.getCode() == 0) {
                        md5pay = grxx.getData().getMd5Pay();
                        isshop = grxx.getData().getIsShop();
                        if ("true".equals(md5pay)) {
                            xiugaiPwd.setVisibility(View.VISIBLE);
                        } else {
                            xiugaiPwd.setVisibility(View.GONE);
                        }

                        if (isshop == 0) {
                            qrCode.setVisibility(View.GONE);
                        } else {
                            qrCode.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (Exception e) {

                }

            }
        });

    }

    @OnClick(R.id.qr_code)
    public void onViewClicked() {
        LbzzActivity.startActivity(this);
    }
}
