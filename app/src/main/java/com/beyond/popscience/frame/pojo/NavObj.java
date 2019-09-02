package com.beyond.popscience.frame.pojo;

import android.text.TextUtils;

import com.beyond.popscience.module.home.entity.Address;

/**
 * Created by lenovo on 2017/6/14.
 */

public class NavObj extends BaseObject {

    private String classId;
    private String className;
    private int fiexed; //是否订阅 1 固定 0 不固定
    /**
     *
     */
    private Address address;

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getFiexed() {
        return fiexed;
    }

    public void setFiexed(int fiexed) {
        this.fiexed = fiexed;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * 是否是固定标签
     * @return
     */
    public boolean isFiexed(){
        return 1== fiexed;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NavObj ){
            NavObj nav = (NavObj)obj;
            return TextUtils.equals(this.getClassId(),nav.getClassId());
        }
        return super.equals(obj);
    }
}
