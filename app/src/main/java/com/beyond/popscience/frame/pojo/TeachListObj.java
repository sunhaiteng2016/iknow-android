package com.beyond.popscience.frame.pojo;

import java.util.List;

/**
 * 教学列表
 * Created by yao.cui on 2017/7/17.
 */

public class TeachListObj extends BaseObject {
    private List<TeachItem> teachList;

    public List<TeachItem> getTeachList() {
        return teachList;
    }

    public void setTeachList(List<TeachItem> teachList) {
        this.teachList = teachList;
    }
}
