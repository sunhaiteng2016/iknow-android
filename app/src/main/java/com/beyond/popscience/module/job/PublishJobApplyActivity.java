package com.beyond.popscience.module.job;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.DateUtil;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.JobRestUsage;
import com.beyond.popscience.frame.pojo.JobDetail;
import com.beyond.popscience.frame.util.SPUtils;
import com.beyond.popscience.frame.view.SelectCultureDialog;
import com.beyond.popscience.module.building.ClassifyBuildingActivity;
import com.beyond.popscience.module.home.AddressPickTask;
import com.beyond.popscience.module.home.WelcomeActivity;
import com.beyond.popscience.module.home.entity.Address;
import com.beyond.popscience.module.square.ClassifyActivity;
import com.beyond.popscience.module.town.TownCategoryActivity;
import com.beyond.popscience.widget.wheelview.WheelMenuInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;

/**
 * 发布求职
 * Created by danxiang.feng on 2017/10/15.
 */

public class PublishJobApplyActivity extends BaseActivity {

    private final int REQ_PUBLISH_ID = 1001;

     private final int CODE_SELECT_ADDRES = 1001;  //选择地点
     private final int CODE_SELECT_POSITION = 1002; //选择职位
     private final int CODE_SELECT_INDUSTRY = 1003; //选择行业

     private static final String JOB_DETAIL = "jobDetail";

     @BindView(R.id.tv_title)
     protected TextView tv_title;
     @BindView(R.id.titleEditView)
     protected EditText titleEditView;
     @BindView(R.id.publishView)
     protected TextView publishView;
     @BindView(R.id.positionTxtView)
     protected TextView positionTxtView;
     @BindView(R.id.industryTxtView)
     protected TextView industryTxtView;
     @BindView(R.id.addressTextView)
     protected TextView addressTextView;
     @BindView(R.id.addressDetailEditView)
     protected EditText addressDetailEditView;
     @BindView(R.id.priceTxtView)
     protected TextView priceTxtView;
     @BindView(R.id.descriptEditTxt)
     protected EditText descriptEditTxt;
     @BindView(R.id.userNameEditTxt)
     protected EditText userNameEditTxt;
     @BindView(R.id.phoneEditTxt)
     protected EditText phoneEditTxt;
     @BindView(R.id.addressTip)
     protected TextView addressTip;
     //上传职位新增字段
     @BindView(R.id.educationTxtViewssss)
     protected TextView educationTxtView;
     @BindView(R.id.ddsjtwo)
     protected EditText ddsjtwo;
     @BindView(R.id.educationTxtViewsss)
     protected EditText educationTxtViewsss;

     /**
     * 文化程度
     */
    private SelectCultureDialog selectEducationDialog;
    @Request
    private JobRestUsage mRestUsage;

    private JobDetail request = new JobDetail();
    private JobDetail jobDetail;
    private long lastClickTime = 0;   //上次点击的时间戳
    /**
     * 当前的地址
     */
    private Address currAddress;
    /**
     * 薪资选择
     */
    private SelectCultureDialog selectSalaryDialog;
    private String mAddress;

    public static void startActivity(Context context, JobDetail jobDetail){
        Intent intent = new Intent(context, PublishJobApplyActivity.class);
        intent.putExtra(JOB_DETAIL, jobDetail);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_publish_jobapply_layout;
    }

    @Override
    public void initUI() {
        super.initUI();
        jobDetail = (JobDetail) getIntent().getSerializableExtra(JOB_DETAIL);
        tv_title.setText("发布求职");
        iniEditStatusView();
    }

    private void iniEditStatusView(){
        if(jobDetail != null){
            titleEditView.setText(jobDetail.title);
            positionTxtView.setText(jobDetail.position);
            request.position = jobDetail.position;
            industryTxtView.setText(jobDetail.industry);
            request.industry = jobDetail.industry;
            addressTextView.setText(jobDetail.area);
            request.area = jobDetail.areaCode;
            addressDetailEditView.setText(jobDetail.address);
            String salary = SelectCultureDialog.getMenuName(jobDetail.salary, SelectCultureDialog.TYPE_SALARY_X);
            if (TextUtils.isEmpty(salary)){
                salary = "面议";
            }
            educationTxtView.setText(jobDetail.education);
            educationTxtViewsss.setText(jobDetail.age);
            ddsjtwo.setText(jobDetail.arrivalTime);
            priceTxtView.setText(salary);
            request.salary = jobDetail.salary;
            descriptEditTxt.setText(jobDetail.introduce);
            userNameEditTxt.setText(jobDetail.realName);
            phoneEditTxt.setText(jobDetail.mobile);
        }
    }

