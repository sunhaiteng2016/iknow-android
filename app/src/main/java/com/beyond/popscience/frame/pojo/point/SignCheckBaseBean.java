package com.beyond.popscience.frame.pojo.point;

import com.beyond.popscience.frame.pojo.BaseObject;

import java.util.List;

public class SignCheckBaseBean extends BaseObject {
    private String code;
    private String message;
    private List<SignCheckBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<SignCheckBean> getData() {
        return data;
    }

    public void setData(List<SignCheckBean> data) {
        this.data = data;
    }
}
