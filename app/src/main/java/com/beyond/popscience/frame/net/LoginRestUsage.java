package com.beyond.popscience.frame.net;

import com.beyond.library.network.net.httpclient.NewCustomResponseHandler;
import com.beyond.popscience.frame.net.base.AppBaseRestUsageV2;
import com.beyond.popscience.frame.pojo.UserInfo;
import com.beyond.popscience.utils.SystemUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录
 * Created by linjinfa on 2017/6/13.
 * email 331710168@qq.com
 */
public class LoginRestUsage extends AppBaseRestUsageV2 {

    /**
     *登录
     */
    private final String LOGIN = "/user/login";
    /**
     * 退出登录
     */
    private final String LOGOUT = "/user/logout";

    /**
     * 登录
     */
    public void login(int taskId, String account, String pwd){
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("account", account);
        paramMap.put("password", pwd);
        paramMap.put("phoneBrand",SystemUtil.getSystemModel());
        post(LOGIN, paramMap, new NewCustomResponseHandler<UserInfo>(taskId){}.setCallSuperRefreshUI(false));
    }

    /**
     * 退出登录
     */
    public void loginOut(int taskId){
        post(LOGOUT, null, new NewCustomResponseHandler<String>(taskId){});
    }

}
