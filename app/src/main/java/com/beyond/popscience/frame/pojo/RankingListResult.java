package com.beyond.popscience.frame.pojo;

import java.util.List;

/**
 * Created by Administrator on 2018/5/9 0009.
 * 积分商城排行榜
 */

public class RankingListResult extends BaseObject {
    private Ranking score;
    private List<Ranking> liststq;

    public Ranking getScore() {
        return score;
    }

    public void setScore(Ranking score) {
        this.score = score;
    }

    public List<Ranking> getListstq() {
        return liststq;
    }

    public void setListstq(List<Ranking> liststq) {
        this.liststq = liststq;
    }

    public class Ranking {
        private String recordid;
        private String recordname;
        private String recordtime;
        private String recordintegral;
        private String recordimg;
        private String userid;
        private String sum;

        public String getRecordid() {
            return recordid;
        }

        public void setRecordid(String recordid) {
            this.recordid = recordid;
        }

        public String getRecordname() {
            return recordname;
        }

        public void setRecordname(String recordname) {
            this.recordname = recordname;
        }

        public String getRecordtime() {
            return recordtime;
        }

        public void setRecordtime(String recordtime) {
            this.recordtime = recordtime;
        }

        public String getRecordintegral() {
            return recordintegral;
        }

        public void setRecordintegral(String recordintegral) {
            this.recordintegral = recordintegral;
        }

        public String getRecordimg() {
            return recordimg;
        }

        public void setRecordimg(String recordimg) {
            this.recordimg = recordimg;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getSum() {
            return sum;
        }

        public void setSum(String sum) {
            this.sum = sum;
        }

        @Override
        public String toString() {
            return "Ranking{" +
                    "recordid='" + recordid + '\'' +
                    ", recordname='" + recordname + '\'' +
                    ", recordtime='" + recordtime + '\'' +
                    ", recordintegral='" + recordintegral + '\'' +
                    ", recordimg='" + recordimg + '\'' +
                    ", userid='" + userid + '\'' +
                    ", sum='" + sum + '\'' +
                    '}';
        }
    }
}
