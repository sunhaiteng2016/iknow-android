package com.beyond.popscience.locationgoods.bean;


import com.beyond.popscience.frame.pojo.BaseObject;


public class MAddressListBean extends BaseObject {


        /**
         * id : 50
         * userId : 151623
         * name : 孙海腾2
         * phoneNumber : 13561173094
         * defaultStatus : 0
         * postCode : 331082
         * province : 浙江省
         * city : 台州市
         * region : 临海市
         * detailAddress : 第二个详细地址
         */

        private int id;
        private int userId;
        private String name;
        private String phoneNumber;
        private int defaultStatus;
        private String postCode;
        private String province;
        private String city;
        private String region;
        private String detailAddress;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public int getDefaultStatus() {
            return defaultStatus;
        }

        public void setDefaultStatus(int defaultStatus) {
            this.defaultStatus = defaultStatus;
        }

        public String getPostCode() {
            return postCode;
        }

        public void setPostCode(String postCode) {
            this.postCode = postCode;
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

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getDetailAddress() {
            return detailAddress;
        }

        public void setDetailAddress(String detailAddress) {
            this.detailAddress = detailAddress;
        }
}
