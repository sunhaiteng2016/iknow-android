package com.beyond.popscience.widget.wheelview;

import java.io.Serializable;

/**
 * Created by linjinfa 331710168@qq.com on 2015/9/7.
 */
public class WheelMenuInfo implements Serializable {

    private String code;
    private String name;

    public WheelMenuInfo() {
    }

    public WheelMenuInfo(String name) {
        this.name = name;
    }

    public WheelMenuInfo(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
