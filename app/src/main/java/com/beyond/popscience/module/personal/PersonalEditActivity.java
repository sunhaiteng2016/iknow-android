package com.beyond.popscience.module.personal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.InputMethodUtil;
import com.beyond.library.util.InvokeUtil;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.AccountRestUsage;
import com.beyond.popscience.frame.pojo.UserInfo;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.frame.view.SelectCultureDialog;
import com.beyond.popscience.frame.view.SelectDateDialog;
import com.beyond.popscience.frame.view.SexDialog;
import com.beyond.popscience.widget.wheelview.WheelMenuInfo;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 个人资料编辑
 * Created by linjinfa on 2017/6/11.
 * email 331710168@qq.com
 */
public class PersonalEditActivity extends BaseActivity {

    /**
     * 更新用户信息
     */
    private final int REQUEST_UPDATE_USER_INFO_TASK_ID = 101;
    /**
     *
     */
    private static final String EXTRA_EDIT_TYPE_KEY = "editType";
    /**
     * 昵称修改
     */
    public static final int TYPE_EDIT_NICKNAME = 0;
    /**
     * 个人职业修改
     */
    private static final int TYPE_EDIT_CAREER = 1;
    /**
     * 健康情况修改
     */
    private static final int TYPE_EDIT_HEALTH = 2;
    /**
     * 联系方式修改
     */
    private static final int TYPE_EDIT_CONTACT = 3;
    /**
     * 性别修改
     */
    public static final int TYPE_EDIT_SEX = 4;
    /**
     * 生日修改
     */
    private static final int TYPE_EDIT_BIRTHDAY = 5;
    /**
     * 文化程度修改
     */
    private static final int TYPE_EDIT_CULTURE = 6;
    /**
     *
     */
    @BindView((R.id.contentEditTxt))
    protected EditText contentEditTxt;
    /**
     *
     */
    @BindView(R.id.tv_title)
    protected TextView titleTxtView;

    @BindView(R.id.leftTxtView)
    protected TextView leftTxtView;

    @BindView(R.id.tv_right)
    protected TextView rightTxtView;

    @BindView(R.id.ib_back)
    protected ImageButton backImgBtn;

    /**
     * 编辑类型
     */
    private int editType;
    /**
     * 性别选择
     */
    private SexDialog sexDialog;
    /**
     * 生日
     */
    private SelectDateDialog selectDateDialog;
    /**
     * 文化程度
     */
    private SelectCultureDialog selectCultureDialog;
    /**
     * 个人职业
     */
    private SelectCultureDialog selectCareerDialog;
    @Request
    private AccountRestUsage accountRestUsage;

    /**
     * 昵称修改
     * @param context
     */
    public static void startActivityNickName(Context context){
        startActivity(context, TYPE_EDIT_NICKNAME);
    }

    /**
     * 个人职业修改
     * @param context
     */
    public static void startActivityCareer(Context context){
        startActivity(context, TYPE_EDIT_CAREER);
    }

    /**
     * 健康情况修改
     * @param context
     */
    public static void startActivityHealth(Context context){
        startActivity(context, TYPE_EDIT_HEALTH);
    }

    /**
     * 联系方式修改
     * @param context
     */
    public static void startActivityContact(Context context){
        startActivity(context, TYPE_EDIT_CONTACT);
    }

    /**
     * 性别修改
     * @param context
     */
    public static void startActivitySex(Context context){
        startActivity(context, TYPE_EDIT_SEX);
    }

    /**
     * 生日修改
     * @param context
     */
    public static void startActivityBirthday(Context context){
        startActivity(context, TYPE_EDIT_BIRTHDAY);
    }

    /**
     * 文化程度修改
     * @param context
     */
    public static void startActivityCulture(Context context){
        startActivity(context, TYPE_EDIT_CULTURE);
    }

    /**
     *
     * @param context
     */
    private static void startActivity(Context context, int type){
        Intent intent = new Intent(context, PersonalEditActivity.class);
        intent.putExtra(EXTRA_EDIT_TYPE_KEY, type);
        ((Activity) context).startActivityForResult(intent,type);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_personal_edit;
    }

