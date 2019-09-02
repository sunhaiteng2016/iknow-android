package com.beyond.popscience.locationgoods.bean;

import com.beyond.library.network.net.bean.BaseResponse;

import java.io.Serializable;
import java.util.List;

public class ProductDetail extends BaseResponse {


    /**
     * product : {"id":2,"shopId":4,"area":"仙居县","categoryOne":"果品","categoryTwo":"杨梅","name":"杨梅3号","subTitle":"副标题","pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/8cf24b21edaa4e13bc9d3d8268d8fe99.jpeg","lunboPics":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/8cf24b21edaa4e13bc9d3d8268d8fe99.jpeg","sort":0,"sale":0,"description":"[\n  {\n    \"image\" : \"http:\\/\\/ikow.oss-cn-shanghai.aliyuncs.com\\/newsImages\\/8cf24b21edaa4e13bc9d3d8268d8fe99.jpeg\",\n    \"text\" : \"\"\n  },\n  {\n    \"image\" : \"http:\\/\\/ikow.oss-cn-shanghai.aliyuncs.com\\/newsImages\\/980fffbec831404682f9d095f9ef506a.jpeg\",\n    \"text\" : \"这样就好了\"\n  }\n]","deliveryStatus":1,"expressFee":0,"groupStatus":1,"groupSize":8,"deleteStatus":0,"publishStatus":1,"recommandStatus":0,"verifyStatus":1,"skuList":[{"id":2,"productId":2,"sp1":"果径:10-20mm|价格单位:500g|包装:纸箱","sale":1,"price":15,"groupPrice":10,"stock":299,"lockStock":298},{"id":3,"productId":2,"sp1":"果径:15-25mm|价格单位:500g|包装:纸箱","sale":1,"price":23,"groupPrice":18,"stock":99,"lockStock":100},{"id":4,"productId":2,"sp1":"果径:25-49mm|价格单位:500g|包装:纸箱+泡沫箱+冰块","sale":0,"price":88,"stock":40,"lockStock":40}],"brand":"自产","place":"湫山"}
     * shop : {"id":4,"userId":250803,"shopName":"仙居高山杨梅直销","shopTel":"18964022383","shopContacts":"陈","shopAddress":"湫山乡湫山村","shopDetail":"十年老店","shopImgList":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/6c05fa305c1c400d92a4417000d8f376.jpeg,http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/605853535c4542019fd2595febb42306.jpeg,http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/707e03e6bc8645f0a978e5d5314aedf6.jpeg,http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/b37882facc65453c90139e38b005378e.jpeg","openTime":"全天候"}
     * pt : {"id":100006,"userId":285436,"productId":2,"shopPtNumber":2,"ptNumber":0,"createTime":"2019-05-20 15:09:45","endTime":"2019-05-21 15:09:55","ptStatus":0,"nickName":"132","avatar":"http://www.appwzd.cn/micro_mart/ncss/logo.png"}
     * collect : 0
     */

    private ProductBean product;
    private ShopBean shop;
    private PtBean pt;
    private int collect;

    public ProductBean getProduct() {
        return product;
    }

    public void setProduct(ProductBean product) {
        this.product = product;
    }

    public ShopBean getShop() {
        return shop;
    }

    public void setShop(ShopBean shop) {
        this.shop = shop;
    }

    public PtBean getPt() {
        return pt;
    }

    public void setPt(PtBean pt) {
        this.pt = pt;
    }

    public int getCollect() {
        return collect;
    }

    public void setCollect(int collect) {
        this.collect = collect;
    }

    public static class ProductBean implements Serializable {
        /**
         * id : 10000053
         * shopId : 10000005
         * area : 椒江区
         * categoryOne : 果品
         * categoryTwo : 杨梅
         * name : 杨梅
         * subTitle : 副标题
         * pic : http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/f62347fdcad84ae784f323770541edad.jpeg
         * lunboPics : http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/f62347fdcad84ae784f323770541edad.jpeg
         * sort : 0
         * description : [
         * {
         * "image" : "http:\/\/ikow.oss-cn-shanghai.aliyuncs.com\/newsImages\/f62347fdcad84ae784f323770541edad.jpeg",
         * "text" : ""
         * },
         * {
         * "image" : "http:\/\/ikow.oss-cn-shanghai.aliyuncs.com\/newsImages\/021158a68d1f487894dc1768e70937c8.jpeg",
         * "text" : ""
         * }
         * ]
         * deliveryStatus : 2
         * expressFee : 20
         * groupStatus : 1
         * deleteStatus : 0
         * publishStatus : 1
         * recommandStatus : 0
         * verifyStatus : 1
         * skuList : [{"id":41,"productId":10000053,"sp1":"20kg/箱20g/个纸箱","sale":0,"price":0.01,"groupPrice":0.01,"stock":1000,"lockStock":996},{"id":62,"productId":10000053,"lowStock":0,"sp1":"string","sp2":"string","sp3":"string","pic":"string","sale":0,"price":0.01,"groupPrice":0.01,"stock":0,"lockStock":0}]
         * brand : 自产
         * place : 台州市椒江区
         */

        private int id;
        private int shopId;
        private String area;
        private String categoryOne;
        private String categoryTwo;
        private String name;
        private String subTitle;
        private String pic;
        private String lunboPics;
        private int sort;
        private String description;
        private int deliveryStatus;
        private Object expressFee;
        private int groupStatus;
        private int groupSize;

