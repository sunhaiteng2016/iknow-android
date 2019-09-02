package com.beyond.popscience.locationgoods.bean;

import com.beyond.library.network.net.bean.BaseResponse;

public class SearchUserbean extends BaseResponse {


    /**
     * uid : null
     * communityid : 14
     * userid : 151623
     * type : 2
     * jointime : null
     * nickname : 孙虫子
     * avatar : http://kpnew.appwzd.cn/header/b7dc17bb47a943dc9a0764a93f2a3623.jpg
     */

    private Object uid;
    private int communityid;
    private int userid;
    private int type;
    private Object jointime;
    private String nickname;
    private String avatar;

    public Object getUid() {
        return uid;
    }

    public void setUid(Object uid) {
        this.uid = uid;
    }

    public int getCommunityid() {
        return communityid;
    }

    public void setCommunityid(int communityid) {
        this.communityid = communityid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getJointime() {
        return jointime;
    }

    public void setJointime(Object jointime) {
        this.jointime = jointime;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
