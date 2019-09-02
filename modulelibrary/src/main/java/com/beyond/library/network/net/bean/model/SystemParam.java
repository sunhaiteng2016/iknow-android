package com.beyond.library.network.net.bean.model;

import java.io.Serializable;

/**
 * Created by East.K on 2016/12/15.
 *
 * @ClassName: SystemParam
 * @Description: 前端系统级参数
 */

public class SystemParam implements Serializable {
    /********************************************************
     * 应用标识符(包名)
     ********************************************************/
    private String appIdentifier = null;

    /********************************************************
     * 应用类型
     ********************************************************/
    private Application application = null;

    /********************************************************
     * APP当前页面简单类名
     ********************************************************/
    private String pageName = null;

    /********************************************************
     * 应用版本号
     ********************************************************/
    private String appVersion;

    /********************************************************
     * 系统版本号
     ********************************************************/
    private String systemVersion;

    /********************************************************
     * 硬件类型(如“iphone 6s”, “Galaxy Edge7”等)
     ********************************************************/
    private String hardware;

    public String getAppIdentifier() {
        return appIdentifier;
    }

    public void setAppIdentifier(String appIdentifier) {
        this.appIdentifier = appIdentifier;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }

    public String getHardware() {
        return hardware;
    }

    public void setHardware(String hardware) {
        this.hardware = hardware;
    }
}
