package com.beyond.popscience.locationgoods.http;

import com.beyond.library.network.net.bean.BaseResponse;
import com.beyond.library.network.net.httpclient.NewCustomResponseHandler;
import com.beyond.popscience.frame.net.base.AppBaseRestUsageV2;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.locationgoods.bean.BannerBean;
import com.beyond.popscience.locationgoods.bean.NotificationBean;

import java.util.HashMap;
import java.util.List;

public class NotificationRestUsage extends AppBaseRestUsageV2 {
    /**
     * 系统消息列表
     */
    private final String pushList = AddressRestUsage.baseUrl + "push/pushList";
    private final String delete = AddressRestUsage.baseUrl + "push/delete/";
    private final String setIsRead = AddressRestUsage.baseUrl + "push/setIsRead/";
    private final String pushSize = AddressRestUsage.baseUrl + "push/pushSize/";

    public void pushList(int taskId, HashMap<String, Object> paramMap) {
        post(pushList, paramMap, new NewCustomResponseHandler<List<NotificationBean>>(taskId) {
        });
    }

    public void delete(int taskId, String id) {
        post(delete + id, null, new NewCustomResponseHandler<String>(taskId) {
        });
    }

    public void setIsRead(int taskId, String id) {
        post(setIsRead + id, null, new NewCustomResponseHandler<String>(taskId) {
        });
    }

    public void pushSize(int taskId, int type) {
        get(pushSize + type + "/" + UserInfoUtil.getInstance().getUserInfo().getUserId(), null, new NewCustomResponseHandler<String>(taskId) {
        });
    }

}
