package com.beyond.popscience.locationgoods.bean;

import com.beyond.library.network.net.bean.BaseResponse;

public class ProductList extends BaseResponse {


    /**
     * id : 10000053
     * shopId : 10000005
     * area : 椒江区
     * categoryOne : 果品
     * categoryTwo : 杨梅
     * name : 杨梅
     * subTitle : 副标题
     * pic : http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/f62347fdcad84ae784f323770541edad.jpeg
     * deliveryStatus : 2
     * expressFee : 0.01
     * sku : {"id":41,"productId":10000053,"sale":0,"price":0.01,"groupPrice":0.01,"stock":1000,"lockStock":946}
     * score : 4
     */

    private int id;
    private int shopId;
    private int sale;
    private String area;
    private String categoryOne;
    private String categoryTwo;
    private String name;
    private String subTitle;
    private String pic;
    private int deliveryStatus;
    private int groupSize;
    private double expressFee;
    private SkuBean sku;
    private int score;
    private String shopAddress;

    public int getGroupSize() {
        return groupSize;
    }

    public void setGroupSize(int groupSize) {
        this.groupSize = groupSize;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public int getSale() {
        return sale;
    }

    public void setSale(int sale) {
        this.sale = sale;
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

    public int getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(int deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public double getExpressFee() {
        return expressFee;
    }

    public void setExpressFee(double expressFee) {
        this.expressFee = expressFee;
    }

    public SkuBean getSku() {
        return sku;
    }

    public void setSku(SkuBean sku) {
        this.sku = sku;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public static class SkuBean {
        /**
         * id : 41
         * productId : 10000053
         * sale : 0
         * price : 0.01
         * groupPrice : 0.01
         * stock : 1000
         * lockStock : 946
         */

        private int id;
        private int productId;
        private int sale;
        private double price;
        private double groupPrice;
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

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getGroupPrice() {
            return groupPrice;
        }

        public void setGroupPrice(double groupPrice) {
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
