package com.beyond.popscience.frame.pojo;

import android.text.TextUtils;

/**
 * Created by linjinfa on 2017/6/16.
 * email 331710168@qq.com
 */
public class AppInfo extends BaseObject {

    private String version;
    private String url;
    private  String hint;

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取版本号
     * @return
     */
    public int getVersionCode(){
        if(!TextUtils.isEmpty(version)){
            String mVersion = version;
            if(mVersion.contains(".")){
                mVersion = mVersion.replace(".", "");
            }
            if (mVersion.length() < 3) {
                for(int i = 0; i <= (3 - mVersion.length()); i++){
                    mVersion +="0";
                }
            }
            try {
                return Integer.parseInt(mVersion);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

}
