package com.beyond.popscience.frame.pojo;

/**
 * Created by linjinfa on 2017/6/14.
 * email 331710168@qq.com
 */
public class SystemTime extends BaseObject {

    private String systemTime;
    private long updateTimeStamp;

    public String getSystemTime() {
        return systemTime;
    }

    public void setSystemTime(String systemTime) {
        this.systemTime = systemTime;
    }

    public long getUpdateTimeStamp() {
        return updateTimeStamp;
    }

    public void setUpdateTimeStamp(long updateTimeStamp) {
        this.updateTimeStamp = updateTimeStamp;
    }
}
