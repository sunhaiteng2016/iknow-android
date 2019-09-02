package com.beyond.popscience.frame.pojo;

/**
 * Created by Administrator on 2017/11/11 0011.
 */

public class GroupInfoBean {

    private String storeId;
    private String name;
    private String logo;
    private boolean isChoosed;

    public GroupInfoBean(String storeId, String name, String logo, boolean isChoosed) {
        this.storeId = storeId;
        this.name = name;
        this.logo = logo;
        this.isChoosed = isChoosed;

    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }
}
