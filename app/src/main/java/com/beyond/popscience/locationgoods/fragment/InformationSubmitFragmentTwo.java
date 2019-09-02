package com.beyond.popscience.locationgoods.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.L;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseFragment;
import com.beyond.popscience.frame.thirdsdk.ThirdSDKManager;
import com.beyond.popscience.locationgoods.ZyfwActivity;
import com.beyond.popscience.locationgoods.http.AddressRestUsage;
import com.beyond.popscience.module.home.AddressPickTask;
import com.beyond.popscience.module.home.WelcomeActivity;
import com.bumptech.glide.Glide;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.common.ImageLoader;
import com.yuyh.library.imgsel.config.ISListConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;

import static android.app.Activity.RESULT_OK;


/**
 * 信息提交
 */
public class InformationSubmitFragmentTwo extends BaseFragment {
    @BindView(R.id.ed_shop_name)
    EditText edShopName;
    @BindView(R.id.ed_shop_person)
    EditText edShopPerson;
    @BindView(R.id.ed_shop_phone)
    EditText edShopPhone;
    @BindView(R.id.ed_shop_address)
    EditText edShopAddress;
    @BindView(R.id.ed_ywy)
    EditText edYwy;
    @BindView(R.id.ed_ywy_phone)
    EditText edYwyPhone;
    @BindView(R.id.ed_jyfw)
    EditText edJyfw;
    @BindView(R.id.ed_dpjs)
    EditText edDpjs;
    Unbinder unbinder;
    @BindView(R.id.tv_sd_card_z)
    ImageView tvSdCardZ;
    @BindView(R.id.tv_sd_card_f)
    ImageView tvSdCardF;
    @BindView(R.id.tv_hand_hold_id_card)
    ImageView tvHandHoldIdCard;
    @BindView(R.id.tv_yyzz)
    ImageView tvYyzz;
    @BindView(R.id.tv_shop_icon)
    ImageView tvShopIcon;
    @BindView(R.id.tv_dpsj1)
    ImageView tvDpsj1;
    @BindView(R.id.tv_dpsj2)
    ImageView tvDpsj2;
    @BindView(R.id.tv_dpsj3)
    ImageView tvDpsj3;
    @BindView(R.id.tv_dpsj4)
    ImageView tvDpsj4;
    public List<UpLoadImg> upListUpLoadImg = new ArrayList<>();
    @BindView(R.id.tv_spqy)
    TextView tvSpqy;
    @BindView(R.id.ed_spdz)
    EditText edSpdz;
    @BindView(R.id.rl1)
    LinearLayout rl1;
    @BindView(R.id.submit_data)
    TextView submitData;
    @BindView(R.id.ed_yhkzh)
    EditText edYhkzh;
    @BindView(R.id.ed_khyh)
    EditText edKhyh;
    @BindView(R.id.ed_zfbhm)
    EditText edZfbhm;
    @BindView(R.id.ed_zfbzh)
    EditText edZfbzh;
    @BindView(R.id.ed_yysj)
    EditText edYysj;
    @BindView(R.id.ed_yqm)
    EditText edYqm;
    @BindView(R.id.tv_zyfw)
    TextView tvZyfw;
    private ConcurrentLinkedQueue<UpLoadImg> needUploadImgGoodsDetailViewQueue = new ConcurrentLinkedQueue<>();
    @Request
    AddressRestUsage addressRestUsage;

    /**
     * 当前点击的 TextView
     */
    private TextView currDragLinerView = null;
    /**
     * 处理图片
     */
    private final int HANDL_PHOTO_TASK_ID = 101;
    private String shopName;
    private String shopPerson;
    private String shopPhone;
    private String spqy;
    private String spqz;
    private String yhkhm;
    private String yhkzh;
    private String khyh;
    private String zfbhm;
    private String zfbzh;
    private String ywy;
    private String ywyPhome;
    private String jyfw;
    private String dpjs;

