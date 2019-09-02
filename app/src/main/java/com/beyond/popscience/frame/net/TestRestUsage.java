package com.beyond.popscience.frame.net;

import com.beyond.library.network.net.httpclient.NewCustomResponseHandler;
import com.beyond.popscience.frame.net.base.AppBaseRestUsageV2;

/**
 * Created by linjinfa on 2017/6/8.
 * email 331710168@qq.com
 */
public class TestRestUsage extends AppBaseRestUsageV2 {

    /**
     * 请求有缓存
     * @param taskId
     */
    public void test(int taskId){
        postCache("/test/linjinfa", null, new NewCustomResponseHandler<Object>(taskId){});
    }

    /**
     * 请求无缓存
     * @param taskId
     */
    public void test2(int taskId){
        post("/test/linjinfa", null, new NewCustomResponseHandler<Object>(taskId){});
    }

}
