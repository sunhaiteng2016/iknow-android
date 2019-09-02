package com.beyond.library.network.net.bean.model;

import java.io.Serializable;

/**
 * Created by East.K on 2016/12/15.
 *
 * @ClassName: Session
 * @Description: 用户会话数据
 */

public class Session implements Serializable {
    /********************************************************
     * 用户 id
     ********************************************************/
    private long wid = 0;

    /********************************************************
     * 用户令牌,当登录另一设备时,token 改变
     ********************************************************/
    private String token = null;

    public long getWid() {
        return wid;
    }

    public void setWid(long wid) {
        this.wid = wid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
