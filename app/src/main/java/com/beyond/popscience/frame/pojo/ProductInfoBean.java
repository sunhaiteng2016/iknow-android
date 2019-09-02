package com.beyond.popscience.frame.pojo;

/**
 * Created by Administrator on 2017/11/11 0011.
 */

public class ProductInfoBean {

    /**
     * cartid : 21
     * goods_name : 测试商品5
     * price : 100.00
     * logo_pic : http://txl.unohacha.com/Public/Wxin/Images/ind_jf_2.jpg
     * num : 3
     * goods_id : 5
     * sku_id :
     * store_id : 8
     * store : 100
     */

    private String cartid;
    private String goods_name;
    private String price;
    private String logo_pic;
    private String num;
    private String goods_id;
    private String sku_id;
    private String store_id;
    private String store;
    private String sku_name;
    private boolean isChecked;
    private String goods_type;
    private String type_status;

    public ProductInfoBean(String cartid, String goods_name, String price, String logo_pic, String num, String goods_id, String sku_id, String store_id, String store, String sku_name, boolean isChecked, String goods_type, String type_status) {
        this.cartid = cartid;
        this.goods_name = goods_name;
        this.price = price;
        this.logo_pic = logo_pic;
        this.num = num;
        this.goods_id = goods_id;
        this.sku_id = sku_id;
        this.store_id = store_id;
        this.store = store;
        this.sku_name = sku_name;
        this.isChecked = isChecked;
        this.goods_type = goods_type;
        this.type_status = type_status;
    }

    public String getType_status() {
        return type_status;
    }

    public void setType_status(String type_status) {
        this.type_status = type_status;
    }

    public String getGoods_type() {
        return goods_type;
    }

    public void setGoods_type(String goods_type) {
        this.goods_type = goods_type;
    }

    public String getCartid() {
        return cartid;
    }

    public void setCartid(String cartid) {
        this.cartid = cartid;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLogo_pic() {
        return logo_pic;
    }

    public void setLogo_pic(String logo_pic) {
        this.logo_pic = logo_pic;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getSku_id() {
        return sku_id;
    }

    public void setSku_id(String sku_id) {
        this.sku_id = sku_id;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getSku_name() {
        return sku_name;
    }

    public void setSku_name(String sku_name) {
        this.sku_name = sku_name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
