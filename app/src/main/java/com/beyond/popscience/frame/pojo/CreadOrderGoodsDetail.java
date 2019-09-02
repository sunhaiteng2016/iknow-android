package com.beyond.popscience.frame.pojo;

/**
 * 说明:   生成订单的sku
 * Created by Administrator on 2018/3/14.
 */

public class CreadOrderGoodsDetail extends BaseObject{
    public String productId;
    public int bill_num;
    public String coverPic;

    public CreadOrderGoodsDetail() {
    }

    public CreadOrderGoodsDetail(String productId, int bill_num, String coverPic) {
        this.productId = productId;
        this.bill_num = bill_num;
        this.coverPic = coverPic;
    }
}
