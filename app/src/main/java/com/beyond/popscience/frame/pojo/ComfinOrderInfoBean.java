package com.beyond.popscience.frame.pojo;

import java.util.List;

/**
 * Created by Administrator on 2017-10-10.
 */

public class ComfinOrderInfoBean {

    /**
     * lebao : 0
     * lebei : 0
     * store_list : [{"lebao_rate":"0.00","lebei_rate":"0.00","store_id":"1768385426","user_id":"225","store_name":"六月花艺馆","store_title":"","logo_pic":"http://txl.unohacha.com/Uploads/Picture/uploads/20180112/5a58651d72978.jpg","nums":"1","prices":"329","exp":"0","goods_info":[{"goods_id":"617","store_id":"1768385426","goods_name":"圣诞款之抱抱桶","logo_pic":"http://txl.unohacha.com/Uploads/Picture/uploads/20180112/5a586abfa2e14.jpg","price":"329.00","store":"999","weight":"0.00","freight_id":"","is_zt":"0","is_shopping":"0","type_status":"0","lebao_rate":"0.00","lebei_rate":"0.00","freight_info":{"id":"","classname":"","addtime":"","updata_time":"","store_id":"","supplier_id":""},"sku_info":"圣诞款之抱抱桶","num":"1"}],"coupon_list":[]}]
     * re : 1
     * coupon_list_wf : []
     * zk_express : 0
     * dec_lbei : 0
     * user_lbei : 0
     * zk_total : 329
     * express_fee : 0
     * num : 1
     * subtotal : 329
     * total : 329
     * weight : 0
     * type : 3
     * money_ldian : 1309.00
     * money_lbei : 46.00
     * money_lbao : 13.00
     * address : {"address_id":"181","consignee":"测试","province":"浙江省","city":"杭州市","district":"西湖区","place":"个咯啦咯啦","telephone":"13201672759","moren":"1"}
     * address_status : 1
     */

    private String lebao;
    private String lebei;
    private String re;
    private String zk_express;
    private String dec_lbei;
    private String user_lbei;
    private String zk_total;
    private String express_fee;
    private String num;
    private String subtotal;
    private String total;
    private String weight;
    private String type;
    private String money_ldian;
    private String money_lbei;
    private String money_lbao;
    private AddressBean address;
    private String address_status;
    private List<StoreListBean> store_list;
    private List<?> coupon_list_wf;

    public String getLebao() {
        return lebao;
    }

    public void setLebao(String lebao) {
        this.lebao = lebao;
    }

    public String getLebei() {
        return lebei;
    }

    public void setLebei(String lebei) {
        this.lebei = lebei;
    }

    public String getRe() {
        return re;
    }

    public void setRe(String re) {
        this.re = re;
    }

    public String getZk_express() {
        return zk_express;
    }

    public void setZk_express(String zk_express) {
        this.zk_express = zk_express;
    }

    public String getDec_lbei() {
        return dec_lbei;
    }

    public void setDec_lbei(String dec_lbei) {
        this.dec_lbei = dec_lbei;
    }

    public String getUser_lbei() {
        return user_lbei;
    }

    public void setUser_lbei(String user_lbei) {
        this.user_lbei = user_lbei;
    }

    public String getZk_total() {
        return zk_total;
    }

    public void setZk_total(String zk_total) {
        this.zk_total = zk_total;
    }

    public String getExpress_fee() {
        return express_fee;
    }

