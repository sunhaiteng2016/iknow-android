package com.beyond.popscience.locationgoods.bean;

import com.beyond.library.network.net.bean.BaseResponse;

import java.io.Serializable;
import java.util.List;

public class OrderlsitBean extends BaseResponse {

    /**
     * id : 100072
     * shopId : 10000005
     * userId : 151623
     * orderSn : 201905112229234700001
     * createTime : 2019-05-11 22:29:23
     * totalAmount : 0.01
     * freightAmount : 0.01
     * payAmount : 0.02
     * payType : 0
     * status : 0
     * orderType : 0
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
     * modifyTime : 2019-05-12 10:07:26
     * itemList : [{"id":100070,"orderId":100072,"orderSn":"201905112229234700001","productId":10000053,"productPic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/f62347fdcad84ae784f323770541edad.jpeg","productName":"杨梅","productPrice":0.01,"productQuantity":1,"productSkuId":41,"productCategory":"杨梅","sp1":"20kg/箱20g/个纸箱","createTime":"2019-05-11 22:29:23"}]
     * shopName : 张三的店
     * shopImg : http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/b355267c96ce412786564d9383b54448.jpeg
     */

    private int id;
    private int shopId;
    private int userId;
    private String orderSn;
    private String createTime;
    private double totalAmount;
    private double freightAmount;
    private double payAmount;
    private int payType;
    private int status;
    private int orderType;
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

    public static class ItemListBean  implements Serializable {
        /**
         * id : 100070
         * orderId : 100072
         * orderSn : 201905112229234700001
         * productId : 10000053
         * productPic : http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/f62347fdcad84ae784f323770541edad.jpeg
         * productName : 杨梅
         * productPrice : 0.01
         * productQuantity : 1
         * productSkuId : 41
         * productCategory : 杨梅
         * sp1 : 20kg/箱20g/个纸箱
         * createTime : 2019-05-11 22:29:23
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