        private int deleteStatus;
        private int publishStatus;
        private int recommandStatus;
        private int verifyStatus;
        private String brand;
        private String place;
        private List<SkuListBean> skuList;


        public int getGroupSize() {
            return groupSize;
        }

        public void setGroupSize(int groupSize) {
            this.groupSize = groupSize;
        }

        public void setExpressFee(Object expressFee) {
            this.expressFee = expressFee;
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

        public String getLunboPics() {
            return lunboPics;
        }

        public void setLunboPics(String lunboPics) {
            this.lunboPics = lunboPics;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
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

        public void setExpressFee(int expressFee) {
            this.expressFee = expressFee;
        }

        public int getGroupStatus() {
            return groupStatus;
        }

        public void setGroupStatus(int groupStatus) {
            this.groupStatus = groupStatus;
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

        public int getRecommandStatus() {
            return recommandStatus;
        }

        public void setRecommandStatus(int recommandStatus) {
            this.recommandStatus = recommandStatus;
        }

        public int getVerifyStatus() {
            return verifyStatus;
        }

        public void setVerifyStatus(int verifyStatus) {
            this.verifyStatus = verifyStatus;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public List<SkuListBean> getSkuList() {
            return skuList;
        }

        public void setSkuList(List<SkuListBean> skuList) {
            this.skuList = skuList;
        }

        public static class SkuListBean implements Serializable {
            /**
             * id : 41
             * productId : 10000053
             * sp1 : 20kg/箱20g/个纸箱
             * sale : 0
             * price : 0.01
             * groupPrice : 0.01
             * stock : 1000
             * lockStock : 996
             * lowStock : 0
             * sp2 : string
             * sp3 : string
             * pic : string
             */

            private int id;
            private int productId;
            private String sp1;
            private int sale;
            private Object price;
            private Object groupPrice;
            private int stock;
            private int lockStock;
            private int lowStock;
            private String sp2;
            private String sp3;
            private String pic;
            private boolean flag;

            public boolean isFlag() {
                return flag;
            }

            public void setFlag(boolean flag) {
                this.flag = flag;
            }

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

            public String getSp1() {
                return sp1;
            }

            public void setSp1(String sp1) {
                this.sp1 = sp1;
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

            public void setPrice(double price) {
                this.price = price;
            }

            public Object getGroupPrice() {
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

            public int getLowStock() {
                return lowStock;
            }

            public void setLowStock(int lowStock) {
                this.lowStock = lowStock;
            }

            public String getSp2() {
                return sp2;
            }

            public void setSp2(String sp2) {
                this.sp2 = sp2;
            }

            public String getSp3() {
                return sp3;
            }

            public void setSp3(String sp3) {
                this.sp3 = sp3;
            }

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }
        }
    }

    public static class ShopBean implements Serializable {
        /**
         * id : 10000005
         * shopName : 张三的店
         * shopTel : 13514990174
         * shopContacts : 张三
         * shopAddress : 海洋广场
         * shopDetail : 店铺介绍
         * shopImgList : http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/b355267c96ce412786564d9383b54448.jpeg,http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/bcea697def58488c86bcfe380cd45b6c.jpeg,,
         */

        private int id;
        private int userId;
        private String shopName;
        private String shopTel;
        private String shopContacts;
        private String shopAddress;
        private String shopDetail;
        private String shopImgList;
        private String openTime;
        private int shopAuth;

        public int getShopAuth() {
            return shopAuth;
        }

        public void setShopAuth(int shopAuth) {
            this.shopAuth = shopAuth;
        }

        public String getOpenTime() {
            return openTime;
        }

        public void setOpenTime(String openTime) {
            this.openTime = openTime;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getShopTel() {
            return shopTel;
        }

        public void setShopTel(String shopTel) {
            this.shopTel = shopTel;
        }

        public String getShopContacts() {
            return shopContacts;
        }

        public void setShopContacts(String shopContacts) {
            this.shopContacts = shopContacts;
        }

        public String getShopAddress() {
            return shopAddress;
        }

        public void setShopAddress(String shopAddress) {
            this.shopAddress = shopAddress;
        }

        public String getShopDetail() {
            return shopDetail;
        }

        public void setShopDetail(String shopDetail) {
            this.shopDetail = shopDetail;
        }

        public String getShopImgList() {
            return shopImgList;
        }

        public void setShopImgList(String shopImgList) {
            this.shopImgList = shopImgList;
        }
    }

    public static class PtBean implements  Serializable{
        /**
         * id : 100006
         * userId : 285436
         * productId : 2
         * shopPtNumber : 2
         * ptNumber : 0
         * createTime : 2019-05-20 15:09:45
         * endTime : 2019-05-21 15:09:55
         * ptStatus : 0
         * nickName : 132
         * avatar : http://www.appwzd.cn/micro_mart/ncss/logo.png
         */

        private int id;
        private int userId;
        private int productId;
        private int shopPtNumber;
        private int ptNumber;
        private String createTime;
        private String endTime;
        private int ptStatus;
        private String nickName;
        private String avatar;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public int getShopPtNumber() {
            return shopPtNumber;
        }

        public void setShopPtNumber(int shopPtNumber) {
            this.shopPtNumber = shopPtNumber;
        }

        public int getPtNumber() {
            return ptNumber;
        }

        public void setPtNumber(int ptNumber) {
            this.ptNumber = ptNumber;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public int getPtStatus() {
            return ptStatus;
        }

        public void setPtStatus(int ptStatus) {
            this.ptStatus = ptStatus;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