    public void setExpress_fee(String express_fee) {
        this.express_fee = express_fee;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMoney_ldian() {
        return money_ldian;
    }

    public void setMoney_ldian(String money_ldian) {
        this.money_ldian = money_ldian;
    }

    public String getMoney_lbei() {
        return money_lbei;
    }

    public void setMoney_lbei(String money_lbei) {
        this.money_lbei = money_lbei;
    }

    public String getMoney_lbao() {
        return money_lbao;
    }

    public void setMoney_lbao(String money_lbao) {
        this.money_lbao = money_lbao;
    }

    public AddressBean getAddress() {
        return address;
    }

    public void setAddress(AddressBean address) {
        this.address = address;
    }

    public String getAddress_status() {
        return address_status;
    }

    public void setAddress_status(String address_status) {
        this.address_status = address_status;
    }

    public List<StoreListBean> getStore_list() {
        return store_list;
    }

    public void setStore_list(List<StoreListBean> store_list) {
        this.store_list = store_list;
    }

    public List<?> getCoupon_list_wf() {
        return coupon_list_wf;
    }

    public void setCoupon_list_wf(List<?> coupon_list_wf) {
        this.coupon_list_wf = coupon_list_wf;
    }

    public static class AddressBean {
        /**
         * address_id : 181
         * consignee : 测试
         * province : 浙江省
         * city : 杭州市
         * district : 西湖区
         * place : 个咯啦咯啦
         * telephone : 13201672759
         * moren : 1
         */

        private String address_id;
        private String consignee;
        private String province;
        private String city;
        private String district;
        private String place;
        private String telephone;
        private String moren;

        public String getAddress_id() {
            return address_id;
        }

        public void setAddress_id(String address_id) {
            this.address_id = address_id;
        }

        public String getConsignee() {
            return consignee;
        }

        public void setConsignee(String consignee) {
            this.consignee = consignee;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getMoren() {
            return moren;
        }

        public void setMoren(String moren) {
            this.moren = moren;
        }
    }

    public static class StoreListBean {
        /**
         * lebao_rate : 0.00
         * lebei_rate : 0.00
         * store_id : 1768385426
         * user_id : 225
         * store_name : 六月花艺馆
         * store_title :
         * logo_pic : http://txl.unohacha.com/Uploads/Picture/uploads/20180112/5a58651d72978.jpg
         * nums : 1
         * prices : 329
         * exp : 0
         * goods_info : [{"goods_id":"617","store_id":"1768385426","goods_name":"圣诞款之抱抱桶","logo_pic":"http://txl.unohacha.com/Uploads/Picture/uploads/20180112/5a586abfa2e14.jpg","price":"329.00","store":"999","weight":"0.00","freight_id":"","is_zt":"0","is_shopping":"0","type_status":"0","lebao_rate":"0.00","lebei_rate":"0.00","freight_info":{"id":"","classname":"","addtime":"","updata_time":"","store_id":"","supplier_id":""},"sku_info":"圣诞款之抱抱桶","num":"1"}]
         * coupon_list : []
         */

        private String lebao_rate;
        private String lebei_rate;
        private String store_id;
        private String user_id;
        private String store_name;
        private String store_title;
        private String logo_pic;
        private String nums;
        private String prices;
        private String exp;
        private List<GoodsInfoBean> goods_info;
        private List<?> coupon_list;

        public String getLebao_rate() {
            return lebao_rate;
        }

        public void setLebao_rate(String lebao_rate) {
            this.lebao_rate = lebao_rate;
        }

        public String getLebei_rate() {
            return lebei_rate;
        }

        public void setLebei_rate(String lebei_rate) {
            this.lebei_rate = lebei_rate;
        }

        public String getStore_id() {
            return store_id;
        }

        public void setStore_id(String store_id) {
            this.store_id = store_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getStore_name() {
            return store_name;
        }

        public void setStore_name(String store_name) {
            this.store_name = store_name;
        }

        public String getStore_title() {
            return store_title;
        }

        public void setStore_title(String store_title) {
            this.store_title = store_title;
        }

        public String getLogo_pic() {
            return logo_pic;
        }

        public void setLogo_pic(String logo_pic) {
            this.logo_pic = logo_pic;
        }

        public String getNums() {
            return nums;
        }

        public void setNums(String nums) {
            this.nums = nums;
        }

        public String getPrices() {
            return prices;
        }

        public void setPrices(String prices) {
            this.prices = prices;
        }

        public String getExp() {
            return exp;
        }

        public void setExp(String exp) {
            this.exp = exp;
        }

        public List<GoodsInfoBean> getGoods_info() {
            return goods_info;
        }

        public void setGoods_info(List<GoodsInfoBean> goods_info) {
            this.goods_info = goods_info;
        }

        public List<?> getCoupon_list() {
            return coupon_list;
        }

        public void setCoupon_list(List<?> coupon_list) {
            this.coupon_list = coupon_list;
        }

        public static class GoodsInfoBean {
            /**
             * goods_id : 617
             * store_id : 1768385426
             * goods_name : 圣诞款之抱抱桶
             * logo_pic : http://txl.unohacha.com/Uploads/Picture/uploads/20180112/5a586abfa2e14.jpg
             * price : 329.00
             * store : 999
             * weight : 0.00
             * freight_id :
             * is_zt : 0
             * is_shopping : 0
             * type_status : 0
             * lebao_rate : 0.00
             * lebei_rate : 0.00
             * freight_info : {"id":"","classname":"","addtime":"","updata_time":"","store_id":"","supplier_id":""}
             * sku_info : 圣诞款之抱抱桶
             * num : 1
             */

            private String goods_id;
            private String store_id;
            private String goods_name;
            private String logo_pic;
            private String price;
            private String store;
            private String weight;
            private String freight_id;
            private String is_zt;
            private String is_shopping;
            private String type_status;
            private String lebao_rate;
            private String lebei_rate;
            private FreightInfoBean freight_info;
            private String sku_info;
            private String num;

            public String getGoods_id() {
                return goods_id;
            }

            public void setGoods_id(String goods_id) {
                this.goods_id = goods_id;
            }

            public String getStore_id() {
                return store_id;
            }

            public void setStore_id(String store_id) {
                this.store_id = store_id;
            }

            public String getGoods_name() {
                return goods_name;
            }

            public void setGoods_name(String goods_name) {
                this.goods_name = goods_name;
            }

            public String getLogo_pic() {
                return logo_pic;
            }

            public void setLogo_pic(String logo_pic) {
                this.logo_pic = logo_pic;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getStore() {
                return store;
            }

            public void setStore(String store) {
                this.store = store;
            }

            public String getWeight() {
                return weight;
            }

            public void setWeight(String weight) {
                this.weight = weight;
            }

            public String getFreight_id() {
                return freight_id;
            }

            public void setFreight_id(String freight_id) {
                this.freight_id = freight_id;
            }

            public String getIs_zt() {
                return is_zt;
            }

            public void setIs_zt(String is_zt) {
                this.is_zt = is_zt;
            }

            public String getIs_shopping() {
                return is_shopping;
            }

            public void setIs_shopping(String is_shopping) {
                this.is_shopping = is_shopping;
            }

            public String getType_status() {
                return type_status;
            }

            public void setType_status(String type_status) {
                this.type_status = type_status;
            }

            public String getLebao_rate() {
                return lebao_rate;
            }

            public void setLebao_rate(String lebao_rate) {
                this.lebao_rate = lebao_rate;
            }

            public String getLebei_rate() {
                return lebei_rate;
            }

            public void setLebei_rate(String lebei_rate) {
                this.lebei_rate = lebei_rate;
            }

            public FreightInfoBean getFreight_info() {
                return freight_info;
            }

            public void setFreight_info(FreightInfoBean freight_info) {
                this.freight_info = freight_info;
            }

            public String getSku_info() {
                return sku_info;
            }

            public void setSku_info(String sku_info) {
                this.sku_info = sku_info;
            }

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }

            public static class FreightInfoBean {
                /**
                 * id :
                 * classname :
                 * addtime :
                 * updata_time :
                 * store_id :
                 * supplier_id :
                 */

                private String id;
                private String classname;
                private String addtime;
                private String updata_time;
                private String store_id;
                private String supplier_id;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getClassname() {
                    return classname;
                }

                public void setClassname(String classname) {
                    this.classname = classname;
                }

                public String getAddtime() {
                    return addtime;
                }

                public void setAddtime(String addtime) {
                    this.addtime = addtime;
                }

                public String getUpdata_time() {
                    return updata_time;
                }

                public void setUpdata_time(String updata_time) {
                    this.updata_time = updata_time;
                }

                public String getStore_id() {
                    return store_id;
                }

                public void setStore_id(String store_id) {
                    this.store_id = store_id;
                }

                public String getSupplier_id() {
                    return supplier_id;
                }

                public void setSupplier_id(String supplier_id) {
                    this.supplier_id = supplier_id;
                }
            }
        }
    }
}