    @Override
    public void initUI() {
        editType = getIntent().getIntExtra(EXTRA_EDIT_TYPE_KEY, editType);

        backImgBtn.setVisibility(View.GONE);
        leftTxtView.setVisibility(View.VISIBLE);
        rightTxtView.setVisibility(View.VISIBLE);
        leftTxtView.setText("取消");
        rightTxtView.setText("确定");

        switchView();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId){
            case REQUEST_UPDATE_USER_INFO_TASK_ID:  //更新用户信息
                if(msg.getIsSuccess()){
                    ParamKeyValue paramKeyValue = (ParamKeyValue) msg.getTargetObj();
                    if(paramKeyValue!=null && !TextUtils.isEmpty(paramKeyValue.key) && !TextUtils.isEmpty(paramKeyValue.value)){
                        UserInfo userInfo = UserInfoUtil.getInstance().getUserInfo();
                        InvokeUtil.setFieldValue(userInfo, paramKeyValue.key, paramKeyValue.value);
                        UserInfoUtil.getInstance().saveUserInfo(userInfo);
                    }
                    ToastUtil.showCenter(this, "修改成功");
                    setResult(RESULT_OK, new Intent());
                    finish();
                }
                dismissProgressDialog();
                break;
        }
    }

    /**
     * 根据 type 设置相关 view
     */
    private void switchView(){
        if(editType == TYPE_EDIT_NICKNAME){ //用户名
            titleTxtView.setText("用户名");
            contentEditTxt.setText(UserInfoUtil.getInstance().getUserInfo().getNickName());
            showSoftInput();
        }else if(editType == TYPE_EDIT_CAREER){ //个人职业
            titleTxtView.setText("个人职业");
            contentEditTxt.setText(SelectCultureDialog.getMenuName(UserInfoUtil.getInstance().getUserInfo().getCareer(), SelectCultureDialog.TYPE_CAREER));
            contentEditTxt.clearFocus();
            contentEditTxt.setFocusable(false);
            contentEditTxt.setFocusableInTouchMode(false);
            contentEditTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSelectCareerDialog();
                }
            });
        }else if(editType == TYPE_EDIT_HEALTH){ //健康情况
            titleTxtView.setText("健康情况");
            showSoftInput();
        }else if(editType == TYPE_EDIT_CONTACT){ //联系方式
            titleTxtView.setText("联系方式");
            contentEditTxt.setText(UserInfoUtil.getInstance().getUserInfo().getMobile());
            contentEditTxt.setInputType(InputType.TYPE_CLASS_NUMBER);
            showSoftInput();
        }else if(editType == TYPE_EDIT_SEX){    //性别
            titleTxtView.setText("性别");
            contentEditTxt.setText(UserInfoUtil.getInstance().getUserInfo().isMan()?"男":"女");
            contentEditTxt.clearFocus();
            contentEditTxt.setFocusable(false);
            contentEditTxt.setFocusableInTouchMode(false);
            contentEditTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSexDialog();
                }
            });
        }else if(editType == TYPE_EDIT_BIRTHDAY){    //生日
            titleTxtView.setText("出生年月");
            contentEditTxt.setText(UserInfoUtil.getInstance().getUserInfo().getDisplayBirthday());
            contentEditTxt.clearFocus();
            contentEditTxt.setFocusable(false);
            contentEditTxt.setFocusableInTouchMode(false);
            contentEditTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSelectDateDialog();
                }
            });
        }else if(editType == TYPE_EDIT_CULTURE){    //文化程度
            titleTxtView.setText("文化程度");
            contentEditTxt.setText(SelectCultureDialog.getMenuName(UserInfoUtil.getInstance().getUserInfo().getEducation()));
            contentEditTxt.clearFocus();
            contentEditTxt.setFocusable(false);
            contentEditTxt.setFocusableInTouchMode(false);
            contentEditTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSelectCultureDialog();
                }
            });
        }
    }

    /**
     * 显示 sexDialog
     */
    private void showSexDialog(){
        if(sexDialog == null){
            sexDialog = new SexDialog(PersonalEditActivity.this);
            sexDialog.setSex(UserInfoUtil.getInstance().getUserInfo().isMan() ? "男" : "女");
            sexDialog.setOnSexClickListener(new SexDialog.OnSexClickListener() {
                @Override
                public void onOk(boolean isMan) {
                    contentEditTxt.setText(isMan ? "男" : "女");
                }
            });
        }
        sexDialog.show();
    }

    /**
     * 显示 showSelectDateDialog
     */
    private void showSelectDateDialog(){
        if(selectDateDialog == null){
            selectDateDialog = new SelectDateDialog(PersonalEditActivity.this);
            selectDateDialog.setDayVisibility(View.GONE);
            selectDateDialog.setYear(UserInfoUtil.getInstance().getUserInfo().getBirthdayYear());
            selectDateDialog.setMonth(UserInfoUtil.getInstance().getUserInfo().getBirthdayMonth());
            selectDateDialog.setOnSelectClickListener(new SelectDateDialog.OnSelectClickListener() {
                @Override
                public void onOk(String year, String month, String day, String format) {
                    contentEditTxt.setText(year+"年"+month+"月");
                }

                @Override
                public void onCancel() {

                }
            });
        }
        selectDateDialog.show();
    }

    /**
     * 显示 showSelectDateDialog
     */
    private void showSelectCultureDialog(){
        if(selectCultureDialog == null){
            selectCultureDialog = new SelectCultureDialog(PersonalEditActivity.this);
            selectCultureDialog.setSelectedMenu(UserInfoUtil.getInstance().getUserInfo().getEducation());
            selectCultureDialog.setOnSelectClickListener(new SelectCultureDialog.OnSelectClickListener() {
                @Override
                public void onOk(WheelMenuInfo wheelMenuInfo) {
                    if(wheelMenuInfo!=null){
                        contentEditTxt.setText(wheelMenuInfo.getName());
                    }
                }
            });
        }
        selectCultureDialog.show();
    }

    /**
     * 显示 showSelectCareerDialog
     */
    private void showSelectCareerDialog(){
        if(selectCareerDialog == null){
            selectCareerDialog = new SelectCultureDialog(PersonalEditActivity.this, SelectCultureDialog.TYPE_CAREER);
            selectCareerDialog.setSelectedMenu(UserInfoUtil.getInstance().getUserInfo().getCareer());
            selectCareerDialog.setOnSelectClickListener(new SelectCultureDialog.OnSelectClickListener() {
                @Override
                public void onOk(WheelMenuInfo wheelMenuInfo) {
                    if(wheelMenuInfo!=null){
                        contentEditTxt.setText(wheelMenuInfo.getName());
                    }
                }
            });
        }
        selectCareerDialog.show();
    }

    /**
     * 显示软键盘
     */
    private void showSoftInput(){
        contentEditTxt.requestFocus();
        InputMethodUtil.showSoftInput(this, contentEditTxt);
    }

    /**
     * 取消
     */
    @OnClick(R.id.leftTxtView)
    public void cancelClick(){
        backClick(null);
    }

    /**
     * 确定
     */
    @OnClick(R.id.tv_right)
    public void okClick(){
        if(TextUtils.isEmpty(contentEditTxt.getText().toString())){
            ToastUtil.showCenter(this, "请输入或选择");
            return ;
        }
        ParamKeyValue paramKeyValue = new ParamKeyValue();
        paramKeyValue.value = contentEditTxt.getText().toString();
        if(editType == TYPE_EDIT_NICKNAME){ //用户名
            paramKeyValue.key = "nickName";
        }else if(editType == TYPE_EDIT_CAREER){ //个人职业
            paramKeyValue.key = "career";
            if(selectCareerDialog==null || selectCareerDialog.getSelectedMenu() == null){
                paramKeyValue.value = UserInfoUtil.getInstance().getUserInfo().getCareer();
            }else{
                paramKeyValue.value = selectCareerDialog.getSelectedMenu().getCode();
            }
        }else if(editType == TYPE_EDIT_HEALTH){ //健康情况
        }else if(editType == TYPE_EDIT_CONTACT){ //联系方式
        }else if(editType == TYPE_EDIT_SEX){    //性别
            paramKeyValue.key = "sex";
            paramKeyValue.value = "男".equals(paramKeyValue.value) ? "1" : "0";
        }else if(editType == TYPE_EDIT_BIRTHDAY){    //生日
            paramKeyValue.key = "birthday";
            if(selectDateDialog == null){
                paramKeyValue.value = UserInfoUtil.getInstance().getUserInfo().getBirthday();
            }else{
                paramKeyValue.value = selectDateDialog.getYear()+","+selectDateDialog.getMonth();
            }
        }else if(editType == TYPE_EDIT_CULTURE){    //文化程度
            paramKeyValue.key = "education";
            if(selectCultureDialog == null || selectCultureDialog.getSelectedMenu() == null){
                paramKeyValue.value = UserInfoUtil.getInstance().getUserInfo().getEducation();
            }else{
                paramKeyValue.value = selectCultureDialog.getSelectedMenu().getCode();
            }
        }

        if(!TextUtils.isEmpty(paramKeyValue.key) && !TextUtils.isEmpty(paramKeyValue.value)){
            showProgressDialog();
            accountRestUsage.updateUserInfo(REQUEST_UPDATE_USER_INFO_TASK_ID, paramKeyValue.key, paramKeyValue.value, paramKeyValue);
        }
    }

    class ParamKeyValue{
        String key;
        String value;
    }

}
