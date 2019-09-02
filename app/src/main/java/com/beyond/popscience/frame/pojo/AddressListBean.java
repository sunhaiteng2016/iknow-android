package com.beyond.popscience.frame.pojo;

import java.util.List;

/**
 * 获取地址列表的实体类
 */

public class AddressListBean extends BaseObject {
    /**
     * code : 0
     * message : 获取列表成功
     * data : [{"addressId":1045,"userId":125396,"contactName":"啊啊啊啊啊啊啊啊啊","contactPhone":"1899999999999","province":"河南54554","city":"信阳","area":"拱墅区","street":"456号街","addressDetail":"456号","createTime":1520859344000,"updateTime":1520859344000,"status":2,"isDelete":0},{"addressId":1064,"userId":125396,"contactName":"华为客服","contactPhone":"4008308300","province":"安徽省","city":"安庆市","area":"枞阳县","street":"挂着的","addressDetail":"挂着的","createTime":1520908773000,"updateTime":1520908773000,"status":2,"isDelete":0},{"addressId":1065,"userId":125396,"contactName":"空","contactPhone":"15365734646","province":"安徽省","city":"安庆市","area":"枞阳县","street":"谁知道呢","addressDetail":"谁知道呢","createTime":1520909498000,"updateTime":1520909498000,"status":2,"isDelete":0},{"addressId":1066,"userId":125396,"contactName":"空","contactPhone":"15365734646","province":"安徽省","city":"安庆市","area":"枞阳县","street":"谁知道呢","addressDetail":"谁知道呢","createTime":1520909533000,"updateTime":1520909533000,"status":2,"isDelete":0},{"addressId":1067,"userId":125396,"contactName":"空","contactPhone":"15365734646","province":"安徽省","city":"安庆市","area":"枞阳县","street":"谁知道呢1","addressDetail":"谁知道呢1","createTime":1520909871000,"updateTime":1520909871000,"status":2,"isDelete":0},{"addressId":1068,"userId":125396,"contactName":"华为客服","contactPhone":"4008308300","province":"安徽省","city":"宣城市","area":"广德县","street":"cup地图","addressDetail":"cup地图","createTime":1520911332000,"updateTime":1520911332000,"status":2,"isDelete":0},{"addressId":1073,"userId":125396,"contactName":"luop罗鹏","contactPhone":"18779886073","province":"安徽省","city":"安庆市","area":"枞阳县","street":"后rosy","addressDetail":"后rosy","createTime":1520913246000,"updateTime":1520913246000,"status":2,"isDelete":0},{"addressId":1089,"userId":125396,"contactName":"杨阳洋","contactPhone":"18257126874","province":"安徽省","city":"安庆市","area":"佛教","street":"280号","addressDetail":"280号","createTime":1520923318000,"updateTime":1520923318000,"status":2,"isDelete":0},{"addressId":1090,"userId":125396,"contactName":"luop罗鹏","contactPhone":"18779886073","province":"安徽省","city":"安庆市","area":"枞阳县","street":"扩大朋","addressDetail":"扩大朋","createTime":1520925214000,"updateTime":1520925214000,"status":1,"isDelete":0},{"addressId":1091,"userId":125396,"contactName":"luop罗鹏","contactPhone":"18779886073","province":"安徽省","city":"安庆市","area":"枞阳县","street":"扩大朋","addressDetail":"扩大朋","createTime":1520925214000,"updateTime":1520925214000,"status":1,"isDelete":0},{"addressId":1092,"userId":125396,"contactName":"华为客服","contactPhone":"4008308300","province":"安徽省","city":"安庆市","area":"枞阳县","street":"你说呢你说呢","addressDetail":"你说呢你说呢","createTime":1520925348000,"updateTime":1520925348000,"status":1,"isDelete":0},{"addressId":1093,"userId":125396,"contactName":"luop罗鹏","contactPhone":"18779886073","province":"安徽省","city":"安庆市","area":"枞阳县","street":"嗯嗯","addressDetail":"嗯嗯","createTime":1520925462000,"updateTime":1520925462000,"status":1,"isDelete":0},{"addressId":1094,"userId":125396,"contactName":"杨阳洋","contactPhone":"18257126874","province":"安徽省","city":"安庆市","area":"佛教","street":"280号","addressDetail":"280号","createTime":1520928868000,"updateTime":1520928868000,"status":1,"isDelete":0},{"addressId":1095,"userId":125396,"contactName":"闯","contactPhone":"18257126874","province":"安徽省","city":"安庆市","area":"枞阳县","street":"安徽的","addressDetail":"安徽的","createTime":1520928900000,"updateTime":1520928900000,"status":1,"isDelete":0}]
     */

    private int code;
    private String message;
    private List<DataBean>data ;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

//
//    private int code;
//    private String message;
//    private List<DataBean> data;
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
//    public List<DataBean> getData() {
//        return data;
//    }
//
//    public void setData(List<DataBean> data) {
//        this.data = data;
//    }
//
//    public static class DataBean {
//        /**
//         * addressId : 1043
//         * userId : 101631
//         * contactName : 赵英
//         * contactPhone : 18212189630
//         * province : 河北
//         * city : 石家庄
//         * area : 789
//         * street : 固安
//         * addressDetail : 地球村306号
//         * createTime : 1520335521000
//         * updateTime : 1520335523000
//         * status : 3
//         * isDelete : 2
//         */
//
//        private int addressId;
//        private int userId;
//        private String contactName;
//        private String contactPhone;
//        private String province;
//        private String city;
//        private String area;
//        private String street;
//        private String addressDetail;
//        private long createTime;
//        private long updateTime;
//        private int status;
//        private int isDelete;
//
//        public int getAddressId() {
//            return addressId;
//        }
//
//        public void setAddressId(int addressId) {
//            this.addressId = addressId;
//        }
//
//        public int getUserId() {
//            return userId;
//        }
//
//        public void setUserId(int userId) {
//            this.userId = userId;
//        }
//
//        public String getContactName() {
//            return contactName;
//        }
//
//        public void setContactName(String contactName) {
//            this.contactName = contactName;
//        }
//
//        public String getContactPhone() {
//            return contactPhone;
//        }
//
//        public void setContactPhone(String contactPhone) {
//            this.contactPhone = contactPhone;
//        }
//
//        public String getProvince() {
//            return province;
//        }
//
//        public void setProvince(String province) {
//            this.province = province;
//        }
//
//        public String getCity() {
//            return city;
//        }
//
//        public void setCity(String city) {
//            this.city = city;
//        }
//
//        public String getArea() {
//            return area;
//        }
//
//        public void setArea(String area) {
//            this.area = area;
//        }
//
//        public String getStreet() {
//            return street;
//        }
//
//        public void setStreet(String street) {
//            this.street = street;
//        }
//
//        public String getAddressDetail() {
//            return addressDetail;
//        }
//
//        public void setAddressDetail(String addressDetail) {
//            this.addressDetail = addressDetail;
//        }
//
//        public long getCreateTime() {
//            return createTime;
//        }
//
//        public void setCreateTime(long createTime) {
//            this.createTime = createTime;
//        }
//
//        public long getUpdateTime() {
//            return updateTime;
//        }
//
//        public void setUpdateTime(long updateTime) {
//            this.updateTime = updateTime;
//        }
//
//        public int getStatus() {
//            return status;
//        }
//
//        public void setStatus(int status) {
//            this.status = status;
//        }
//
//        public int getIsDelete() {
//            return isDelete;
//        }
//
//        public void setIsDelete(int isDelete) {
//            this.isDelete = isDelete;
//        }
//    }



}
