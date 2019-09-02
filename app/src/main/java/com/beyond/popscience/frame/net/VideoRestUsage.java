package com.beyond.popscience.frame.net;

import com.beyond.library.network.net.httpclient.NewCustomResponseHandler;
import com.beyond.popscience.frame.net.base.AppBaseRestUsageV2;
import com.beyond.popscience.frame.pojo.NewsDetailObj;

/**
 * Created by zhouhuakang on 2017/8/27.
 */

public class VideoRestUsage extends AppBaseRestUsageV2 {


    public void getVideo(int taskId,String url){
        get(url,null,false,new NewCustomResponseHandler<Object>(taskId){});
    }
}
