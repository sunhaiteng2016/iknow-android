package com.beyond.library.sharesdk;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.view.KeyEvent;
import android.view.View;

import com.beyond.library.sharesdk.shareInfo.BaseShareFields;
import com.mob.tools.FakeActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;

public class PlatformListFakeActivity extends FakeActivity {
    protected HashMap<String, Object> shareParamsMap;
    protected HashMap<String, BaseShareFields> mixFields;
    protected boolean silent;
    protected ArrayList<CustomerLogo> customerLogos;
    protected HashMap<String, String> hiddenPlatforms;
    private boolean canceled = false;
    protected View backgroundView;
    protected SpannableString shareTip;
    //Special for SharedProfitMode
    protected boolean shareProfitMode;

    protected OnShareButtonClickListener onShareButtonClickListener;
    protected IndirectListener indirectListener;
    protected IShareStatistics shareStatistics;

    protected boolean dialogMode = false;
    protected ThemeShareCallback themeShareCallback;

    public interface OnShareButtonClickListener {
        void onClick(View v, List<Platform> checkPlatforms);
    }

    public interface IndirectListener {
        void onClick(List<Platform> checkPlatform);
    }

    public interface OnResultListener{
        void onSuccess(Platform platform);
        void onError(Platform platform);
        void onCancel(Platform platform);
    }

    /**
     * 分享统计接口
     */
    public interface IShareStatistics {
        void statistics(List<Platform> checkedPlatforms);
    }

    public void onCreate() {
        super.onCreate();

        canceled = false;

        if (themeShareCallback == null) {
            finish();
        }
    }

    public void setShareProfitMode(boolean shareProfitMode) {
        this.shareProfitMode = shareProfitMode;
    }

    public void setShareTip(SpannableString shareTip) {
        this.shareTip = shareTip;
    }

    public boolean onKeyEvent(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            canceled = true;
        }
        return super.onKeyEvent(keyCode, event);
    }

    protected void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public boolean onFinish() {

        // 取消分享菜单的统计
        if (canceled) {
            ShareSDK.logDemoEvent(2, null);
        }

        ShareUtil.onResult(!canceled);
        return super.onFinish();
    }

    @Override
    public void show(Context context, Intent i) {
        super.show(context, i);
    }

//	public HashMap<String, Object> getShareParamsMap() {
//		return shareParamsMap;
//	}

//	public void setShareParamsMap(HashMap<String, Object> shareParamsMap) {
//		this.shareParamsMap = shareParamsMap;
//	}

    public void setMixFields(HashMap<String, BaseShareFields> mixFields) {
        this.mixFields = mixFields;
    }

    public boolean isSilent() {
        return silent;
    }

    public void setSilent(boolean silent) {
        this.silent = silent;
    }

    public ArrayList<CustomerLogo> getCustomerLogos() {
        return customerLogos;
    }

    public void setCustomerLogos(ArrayList<CustomerLogo> customerLogos) {
        this.customerLogos = customerLogos;
    }

    public HashMap<String, String> getHiddenPlatforms() {
        return hiddenPlatforms;
    }

    public void setHiddenPlatforms(HashMap<String, String> hiddenPlatforms) {
        this.hiddenPlatforms = hiddenPlatforms;
    }

    public View getBackgroundView() {
        return backgroundView;
    }

    public void setBackgroundView(View backgroundView) {
        this.backgroundView = backgroundView;
    }

    public OnShareButtonClickListener getOnShareButtonClickListener() {
        return onShareButtonClickListener;
    }

    public void setOnShareButtonClickListener(OnShareButtonClickListener onShareButtonClickListener) {
        this.onShareButtonClickListener = onShareButtonClickListener;
    }

    public void setIShareStatistics(IShareStatistics shareStatistics) {
        this.shareStatistics = shareStatistics;
    }

    public void setIndirectListener(IndirectListener indirectListener) {
        this.indirectListener = indirectListener;
    }

    public boolean isDialogMode() {
        return dialogMode;
    }

    public void setDialogMode(boolean dialogMode) {
        this.dialogMode = dialogMode;
    }

    public ThemeShareCallback getThemeShareCallback() {
        return themeShareCallback;
    }

    public void setThemeShareCallback(ThemeShareCallback themeShareCallback) {
        this.themeShareCallback = themeShareCallback;
    }

    protected void onShareButtonClick(View v, List<Platform> checkedPlatforms) {
        if (shareStatistics != null) {
            shareStatistics.statistics(checkedPlatforms);
        }
        if (indirectListener != null) {
            indirectListener.onClick(checkedPlatforms);
            finish();
            return;
        }

        if (onShareButtonClickListener != null) {
            onShareButtonClickListener.onClick(v, checkedPlatforms);
        }

        HashMap<Platform, HashMap<String, Object>> silentShareData = new HashMap<Platform, HashMap<String, Object>>();

        Platform plat;
        HashMap<String, Object> shareParamsMap;
        HashMap<String, Object> shareParam;
        for (Object item : checkedPlatforms) {
            plat = (Platform) item;
            String name = plat.getName();
            shareParamsMap = mixFields.get(name).getShareParamsMap();

            // EditPage不支持微信平台、Google+、QQ分享、Pinterest、信息和邮件，总是执行直接分享
            if (silent || ShareCore.isDirectShare(plat)) {
                shareParam = new HashMap<String, Object>(shareParamsMap);
                shareParam.put("platform", name);
                silentShareData.put(plat, shareParam);
            }
        }

        /********************************************************************************************************
         * Here is the real send request to share
         ********************************************************************************************************/
        if (silentShareData.size() > 0) {
            themeShareCallback.doShare(silentShareData);
        }
        // 兼容个别情况不回调onfinish的情况
        ShareUtil.onResult(true);
        finish();
    }
}
