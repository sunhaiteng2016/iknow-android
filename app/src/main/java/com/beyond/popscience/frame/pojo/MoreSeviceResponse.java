package com.beyond.popscience.frame.pojo;

import java.util.List;

/**
 * Created by zhouhuakang on 2017/9/13.
 */

public class MoreSeviceResponse extends BaseObject{
    private List<MoreService> list;

    public List<MoreService> getList() {
        return list;
    }

    public void setList(List<MoreService> list) {
        this.list = list;
    }
}
