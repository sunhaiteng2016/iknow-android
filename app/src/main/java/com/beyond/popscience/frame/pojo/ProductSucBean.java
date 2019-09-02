package com.beyond.popscience.frame.pojo;

import com.beyond.popscience.frame.pojo.point.IndexGoods;

/**
 * Created by Administrator on 2018/5/10 0010.
 */

public class ProductSucBean extends BaseObject {
    private IndexGoods st;
    private OrderInfoBean orde;

    public IndexGoods getSt() {
        return st;
    }

    public void setSt(IndexGoods st) {
        this.st = st;
    }

    public OrderInfoBean getOrde() {
        return orde;
    }

    public void setOrde(OrderInfoBean orde) {
        this.orde = orde;
    }
}
