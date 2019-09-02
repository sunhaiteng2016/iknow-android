package com.beyond.popscience.frame.net;

import com.beyond.library.network.net.httpclient.NewCustomResponseHandler;
import com.beyond.popscience.frame.net.base.AppBaseRestUsageV2;

/**
 * Created by zhouhuakang on 2017/9/27.
 */

public class TabClickCountRestUsage extends AppBaseRestUsageV2{
    private final String GET_TAB_CLICK = "/user/clickTab/%s";

    /**
     * 上传tab点击次数
     * @param taskId
     * @param type
     */
    public void postTabClick(int taskId, int type){
        String url = String.format(GET_TAB_CLICK, type);
        get(url, null,new NewCustomResponseHandler<Object>(taskId){}.setCallSuperRefreshUI(true));
    }

}
