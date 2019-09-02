package com.beyond.popscience.frame.net;

import com.beyond.library.network.net.httpclient.NewCustomResponseHandler;
import com.beyond.library.util.L;
import com.beyond.popscience.frame.net.base.AppBaseRestUsageV2;
import com.beyond.popscience.frame.pojo.CollectionResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by linjinfa on 2017/6/14.
 * email 331710168@qq.com
 */
public class CollectionRestUsage extends AppBaseRestUsageV2 {

    private final String GET_MY_LIKE_NEWS = "/user/getMyLikeNews/%s";

    /**
     * 新闻收藏
     */
    private final String NEWS_COLLECTION = "/user/likeNews";

    /**
     * 删除我的收藏
     */
    private final String DELETE_NEWS_COLLECTION = "/user/delete/likeNews";

    /**
     * 获取我的新闻
     * @param taskId
     * @param page
     */
    public void getMyLikeNews(int taskId, int page){
        get(String.format(GET_MY_LIKE_NEWS, page), null, new NewCustomResponseHandler<CollectionResponse>(taskId){});
    }

    /**
     * 新闻收藏
     */
    public void newCollection(int taskId, String newsId){
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("newsId", newsId);

        L.e("===================新闻收藏======================================newsId=" + newsId + "=========" + NEWS_COLLECTION + "===paramMap= " + paramMap);
        post(NEWS_COLLECTION, paramMap, new NewCustomResponseHandler<String>(taskId){}.setCallSuperRefreshUI(false));
    }

    /**
     * 删除新闻收藏
     */
    public void deleteNewCollection(int taskId, String newsId, Object targetObj){
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("newsId", newsId);
        post(DELETE_NEWS_COLLECTION, paramMap, new NewCustomResponseHandler<String>(taskId){}.setTargetObj(targetObj));
    }

}
