package com.beyond.popscience.frame.pojo.point;

import com.beyond.popscience.frame.pojo.BaseObject;
import com.beyond.popscience.frame.pojo.GoodsDetail;
import com.beyond.popscience.frame.pojo.GoodsExchangeBean;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;

import java.util.List;

public class IndexDataBean extends BaseObject {
    private List<StBean> st;
    private String town;
    private StuBean stu;
    private List<IndexGoods> list;
    public List<StBean> getSt() {
        return st;
    }

    public void setSt(List<StBean> st) {
        this.st = st;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public StuBean getStu() {
        return stu;
    }

    public void setStu(StuBean stu) {
        this.stu = stu;
    }

    public List<IndexGoods> getList() {
        return list;
    }

    public void setList(List<IndexGoods> list) {
        this.list = list;
    }
}
