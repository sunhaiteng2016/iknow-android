package com.beyond.library.sharesdk.shareInfo;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;

import java.util.HashMap;

import static com.mob.tools.utils.BitmapHelper.captureView;

/**
 * Created by york on 2015/8/12.
 *
 * @author york.hao
 */
public class BaseShareFields {

    public static final String FIELDS_TITLE = "title";
    public final String FIELDS_TITLE_URL = "titleUrl";
    public final String FIELDS_URL = "url";
    public final String FIELDS_TEXT = "text";
    public final String FIELDS_ADDRESS = "address";
    public final String FIELDS_FILEPATH = "filePath";
    public final String FIELDS_COMMENT = "comment";
    public final String FIELDS_SITE = "site";
    public final String FIELDS_SITEURL = "siteUrl";
    public final String FIELDS_VENUENAME = "venueName";
    public final String FIELDS_VENUE_DESCRIPTION = "venueDescription";
    public final String FIELDS_LATITUDE = "latitude";
    public final String FIELDS_LONGITUDE = "longitude";
    public final String FIELDS_PLATFORM = "platform";
    public final String FIELDS_INSTALLURL = "installurl";
    public final String FIELDS_EXECUTEURL = "executeurl";
    public final String FIELDS_MUSICURL = "musicUrl";
    public static final String FIELDS_IMAGE_PATH = "imagePath";
    public static final String FIELDS_IMAGE_URL = "imageUrl";
    public static final String FIELDS_IMAGE_DATA = "imageData";
    public static final String FIELDS_IMAGE_ARRAY = "imageArray";
    public final String FIELDS_IS_SHARE_TENCENTWEIBO = "isShareTencentWeibo";
    public final String FIELDS_VIEW_TO_SHARE = "viewToShare";
    public final String FIELDS_DIALOG_MODE = "dialogMode";

    private HashMap<String, Object> shareParamsMap;

    public BaseShareFields() {
        shareParamsMap = new HashMap<>();
    }

    public HashMap<String, Object> getShareParamsMap() {
        return shareParamsMap;
    }

    /**
     * title标题，在印象笔记、邮箱、信息、微信（包括好友、朋友圈和收藏）、
     * 易信（包括好友、朋友圈）、人人网和QQ空间使用，否则可以不提供
     */
    public void setTitle(String title) {
        shareParamsMap.put(FIELDS_TITLE, title);
    }

    /**
     * title标题
     *
     * @return
     */
    public String getTitle() {
        return shareParamsMap.containsKey(FIELDS_TITLE) ? String.valueOf(shareParamsMap.get(FIELDS_TITLE)) : null;
    }

    /**
     * titleUrl是标题的网络链接，仅在人人网和QQ空间使用，否则可以不提供
     */
    public void setTitleUrl(String titleUrl) {
        shareParamsMap.put(FIELDS_TITLE_URL, titleUrl);
    }

    /**
     * text是分享文本，所有平台都需要这个字段
     */
    public void setText(String text) {
        shareParamsMap.put(FIELDS_TEXT, text);
    }

    /**
     * 获取text字段的值
     */
    public String getText() {
        return shareParamsMap.containsKey(FIELDS_TEXT) ? String.valueOf(shareParamsMap.get(FIELDS_TEXT)) : null;
    }

    /**
     * imagePath是本地的图片路径，除Linked-In外的所有平台都支持这个字段
     */
    public void setImagePath(String imagePath) {
        if (!TextUtils.isEmpty(imagePath))
            shareParamsMap.put(FIELDS_IMAGE_PATH, imagePath);
    }

    /**
     * @return
     */
    public String getImagePath() {
        return shareParamsMap.containsKey(FIELDS_IMAGE_PATH) ? String.valueOf(shareParamsMap.get(FIELDS_IMAGE_PATH)) : null;
    }

    /**
     * imageUrl是图片的网络路径，新浪微博、人人网、QQ空间和Linked-In支持此字段
     */
    public void setImageUrl(String imageUrl) {
        if (!TextUtils.isEmpty(imageUrl))
            shareParamsMap.put(FIELDS_IMAGE_URL, imageUrl);
    }

