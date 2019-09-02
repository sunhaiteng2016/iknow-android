package com.beyond.popscience.module.home.fragment.view;



import java.io.Serializable;

public class ContactBean implements Comparable<ContactBean>, Serializable {
    private String pinyin;
    public String name;
    public PinYinStyle pinYinStyle = new PinYinStyle();
    //使用成员变量生成构造方法：alt+shift+s->o

    private int userid;
    private String nickname;
    private String mobile;
    private String address;
    private String headImg;
    private int type;
    private int flag1;
    private int flag2;

    public int getFlag1() {
        return flag1;
    }

    public void setFlag1(int flag1) {
        this.flag1 = flag1;
    }

    public int getFlag2() {
        return flag2;
    }

    public void setFlag2(int flag2) {
        this.flag2 = flag2;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    //是否选择
    public boolean check = false;

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public ContactBean(String name) {
        super();
        this.name = name;
        //一开始就转化好拼音
        setPinyin(PinYinUtil.getPinyin(name));
    }

    @Override
    public int compareTo(ContactBean another) {
        return getPinyin().compareTo(another.getPinyin());
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
