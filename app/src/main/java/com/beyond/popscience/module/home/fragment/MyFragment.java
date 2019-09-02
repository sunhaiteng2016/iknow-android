package com.beyond.popscience.module.home.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.LoginActivity;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseFragment;
import com.beyond.popscience.frame.net.CommonRestUsage;
import com.beyond.popscience.frame.net.FromDataRestUsage;
import com.beyond.popscience.frame.net.LoginRestUsage;
import com.beyond.popscience.frame.pojo.AppInfo;
import com.beyond.popscience.frame.pojo.UserInfo;
import com.beyond.popscience.frame.util.D;
import com.beyond.popscience.frame.util.DTwo;
import com.beyond.popscience.frame.util.DownloadFileFromURL;
import com.beyond.popscience.frame.util.FastBlur;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.SPUtils;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.frame.util.Util;
import com.beyond.popscience.locationgoods.MyYjActivity;
import com.beyond.popscience.locationgoods.MyshopActivity;
import com.beyond.popscience.locationgoods.OrderTwoActivity;
import com.beyond.popscience.locationgoods.ReceivingAddressActivity;
import com.beyond.popscience.locationgoods.XieYiActivity;
import com.beyond.popscience.locationgoods.http.AddressRestUsage;
import com.beyond.popscience.module.collection.CollectionActivity;
import com.beyond.popscience.module.home.ClearCacheTask;
import com.beyond.popscience.module.home.DingdanListActivity;
import com.beyond.popscience.module.home.MySocialActivity;
import com.beyond.popscience.module.home.entity.Grxx;
import com.beyond.popscience.module.home.shopcart.CartMakeSureActivity;
import com.beyond.popscience.module.mservice.PublishListActivity;
import com.beyond.popscience.module.news.GlobalSearchActivity;
import com.beyond.popscience.module.personal.AboutUsActivity;
import com.beyond.popscience.module.personal.FeedbackActivity;
import com.beyond.popscience.module.personal.HelpActivity;
import com.beyond.popscience.module.personal.LbzzOneActivity;
import com.beyond.popscience.module.personal.MyTeamActivity;
import com.beyond.popscience.module.personal.PersonalActivity;
import com.beyond.popscience.module.personal.QRcodeActivity;
import com.beyond.popscience.module.personal.SetPwdActivity;
import com.beyond.popscience.module.point.PonitShopActivity;
import com.beyond.popscience.module.social.CommentNoticeActivity;
import com.beyond.popscience.utils.DataCleanManager;
import com.hyphenate.chat.EMClient;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.okhttp.CallBackUtil;
import com.okhttp.OkhttpUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 我的
 * Created by yao.cui on 2ore017/6/8.
 */

public class MyFragment extends BaseFragment {

    /**
     * 清除缓存
     */
    private final int REQUEST_CLEAR_CACHE_TASK_ID = 101;
    /**
     * 退出登录
     */
    private final int REQUEST_LOGIN_OUT_TASK_ID = 102;
    /**
     * 请求最新版本
     */
    private final int REQUEST_VERSION_TASK_ID = 103;
    /**
     * 获取阅读竞赛绿币
     */
    private final int REQUEST_SCORE_TASK_ID = 104;
    /**
     * 帮助
     */
    private final int HELP_TASK_ID = 104;

    @BindView(R.id.ivHeaderBg)
    ImageView ivHeaderBg;
    /**
     * 我的收藏
     */
    @BindView(R.id.myCollectionTxtvIew)
    TextView myCollectionTxtvIew;
    /**
     * 我的科普绿币
     */
    @BindView(R.id.myIntegralTxtView)
    TextView myIntegralTxtView;
    /**
     * 个人资料
     */
    @BindView(R.id.personalInfoTxtView)
    TextView personalInfoTxtView;
    /**
     * 清空缓存
     */
    @BindView(R.id.clearCacheTxtView)
    LinearLayout clearCacheTxtView;
    @BindView(R.id.huancun)
    TextView huancun;
    /**
     * 登出
     */
    @BindView(R.id.loginOutTxtView)
    TextView loginOutTxtView;
    /**
     * 绿币商城
     */
    @BindView(R.id.myIntegralMallTxtView)
    TextView myIntegralMallTxtView;  //

    @BindView(R.id.tvName)
    TextView tvName;

    @BindView(R.id.hostorydingdan)
    TextView hostorydingdan;