    /**
     * @return
     */
    public String getImageUrl() {
        return shareParamsMap.containsKey(FIELDS_IMAGE_URL) ? String.valueOf(shareParamsMap.get(FIELDS_IMAGE_URL)) : null;
    }

    /**
     * imageData
     */
    public void setImageData(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled())
            shareParamsMap.put(FIELDS_IMAGE_DATA, bitmap);
    }

    /**
     * url在微信（包括好友、朋友圈收藏）和易信（包括好友和朋友圈）中使用，否则可以不提供
     */
    public void setUrl(String url) {
        shareParamsMap.put(FIELDS_URL, url);
    }

    /**
     * @return
     */
    public String getUrl() {
        return shareParamsMap.containsKey(FIELDS_URL) ? String.valueOf(shareParamsMap.get(FIELDS_URL)) : null;
    }

    /**
     * filePath是待分享应用程序的本地路劲，仅在微信（易信）好友和Dropbox中使用，否则可以不提供
     */
    public void setFilePath(String filePath) {
        shareParamsMap.put(FIELDS_FILEPATH, filePath);
    }

    /**
     * comment是我对这条分享的评论，仅在人人网和QQ空间使用，否则可以不提供
     */
    public void setComment(String comment) {
        shareParamsMap.put(FIELDS_COMMENT, comment);
    }

    /**
     * site是分享此内容的网站名称，仅在QQ空间使用，否则可以不提供
     */
    public void setSite(String site) {
        shareParamsMap.put(FIELDS_SITE, site);
    }

    /**
     * siteUrl是分享此内容的网站地址，仅在QQ空间使用，否则可以不提供
     */
    public void setSiteUrl(String siteUrl) {
        shareParamsMap.put(FIELDS_SITEURL, siteUrl);
    }

    /**
     * foursquare分享时的地方名
     */
    public void setVenueName(String venueName) {
        shareParamsMap.put(FIELDS_VENUENAME, venueName);
    }

    /**
     * foursquare分享时的地方描述
     */
    public void setVenueDescription(String venueDescription) {
        shareParamsMap.put(FIELDS_VENUE_DESCRIPTION, venueDescription);
    }

    /**
     * 分享地纬度，新浪微博、腾讯微博和foursquare支持此字段
     */
    public void setLatitude(float latitude) {
        shareParamsMap.put(FIELDS_LATITUDE, latitude);
    }

    /**
     * 分享地经度，新浪微博、腾讯微博和foursquare支持此字段
     */
    public void setLongitude(float longitude) {
        shareParamsMap.put(FIELDS_LONGITUDE, longitude);
    }

    /**
     * 设置编辑页的初始化选中平台
     */
    public void setPlatform(String platform) {
        shareParamsMap.put(FIELDS_PLATFORM, platform);
    }

    /**
     * 设置KakaoTalk的应用下载地址
     */
    public void setInstallUrl(String installurl) {
        shareParamsMap.put(FIELDS_INSTALLURL, installurl);
    }

    /**
     * 设置KakaoTalk的应用打开地址
     */
    public void setExecuteUrl(String executeurl) {
        shareParamsMap.put(FIELDS_EXECUTEURL, executeurl);
    }

    /**
     * 设置微信分享的音乐的地址
     */
    public void setMusicUrl(String musicUrl) {
        shareParamsMap.put(FIELDS_MUSICURL, musicUrl);
    }

    /**
     * 腾讯微博分享多张图片
     */
    public void setImageArray(String[] imageArray) {
        shareParamsMap.put(FIELDS_IMAGE_ARRAY, imageArray);
    }

    /**
     * 是否支持QQ,QZone授权登录后发微博
     */
    public void setShareFromQQAuthSupport(boolean shareFromQQLogin) {
        shareParamsMap.put(FIELDS_IS_SHARE_TENCENTWEIBO, shareFromQQLogin);
    }


    /**
     * 设置一个将被截图分享的View , surfaceView是截不了图片的
     */
    public void setViewToShare(View viewToShare) {
        try {
            Bitmap bm = captureView(viewToShare, viewToShare.getWidth(), viewToShare.getHeight());
            shareParamsMap.put(FIELDS_VIEW_TO_SHARE, bm);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
