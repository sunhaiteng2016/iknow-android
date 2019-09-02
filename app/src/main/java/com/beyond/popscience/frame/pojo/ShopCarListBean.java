package com.beyond.popscience.frame.pojo;

import java.util.List;

/**
 * Created by Administrator on 2017-11-07.
 */

public class ShopCarListBean {

    /**
     * re : 1
     * info : [{"store_id":"8","store_name":"京东旗舰店","store_logo":"http://txl.unohacha.com/Public/Wxin/Images/shangjia_3.jpg","goods":[{"cartid":"21","goods_name":"测试商品5","price":"100.00","logo_pic":"http://txl.unohacha.com/Public/Wxin/Images/ind_jf_2.jpg","num":"3","goods_id":"5","sku_id":"","store_id":"8","store":"100"},{"cartid":"20","goods_name":"测试商品4","price":"100.00","logo_pic":"http://txl.unohacha.com/Public/Wxin/Images/ind_jf_2.jpg","num":"2","goods_id":"4","sku_id":"","store_id":"8","store":"100"}]},{"store_id":"4","store_name":"苹果旗舰店","store_logo":"http://txl.unohacha.com/Public/Wxin/Images/shangjia_2.jpg","goods":[{"cartid":"19","goods_name":"测试商品2","price":"188.00","logo_pic":"http://txl.unohacha.com/Public/Wxin/Images/ind_jf_2.jpg","num":"1","goods_id":"2","sku_id":"47","store_id":"4","store":"188"},{"cartid":"16","goods_name":"测试商品2","price":"188.00","logo_pic":"http://txl.unohacha.com/Public/Wxin/Images/ind_jf_2.jpg","num":"1","goods_id":"2","sku_id":"51","store_id":"4","store":"188"},{"cartid":"15","goods_name":"测试商品2","price":"188.00","logo_pic":"http://txl.unohacha.com/Public/Wxin/Images/ind_jf_2.jpg","num":"4","goods_id":"2","sku_id":"45","store_id":"4","store":"188"},{"cartid":"14","goods_name":"测试商品2","price":"188.00","logo_pic":"http://txl.unohacha.com/Public/Wxin/Images/ind_jf_2.jpg","num":"1","goods_id":"2","sku_id":"50","store_id":"4","store":"188"}]}]
     */

    private String re;
    private List<InfoBean> info;
    private String have_ldian;

    public String getHave_ldian() {
        return have_ldian;
    }

    public void setHave_ldian(String have_ldian) {
        this.have_ldian = have_ldian;
    }

    public String getRe() {
        return re;
    }

    public void setRe(String re) {
        this.re = re;
    }

    public List<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<InfoBean> info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * store_id : 8
         * store_name : 京东旗舰店
         * store_logo : http://txl.unohacha.com/Public/Wxin/Images/shangjia_3.jpg
         * goods : [{"cartid":"21","goods_name":"测试商品5","price":"100.00","logo_pic":"http://txl.unohacha.com/Public/Wxin/Images/ind_jf_2.jpg","num":"3","goods_id":"5","sku_id":"","store_id":"8","store":"100"},{"cartid":"20","goods_name":"测试商品4","price":"100.00","logo_pic":"http://txl.unohacha.com/Public/Wxin/Images/ind_jf_2.jpg","num":"2","goods_id":"4","sku_id":"","store_id":"8","store":"100"}]
         */

        private String store_id;
        private String store_name;
        private String store_logo;
        private boolean isChoosed;
        private List<GoodsBean> goods;

        public InfoBean() {
        }

        public InfoBean(String store_id, String store_name, String store_logo, boolean isChoosed, List<GoodsBean> goods) {
            this.store_id = store_id;
            this.store_name = store_name;
            this.store_logo = store_logo;
            this.isChoosed = isChoosed;
            this.goods = goods;
        }

        public InfoBean(int i, String store_name, String store_logo, boolean isChoosed, List<GoodsBean> mGoodsBeanList) {
        }

        public String getStore_id() {
            return store_id;
        }

        public void setStore_id(String store_id) {
            this.store_id = store_id;
        }

        public String getStore_name() {
            return store_name;
        }

        public void setStore_name(String store_name) {
            this.store_name = store_name;
        }

        public String getStore_logo() {
            return store_logo;
        }

        public void setStore_logo(String store_logo) {
            this.store_logo = store_logo;
        }

        public boolean isChoosed() {
            return isChoosed;
        }

        public void setChoosed(boolean choosed) {
            isChoosed = choosed;
        }

        public List<GoodsBean> getGoods() {
            return goods;
        }

        public void setGoods(List<GoodsBean> goods) {
            this.goods = goods;
        }

        public static class GoodsBean {
            /**
             * cartid : 21
             * goods_name : 测试商品5
             * price : 100.00
             * logo_pic : http://txl.unohacha.com/Public/Wxin/Images/ind_jf_2.jpg
             * num : 3
             * goods_id : 5
             * sku_id :
             * store_id : 8
             * store : 100
             * "is_integral": "1",
             * "type_status": "0",
             * "goods_type": "3",
             */

            private String cartid;
            private String goods_name;
            private String price;
            private String logo_pic;
            private String num;
            private String goods_id;
            private String sku_id;
            private String store_id;
            private String store;
            private String sku_name;
            private String is_integral;
            private String type_status;
            private String goods_type;
            private boolean isChecked;

            public boolean isChecked() {
                return isChecked;
            }

            public void setChecked(boolean checked) {
                isChecked = checked;
            }

            public String getIs_integral() {
                return is_integral;
            }

            public void setIs_integral(String is_integral) {
                this.is_integral = is_integral;
            }

            public String getType_status() {
                return type_status;
            }

            public void setType_status(String type_status) {
                this.type_status = type_status;
            }

            public String getGoods_type() {
                return goods_type;
            }

            public void setGoods_type(String goods_type) {
                this.goods_type = goods_type;
            }

            public String getCartid() {
                return cartid;
            }

            public void setCartid(String cartid) {
                this.cartid = cartid;
            }

            public String getGoods_name() {
                return goods_name;
            }

            public void setGoods_name(String goods_name) {
                this.goods_name = goods_name;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getLogo_pic() {
                return logo_pic;
            }

            public void setLogo_pic(String logo_pic) {
                this.logo_pic = logo_pic;
            }

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }

            public String getGoods_id() {
                return goods_id;
            }

            public void setGoods_id(String goods_id) {
                this.goods_id = goods_id;
            }

            public String getSku_id() {
                return sku_id;
            }

            public void setSku_id(String sku_id) {
                this.sku_id = sku_id;
            }

            public String getStore_id() {
                return store_id;
            }

            public void setStore_id(String store_id) {
                this.store_id = store_id;
            }

            public String getStore() {
                return store;
            }

            public void setStore(String store) {
                this.store = store;
            }

            public String getSku_name() {
                return sku_name;
            }

            public void setSku_name(String sku_name) {
                this.sku_name = sku_name;
            }
        }
    }
}
