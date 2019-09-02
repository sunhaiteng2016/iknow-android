package com.beyond.popscience.frame.net;

import com.beyond.library.network.net.httpclient.NewCustomResponseHandler;
import com.beyond.popscience.frame.net.base.AppBaseRestUsageV2;

import java.util.HashMap;

/**
 * Created by yao.cui on 2017/6/14.
 */

public class GuideRestUsage extends AppBaseRestUsageV2 {

    private final String WELCOME_IMG = "/user/launchPage";//欢迎界面

    /**
     * 获取欢迎图片
     *
     * @param taskId
     */
    public void getWelcomeImg(int taskId) {
        getCache(WELCOME_IMG, null, new NewCustomResponseHandler<HashMap<String, String>>(taskId) {
        }.setCallSuperRefreshUI(false));
    }
}
