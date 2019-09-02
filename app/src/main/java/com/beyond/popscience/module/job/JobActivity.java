package com.beyond.popscience.module.job;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.util.Util;
import com.beyond.popscience.frame.view.PopupMenu;
import com.beyond.popscience.module.home.WelcomeActivity;
import com.beyond.popscience.module.job.fragment.JobFragment;
import com.beyond.popscience.module.job.fragment.JobFragmentTwo;
import com.beyond.popscience.module.news.GlobalSearchActivity;
import com.okhttp.CallBackUtil;
import com.okhttp.OkhttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;
import okhttp3.Call;

/**
 * Created by danxiang.feng on 2017/10/15.
 */

public class JobActivity extends BaseActivity {


    /**
     *
     */
    private final static String EXTRA_SELECT_INDEX_KEY = "selectIndex";
    /**
     *
     */
    private final static String EXTRA_KEY_WORD_KEY = "keyWord";

    private final String CATEGORY[] = {"招聘", "求职"};
    public static final int JOB_PROVIDE = 1;    //招聘
    public static final int JOB_APPLY = 2;    //求职


    @BindView(R.id.topReLay)
    protected RelativeLayout topReLay;
    @BindView(R.id.textView4)
    protected TextView textView4;
    @BindView(R.id.searchTopLinLay)
    protected LinearLayout searchTopLinLay;
    @BindView(R.id.searchEditTxt)
    protected EditText searchEditTxt;

    @BindView(R.id.ib_back)
    protected ImageButton ib_back;
    @BindView(R.id.rightImgView)
    protected TextView rightImgView;
    @BindView(R.id.titleLayout)
    protected LinearLayout titleLayout;
    @BindView(R.id.titleTxtView)
    protected TextView titleTxtView;
    @BindView(R.id.titleImageView)
    protected ImageView titleImageView;
    @BindView(R.id.frameLayout)
    protected FrameLayout frameLayout;
    @BindView(R.id.zp)
    TextView zp;
    @BindView(R.id.qz)
    TextView qz;
    @BindView(R.id.shadowView)
    View shadowView;
    @BindView(R.id.searchBtn)
    Button searchBtn;

    private boolean isPopupShow;

    private PopupMenu popupMenu;
    private int selectIndex = JOB_PROVIDE;
    /**
     * 搜索关键字
     */
    private String keyWord;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, JobActivity.class);
        context.startActivity(intent);
    }

    /**
     * 搜索结果
     *
     * @param context
     * @param selectIndex
     */
    public static void startActivitySearchResult(Context context, int selectIndex, String keyword) {
        Intent intent = new Intent(context, JobActivity.class);
        intent.putExtra(EXTRA_SELECT_INDEX_KEY, selectIndex);
        intent.putExtra(EXTRA_KEY_WORD_KEY, keyword);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_job_layout;
    }

    @Override
    public void initUI() {
        super.initUI();

        selectIndex = getIntent().getIntExtra(EXTRA_SELECT_INDEX_KEY, selectIndex);
        keyWord = getIntent().getStringExtra(EXTRA_KEY_WORD_KEY);

        initMenu();
        switchFragment(selectIndex);
        initSearchView();
        textView4.setText(WelcomeActivity.seletedAdress.split("-")[1]);
        textView4.setOnClickListener(new View.OnClickListener() {
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
                            AddressPicker picker = new AddressPicker(JobActivity.this, data);
                            picker.setShadowVisible(true);
                            picker.setHideProvince(true);
                            picker.setTextSize(16);
                            picker.setPadding(10);
                            picker.setSelectedItem("", "台州市", "仙居县");
                            picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {

                                @Override
                                public void onAddressPicked(Province province, City city, County county) {
                                    WelcomeActivity.seletedAdress = city.getAreaName() + "-" + county.getAreaName();
                                    //重新的刷新  列表的数据
                                    switchFragment(1);
                                    textView4.setText(WelcomeActivity.seletedAdress.split("-")[1]);
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

    /**
     *
     */
    private void initSearchView() {
        if (TextUtils.isEmpty(keyWord)) {
            topReLay.setVisibility(View.VISIBLE);
            searchTopLinLay.setVisibility(View.GONE);
        } else {  //搜索结果
            topReLay.setVisibility(View.GONE);
            searchTopLinLay.setVisibility(View.VISIBLE);
        }
        searchEditTxt.setText(keyWord);
    }

    private void switchFragment(int index) {
        selectIndex = index;
        titleTxtView.setText(CATEGORY[selectIndex - 1]);
        replaceFragment(R.id.frameLayout, JobFragment.newInstance(selectIndex, keyWord), false, false);
    }

    private void initMenu() {
        /*popupMenu = new PopupMenu(this);
        popupMenu.setOnItemClickListener(new PopupMenu.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                switchFragment(position + 1);
            }
        });
        popupMenu.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                dismissPopupAnim();
                isPopupShow = false;
            }
        });*/
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //switchFragment(1);
    }

    private void showPopupAnim() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(titleImageView, "rotation", 0, 180));
        animatorSet.setDuration(300);
        animatorSet.start();
    }

    private void dismissPopupAnim() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(titleImageView, "rotation", -180, 0));
        animatorSet.setDuration(300);
        animatorSet.start();
    }

    @OnClick(R.id.titleLayout)
    void topTitleSwitch() {
       /* int xoff = (Util.getScreenWidth(this) - popupMenu.getWidth()) / 2;
        popupMenu.showLocation(titleLayout, xoff, Util.dp2px(this, -8));
        popupMenu.setDataList(Arrays.asList(CATEGORY), selectIndex - 1);

        if (!isPopupShow) {
            showPopupAnim();
            isPopupShow = true;
        }*/
    }

    @OnClick(R.id.rightImgView)
    void rightClick() {
        if (selectIndex == JOB_PROVIDE) {
            GlobalSearchActivity.startActivityJobProvide(this);
        } else {
            GlobalSearchActivity.startActivityJobApply(this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.zp, R.id.qz})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.zp:
                zp.setTextSize(20);
                qz.setTextSize(15);
                switchFragment(1);
                break;
            case R.id.qz:
                zp.setTextSize(15);
                qz.setTextSize(20);
                switchFragment(2);
                break;
        }
    }
}