    @OnClick(R.id.positionLayout)
    void positionLayout(){  // 职位
        ClassifyActivity.startActivityForResult(this, ClassifyActivity.TYPE_SELECT_JOB_POSITION, request.position, CODE_SELECT_POSITION);
    }
    @OnClick(R.id.industryLayout)
    void industryLayout(){  // 行业
        ClassifyBuildingActivity.startActivityForResult(this, ClassifyBuildingActivity.TYPE_JOB_INDUSTRY, request.industry,true, 3,CODE_SELECT_INDUSTRY);
    }
    @OnClick(R.id.addressLayout)
    void areaLayout(){  //工作地点
       TownCategoryActivity.startActivityForResult(this, currAddress, CODE_SELECT_ADDRES);
    }
    @OnClick(R.id.priceLayout)
    void priceLayout(){  // 薪资要求
        if(selectSalaryDialog == null){
            selectSalaryDialog = new SelectCultureDialog(this ,SelectCultureDialog.TYPE_SALARY_X);
            selectSalaryDialog.setCyclic(false);
            selectSalaryDialog.setOnSelectClickListener(new SelectCultureDialog.OnSelectClickListener() {
                @Override
                public void onOk(WheelMenuInfo wheelMenuInfo) {
                    if(wheelMenuInfo!=null){
                        priceTxtView.setText(wheelMenuInfo.getName());
                        request.salary = wheelMenuInfo.getCode();
                    }
                }
            });
        }
        selectSalaryDialog.setSelectedMenuName(priceTxtView.getText().toString());
        selectSalaryDialog.show();
    }
    @OnClick(R.id.educationLayout)
    void educationLayout() {  //学历
        if (selectEducationDialog == null) {
            selectEducationDialog = new SelectCultureDialog(this, SelectCultureDialog.TYPE_DEFAULT);
            selectEducationDialog.setCyclic(false);
            selectEducationDialog.setOnSelectClickListener(new SelectCultureDialog.OnSelectClickListener() {
                @Override
                public void onOk(WheelMenuInfo wheelMenuInfo) {
                    if (wheelMenuInfo != null) {
                        educationTxtView.setText(wheelMenuInfo.getName());
                        request.education = wheelMenuInfo.getCode();
                    }
                }
            });
        }
        selectEducationDialog.show();
    }
    @OnClick(R.id.publishView)
    void publishView(){  // 发布
        long currClickTime = DateUtil.getCurrentTime();
        if(currClickTime - lastClickTime < 1000){
            return;
        }
        lastClickTime = currClickTime;


        if(TextUtils.isEmpty(positionTxtView.getText().toString())){
            ToastUtil.showCenter(this, "请选择期望职位");
            return;
        }
        if(TextUtils.isEmpty(industryTxtView.getText().toString())){
            ToastUtil.showCenter(this, "请选择期望行业");
            return;
        }
        if (TextUtils.isEmpty(addressDetailEditView.getText().toString())) {
            ToastUtil.showCenter(this, "请输入详细地址");
            return;
        }
        if(TextUtils.isEmpty(priceTxtView.getText().toString())){
            ToastUtil.showCenter(this, "请选择薪资需求");
            return;
        }
        if(TextUtils.isEmpty(educationTxtViewsss.getText().toString())){
            ToastUtil.showCenter(this, "请输入年龄");
            return;
        }
        if(TextUtils.isEmpty(ddsjtwo.getText().toString())){
            ToastUtil.showCenter(this, "请输入到岗时间");
            return;
        }
        if(TextUtils.isEmpty(descriptEditTxt.getText().toString())){
            ToastUtil.showCenter(this, "详细描述您的经历");
            return;
        }
        if(TextUtils.isEmpty(userNameEditTxt.getText().toString())){
            ToastUtil.showCenter(this, "请填写姓名");
            return;
        }
        if(TextUtils.isEmpty(phoneEditTxt.getText().toString())){
            ToastUtil.showCenter(this, "请填写联系方式");
            return;
        }

        request.title = titleEditView.getText().toString();
        request.address = addressDetailEditView.getText().toString();
        request.introduce = descriptEditTxt.getText().toString();
        request.realName = userNameEditTxt.getText().toString();
        request.mobile = phoneEditTxt.getText().toString();

        //上传职位新增字段
     //   String educationtxt = educationTxtView.getText().toString();


        request.age = educationTxtViewsss.getText().toString();
        request.arrivalTime = ddsjtwo.getText().toString();

        String applyId = jobDetail != null? jobDetail.applyId : null;
        mRestUsage.publishJobApply(REQ_PUBLISH_ID, applyId, request);
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId){
            case REQ_PUBLISH_ID:
                if (msg.getIsSuccess()){
                    if (jobDetail != null) {
                        ToastUtil.showCenter(this, "修改成功");
                    } else {
                        ToastUtil.showCenter(this, "发布成功");
                        JobDetail job = (JobDetail) msg.getObj();
                        if (job != null && !TextUtils.isEmpty(job.applyId)) {
                            //JobApplyDetailActivity.startActivty(this, job.applyId);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CODE_SELECT_ADDRES:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    Address address = (Address) data.getSerializableExtra("address");
                    if(address != null){
                        currAddress = address;
                        addressTextView.setText(address.getName());
                        request.area = String.valueOf(address.getId());
                        request.areaName=mAddress;
                    }
                }
                break;
            case CODE_SELECT_POSITION:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    String position = data.getStringExtra(ClassifyActivity.EXTRA_CLASSIFY_KEY);
                    positionTxtView.setText(position);
                    request.position = position;
                }
                break;
            case CODE_SELECT_INDUSTRY:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    String industry = data.getStringExtra(ClassifyBuildingActivity.EXTRA_CLASSIFY_RESULT_KEY);
                    industryTxtView.setText(industry);
                    request.industry = industry;
                }
                break;
        }
    }
}
