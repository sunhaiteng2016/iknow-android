package com.beyond.library.sharesdk.shareInfo;

import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by lenovo on 2015/8/19.
 */
public class MutiShareFields {

    private HashMap<String, BaseShareFields> mutiMap;
    private Platform[] platforms;
    private ICustomShareFields i_shareField;

    public MutiShareFields(ICustomShareFields listener) {
        mutiMap = new HashMap<>();
        platforms = ShareSDK.getPlatformList();
        i_shareField = listener;
    }

    public HashMap<String, BaseShareFields> getMutiMap() {
        return mutiMap;
    }

    public void initData(List<Platform> platformName) {
        if (platforms == null || platforms.length <= 0) {
            return;
        }
        BaseShareFields baseFields;
        if (platformName == null) {
            for (Platform platform : platforms) {
                baseFields = new BaseShareFields();
                String name = platform.getName();
                if (i_shareField != null)
                    i_shareField.setSelf(name, baseFields);
                mutiMap.put(name, baseFields);
            }
        } else {
            for (Platform item : platformName) {
                baseFields = new BaseShareFields();
                String name = item.getName();
                if (i_shareField != null) {
                    i_shareField.setSelf(name, baseFields);
                }
                mutiMap.put(name, baseFields);
            }
        }
    }
}
