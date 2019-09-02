package com.beyond.popscience.module.mservice;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.SquareRestUsage;
import com.beyond.popscience.frame.pojo.AuthResult;
import com.beyond.popscience.frame.util.D;

/**
 * 认证工具类
 * Created by danxiang.feng on 2017/10/12.
 */

public class CheckAuthTool extends BaseActivity {

    private final int REQ_AUTH_STATUS = 2001;
    private static final String EXTRA_KEY_CLASS = "class";
    private static final String EXTRA_KEY_CHECKAUTHTYPE = "checkAuthType";

    public static final String AUTH_RESULT = "authResult";

    @Request
    private SquareRestUsage restUsage;


    private static Context srcContext = null;
    private Class<?> destClass = null;
    private int checkAuthType;  //验证类型   0：任意一个就行  1： 必须个人认证   2：必须企业认证  3：两者都必须验证
    private Bundle paramsBundle; //参数
    public static AuthResult result;

    /**
     * @param context 默认  任意一个就行
     * @param clz     认证后跳转页面
     * @param bundle  参数
     */
    public static void startActivity(Context context, Class<?> clz, Bundle bundle) {
        startActivity(context, clz, 0, bundle);
    }

    /**
     * @param context
     * @param clz           认证后跳转页面
     * @param checkAuthType 验证类型   0：任意一个就行  1： 只需个人认证   2：只需企业认证  3：两者都必须验证
     * @param bundle        参数
     */
    public static void startActivity(Context context, Class<?> clz, int checkAuthType, Bundle bundle) {
        srcContext = context;
        Intent intent = new Intent(context, CheckAuthTool.class);
        intent.putExtra(EXTRA_KEY_CLASS, clz);
        intent.putExtra(EXTRA_KEY_CHECKAUTHTYPE, checkAuthType);
        if(bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        destClass = (Class<?>) getIntent().getSerializableExtra(EXTRA_KEY_CLASS);
        checkAuthType = getIntent().getIntExtra(EXTRA_KEY_CHECKAUTHTYPE, 0);
        paramsBundle = getIntent().getExtras();

        checkAuth();
    }

    @Override
    protected int getLayoutResID() {
        return View.NO_ID;
    }

    private void checkAuth() {
        showProgressDialogNoCancel();
        restUsage.getAuthStatus(REQ_AUTH_STATUS);
    }

    private void dealWithAuthResult() {
        switch (checkAuthType) {
            case 0:  //0：任意一个就行
                if ("0".equals(result.getCompany()) || "0".equals(result.getPeople())) {
                    alreadyAuth();
                } else {
                    showAuthDialog();
                }
                break;
            case 1:  //1： 必须个人认证
                if ("0".equals(result.getPeople())) {
                    alreadyAuth();
                } else {
                    showAuthOneDialog();
                }
                break;
            case 2:  //2：必须企业认证
                if ("0".equals(result.getCompany())) {
                    alreadyAuth();
                } else {
                    showAuthOneDialog();
                }
                break;
            case 3:  //3：两者都必须验证
                if ("0".equals(result.getCompany()) && "0".equals(result.getPeople())) {
                    alreadyAuth();
                } else {
                    showAuthDialog();
                }
                break;
        }

    }

    private void alreadyAuth() {
        if (srcContext != null && destClass != null) {
            Intent intent = new Intent(srcContext, destClass);
            intent.putExtra(AUTH_RESULT, result);
            if(paramsBundle != null) {
                intent.putExtras(paramsBundle);
            }
            startActivity(intent);
            srcContext = null;
            destClass = null;
        }
        backNoAnim();
    }

    private void showAuthOneDialog() {
        String rightTxt = checkAuthType == 1 ? "个人认证" : "企业认证";
        D.show(this, null, "发布前请先进行认证", "取消", rightTxt, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    if (checkAuthType == 1) {   //个人认证
                        if ("0".equals(result.getPeople())) {
                            ToastUtil.showCenter(CheckAuthTool.this, "认证审核中");
                        } else if ("1".equals(result.getPeople())){
                            ToastUtil.showCenter(CheckAuthTool.this, "认证失败");
                        }
                        else {
                            PersonalCertificationActivity.startActivity(srcContext);
                        }
                    } else {   //企业认证
                        if ("0".equals(result.getCompany())) {
                            ToastUtil.showCenter(CheckAuthTool.this, "认证审核中");
                            backNoAnim();
                        } else if ("1".equals(result.getCompany())){
                            ToastUtil.showCenter(CheckAuthTool.this, "认证失败");
                        }else {
                            CompanyCertificationActivity.startActivity(srcContext);
                        }
                    }
                }
                backNoAnim();
            }
        });
    }

    private void showAuthDialog() {
        D.show(this, true, null, "发布前请先进行认证", "个人认证", "企业认证", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {  //企业认证
                    if ("0".equals(result.getCompany())) {
                        ToastUtil.showCenter(CheckAuthTool.this, "已经认证");
                        backNoAnim();
                    } else if ("1".equals(result.getCompany())) {
                        ToastUtil.showCenter(CheckAuthTool.this, "认证审核中");
                        backNoAnim();
                    } else {
                        CompanyCertificationActivity.startActivity(srcContext);
                    }
                } else {
                    if ("0".equals(result.getPeople())) {
                        ToastUtil.showCenter(CheckAuthTool.this, "已经认证");

                    } else if ("1".equals(result.getPeople())) {
                        ToastUtil.showCenter(CheckAuthTool.this, "认证审核中");
                        backNoAnim();
                    } else {
                        PersonalCertificationActivity.startActivity(srcContext);
                    }
                }
            }
        }).setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (!CheckAuthTool.this.isFinishing()) {
                    backNoAnim();
                }
            }
        });
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        dismissProgressDialog();
        switch (taskId) {
            case REQ_AUTH_STATUS:
                if (msg.getIsSuccess()) {
                    result = (AuthResult) msg.getObj();
                    if (result != null) {
                        dealWithAuthResult();
                        return;
                    }
                }
                backNoAnim();
                break;
        }
    }
}
