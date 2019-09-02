package com.beyond.popscience.frame.pojo;

import java.util.List;

/**
 * Created by zhouhuakang on 2017/9/14.
 */

public class MoreService extends BaseObject {
    private String serviceName;
    private List<MoreServiceContent> serviceContent;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<MoreServiceContent> getMoreServiceContentList() {
        return serviceContent;
    }

    public void setMoreServiceContentList(List<MoreServiceContent> moreServiceContentList) {
        this.serviceContent = moreServiceContentList;
    }
}
