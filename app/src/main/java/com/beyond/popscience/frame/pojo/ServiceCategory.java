package com.beyond.popscience.frame.pojo;

/**
 * 服务 类型 外链+c2c
 * Created by yao.cui on 2017/6/29.
 */

public class ServiceCategory extends BaseObject {
    private String tabId;
    private String tabName;
    private String tabType;
    private String tabPic;
    private String tabUrl;
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTabId() {
        return tabId;
    }

    public void setTabId(String tabId) {
        this.tabId = tabId;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public String getTabType() {
        return tabType;
    }

    public void setTabType(String tabType) {
        this.tabType = tabType;
    }

    public String getTabPic() {
        return tabPic;
    }

    public void setTabPic(String tabPic) {
        this.tabPic = tabPic;
    }

    public String getTabUrl() {
        return tabUrl;
    }

    public void setTabUrl(String tabUrl) {
        this.tabUrl = tabUrl;
    }


    @Override
    public String toString() {
        return "ServiceCategory{" +
                "tabId='" + tabId + '\'' +
                ", tabName='" + tabName + '\'' +
                ", tabType='" + tabType + '\'' +
                ", tabPic='" + tabPic + '\'' +
                ", tabUrl='" + tabUrl + '\'' +
                '}';
    }
}
