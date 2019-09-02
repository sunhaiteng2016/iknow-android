package com.beyond.popscience.locationgoods.bean;

import java.util.List;

public class WlBean  {

    /**
     * status : 0
     * msg : ok
     * result : {"number":"231580571635","type":"SFEXPRESS","list":[{"time":"2019-05-13 09:33:29","status":"[安阳市]快件到达 【安阳市林州市向阳街营业点】"},{"time":"2019-05-13 09:10:19","status":"[安阳市]快件交给付潮杰，正在派送途中（联系电话：17539573570）"},{"time":"2019-05-13 08:54:30","status":"[安阳市]正在派送途中"},{"time":"2019-05-13 06:31:06","status":"[安阳市]快件已发车"},{"time":"2019-05-12 23:45:31","status":"[安阳市]快件在【安阳高新集散点】已装车"},{"time":"2019-05-12 19:54:15","status":"[安阳市]快件到达 【安阳高新集散点】"},{"time":"2019-05-12 16:59:13","status":"[郑州市]快件已发车"},{"time":"2019-05-12 14:26:18","status":"[郑州市]快件已发车"},{"time":"2019-05-12 14:14:59","status":"[郑州市]快件在【郑州港区中转场】已装车"},{"time":"2019-05-12 14:08:36","status":"[郑州市]快件到达 【郑州总集散中心】"},{"time":"2019-05-12 13:46:58","status":"[郑州市]快件到达 【郑州港区中转场】"},{"time":"2019-05-12 13:25:09","status":"[郑州市]快件在【郑州总集散中心】已装车"},{"time":"2019-05-12 02:49:26","status":"[深圳市]快件在【深圳总集散中心】已装车"},{"time":"2019-05-12 01:55:59","status":"[深圳市]快件到达 【深圳总集散中心】"},{"time":"2019-05-11 23:18:28","status":"[惠州市]快件在【惠州陈江集散中心】已装车"},{"time":"2019-05-11 22:13:56","status":"[惠州市]快件到达 【惠州陈江集散中心】"},{"time":"2019-05-11 19:22:55","status":"[惠州市]快件在【惠州市惠城区集收客户营业部】已装车"},{"time":"2019-05-11 19:18:52","status":"[惠州市]顺丰速运 已收取快件"}],"deliverystatus":"2","issign":"0","expName":"顺丰快递","expSite":"www.sf-express.com ","expPhone":"95338","logo":"http://img3.fegine.com/express/sf.jpg","courier":"","courierPhone":""}
     */

    private String status;
    private String msg;
    private ResultBean result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * number : 231580571635
         * type : SFEXPRESS
         * list : [{"time":"2019-05-13 09:33:29","status":"[安阳市]快件到达 【安阳市林州市向阳街营业点】"},{"time":"2019-05-13 09:10:19","status":"[安阳市]快件交给付潮杰，正在派送途中（联系电话：17539573570）"},{"time":"2019-05-13 08:54:30","status":"[安阳市]正在派送途中"},{"time":"2019-05-13 06:31:06","status":"[安阳市]快件已发车"},{"time":"2019-05-12 23:45:31","status":"[安阳市]快件在【安阳高新集散点】已装车"},{"time":"2019-05-12 19:54:15","status":"[安阳市]快件到达 【安阳高新集散点】"},{"time":"2019-05-12 16:59:13","status":"[郑州市]快件已发车"},{"time":"2019-05-12 14:26:18","status":"[郑州市]快件已发车"},{"time":"2019-05-12 14:14:59","status":"[郑州市]快件在【郑州港区中转场】已装车"},{"time":"2019-05-12 14:08:36","status":"[郑州市]快件到达 【郑州总集散中心】"},{"time":"2019-05-12 13:46:58","status":"[郑州市]快件到达 【郑州港区中转场】"},{"time":"2019-05-12 13:25:09","status":"[郑州市]快件在【郑州总集散中心】已装车"},{"time":"2019-05-12 02:49:26","status":"[深圳市]快件在【深圳总集散中心】已装车"},{"time":"2019-05-12 01:55:59","status":"[深圳市]快件到达 【深圳总集散中心】"},{"time":"2019-05-11 23:18:28","status":"[惠州市]快件在【惠州陈江集散中心】已装车"},{"time":"2019-05-11 22:13:56","status":"[惠州市]快件到达 【惠州陈江集散中心】"},{"time":"2019-05-11 19:22:55","status":"[惠州市]快件在【惠州市惠城区集收客户营业部】已装车"},{"time":"2019-05-11 19:18:52","status":"[惠州市]顺丰速运 已收取快件"}]
         * deliverystatus : 2
         * issign : 0
         * expName : 顺丰快递
         * expSite : www.sf-express.com
         * expPhone : 95338
         * logo : http://img3.fegine.com/express/sf.jpg
         * courier :
         * courierPhone :
         */

        private String number;
        private String type;
        private String deliverystatus;
        private String issign;
        private String expName;
        private String expSite;
        private String expPhone;
        private String logo;
        private String courier;
        private String courierPhone;
        private List<ListBean> list;

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDeliverystatus() {
            return deliverystatus;
        }

        public void setDeliverystatus(String deliverystatus) {
            this.deliverystatus = deliverystatus;
        }

        public String getIssign() {
            return issign;
        }

        public void setIssign(String issign) {
            this.issign = issign;
        }

        public String getExpName() {
            return expName;
        }

        public void setExpName(String expName) {
            this.expName = expName;
        }

        public String getExpSite() {
            return expSite;
        }

        public void setExpSite(String expSite) {
            this.expSite = expSite;
        }

        public String getExpPhone() {
            return expPhone;
        }

        public void setExpPhone(String expPhone) {
            this.expPhone = expPhone;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getCourier() {
            return courier;
        }

        public void setCourier(String courier) {
            this.courier = courier;
        }

        public String getCourierPhone() {
            return courierPhone;
        }

        public void setCourierPhone(String courierPhone) {
            this.courierPhone = courierPhone;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * time : 2019-05-13 09:33:29
             * status : [安阳市]快件到达 【安阳市林州市向阳街营业点】
             */

            private String time;
            private String status;

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }
    }
}