    public String curFlag = "";
    private int mainPosition;

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_information_submit;
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

    @Override
    public void initUI() {
        super.initUI();
        ISNav.getInstance().init(new ImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });
        initView();
        //tvSpqy.setText(BeyondApplication.getInstance().getLocation().city);
    }

    private void initView() {
        upListUpLoadImg.add(new UpLoadImg("", "sdCardZ"));
        upListUpLoadImg.add(new UpLoadImg("", "sdCardF"));
        upListUpLoadImg.add(new UpLoadImg("", "holdIdCard"));
        upListUpLoadImg.add(new UpLoadImg("", "YYZZ"));
        upListUpLoadImg.add(new UpLoadImg("", "shopIcon"));
        upListUpLoadImg.add(new UpLoadImg("", "dpsj1"));
        upListUpLoadImg.add(new UpLoadImg("", "dpsj2"));
        upListUpLoadImg.add(new UpLoadImg("", "dpsj3"));
        upListUpLoadImg.add(new UpLoadImg("", "dpsj4"));
    }

    private static final int REQUEST_LIST_CODE = 0;

    /**
     * 选择图片
     */
    private void startSelectImageActivity(int maxNum) {
        if (maxNum <= 0) {
            return;
        }
        ISListConfig config = new ISListConfig.Builder()
                .multiSelect(true)
                .maxNum(1)
                // 是否记住上次选中记录
                .rememberSelected(false)
                // 使用沉浸式状态栏
                .statusBarColor(Color.parseColor("#0085D0")).build();

        ISNav.getInstance().toListActivity(this, config, REQUEST_LIST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LIST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra("result");
            Log.e("", "");
            if (curFlag.equals("sdCardZ")) {
                requestUploadImgV2(pathList.get(0));
                Glide.with(getActivity()).load(pathList.get(0)).into(tvSdCardZ);
            }
            if (curFlag.equals("sdCardF")) {
                requestUploadImgV2(pathList.get(0));
                Glide.with(getActivity()).load(pathList.get(0)).into(tvSdCardF);
            }
            if (curFlag.equals("holdIdCard")) {
                requestUploadImgV2(pathList.get(0));
                Glide.with(getActivity()).load(pathList.get(0)).into(tvHandHoldIdCard);
            }
            if (curFlag.equals("YYZZ")) {
                requestUploadImgV2(pathList.get(0));
                Glide.with(getActivity()).load(pathList.get(0)).into(tvYyzz);
            }
            if (curFlag.equals("shopIcon")) {
                requestUploadImgV2(pathList.get(0));
                Glide.with(getActivity()).load(pathList.get(0)).into(tvShopIcon);

            }
            if (curFlag.equals("dpsj1")) {
                requestUploadImgV2(pathList.get(0));
                Glide.with(getActivity()).load(pathList.get(0)).into(tvDpsj1);
            }
            if (curFlag.equals("dpsj2")) {
                requestUploadImgV2(pathList.get(0));
                Glide.with(getActivity()).load(pathList.get(0)).into(tvDpsj2);
            }
            if (curFlag.equals("dpsj3")) {
                requestUploadImgV2(pathList.get(0));
                Glide.with(getActivity()).load(pathList.get(0)).into(tvDpsj3);
            }
            if (curFlag.equals("dpsj4")) {
                requestUploadImgV2(pathList.get(0));
                Glide.with(getActivity()).load(pathList.get(0)).into(tvDpsj4);
            }
        }
        //主营范围选择回调
        if (requestCode == 100&& data != null) {

            String mainScope = data.getStringExtra("name");
            mainPosition = data.getIntExtra("position", 0);
            tvZyfw.setText(mainScope);
        }
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case HANDL_PHOTO_TASK_ID:   //处理图片
                if (msg.getIsSuccess()) {
                    List<String> imgPathList = (List<String>) msg.getObj();
                }
                dismissProgressDialog();
                break;
            case 1008611:
                dismissProgressDialog();
                if (msg.getIsSuccess()) {
                    ToastUtil.showCenter(getActivity(), msg.getMsg());
                    getActivity().finish();
                } else {
                    //提示交给后台了
                    ToastUtil.showCenter(getActivity(), msg.getMsg());
                }
                break;
        }
    }

    private void requestUploadImgV2(String url) {
        UpLoadImg goodsDetailView = needUploadImgGoodsDetailViewQueue.poll();
        List<String> pathList = new ArrayList<>();
        pathList.add(url);
        //我可以保证9张图
        ThirdSDKManager.getInstance().uploadImage(pathList, new ThirdSDKManager.UploadCallback(goodsDetailView) {

            @Override
            public void onSuccess(Map<String, String> successMap, List<String> failureList, Object targetObj) {
                super.onSuccess(successMap, failureList, targetObj);
                List<String> lists = new ArrayList<>();
                dismissProgressDialog();
                L.v("上传成功=========> " + targetObj);
                for (Map.Entry<String, String> entry : successMap.entrySet()) {
                    lists.add(entry.getValue());
                }
                if (curFlag.equals("sdCardZ")) {
                    upListUpLoadImg.get(0).setImg(lists.get(0));
                }
                if (curFlag.equals("sdCardF")) {
                    upListUpLoadImg.get(1).setImg(lists.get(0));
                }
                if (curFlag.equals("holdIdCard")) {
                    upListUpLoadImg.get(2).setImg(lists.get(0));
                }
                if (curFlag.equals("YYZZ")) {
                    upListUpLoadImg.get(3).setImg(lists.get(0));
                }
                if (curFlag.equals("shopIcon")) {
                    upListUpLoadImg.get(4).setImg(lists.get(0));
                }
                if (curFlag.equals("dpsj1")) {
                    upListUpLoadImg.get(5).setImg(lists.get(0));
                }
                if (curFlag.equals("dpsj2")) {
                    upListUpLoadImg.get(6).setImg(lists.get(0));
                }
                if (curFlag.equals("dpsj3")) {
                    upListUpLoadImg.get(7).setImg(lists.get(0));
                }
                if (curFlag.equals("dpsj4")) {
                    upListUpLoadImg.get(8).setImg(lists.get(0));
                }
                Log.e("ss", upListUpLoadImg.size() + "");
            }

            @Override
            public void onFailure(List<String> failureList, String errCode, String errMsg, Object targetObj) {
                super.onFailure(failureList, errCode, errMsg, targetObj);
                ToastUtil.showCenter(getActivity(), "上传失败，请重新上传！(不要上传相同图片)");
                dismissProgressDialog();
            }
        });
    }

    private void UPData() {
        //把数据上传了就行了
        HashMap<String, String> map = new HashMap<>();
        map.put("shopName", shopName);
        map.put("shopType", mainPosition + "");
        map.put("shopContacts", shopPerson);
        map.put("shopTel", shopPhone);
        map.put("shopArea", spqy);
        map.put("shopAddress", spqz);
        map.put("bankUserName", zfbhm);
        map.put("bankNo", yhkzh);
        map.put("bankArea", khyh);
        map.put("bankName", yhkhm);
        map.put("aliName", zfbhm);
        map.put("aliPay", zfbzh);
        map.put("salesman", ywy);
        map.put("salesmanTel", ywyPhome);
        map.put("scopeBusiness", jyfw);
        map.put("shopDetail", dpjs);
        map.put("openTime", edYysj.getText().toString().trim());
        //准备农户实景的图片
        StringBuilder sb = new StringBuilder();
        //上传图片
        for (UpLoadImg bean : upListUpLoadImg) {
            if (bean.flag.equals("sdCardZ")) {
                map.put("idCardUp", bean.getImg());
            }
            if (bean.flag.equals("sdCardF")) {
                map.put("idCardBottom", bean.getImg());
            }
            if (bean.flag.equals("holdIdCard")) {
                map.put("peopleCardUp", bean.getImg());
            }
            if (bean.flag.equals("shopIcon")) {
                map.put("shopImg", bean.getImg());
            }
            if (bean.flag.equals("YYZZ")) {
                map.put("businessLicense", bean.getImg());
            }
            if (bean.flag.equals("dpsj1")) {
                sb.append(bean.getImg() + ",");
            }
            if (bean.flag.equals("dpsj2")) {
                sb.append(bean.getImg() + ",");
            }
            if (bean.flag.equals("dpsj3")) {
                sb.append(bean.getImg() + ",");
            }
            if (bean.flag.equals("dpsj4")) {
                sb.append(bean.getImg());
            }
        }
        map.put("shopImgList", sb.toString());
        map.put("yqCode", edYqm.getText().toString().trim());
        addressRestUsage.JoinUs(1008611, map);
    }

    @OnClick(R.id.submit_data)
    public void onViewClicked() {
        //我看一下 集合里面上传的 图片
        //上传图片
        needUploadImgGoodsDetailViewQueue.addAll(upListUpLoadImg);
        shopName = edShopName.getText().toString().trim();
        shopPerson = edShopPerson.getText().toString().trim();
        shopPhone = edShopPhone.getText().toString().trim();
        spqy = tvSpqy.getText().toString().trim();
        spqz = edSpdz.getText().toString().trim();
        yhkhm = edShopAddress.getText().toString().trim();
        yhkzh = edYhkzh.getText().toString().trim();
        khyh = edKhyh.getText().toString().trim();
        zfbhm = edZfbhm.getText().toString().trim();
        zfbzh = edZfbzh.getText().toString().trim();
        ywy = edYwy.getText().toString().trim();
        ywyPhome = edYwyPhone.getText().toString().trim();
        jyfw = edJyfw.getText().toString().trim();
        dpjs = edDpjs.getText().toString().trim();
        if (TextUtils.isEmpty(shopName)) {
            ToastUtil.showCenter(getActivity(), "请输入店铺名称");
            return;
        }
        if (TextUtils.isEmpty(shopPerson)) {
            ToastUtil.showCenter(getActivity(), "请输入店铺联系人");
            return;
        }
        if (TextUtils.isEmpty(shopPhone)) {
            ToastUtil.showCenter(getActivity(), "请输入联系方式");
            return;
        }
        if (TextUtils.isEmpty(shopPerson)) {
            ToastUtil.showCenter(getActivity(), "请输入店铺联系人");
            return;
        }
        if (TextUtils.isEmpty(spqy)) {
            ToastUtil.showCenter(getActivity(), "请选择商铺区域");
            return;
        }
        if (TextUtils.isEmpty(spqz)) {
            ToastUtil.showCenter(getActivity(), "请输入商铺地址");
            return;
        }
        if (TextUtils.isEmpty(yhkhm)) {
            ToastUtil.showCenter(getActivity(), "请输入银行卡户名");
            return;
        }
        if (mainPosition == 0) {
            ToastUtil.showCenter(getActivity(), "请输入银行卡账号");
            return;
        }
        /*if (TextUtils.isEmpty(khyh)) {
            ToastUtil.showCenter(getActivity(), "请输入开户银行");
            return;
        }
        if (TextUtils.isEmpty(zfbhm)) {
            ToastUtil.showCenter(getActivity(), "请输入支付宝户名");
            return;
        }
        if (TextUtils.isEmpty(zfbzh)) {
            ToastUtil.showCenter(getActivity(), "请输入支付宝账号");
            return;
        }*/
        if (TextUtils.isEmpty(ywy)) {
            ToastUtil.showCenter(getActivity(), "请输入业务员名称");
            return;
        }
        if (TextUtils.isEmpty(ywyPhome)) {
            ToastUtil.showCenter(getActivity(), "请输入业务员电话");
            return;
        }
        if (TextUtils.isEmpty(jyfw)) {
            ToastUtil.showCenter(getActivity(), "请输入经营范围");
            return;
        }
        if (TextUtils.isEmpty(dpjs)) {
            ToastUtil.showCenter(getActivity(), "请输入店铺介绍");
            return;
        }
        if (TextUtils.isEmpty(edYysj.getText().toString().trim())) {
            ToastUtil.showCenter(getActivity(), "请输入营业时间");
            return;
        }
        for (UpLoadImg bean : upListUpLoadImg) {
            if (bean.getFlag().equals("sdCardZ")) {
                if (bean.getImg().equals("")) {
                    ToastUtil.showCenter(getActivity(), "请选择身份证正面");
                    return;
                }

            }
            if (bean.getFlag().equals("sdCardF")) {
                if (bean.getImg().equals("")) {
                    ToastUtil.showCenter(getActivity(), "请选择身份证反面");
                    return;
                }

            }
            if (bean.getFlag().equals("holdIdCard")) {
                if (bean.getImg().equals("")) {
                    ToastUtil.showCenter(getActivity(), "请选择手持身份证照片");
                    return;
                }
            }
            if (bean.getFlag().equals("shopIcon")) {
                if (bean.getImg().equals("")) {
                    ToastUtil.showCenter(getActivity(), "请选择商店图标");
                    return;
                }
            }
        }
        showProgressDialog();
        UPData();
    }

    @OnClick({R.id.tv_spqy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_spqy:
                selCity();
                break;
        }
    }

    private void selCity() {
        AddressPickTask task = new AddressPickTask(getActivity());
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
                    tvSpqy.setText(county.getAreaName());
                }
            }
        });
        task.execute(WelcomeActivity.seletedAdress_two.split("-")[0], WelcomeActivity.seletedAdress_two.split("-")[1], WelcomeActivity.seletedAdress_two.split("-")[2]);
    }

    @OnClick({R.id.tv_sd_card_z, R.id.tv_zyfw, R.id.tv_sd_card_f, R.id.tv_hand_hold_id_card, R.id.tv_yyzz, R.id.tv_shop_icon, R.id.tv_dpsj1, R.id.tv_dpsj2, R.id.tv_dpsj3, R.id.tv_dpsj4})
    public void onViewClickedsss(View view) {
        switch (view.getId()) {
            case R.id.tv_sd_card_z:
                curFlag = "sdCardZ";
                startSelectImageActivity(1);
                break;
            case R.id.tv_zyfw:
                Intent intent = new Intent(getActivity(), ZyfwActivity.class);
                startActivityForResult(intent, 100);
                break;
            case R.id.tv_sd_card_f:
                curFlag = "sdCardF";
                startSelectImageActivity(1);
                break;
            case R.id.tv_hand_hold_id_card:
                curFlag = "holdIdCard";
                startSelectImageActivity(1);
                break;
            case R.id.tv_yyzz:
                curFlag = "YYZZ";
                startSelectImageActivity(1);
                break;
            case R.id.tv_shop_icon:
                curFlag = "shopIcon";
                startSelectImageActivity(1);
                break;
            case R.id.tv_dpsj1:
                curFlag = "dpsj1";
                startSelectImageActivity(1);
                break;
            case R.id.tv_dpsj2:
                curFlag = "dpsj2";
                startSelectImageActivity(1);
                break;
            case R.id.tv_dpsj3:
                curFlag = "dpsj3";
                startSelectImageActivity(1);
                break;
            case R.id.tv_dpsj4:
                curFlag = "dpsj4";
                startSelectImageActivity(1);
                break;
        }
    }


    class UpLoadImg {
        String img;
        String flag;

        public UpLoadImg(String img, String flag) {
            this.img = img;
            this.flag = flag;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }
    }
}
