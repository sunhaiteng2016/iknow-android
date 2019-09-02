package com.beyond.popscience.frame.pojo;

/**
 * 服务商品item
 *
 * Created by yao.cui on 2017/6/23.
 */

public class ServiceGoodsItem extends BaseObject {

    private String realName;
    private String uid;   //技能或任务ID，获取列表时返回的
    private String distance;
    private String classifyName;

    private String createTime;//发布时间
    private String title;//	title
    private String price;//price
    private String coverPic;//coverPic
    private String productId;//商品ID
    private String address;
    /**
     * 1:技能 2:任务 3:出租出售 4求租求购 5商品
     */
    private String appGoodsType;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getAppGoodsType() {
        return appGoodsType;
    }

    public void setAppGoodsType(String appGoodsType) {
        this.appGoodsType = appGoodsType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "ServiceGoodsItem{" +
                "realName='" + realName + '\'' +
                ", uid='" + uid + '\'' +
                ", distance='" + distance + '\'' +
                ", classifyName='" + classifyName + '\'' +
                ", createTime='" + createTime + '\'' +
                ", title='" + title + '\'' +
                ", price='" + price + '\'' +
                ", coverPic='" + coverPic + '\'' +
                ", productId='" + productId + '\'' +
                ", address='" + address + '\'' +
                ", appGoodsType='" + appGoodsType + '\'' +
                '}';
    }
}
