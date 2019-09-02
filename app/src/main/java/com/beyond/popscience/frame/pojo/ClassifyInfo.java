package com.beyond.popscience.frame.pojo;

import java.util.List;

/**
 * Created by danxiang.feng on 2017/10/12.
 */

public class ClassifyInfo extends BaseObject {

    private String uid;
    private String name;
    private List<ClassifyMenu> menu;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ClassifyMenu> getMenu() {
        return menu;
    }

    public void setMenu(List<ClassifyMenu> menu) {
        this.menu = menu;
    }
}