    @BindView(R.id.cvHeader)
    CircleImageView cvAvatar;
    @BindView(R.id.test_tv)
    TextView testTv;
    Unbinder unbinder;
    @BindView(R.id.lbzz_one)
    TextView lbzzOne;
    @BindView(R.id.tvHelp)
    TextView tvHelp;
    @BindView(R.id.tvAboutUs)
    TextView tvAboutUs;
    @BindView(R.id.tvFeedback)
    TextView tvFeedback;
    @BindView(R.id.bbh)
    TextView bbh;
    @BindView(R.id.sys)
    ImageView sys;
    public static boolean issetpwd = false;
    @BindView(R.id.ivSearch)
    ImageView ivSearch;
    @BindView(R.id.ivBell)
    ImageView ivBell;
    @BindView(R.id.rlHeader)
    RelativeLayout rlHeader;
    @BindView(R.id.qbdd)
    TextView qbdd;
    @BindView(R.id.tvMyPublish)
    TextView tvMyPublish;
    @BindView(R.id.xfjl)
    TextView xfjl;
    @BindView(R.id.receiving_address_tv)
    TextView receivingAddressTv;
    @BindView(R.id.all_order)
    TextView allOrder;
    @BindView(R.id.join_us)
    TextView joinUs;
    @BindView(R.id.order_manager)
    TextView orderManager;
    @BindView(R.id.mySocialTxtView)
    TextView mySocialTxtView;
    @BindView(R.id.lbzz)
    TextView lbzz;
    @BindView(R.id.order_manager_two)
    TextView orderManagerTwo;
    @BindView(R.id.tv_shop_car)
    TextView tvShopCar;
    @BindView(R.id.tv_my_yj)
    TextView tvMyYj;
    @BindView(R.id.ll_oness)
    LinearLayout llOness;


    @Request
    private LoginRestUsage loginRestUsage;
    @Request
    private CommonRestUsage commonRestUsage;
    @Request
    AddressRestUsage addressRestUsage;
    @Request
    private FromDataRestUsage fromDataRestUsage;
    private String score;
    private String md5pay;
    private String objStatus;
    private String objMsg;

    public static MyFragment getInstance() {
        MyFragment fragment = new MyFragment();
        return fragment;
    }

    @Override
    public void initUI() {
        super.initUI();
        myIntegralTxtView.setText(SPUtils.get(getActivity(), "score", "") + "");
        requestScore();
        getIsPwd();
        getShopStatus();
        getOpenStatus();
        //获取缓存大小
        huancun.setText(DataCleanManager.getTotalCacheSize(getActivity()));
        bbh.setText(Util.getVersionName(getActivity()));
    }

    private void getOpenStatus() {
        fromDataRestUsage.StatusByArea(1003, SPUtils.get(getActivity(), "detailedArea", "").toString().split("-")[1] + "-" + SPUtils.get(getActivity(), "detailedArea", "").toString().split("-")[2], "3");
    }

    //获取商家状态
    private void getShopStatus() {
        addressRestUsage.getShopStatus(10076);
    }

