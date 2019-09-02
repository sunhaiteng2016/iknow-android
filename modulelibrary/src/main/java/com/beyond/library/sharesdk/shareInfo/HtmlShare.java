package com.beyond.library.sharesdk.shareInfo;

import android.text.TextUtils;

import com.beyond.library.sharesdk.ShareSdkConfig;
import com.beyond.library.sharesdk.WebViewShare;
import com.beyond.library.sharesdk.links.Link;

import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.system.text.ShortMessage;

/**
 *
 */
public class HtmlShare implements ICustomShareFields {

    private MutiShareFields mutiFields = null;
    private HashMap<String, WebViewShare> shareMap = null;

    @Override
    public void setSelf(String platformName, BaseShareFields fields) {
        setShareInfo(platformName, fields);
    }

    @Override
    public HashMap<String, BaseShareFields> getMutiMap() {
        return mutiFields.getMutiMap();
    }


    public HtmlShare(List<Platform> platforms, HashMap<String, WebViewShare> shareMap) {
        this.shareMap = shareMap;
        mutiFields = new MutiShareFields(this);
        mutiFields.initData(platforms);
    }

    public void setShareInfo(String key, BaseShareFields fields) {

        WebViewShare obj = shareMap.get(key);

        if (obj == null) {
            return;
        }
        String title = obj.getTitle() == null ? "" : obj.getTitle();
        String message = obj.getDesc() == null ? "" : obj.getDesc();
        String url = obj.getLink() != null ? obj.getLink() : "";
        String imgUrl = obj.getImgUrl() != null ? obj.getImgUrl() : "";

        if (SinaWeibo.NAME.equals(key)) {
            message = title + url;
        } else if (ShortMessage.NAME.equals(key) || Link.NAME.equals(key)) {
            message += url;
        }

        if (!TextUtils.isEmpty(title)) {
            fields.setTitle(title);
        }
        if (!TextUtils.isEmpty(message)) {
            fields.setText(message);
        }
        if (!TextUtils.isEmpty(url)) {
            fields.setTitleUrl(url);
            fields.setUrl(url);
        }
        if (imgUrl == null) {
            setAppLogo(fields);
        } else {
            fields.setImageUrl(imgUrl);
        }
        if (!TextUtils.isEmpty(obj.getImagePath())) {
            fields.setImagePath(obj.getImagePath());
        }
    }

    private void setAppLogo(BaseShareFields fields) {
        String filePath = ShareSdkConfig.getInstance().getLogoPath();
        fields.setImagePath(filePath);
    }
}
