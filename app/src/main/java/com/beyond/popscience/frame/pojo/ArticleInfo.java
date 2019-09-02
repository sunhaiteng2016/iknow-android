package com.beyond.popscience.frame.pojo;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by linjinfa on 2017/7/20.
 * email 331710168@qq.com
 */
public class ArticleInfo extends BaseObject {

    private String nickName;
    private String userId;
    private String avatar;
    private String content;
    /**
     * 	1:已点赞 0:否
     */
    private String myPraise;
    private String detailPics;
    private String replyNum;
    private String praiseNum;
    private String articleId;
    private String publishTime;
    private String communityName;
    private int type;
    private List<Comment> commentList;
    private String url;
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 0：社团圈子   1：乡镇圈子
     */
    private int appType = 0;

    public int getAppType() {
        return appType;
    }

    public void setAppType(int appType) {
        this.appType = appType;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMyPraise() {
        return myPraise;
    }

    public void setMyPraise(String myPraise) {
        this.myPraise = myPraise;
    }

    public String getDetailPics() {
        return detailPics;
    }

    public void setDetailPics(String detailPics) {
        this.detailPics = detailPics;
    }

    public String getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(String replyNum) {
        this.replyNum = replyNum;
    }

    public String getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(String praiseNum) {
        this.praiseNum = praiseNum;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    /**
     *
     * @return
     */
    public boolean isTown(){
       return appType == 1;
    }

    /**
     * 是否已经点赞过
     * @return
     */
    public boolean isPraised(){
        return "1".equals(myPraise);
    }

    /**
     * 设置是否点赞过
     * @param isPraised
     */
    public void setPraised(boolean isPraised){
        myPraise = isPraised ? "1" : "0";
    }

    /**
     *
     * @return
     */
    public long getPraiseNumLong(){
        try {
            return Long.parseLong(praiseNum);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取详情图片
     * @return
     */
    public List<String> getDetailPicList(){
        if(TextUtils.isEmpty(detailPics)){
            return null;
        }
        String imgs[] = detailPics.split(",");
        if(imgs!=null && imgs.length>0){
            return new ArrayList<String>(Arrays.asList(imgs));
        }
        return null;
    }

}
