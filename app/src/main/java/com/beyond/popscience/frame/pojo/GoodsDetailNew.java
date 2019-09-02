package com.beyond.popscience.frame.pojo;

import java.util.List;

/**
 * Describe： 新的商品详情类
 * Date：2018/3/9
 * Time: 10:24
 * Author: Bin.Peng
 */

public class GoodsDetailNew extends BaseObject {
    /**
     * code : 0
     * message : 获取成功
     * data : {"isCollection":"2","data":{"classfyName":"商品买卖","createTime":"2018-03-08 11:39:56","goods":[{"goodsDescImg":"http://ikow.oss-cn-shanghai.aliyuncs.com/images/9B94F218-4BC8-4DA4-A399-7BEBC6A53FCD.jpeg","goodsDesc":"体系运行哦哦"},{"goodsDescImg":"http://ikow.oss-cn-shanghai.aliyuncs.com/images/9B94F218-4BC8-4DA4-A399-7BEBC6A53FCD.jpeg","goodsDesc":""}],"commentCount":"1","avatar":"http://kp.appwzd.cn/header/f8e33c69f14c467a91a586fb6fcdeb98.jpeg","productId":"410","title":"iOS测试","price":"12.00","address":"测试","coverPic":"http://ikow.oss-cn-shanghai.aliyuncs.com/images/04796A57-1989-4334-9960-D0F03CFFF30F.jpeg","userId":"125309","classfyId":"5","realName":"流域","mobile":"12345678907"}}
     */

//    private int code;
//    private String message;
//    private DataBeanX data;
//
//    public int getCode() {
//        return code;
//    }
//
//    public void setCode(int code) {
//        this.code = code;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public DataBeanX getData() {
//        return data;
//    }
//
//    public void setData(DataBeanX data) {
//        this.data = data;
//    }
//
//    public static class DataBeanX {
//        /**
//         * isCollection : 2
//         * data : {"classfyName":"商品买卖","createTime":"2018-03-08 11:39:56","goods":[{"goodsDescImg":"http://ikow.oss-cn-shanghai.aliyuncs.com/images/9B94F218-4BC8-4DA4-A399-7BEBC6A53FCD.jpeg","goodsDesc":"体系运行哦哦"},{"goodsDescImg":"http://ikow.oss-cn-shanghai.aliyuncs.com/images/9B94F218-4BC8-4DA4-A399-7BEBC6A53FCD.jpeg","goodsDesc":""}],"commentCount":"1","avatar":"http://kp.appwzd.cn/header/f8e33c69f14c467a91a586fb6fcdeb98.jpeg","productId":"410","title":"iOS测试","price":"12.00","address":"测试","coverPic":"http://ikow.oss-cn-shanghai.aliyuncs.com/images/04796A57-1989-4334-9960-D0F03CFFF30F.jpeg","userId":"125309","classfyId":"5","realName":"流域","mobile":"12345678907"}
//         */
//
//        private String isCollection;
//        private DataBean data;
//
//        public String getIsCollection() {
//            return isCollection;
//        }
//
//        public void setIsCollection(String isCollection) {
//            this.isCollection = isCollection;
//        }
//
//        public DataBean getData() {
//            return data;
//        }
//
//        public void setData(DataBean data) {
//            this.data = data;
//        }
//
//        public static class DataBean {
//            /**
//             * classfyName : 商品买卖
//             * createTime : 2018-03-08 11:39:56
//             * goods : [{"goodsDescImg":"http://ikow.oss-cn-shanghai.aliyuncs.com/images/9B94F218-4BC8-4DA4-A399-7BEBC6A53FCD.jpeg","goodsDesc":"体系运行哦哦"},{"goodsDescImg":"http://ikow.oss-cn-shanghai.aliyuncs.com/images/9B94F218-4BC8-4DA4-A399-7BEBC6A53FCD.jpeg","goodsDesc":""}]
//             * commentCount : 1
//             * avatar : http://kp.appwzd.cn/header/f8e33c69f14c467a91a586fb6fcdeb98.jpeg
//             * productId : 410
//             * title : iOS测试
//             * price : 12.00
//             * address : 测试
//             * coverPic : http://ikow.oss-cn-shanghai.aliyuncs.com/images/04796A57-1989-4334-9960-D0F03CFFF30F.jpeg
//             * userId : 125309
//             * classfyId : 5
//             * realName : 流域
//             * mobile : 12345678907
//             */
//
//            private String classfyName;
//            private String createTime;
//            private String commentCount;
//            private String avatar;
//            private String productId;
//            private String title;
//            private String price;
//            private String address;
//            private String coverPic;
//            private String userId;
//            private String classfyId;
//            private String realName;
//            private String mobile;
//            private List<GoodsBean> goods;
//
//            public String getClassfyName() {
//                return classfyName;
//            }
//
//            public void setClassfyName(String classfyName) {
//                this.classfyName = classfyName;
//            }
//
//            public String getCreateTime() {
//                return createTime;
//            }
//
//            public void setCreateTime(String createTime) {
//                this.createTime = createTime;
//            }
//
//            public String getCommentCount() {
//                return commentCount;
//            }
//
//            public void setCommentCount(String commentCount) {
//                this.commentCount = commentCount;
//            }
//
//            public String getAvatar() {
//                return avatar;
//            }
//
//            public void setAvatar(String avatar) {
//                this.avatar = avatar;
//            }
//
//            public String getProductId() {
//                return productId;
//            }
//
//            public void setProductId(String productId) {
//                this.productId = productId;
//            }
//
//            public String getTitle() {
//                return title;
//            }
//
//            public void setTitle(String title) {
//                this.title = title;
//            }
//
//            public String getPrice() {
//                return price;
//            }
//
//            public void setPrice(String price) {
//                this.price = price;
//            }
//
//            public String getAddress() {
//                return address;
//            }
//
//            public void setAddress(String address) {
//                this.address = address;
//            }
//
//            public String getCoverPic() {
//                return coverPic;
//            }
//
//            public void setCoverPic(String coverPic) {
//                this.coverPic = coverPic;
//            }
//
//            public String getUserId() {
//                return userId;
//            }
//
//            public void setUserId(String userId) {
//                this.userId = userId;
//            }
//
//            public String getClassfyId() {
//                return classfyId;
//            }
//
//            public void setClassfyId(String classfyId) {
//                this.classfyId = classfyId;
//            }
//
//            public String getRealName() {
//                return realName;
//            }
//
//            public void setRealName(String realName) {
//                this.realName = realName;
//            }
//
//            public String getMobile() {
//                return mobile;
//            }
//
//            public void setMobile(String mobile) {
//                this.mobile = mobile;
//            }
//
//            public List<GoodsBean> getGoods() {
//                return goods;
//            }
//
//            public void setGoods(List<GoodsBean> goods) {
//                this.goods = goods;
//            }
//
//            public static class GoodsBean {
//                /**
//                 * goodsDescImg : http://ikow.oss-cn-shanghai.aliyuncs.com/images/9B94F218-4BC8-4DA4-A399-7BEBC6A53FCD.jpeg
//                 * goodsDesc : 体系运行哦哦
//                 */
//
//                private String goodsDescImg;
//                private String goodsDesc;
//
//                public String getGoodsDescImg() {
//                    return goodsDescImg;
//                }
//
//                public void setGoodsDescImg(String goodsDescImg) {
//                    this.goodsDescImg = goodsDescImg;
//                }
//
//                public String getGoodsDesc() {
//                    return goodsDesc;
//                }
//
//                public void setGoodsDesc(String goodsDesc) {
//                    this.goodsDesc = goodsDesc;
//                }
//            }
//        }
//    }

    public Data data;
    public String isCollection;//是否收藏

    public class Data{
        public String classfyName;
        public String createTime;
        public String title;
        public String price;

        public String address;
        public String coverPic;
        public String classfyId;
        public String userId;
//        public String commentCount;

        public List<GoodsDescImgObj> goods;

        public String realName;
        public String avatar;
        public String productId;
        public String mobile;
    }

}