    private void getAvatar() {
        UserInfo userInfo = UserInfoUtil.getInstance().getUserInfo();
        tvName.setText(userInfo.getNickName());

        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.default_header_icon).showImageOnFail(R.drawable.default_header_icon).showImageForEmptyUri(R.drawable.default_header_icon).displayer(new FadeInBitmapDisplayer(200, true, true, false)).considerExifParams(true).cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderUtil.displayImage(getContext(), userInfo.getAvatar(), cvAvatar, displayImageOptions, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                setHeaderBg(loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
            }
        });
    }


    /**
     * 请求绿币
     */
    private void requestScore() {
        commonRestUsage.getScore(REQUEST_SCORE_TASK_ID);
    }

    private void rehasPwd() {
        commonRestUsage.getScore(REQUEST_SCORE_TASK_ID);
    }

    @Override
    public void onResume() {
        myIntegralTxtView.setText(SPUtils.get(getActivity(), "score", "") + "");
        getAvatar();
        requestScore();
        super.onResume();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case 1003:
                if (msg.getIsSuccess()) {
                    String strObj = (String) msg.getObj();
                    if (strObj.equals("true")) {
                        myIntegralMallTxtView.setVisibility(View.VISIBLE);
                        llOness.setVisibility(View.VISIBLE);
                    } else {
                        myIntegralMallTxtView.setVisibility(View.INVISIBLE);
                        llOness.setVisibility(View.GONE);
                    }
                }
                break;

            case 10076:
                if (msg.getIsSuccess()) {
                    objStatus = (String) msg.getObj();
                    objMsg = msg.getMsg();

                }
                break;
            case REQUEST_CLEAR_CACHE_TASK_ID:   //清除缓存
                ToastUtil.showCenter(getContext(), "清除成功");
                dismissProgressDialog();
                break;
            case REQUEST_VERSION_TASK_ID:   //请求最新版本
                if (msg.getIsSuccess()) {
                    final AppInfo appInfo = (AppInfo) msg.getObj();
                    String codeuel = appInfo.getUrl();
                    if (appInfo != null) {
                        StringBuffer sb = new StringBuffer();
                        Boolean isShowToast = (boolean) msg.getTargetObj();
                        //后台的版本号与app本身对比，提示更新
                        String hint = appInfo.getHint();
                        if (null != hint) {
                            String[] hints = hint.split("&");
                            if (hints.length > 0) {
                                for (String mHint : hints) {
                                    sb.append(mHint + "\n");
                                }
                            } else {
                                sb.append(appInfo.getHint());
                            }
                        } else {
                            sb.append("");
                        }
                        if (appInfo.getVersionCode() > Util.getVersionCode(BeyondApplication.getInstance())) {
                            DTwo.show(getActivity(), null, "检测到最新版本,是否立即更新?\n\n" + sb.toString(), "取消", "更新", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == DialogInterface.BUTTON_POSITIVE) {
                                        if (!TextUtils.isEmpty(appInfo.getUrl())) {
                                            new DownloadFileFromURL(getActivity()).execute(appInfo.getUrl());
                                        }
                                    }
                                    dialog.dismiss();
                                }
                            });
                        } else {
                            if (isShowToast != null && isShowToast) {
                                ToastUtil.showCenter(getActivity(), "已是最新版本");
                            }
                        }
                    }
                }
                dismissProgressDialog();
                break;
            case REQUEST_LOGIN_OUT_TASK_ID: //退出登录
                if (msg.getIsSuccess()) {
                    UserInfoUtil.getInstance().removeUserInfo();
                    SPUtils.remove(getActivity(), "score");
                    BeyondApplication.getInstance().popAllActivityExcept(getActivity());
                    //退出环信
                    EMClient.getInstance().logout(true);//退出登录
                    LoginActivity.startActivity(getActivity());
                    getActivity().finish();
                }
                dismissProgressDialog();
                break;
            case REQUEST_SCORE_TASK_ID: //请求绿币
                Log.e("==我的绿币===", msg.getIsSuccess().toString());
                if (msg.getIsSuccess()) {
                    HashMap<String, String> map = (HashMap<String, String>) msg.getObj();
                    if (map != null) {
                        score = map.get("score");
                        if (!TextUtils.isEmpty(score)) {
                            myIntegralTxtView.setText(score);
                            UserInfo userInfo = UserInfoUtil.getInstance().getUserInfo();
                            userInfo.setScore(score);
                            UserInfoUtil.getInstance().saveUserInfo(userInfo);
                            SPUtils.put(getActivity(), "score", score);
//                            tempScore = score;
                        } else {
//                            myIntegralTxtView.setText("0");
                            myIntegralTxtView.setText(SPUtils.get(getActivity(), "score", "") + "");
                        }
                    } else {
//                        myIntegralTxtView.setText("0");
                        myIntegralTxtView.setText(SPUtils.get(getActivity(), "score", "") + "");
                    }
                } else {
//                    myIntegralTxtView.setText("0");
                    myIntegralTxtView.setText(SPUtils.get(getActivity(), "score", "") + "");
                }

                break;
        }
    }

    private void setHeaderBg(Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        if (ivHeaderBg != null) {
            Bitmap blurBmp = FastBlur.blur(bitmap, bitmap.getWidth(), bitmap.getHeight(), false);
            ivHeaderBg.setImageBitmap(blurBmp);
        }
    }

    /**
     * 清除缓存
     */
    private void requestClearCache() {
        showProgressDialog("正在清除缓存");
        execuTask(new ClearCacheTask(REQUEST_CLEAR_CACHE_TASK_ID));
    }

    /**
     * 退出登录
     */
    private void requestLoginOut() {
        showProgressDialog();
        loginRestUsage.loginOut(REQUEST_LOGIN_OUT_TASK_ID);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_my;
    }

    /**
     * 搜索
     *
     * @param view
     */
    @OnClick(R.id.ivSearch)
    public void searchClick(View view) {
        GlobalSearchActivity.startActivity(getActivity());
    }

    /**
     * 搜索
     *
     * @param view
     */
    @OnClick(R.id.order_manager)
    public void orderManager(View view) {
        if (null==objStatus)return;
        if (objStatus.equals("1")) {
            Intent intent = new Intent(getActivity(), MyshopActivity.class);
            startActivity(intent);
        } else {
            ToastUtil.showCenter(getActivity(), objMsg);
        }
    }


    /**
     * 搜索
     *
     * @param view
     */
    @OnClick(R.id.join_us)
    public void joinus(View view) {
        if (null==objStatus)return;
        if (objStatus.equals("1")) {
            ToastUtil.showCenter(getActivity(), "你已经是商家了！");
        } else {
            Intent intent = new Intent(getActivity(), XieYiActivity.class);
            startActivity(intent);
        }
    }


    @OnClick(R.id.hostorydingdan)
    public void hostoryClick(View view) {
        Intent intent = new Intent(getActivity(), DingdanListActivity.class);
        startActivity(intent);
    }

    /**
     * 消息
     *
     * @param view
     */
    @OnClick(R.id.ivBell)
    public void messageClick(View view) {
        CommentNoticeActivity.startActivity(getActivity());
    }

    @OnClick(R.id.sys)
    public void sysClick(View view) {
        //所要申请的权限,拍照和写入sd卡
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (EasyPermissions.hasPermissions(getActivity(), perms)) {//检查是否获取该权限
            if (md5pay.equals("true")) {
                QRcodeActivity.startActivity(getActivity());
            } else {
                if (issetpwd) {
                    QRcodeActivity.startActivity(getActivity());
                } else {
                    SetPwdActivity.startActivity(getActivity(), "1", "sys");
                }
            }
        } else {
            //第二个参数是被拒绝后再次申请该权限的解释
            //第三个参数是请求码
            //第四个参数是要申请的权限
            EasyPermissions.requestPermissions(getActivity(), "拍照必要的权限", 0, perms);
        }
    }

    @OnClick(R.id.myCollectionTxtvIew)
    public void myCollection(View view) {
        CollectionActivity.startActivity(this);
    }


    @OnClick(R.id.tvMyPublish)
    public void myPublish(View view) {
        PublishListActivity.startActivity(getContext(), "", "");
    }

    /**
     * 绿币商城
     *
     * @param view
     */
    @OnClick(R.id.myIntegralMallTxtView)
    public void setMyIntegralMall(View view) {
        Intent intent = new Intent(getActivity(), PonitShopActivity.class);
        intent.putExtra("score", score);
        startActivity(intent);
    }

    /**
     * 我的圈子
     *
     * @param view
     */
    @OnClick(R.id.mySocialTxtView)
    public void mySocialClick(View view) {
        MySocialActivity.startActivity(getContext());
    }


    /**
     * 清空缓存
     *
     * @param view
     */
    @OnClick(R.id.clearCacheTxtView)
    public void cleanCache(View view) {
        requestClearCache();
        huancun.setText("0M");

    }

    /**
     * 登出
     *
     * @param view
     */
    @OnClick(R.id.loginOutTxtView)
    public void loginout(View view) {
        D.show(getActivity(), null, "确认要退出登录?", "取消", "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    requestLoginOut();
                }
                dialog.dismiss();
            }
        });
    }

    /**
     * 个人资料
     *
     * @param view
     */
    @OnClick(R.id.personalInfoTxtView)
    public void personInfo(View view) {
        PersonalActivity.startActivity(this);
    }

    @OnClick(R.id.lbzz_one)
    public void lbzz_one(View view) {
        LbzzOneActivity.startActivity(getContext());
    }

    @OnClick(R.id.xfjl)
    public void xfjl(View view) {
        // XfjlActivity.startActivity(getContext());
        Intent intent = new Intent(getActivity(), MyTeamActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tvFeedback)
    public void feedback(View view) {
        FeedbackActivity.startActivity(getContext());
    }


    @OnClick(R.id.tvAboutUs)
    public void aboutUs(View view) {
        AboutUsActivity.startActivity(getContext());
    }

    @OnClick(R.id.tvHelp)
    public void help(View view) {
        HelpActivity.startActivity(getContext());
    }


    @OnClick(R.id.test_tv)
    public void setTestTv() {//仅用于测试
        Intent intent = new Intent(getActivity(), CartMakeSureActivity.class);
        startActivity(intent);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        myIntegralTxtView.setText(UserInfoUtil.getInstance().getUserInfo().getScore());
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    //是不是支付密码
    public void getIsPwd() {
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
                        SPUtils.put(getActivity(), "scores", grxx.getData().getScore());
                        md5pay = grxx.getData().getMd5Pay();
                    }
                } catch (Exception e) {

                }

            }
        });
    }

    @OnClick(R.id.receiving_address_tv)
    public void onViewClickedsssss() {
        Intent intent = new Intent(getActivity(), ReceivingAddressActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.all_order)
    public void onViewClickedssssss() {
        Intent intent = new Intent(getActivity(), OrderTwoActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ll_version)
    public void onViewClicked() {
        //检查版本更新
        showProgressDialog();
        CommonRestUsage commonRestUsage = new CommonRestUsage();
        commonRestUsage.setIdentification(getIdentification());
        commonRestUsage.getVersion(REQUEST_VERSION_TASK_ID, true);
    }

    @OnClick(R.id.ll_green_score)
    public void onViewClickedssss() {
        /*Intent intent = new Intent(getActivity(), PonitShopActivity.class);
        intent.putExtra("score", score);
        startActivity(intent);*/
    }

    @OnClick(R.id.tv_my_yj)
    public void sss() {
        Intent intent = new Intent(getActivity(), MyYjActivity.class);
        startActivity(intent);
    }

}
