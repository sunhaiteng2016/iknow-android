package com.beyond.popscience.frame.pojo;

import java.util.List;

/**
 * Created by danxiang.feng on 2017/10/12.
 */

public class ClassifyResponse extends BaseObject {

    private List<ClassifyInfo> category;
    private List<String> list;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public List<ClassifyInfo> getCategory() {
        return category;
    }

    public void setCategory(List<ClassifyInfo> category) {
        this.category = category;
    }
}
