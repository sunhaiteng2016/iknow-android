package com.beyond.library.network.net.bean;


import com.beyond.library.network.net.bean.model.Session;
import com.beyond.library.network.net.bean.model.SystemParam;

import java.io.Serializable;

/**
 * Created by East.K on 2016/12/15.
 *
 * @ClassName: BaseRequest
 * @Description: 前端请求结构
 */

public class BaseRequest<T> implements Serializable {
    /********************************************************
     * 签名
     ********************************************************/
    private String sign = null;

    /********************************************************
     * 系统级参数
     ********************************************************/
    private SystemParam system = null;

    /********************************************************
     * 用户会话信息
     ********************************************************/
    private Session session = null;

    /********************************************************
     * 业务入参
     ********************************************************/
    private T data = null;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public SystemParam getSystem() {
        return system;
    }

    public void setSystem(SystemParam system) {
        this.system = system;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
