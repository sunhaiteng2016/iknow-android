package com.beyond.popscience.locationgoods.bean;

import com.beyond.library.network.net.bean.BaseResponse;

import java.io.Serializable;
import java.util.List;

public class OrderLsitTwoBean extends BaseResponse implements Serializable {


    /**
     * id : 100079
     * shopId : 10000006
     * userId : 151623
     * orderSn : 201905122034324320001
     * pinglunStatus : 0
     * createTime : 2019-05-12 20:33:59
     * totalAmount : 0.01
     * freightAmount : 0.01
     * payAmount : 0.02
     * payType : 1
     * status : 2
     * orderType : 0
     * deliveryCompany : 顺丰
     * deliverySn : 231580571635
     * autoConfirmDay : 10
     * deliveryStatus : 2
     * receiverName : 孙海腾11
     * receiverPhone : 13067781520
     * receiverProvince : 浙江省
     * receiverCity : 台州市
     * receiverRegion : 临海市
     * receiverDetailAddress : 详细地址
     * note :
     * confirmStatus : 0
     * deleteStatus : 0
     * paymentTime : 2019-05-12 20:34:28
     * deliveryTime : 2019-05-12 20:35:50
     * modifyTime : 2019-05-12 20:35:16
     * itemList : [{"id":100077,"orderId":100079,"orderSn":"201905122034324320001","productId":10000055,"productPic":"http://ikow.oss-cn-shanghai.aliyuncs.com/images/c853cb32-49b0-4447-9721-dd0fe53f5de1.jpg","productName":"看看咯","productPrice":0.01,"productQuantity":1,"productSkuId":64,"productCategory":"杨梅","sp1":"考虑考虑,看看咯,恐龙妹,","createTime":"2019-05-12 20:34:33"}]
     * shopName : 店铺名称
     * shopImg : http://ikow.oss-cn-shanghai.aliyuncs.com/images/d3368b3e-222e-4e63-9d94-84a36b531133.jpg
     */

    private int id;
    private int shopId;
    private int userId;
    private String orderSn;
    private int pinglunStatus;
    private String createTime;
    private double totalAmount;
    private double freightAmount;
    private double payAmount;
    private int payType;
    private int status;
    private int orderType;
    private String deliveryCompany;
    private String deliverySn;
    private int autoConfirmDay;
    private int deliveryStatus;
    private String receiverName;
    private String receiverPhone;
    private String receiverProvince;
    private String receiverCity;
    private String receiverRegion;
    private String receiverDetailAddress;
    private String note;
    private int confirmStatus;
    private int deleteStatus;
    private String paymentTime;
    private String deliveryTime;
    private String modifyTime;
    private String shopName;
    private String shopImg;
    private List<ItemListBean> itemList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public int getPinglunStatus() {
        return pinglunStatus;
    }

    public void setPinglunStatus(int pinglunStatus) {
        this.pinglunStatus = pinglunStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getFreightAmount() {
        return freightAmount;
    }

    public void setFreightAmount(double freightAmount) {
        this.freightAmount = freightAmount;
    }

    public double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(double payAmount) {
        this.payAmount = payAmount;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getDeliveryCompany() {
        return deliveryCompany;
    }

    public void setDeliveryCompany(String deliveryCompany) {
        this.deliveryCompany = deliveryCompany;
    }

    public String getDeliverySn() {
        return deliverySn;
    }

    public void setDeliverySn(String deliverySn) {
        this.deliverySn = deliverySn;
    }

    public int getAutoConfirmDay() {
        return autoConfirmDay;
    }

    public void setAutoConfirmDay(int autoConfirmDay) {
        this.autoConfirmDay = autoConfirmDay;
    }

    public int getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(int deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverProvince() {
        return receiverProvince;
    }

    public void setReceiverProvince(String receiverProvince) {
        this.receiverProvince = receiverProvince;
    }

    public String getReceiverCity() {
        return receiverCity;
    }

    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity;
    }

    public String getReceiverRegion() {
        return receiverRegion;
    }

    public void setReceiverRegion(String receiverRegion) {
        this.receiverRegion = receiverRegion;
    }

    public String getReceiverDetailAddress() {
        return receiverDetailAddress;
    }

    public void setReceiverDetailAddress(String receiverDetailAddress) {
        this.receiverDetailAddress = receiverDetailAddress;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(int confirmStatus) {
        this.confirmStatus = confirmStatus;
    }

    public int getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(int deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopImg() {
        return shopImg;
    }

    public void setShopImg(String shopImg) {
        this.shopImg = shopImg;
    }

    public List<ItemListBean> getItemList() {
        return itemList;
    }

    public void setItemList(List<ItemListBean> itemList) {
        this.itemList = itemList;
    }

    public static class ItemListBean implements Serializable {
        /**
         * id : 100077
         * orderId : 100079
         * orderSn : 201905122034324320001
         * productId : 10000055
         * productPic : http://ikow.oss-cn-shanghai.aliyuncs.com/images/c853cb32-49b0-4447-9721-dd0fe53f5de1.jpg
         * productName : 看看咯
         * productPrice : 0.01
         * productQuantity : 1
         * productSkuId : 64
         * productCategory : 杨梅
         * sp1 : 考虑考虑,看看咯,恐龙妹,
         * createTime : 2019-05-12 20:34:33
         */

        private int id;
        private int orderId;
        private String orderSn;
        private int productId;
        private String productPic;
        private String productName;
        private double productPrice;
        private int productQuantity;
        private int productSkuId;
        private String productCategory;
        private String sp1;
        private String createTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public String getOrderSn() {
            return orderSn;
        }

        public void setOrderSn(String orderSn) {
            this.orderSn = orderSn;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public String getProductPic() {
            return productPic;
        }

        public void setProductPic(String productPic) {
            this.productPic = productPic;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public double getProductPrice() {
            return productPrice;
        }

        public void setProductPrice(double productPrice) {
            this.productPrice = productPrice;
        }

        public int getProductQuantity() {
            return productQuantity;
        }

        public void setProductQuantity(int productQuantity) {
            this.productQuantity = productQuantity;
        }

        public int getProductSkuId() {
            return productSkuId;
        }

        public void setProductSkuId(int productSkuId) {
            this.productSkuId = productSkuId;
        }

        public String getProductCategory() {
            return productCategory;
        }

        public void setProductCategory(String productCategory) {
            this.productCategory = productCategory;
        }

        public String getSp1() {
            return sp1;
        }

        public void setSp1(String sp1) {
            this.sp1 = sp1;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}
