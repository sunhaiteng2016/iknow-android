package com.beyond.popscience.locationgoods.bean;

import java.util.List;

public class RegisterNumBean {


    /**
     * total : 27
     * list : [{"userid":285888,"nickname":"丁","avatar":"http://www.appwzd.cn/micro_mart/ncss/logo.png","sex":1,"mobile":"13957630951","address":"浙江省仙居县","regtime":1563853039000},{"userid":285887,"nickname":"京","avatar":"http://www.appwzd.cn/micro_mart/ncss/logo.png","sex":1,"mobile":"15957680560","address":"浙江省仙居县","regtime":1563853004000},{"userid":285886,"nickname":"管锦帆","avatar":"http://www.appwzd.cn/micro_mart/ncss/logo.png","sex":1,"mobile":"15968644472","address":"新桥镇新桥居","regtime":1563755077000},{"userid":285885,"nickname":"大伟","avatar":"http://www.appwzd.cn/micro_mart/ncss/logo.png","sex":1,"mobile":"13661378792","address":"厚德","regtime":1563709481000},{"userid":285884,"nickname":"高燕","avatar":"http://www.appwzd.cn/micro_mart/ncss/logo.png","sex":0,"mobile":"13757600345","address":"上马村","regtime":1563621009000},{"userid":285883,"nickname":"开心","avatar":"http://www.appwzd.cn/micro_mart/ncss/logo.png","sex":1,"mobile":"13957686878","address":"浙江省仙居县","regtime":1563602115000},{"userid":285882,"nickname":"额","avatar":"http://www.appwzd.cn/micro_mart/ncss/logo.png","sex":1,"mobile":"13519808353","address":"陈家岙","regtime":1563599220000},{"userid":285881,"nickname":"圆圆","avatar":"http://www.appwzd.cn/micro_mart/ncss/logo.png","sex":1,"mobile":"15868681326","address":"浙江省仙居县","regtime":1563522028000},{"userid":285880,"nickname":"123123","avatar":"http://www.appwzd.cn/micro_mart/ncss/logo.png","sex":1,"mobile":"13575806155","address":"浙江省仙居县","regtime":1563521002000},{"userid":285879,"nickname":"123123","avatar":"http://www.appwzd.cn/micro_mart/ncss/logo.png","sex":1,"mobile":"137586155","address":"浙江省仙居县","regtime":1563520987000},{"userid":285878,"nickname":"百度一下","avatar":"http://www.appwzd.cn/micro_mart/ncss/logo.png","sex":1,"mobile":"13819682580","address":"浙江省仙居县","regtime":1563520963000},{"userid":285877,"nickname":"叮咚","avatar":"http://www.appwzd.cn/micro_mart/ncss/logo.png","sex":1,"mobile":"13018859693","address":"浙江省仙居县","regtime":1563520898000},{"userid":285876,"nickname":"幸福","avatar":"http://www.appwzd.cn/micro_mart/ncss/logo.png","sex":1,"mobile":"15057600500","address":"浙江省仙居县","regtime":1563506600000},{"userid":285875,"nickname":"福气","avatar":"http://www.appwzd.cn/micro_mart/ncss/logo.png","sex":1,"mobile":"13575877754","address":"浙江省仙居县","regtime":1563506558000},{"userid":285873,"nickname":"1111111","avatar":"http://www.appwzd.cn/micro_mart/ncss/logo.png","sex":1,"mobile":"13615869905","address":"浙江省仙居县","regtime":1563504175000},{"userid":285872,"nickname":"无恒","avatar":"http://www.appwzd.cn/micro_mart/ncss/logo.png","sex":1,"mobile":"13586027425","address":"浙江省仙居县","regtime":1563504111000},{"userid":285871,"nickname":"陈陈","avatar":"http://www.appwzd.cn/micro_mart/ncss/logo.png","sex":1,"mobile":"18657655877","address":"浙江省仙居县","regtime":1563504047000},{"userid":285870,"nickname":"佳伟","avatar":"http://www.appwzd.cn/micro_mart/ncss/logo.png","sex":1,"mobile":"13606672692","address":"浙江省仙居县","regtime":1563504007000},{"userid":285869,"nickname":"卖鸡的","avatar":"http://www.appwzd.cn/micro_mart/ncss/logo.png","sex":1,"mobile":"13777632187","address":"浙江省仙居县","regtime":1563503979000},{"userid":285868,"nickname":"卖鞋的","avatar":"http://www.appwzd.cn/micro_mart/ncss/logo.png","sex":1,"mobile":"13486869991","address":"浙江省仙居县","regtime":1563503948000}]
     */

    private int total;
    private List<ListBean> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * userid : 285888
         * nickname : 丁
         * avatar : http://www.appwzd.cn/micro_mart/ncss/logo.png
         * sex : 1
         * mobile : 13957630951
         * address : 浙江省仙居县
         * regtime : 1563853039000
         */

        private int userid;
        private String nickname;
        private String avatar;
        private int sex;
        private String mobile;
        private String address;
        private long regtime;
        private  String shopName;
        private String jobname;
        private String realname;
        private String username;
        private String detailedaddress;
        private  String goodsName;
        private  String title;
        private  String selltype;
        private  String createtime;
        private  String trade;

        public String getTrade() {
            return trade;
        }

        public void setTrade(String trade) {
            this.trade = trade;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getSelltype() {
            return selltype;
        }

        public void setSelltype(String selltype) {
            this.selltype = selltype;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public String getJobname() {
            return jobname;
        }

        public void setJobname(String jobname) {
            this.jobname = jobname;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getDetailedaddress() {
            return detailedaddress;
        }

        public void setDetailedaddress(String detailedaddress) {
            this.detailedaddress = detailedaddress;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public long getRegtime() {
            return regtime;
        }

        public void setRegtime(long regtime) {
            this.regtime = regtime;
        }
    }
}
