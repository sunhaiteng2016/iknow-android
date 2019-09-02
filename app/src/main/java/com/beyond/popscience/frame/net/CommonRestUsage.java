package com.beyond.popscience.frame.net;

import com.beyond.library.network.net.httpclient.NewCustomResponseHandler;
import com.beyond.popscience.frame.net.base.AppBaseRestUsageV2;
import com.beyond.popscience.frame.pojo.AppInfo;
import com.beyond.popscience.frame.pojo.SystemTime;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by linjinfa on 2017/6/13.
 * email 331710168@qq.com
 */
public class CommonRestUsage extends AppBaseRestUsageV2 {

    /**
     * 获取服务器时间接口
     */
    private final String GET_SYSTEM_TIME = "/user/getSystemTime";

    /**
     * 关于我们
     */
    private final String ABOUT_US = "/user/aboutUS";

    /**
     * 意见反馈
     */
    private final String FEEDBACK = "/user/sentAdvice";
    /**
     * 获取版本更新文件
     */
    private final String GET_VERSION = "/user/getVersion";
    /**
     * 帮助
     */
    private final String GET_HELP = "/user/getHelp";
    /**
     * 是否设置了密码
     */
    private final String POST_IS_PWD = "/user/getHelp";
    /**
     * 获取阅读竞赛绿币
     */
    private final String GET_SCORE = "/user/getScore";

    /**
     * 获取版本更新文件
     * @param taskId
     */
    public void getVersion(int taskId, Object targetObj){
        get(GET_VERSION, null, new NewCustomResponseHandler<AppInfo>(taskId){}.setTargetObj(targetObj));
    }

    /**
     * 获取服务器时间
     * @param taskId
     */
    public void getSystemTime(int taskId){
        get(GET_SYSTEM_TIME, null, new NewCustomResponseHandler<SystemTime>(taskId){}.setCallSuperRefreshUI(false));
    }

    /**
     * 关于我们
     * @param taskId
     */
    public void aboutUs(int taskId){
        getCache(ABOUT_US, null, new NewCustomResponseHandler<HashMap<String,String>>(taskId){}.setCallSuperRefreshUI(false));

    }

    /**
     * 帮助
     * @param taskId
     */
    public void help(int taskId){
        getCache(GET_HELP, null, new NewCustomResponseHandler<HashMap<String,String>>(taskId){}.setCallSuperRefreshUI(false));
    }

    /**
     * 获取阅读竞赛绿币
     * @param taskId
     */
    public void getScore(int taskId){
        get (GET_SCORE, null, new NewCustomResponseHandler<HashMap<String,String>>(taskId){}.setCallSuperRefreshUI(false));
    }

    public void postFeedback(int taskId,String content){
        Map<String,String> params = new HashMap<>();
        params.put("comment",content);
        post(FEEDBACK, params, new NewCustomResponseHandler<String>(taskId){});
    }
}
