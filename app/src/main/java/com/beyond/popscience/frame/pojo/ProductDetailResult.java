package com.beyond.popscience.frame.pojo;

import com.beyond.popscience.frame.pojo.point.IndexGoods;

/**
 * Created by Administrator on 2018/5/9 0009.
 */

public class ProductDetailResult extends BaseObject {
    private IndexGoods st;
    private float usercore;

    public IndexGoods getSt() {
        return st;
    }

    public void setSt(IndexGoods st) {
        this.st = st;
    }

    public float getUsercore() {
        return usercore;
    }

    public void setUsercore(float usercore) {
        this.usercore = usercore;
    }

}
