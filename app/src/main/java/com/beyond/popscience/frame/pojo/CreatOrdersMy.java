package com.beyond.popscience.frame.pojo;

/**
 * 说明:
 * Created by Administrator on 2018/3/13.
 */

public class CreatOrdersMy extends BaseObject{

    /**
     * orderInfo : {"orderId":0,"sysAreaId":"","orderAddress":"安徽省安庆市枞阳县扩大朋扩大朋","orderPhone":"18317318630","orderZipcode":"","orderUser":"东东","orderType":1,"orderStatu":0,"orderSubjctId":"","orderPay":0,"orderDes":"","orderDate":1520944454000,"orderCode":"1520944454758","productId":"408","billPrice":1,"billNum":1,"orderLogisticsCode":"","orderLogisticsDate":1520944454000,"orderLogiscompanyCode":"","orderLogiscompanyPrice":0,"sellUserId":34274,"buyUserId":125396,"payType":0,"payEndTime":1520944454000,"endTime":1520944454000,"orderNote":"潘生","billTitle":"无","billDesc":"无","balance":0,"credit":0,"ratio":0.2,"createTime":1520944454000,"isDelete":0}
     */

    private OrderInfoBean orderInfo;

    public OrderInfoBean getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfoBean orderInfo) {
        this.orderInfo = orderInfo;
    }

    public static class OrderInfoBean {
        /**
         * orderId : 0
         * sysAreaId :
         * orderAddress : 安徽省安庆市枞阳县扩大朋扩大朋
         * orderPhone : 18317318630
         * orderZipcode :
         * orderUser : 东东
         * orderType : 1
         * orderStatu : 0
         * orderSubjctId :
         * orderPay : 0
         * orderDes :
         * orderDate : 1520944454000
         * orderCode : 1520944454758
         * productId : 408
         * billPrice : 1
         * billNum : 1
         * orderLogisticsCode :
         * orderLogisticsDate : 1520944454000
         * orderLogiscompanyCode :
         * orderLogiscompanyPrice : 0
         * sellUserId : 34274
         * buyUserId : 125396
         * payType : 0
         * payEndTime : 1520944454000
         * endTime : 1520944454000
         * orderNote : 潘生
         * billTitle : 无
         * billDesc : 无
         * balance : 0
         * credit : 0
         * ratio : 0.2
         * createTime : 1520944454000
         * isDelete : 0
         */

        private int orderId;
        private String sysAreaId;
        private String orderAddress;
        private String orderPhone;
        private String orderZipcode;
        private String orderUser;
        private int orderType;
        private int orderStatu;
        private String orderSubjctId;
        private int orderPay;
        private String orderDes;
        private long orderDate;
        private String orderCode;
        private String productId;
        private double billPrice;
        private int billNum;
        private String orderLogisticsCode;
        private long orderLogisticsDate;
        private String orderLogiscompanyCode;
        private int orderLogiscompanyPrice;
        private int sellUserId;
        private int buyUserId;
        private int payType;
        private long payEndTime;
        private long endTime;
        private String orderNote;
        private String billTitle;
        private String billDesc;
        private int balance;
        private int credit;
        private double ratio;
        private long createTime;
        private int isDelete;

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public String getSysAreaId() {
            return sysAreaId;
        }

        public void setSysAreaId(String sysAreaId) {
            this.sysAreaId = sysAreaId;
        }

        public String getOrderAddress() {
            return orderAddress;
        }

        public void setOrderAddress(String orderAddress) {
            this.orderAddress = orderAddress;
        }

        public String getOrderPhone() {
            return orderPhone;
        }

        public void setOrderPhone(String orderPhone) {
            this.orderPhone = orderPhone;
        }

        public String getOrderZipcode() {
            return orderZipcode;
        }

        public void setOrderZipcode(String orderZipcode) {
            this.orderZipcode = orderZipcode;
        }

        public String getOrderUser() {
            return orderUser;
        }

        public void setOrderUser(String orderUser) {
            this.orderUser = orderUser;
        }

        public int getOrderType() {
            return orderType;
        }

        public void setOrderType(int orderType) {
            this.orderType = orderType;
        }

        public int getOrderStatu() {
            return orderStatu;
        }

        public void setOrderStatu(int orderStatu) {
            this.orderStatu = orderStatu;
        }

        public String getOrderSubjctId() {
            return orderSubjctId;
        }

        public void setOrderSubjctId(String orderSubjctId) {
            this.orderSubjctId = orderSubjctId;
        }

        public int getOrderPay() {
            return orderPay;
        }

        public void setOrderPay(int orderPay) {
            this.orderPay = orderPay;
        }

        public String getOrderDes() {
            return orderDes;
        }

        public void setOrderDes(String orderDes) {
            this.orderDes = orderDes;
        }

        public long getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(long orderDate) {
            this.orderDate = orderDate;
        }

        public String getOrderCode() {
            return orderCode;
        }

        public void setOrderCode(String orderCode) {
            this.orderCode = orderCode;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public double getBillPrice() {
            return billPrice;
        }

        public void setBillPrice(double billPrice) {
            this.billPrice = billPrice;
        }

        public int getBillNum() {
            return billNum;
        }

        public void setBillNum(int billNum) {
            this.billNum = billNum;
        }

        public String getOrderLogisticsCode() {
            return orderLogisticsCode;
        }

        public void setOrderLogisticsCode(String orderLogisticsCode) {
            this.orderLogisticsCode = orderLogisticsCode;
        }

        public long getOrderLogisticsDate() {
            return orderLogisticsDate;
        }

        public void setOrderLogisticsDate(long orderLogisticsDate) {
            this.orderLogisticsDate = orderLogisticsDate;
        }

        public String getOrderLogiscompanyCode() {
            return orderLogiscompanyCode;
        }

        public void setOrderLogiscompanyCode(String orderLogiscompanyCode) {
            this.orderLogiscompanyCode = orderLogiscompanyCode;
        }

        public int getOrderLogiscompanyPrice() {
            return orderLogiscompanyPrice;
        }

        public void setOrderLogiscompanyPrice(int orderLogiscompanyPrice) {
            this.orderLogiscompanyPrice = orderLogiscompanyPrice;
        }

        public int getSellUserId() {
            return sellUserId;
        }

        public void setSellUserId(int sellUserId) {
            this.sellUserId = sellUserId;
        }

        public int getBuyUserId() {
            return buyUserId;
        }

        public void setBuyUserId(int buyUserId) {
            this.buyUserId = buyUserId;
        }

        public int getPayType() {
            return payType;
        }

        public void setPayType(int payType) {
            this.payType = payType;
        }

        public long getPayEndTime() {
            return payEndTime;
        }

        public void setPayEndTime(long payEndTime) {
            this.payEndTime = payEndTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public String getOrderNote() {
            return orderNote;
        }

        public void setOrderNote(String orderNote) {
            this.orderNote = orderNote;
        }

        public String getBillTitle() {
            return billTitle;
        }

        public void setBillTitle(String billTitle) {
            this.billTitle = billTitle;
        }

        public String getBillDesc() {
            return billDesc;
        }

        public void setBillDesc(String billDesc) {
            this.billDesc = billDesc;
        }

        public int getBalance() {
            return balance;
        }

        public void setBalance(int balance) {
            this.balance = balance;
        }

        public int getCredit() {
            return credit;
        }

        public void setCredit(int credit) {
            this.credit = credit;
        }

        public double getRatio() {
            return ratio;
        }

        public void setRatio(double ratio) {
            this.ratio = ratio;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public int getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(int isDelete) {
            this.isDelete = isDelete;
        }

        @Override
        public String toString() {
            return "OrderInfoBean{" +
                    "orderId=" + orderId +
                    ", sysAreaId='" + sysAreaId + '\'' +
                    ", orderAddress='" + orderAddress + '\'' +
                    ", orderPhone='" + orderPhone + '\'' +
                    ", orderZipcode='" + orderZipcode + '\'' +
                    ", orderUser='" + orderUser + '\'' +
                    ", orderType=" + orderType +
                    ", orderStatu=" + orderStatu +
                    ", orderSubjctId='" + orderSubjctId + '\'' +
                    ", orderPay=" + orderPay +
                    ", orderDes='" + orderDes + '\'' +
                    ", orderDate=" + orderDate +
                    ", orderCode='" + orderCode + '\'' +
                    ", productId='" + productId + '\'' +
                    ", billPrice=" + billPrice +
                    ", billNum=" + billNum +
                    ", orderLogisticsCode='" + orderLogisticsCode + '\'' +
                    ", orderLogisticsDate=" + orderLogisticsDate +
                    ", orderLogiscompanyCode='" + orderLogiscompanyCode + '\'' +
                    ", orderLogiscompanyPrice=" + orderLogiscompanyPrice +
                    ", sellUserId=" + sellUserId +
                    ", buyUserId=" + buyUserId +
                    ", payType=" + payType +
                    ", payEndTime=" + payEndTime +
                    ", endTime=" + endTime +
                    ", orderNote='" + orderNote + '\'' +
                    ", billTitle='" + billTitle + '\'' +
                    ", billDesc='" + billDesc + '\'' +
                    ", balance=" + balance +
                    ", credit=" + credit +
                    ", ratio=" + ratio +
                    ", createTime=" + createTime +
                    ", isDelete=" + isDelete +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "CreatOrdersMy{" +
                "orderInfo=" + orderInfo +
                '}';
    }
}
