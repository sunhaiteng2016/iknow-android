package com.beyond.popscience.frame.pojo;

/**
 * 说明:
 * Created by Administrator on 2018/3/13.
 */

public class DataBean extends BaseObject{
    /**
     * addressId : 1045
     * userId : 125396
     * contactName : 啊啊啊啊啊啊啊啊啊
     * contactPhone : 1899999999999
     * province : 河南54554
     * city : 信阳
     * area : 拱墅区
     * street : 456号街
     * addressDetail : 456号
     * createTime : 1520859344000
     * updateTime : 1520859344000
     * status : 2
     * isDelete : 0
     */

    private int addressId;
    private int userId;
    private String contactName;
    private String contactPhone;
    private String province;
    private String city;
    private String area;
    private String street;
    private String addressDetail;
    private long createTime;
    private long updateTime;
    private int status;
    private int isDelete;

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }
}
