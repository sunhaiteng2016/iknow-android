package com.beyond.popscience.frame.pojo;

import com.beyond.popscience.module.home.entity.News;

import java.util.List;

/**
 * 评论
 * Created by linjinfa on 2017/6/10.
 * email 331710168@qq.com
 */
public class Comment extends BaseObject {

    private String praiseNum;
    private String createTime;
    private String nickName;
    private String userId;
    private String avatar;
    private String comment;
    private String commentId;
    private String replyId;
    private String replyNum;
    /**
     * 1:已点赞 0:否
     */
    private String myPraise;

    /**
     * 0：默认评论   1：乡镇评论
     */
    public int appNewsType = 0;
    /**
     * 社团 回复列表
     */
    public List<Comment> replyList;

    /**
     * 是否乡镇评论
     * @return
     */
    public boolean isTownNews(){
        return appNewsType == News.TYPE_TOWN_NEWS;
    }

    public int getAppNewsType() {
        return appNewsType;
    }

    public void setAppNewsType(int appNewsType) {
        this.appNewsType = appNewsType;
    }

    public String getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(String praiseNum) {
        this.praiseNum = praiseNum;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(String replyNum) {
        this.replyNum = replyNum;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public String getMyPraise() {
        return myPraise;
    }

    public void setMyPraise(String myPraise) {
        this.myPraise = myPraise;
    }

    public List<Comment> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<Comment> replyList) {
        this.replyList = replyList;
    }

    /**
     * replyNum long
     * @return
     */
    public long getReplyNumLong(){
        try {
            return Long.parseLong(replyNum);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
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

}
