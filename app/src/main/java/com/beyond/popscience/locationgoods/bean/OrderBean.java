package com.beyond.popscience.locationgoods.bean;

import com.beyond.library.network.net.bean.BaseResponse;

public class OrderBean extends BaseResponse {


    /**
     * id : 100040
     * shopId : 10000005
     * userId : 151623
     * orderSn : 201905111457301070001
     * totalAmount : 0.01
     * freightAmount : 0.01
     * payAmount : 0.02
     * payType : 0
     * status : 0
     * orderType : 0
     * autoConfirmDay : 10
     * receiverName : 孙海腾11
     * receiverPhone : 13067781520
     * receiverProvince : 浙江省
     * receiverCity : 台州市
     * receiverRegion : 临海市
     * receiverDetailAddress : 详细地址
     * note :
     * confirmStatus : 0
     * deleteStatus : 0
     * modifyTime : 2019-05-11 14:57:30
     * orderItem : {"orderId":100040,"orderSn":"201905111457301070001","productId":10000053,"productPic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/f62347fdcad84ae784f323770541edad.jpeg","productName":"杨梅","productPrice":0.01,"productQuantity":1,"productSkuId":41,"productCategory":"杨梅","sp1":"20kg/箱20g/个纸箱","createTime":"2019-05-11 14:57:30"}
     */

    private int id;
    private int shopId;
    private int userId;
    private String orderSn;
    private double totalAmount;
    private double freightAmount;
    private double payAmount;
    private int payType;
    private int status;
    private int orderType;
    private int autoConfirmDay;
    private String receiverName;
    private String receiverPhone;
    private String receiverProvince;
    private String receiverCity;
    private String receiverRegion;
    private String receiverDetailAddress;
    private String note;
    private int confirmStatus;
    private int deleteStatus;
    private String modifyTime;
    private OrderItemBean orderItem;

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

    public int getAutoConfirmDay() {
        return autoConfirmDay;
    }

    public void setAutoConfirmDay(int autoConfirmDay) {
        this.autoConfirmDay = autoConfirmDay;
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

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public OrderItemBean getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItemBean orderItem) {
        this.orderItem = orderItem;
    }

    public static class OrderItemBean {
        /**
         * orderId : 100040
         * orderSn : 201905111457301070001
         * productId : 10000053
         * productPic : http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/f62347fdcad84ae784f323770541edad.jpeg
         * productName : 杨梅
         * productPrice : 0.01
         * productQuantity : 1
         * productSkuId : 41
         * productCategory : 杨梅
         * sp1 : 20kg/箱20g/个纸箱
         * createTime : 2019-05-11 14:57:30
         */

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
