package com.beyond.popscience.frame.pojo;

/**
 * 说明:
 * Created by Administrator on 2018/3/13.
 */

public class OrderInfoBean extends BaseObject{

        /**
         * orderId : 0  订单id
         * sysAreaId :  收件区域
         * orderAddress : 安徽省安庆市枞阳县扩大朋扩大朋  订单地址
         * orderPhone : 18317318630  订单收件电话
         * orderZipcode :  订单收件邮编
         * orderUser : 东东  订单收件人
         * orderType : 1  订单类型
         * orderStatu : 0  0:\r\n"待付款";1:"待发货";2:"已发货";3:"交易成功";4:"交易关闭";5:"已评价";6:"支付中";7:充值；8：订单中的商品已全部退款成功
         * orderSubjctId :  备注字段
         * orderPay : 0
         * orderDes :  订单留言
         * orderDate : 1520938233000
         * orderCode : 1520938233312  订单号
         * productId : 408  订单表对应的商品id
         * billPrice : 1  订单支付金额
         * billNum : 1
         * orderLogisticsCode :  订单物流编码
         * orderLogisticsDate : 1520938233000  发货时间
         * orderLogiscompanyCode :  订单物流公司编码
         * orderLogiscompanyPrice : 0  订单物流金额
         * sellUserId : 34274  卖家的ID
         * buyUserId : 125396  购买者id
         * payType : 0  1：普通支付\r\n            2：微信支付            3：支付宝'
         * payEndTime : 1520938233000  订单支付时间
         * endTime : 1520938233000  订单完成时间
         * orderNote : 潘生  订单备注[商家]
         * billTitle :    发票抬头
         * billDesc : 无  发票备注
         * balance : 0  余额抵扣金额
         * credit : 0  积分抵扣数额
         * ratio : 0.2  下单时后台设置的积分换算比
         * createTime : 1520938233000  订单生成时间
         * isDelete : 0  0代表已删除1代表未删除
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
        private int billPrice;
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

        public int getBillPrice() {
            return billPrice;
        }

        public void setBillPrice(int billPrice) {
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

}
