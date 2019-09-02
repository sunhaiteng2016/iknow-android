package com.beyond.library.sharesdk.shareInfo;


import java.util.HashMap;

/**
 * Created by lenovo on 2015/8/19.
 */
public interface ICustomShareFields {
    void setSelf(String platformName, BaseShareFields fields);

    HashMap<String, BaseShareFields> getMutiMap();
}
