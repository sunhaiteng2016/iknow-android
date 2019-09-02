package com.beyond.popscience.frame.net;

import com.beyond.library.network.net.httpclient.NewCustomResponseHandler;
import com.beyond.popscience.frame.net.base.AppBaseRestUsageV2;
import com.beyond.popscience.frame.pojo.CommentResponse;
import com.beyond.popscience.frame.pojo.ReplyResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * 评论
 * Created by linjinfa on 2017/6/13.
 * email 331710168@qq.com
 */
public class CommentRestUsage extends AppBaseRestUsageV2 {

    /**
     * 发表评论
     */
    private final String SENT_COMMENT = "/user/sentComment";
    /**
     * 发表服务评论
     */
    private final String SENT_SERVICE_COMMENT = "/common/sentComment";
    /**
     * 获取服务评论
     */
    private final String GET_COMMENT = "/common/getComment/%s/%s/%s";
    /**
     * 获取新闻评论列表
     */
    private final String GET_NEWS_COMMENT = "/news/getNewsComment/%s/%s";
    /**
     * 点赞
     */
    private final String PRAISE = "/user/praise/%s/%s";
    /**
     * 举报
     */
    private final String REPORT = "/user/report/%s";
    /**
     * 评论回复
     */
    private final String REPLY_COMMENT = "/user/replyComment";
    /**
     * 获取评论回复
     */
    private final String GET_REPLY = "/news/getReply/%s/%s";

    /**
     * 发表评论
     */
    public void sendComment(int taskId, String newsId, String content){
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("newsId", newsId);
        paramMap.put("comment", content);
        post(SENT_COMMENT, paramMap, new NewCustomResponseHandler<String>(taskId){});
    }

    /**
     * 发表服务评论
     */
    public void sendServiceComment(int taskId, String type, String uid, String content){
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("type", type);
        paramMap.put("uid", uid);
        paramMap.put("comment", content);
        post(SENT_SERVICE_COMMENT, paramMap, new NewCustomResponseHandler<String>(taskId){});
    }

    /**
     * 评论回复
     */
    public void replyComment(int taskId, String commentId, String content){
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("commentId", commentId);
        paramMap.put("comment", content);
        post(REPLY_COMMENT, paramMap, new NewCustomResponseHandler<String>(taskId){});
    }

    /**
     * 获取新闻评论列表
     */
    public void getNewsComment(int taskId, String newsId, int page){
//        if(page == 1){
//            getCache(String.format(GET_NEWS_COMMENT, newsId == null ? "" : newsId, page), null, new NewCustomResponseHandler<CommentResponse>(taskId){});
//        }else{
//            get(String.format(GET_NEWS_COMMENT, newsId == null ? "" : newsId, page), null, new NewCustomResponseHandler<CommentResponse>(taskId){});
//        }
        get(String.format(GET_NEWS_COMMENT, newsId == null ? "" : newsId, page), null, new NewCustomResponseHandler<CommentResponse>(taskId){});
    }

    /**
     * 获取商品评论列表
     */
    public void getComment(int taskId, int page, String type, String uid){
        get(String.format(GET_COMMENT, page, type == null ? "" : type, uid == null ? "" : uid), null, new NewCustomResponseHandler<CommentResponse>(taskId){});
    }

    /**
     * 获取评论回复
     */
    public void getReply(int taskId, String commentId, int page){
//        if(page == 1){
//            getCache(String.format(GET_REPLY, commentId == null ? "" : commentId, page), null, new NewCustomResponseHandler<ReplyResponse>(taskId){});
//        }else{
//            get(String.format(GET_REPLY, commentId == null ? "" : commentId, page), null, new NewCustomResponseHandler<ReplyResponse>(taskId){});
//        }
        get(String.format(GET_REPLY, commentId == null ? "" : commentId, page), null, new NewCustomResponseHandler<ReplyResponse>(taskId){});
    }

    /**
     * 点赞
     * @param type 1:评论 2:回复
     */
    public void praise(int taskId, String type, String typeId, Object targetObj){
        get(String.format(PRAISE, type == null ? "" : type, typeId == null ? "" : typeId), null, new NewCustomResponseHandler<String>(taskId){}.setTargetObj(targetObj).setCallSuperRefreshUI(false));
    }

    /**
     * 举报
     */
    public void report(int taskId, String commentId){
        get(String.format(REPORT, commentId == null ? "" : commentId), null, new NewCustomResponseHandler<String>(taskId){});
    }

}
