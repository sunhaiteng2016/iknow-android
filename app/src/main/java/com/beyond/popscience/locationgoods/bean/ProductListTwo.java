package com.beyond.popscience.locationgoods.bean;

import com.beyond.library.network.net.bean.BaseResponse;

public class ProductListTwo extends BaseResponse {


    /**
     * id : 10000076
     * shopId : 10000006
     * area : 椒江区
     * categoryOne : 本地特产
     * categoryTwo : 杨梅
     * name : 123556
     * subTitle : 副标题
     * pic : http://ikow.oss-cn-shanghai.aliyuncs.com/images/3e508bf1-55c5-485c-99ab-88621ec57840.jpg
     * sale : 0
     * deliveryStatus : 3
     * expressFee : 10
     * deleteStatus : 0
     * publishStatus : 1
     * sku : {"id":80,"productId":10000076,"sale":0,"price":5563,"groupPrice":5555,"stock":8566,"lockStock":8566}
     * createTime : 2019-05-16 11:04:51
     * score : 4
     */

    private int id;
    private int shopId;
    private String area;
    private String categoryOne;
    private String categoryTwo;
    private String name;
    private String subTitle;
    private String pic;
    private int sale;
    private int deliveryStatus;
    private Object expressFee;
    private int deleteStatus;
    private int publishStatus;
    private SkuBean sku;
    private String createTime;
    private int score;
    private int groupSize;

    public int getGroupSize() {
        return groupSize;
    }

    public void setGroupSize(int groupSize) {
        this.groupSize = groupSize;
    }

    private boolean isCheck = false;
    private boolean isEdit = false;

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCategoryOne() {
        return categoryOne;
    }

    public void setCategoryOne(String categoryOne) {
        this.categoryOne = categoryOne;
    }

    public String getCategoryTwo() {
        return categoryTwo;
    }

    public void setCategoryTwo(String categoryTwo) {
        this.categoryTwo = categoryTwo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getSale() {
        return sale;
    }

    public void setSale(int sale) {
        this.sale = sale;
    }

    public int getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(int deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public Object getExpressFee() {
        return expressFee;
    }

    public void setExpressFee(Object expressFee) {
        this.expressFee = expressFee;
    }

    public int getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(int deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public int getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(int publishStatus) {
        this.publishStatus = publishStatus;
    }

    public SkuBean getSku() {
        return sku;
    }

    public void setSku(SkuBean sku) {
        this.sku = sku;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public static class SkuBean {
        /**
         * id : 80
         * productId : 10000076
         * sale : 0
         * price : 5563
         * groupPrice : 5555
         * stock : 8566
         * lockStock : 8566
         */

        private int id;
        private int productId;
        private int sale;
        private Object price;
        private Object groupPrice;
        private int stock;
        private int lockStock;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public int getSale() {
            return sale;
        }

        public void setSale(int sale) {
            this.sale = sale;
        }

        public Object getPrice() {
            return price;
        }

        public void setPrice(Object price) {
            this.price = price;
        }

        public Object getGroupPrice() {
            return groupPrice;
        }

        public void setGroupPrice(Object groupPrice) {
            this.groupPrice = groupPrice;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public int getLockStock() {
            return lockStock;
        }

        public void setLockStock(int lockStock) {
            this.lockStock = lockStock;
        }
    }
}
