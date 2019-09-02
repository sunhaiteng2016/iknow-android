package com.beyond.popscience.module.point;

/**
 * Created by Administrator on 2018/6/5 0005.
 */

public class SHJL {

    /**
     * id : 11
     * userid : 151411
     * mobile : 15823914401
     * account : 15823914401
     * username : 杨杰
     * amount : 30.0
     * createtime : 1528191078000
     * orderout : null
     * status : 1
     */

    private int id;
    private int userid;
    private String mobile;
    private String account;
    private String username;
    private double amount;
    private long createtime;
    private Object orderout;
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public Object getOrderout() {
        return orderout;
    }

    public void setOrderout(Object orderout) {
        this.orderout = orderout;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
