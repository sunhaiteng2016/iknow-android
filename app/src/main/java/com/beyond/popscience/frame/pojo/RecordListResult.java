package com.beyond.popscience.frame.pojo;

import java.util.List;

/**
 * Created by Administrator on 2018/5/9 0009.
 *
 * 积分商城  兑换记录
 */

public class RecordListResult extends BaseList {

    private List<RecordDetail> data;

    public List<RecordDetail> getData() {
        return data;
    }

    public void setData(List<RecordDetail> data) {
        this.data = data;
    }
}
