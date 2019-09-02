package com.beyond.popscience.frame.net;

import android.text.TextUtils;

import com.beyond.library.network.net.httpclient.NewCustomResponseHandler;
import com.beyond.popscience.frame.net.base.AppBaseRestUsageV2;
import com.beyond.popscience.frame.pojo.AddressPicInfo;
import com.beyond.popscience.frame.pojo.Carousel;
import com.beyond.popscience.frame.pojo.CollectionResponse;
import com.beyond.popscience.frame.pojo.CommentResponse;
import com.beyond.popscience.frame.pojo.NewsDetailContent;
import com.beyond.popscience.frame.pojo.NewsDetailObj;
import com.beyond.popscience.frame.pojo.NewsListObg;
import com.beyond.popscience.frame.pojo.NewsResponse;
import com.beyond.popscience.frame.pojo.ReplyResponse;
import com.beyond.popscience.frame.pojo.SuggestVo;
import com.beyond.popscience.module.home.entity.TownUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 城镇
 */
public class TownRestUsage extends AppBaseRestUsageV2 {

    /**
     * 获取我的新闻收藏
     */
    private final String GET_MY_LIKE_NEWS = "/villages/getMyLikeNews/%s";
    /**
     * 乡村简介
     */
    private final String GET_VILLAGE_MESSAGE = "/villages/getVillageMessage/%s";
    /**
     * 举报
     */
    private final String REPORT = "/villages/report/%s";
    /**
     * 点赞
     */
    private final String PRAISE = "/villages/praise/%s/%s";
    /**
     * 删除我的收藏
     */
    private final String DELETE_NEWS_COLLECTION = "/villages/delete/likeNews";
    /**
     * 新闻收藏
     */
    private final String NEWS_COLLECTION = "/villages/likeNews";
    /**
     * 热门搜索
     */
    private final String GET_HOT_SEARCH = "/villages/getHotSearch";
    /**
     * 获取乡镇下所有图片
     */
    private final String GET_PICTURE = "/villages/getPicture";
    /**
     * 获取轮播图
     */
    private final String GET_CAROUSEL = "/villages/getCarousel/%s";
    /**
     * 获取乡镇新闻列表
     */
    private final String GET_NEWS = "/villages/getNews/%s/%s";
    /**
     *
     */
    private final String GET_NEWS_DETAIL = "/villages/getNewsDetail/%s";

    /**
     * 发表评论
     */
    private final String SENT_COMMENT = "/villages/sentComment";
    /**
     * 获取新闻评论列表
     */
    private final String GET_NEWS_COMMENT = "/villages/getNewsComment/%s/%s";
    /**
     * 获取评论回复
     */
    private final String GET_REPLY = "/villages/getReply/%s/%s";

    /**
     * 评论回复
     */
    private final String REPLY_COMMENT = "/villages/replyComment";

    /**
     * 搜索
     */
    private final String SEARCH = "/villages/search/%s";
    /**
     * 搜索
     */
    private final String SEARCH_TOTAL = "/villages/searchTotal/%s";
    /**
     * 新闻操作
     */
    private final String NEWS_DETAIL_OPERATE = "/villages/dp/%s/%s/%s";
    /**
     * 成员列表
     */
    private final String queryVillagesUser = "/villages/queryVillagesUser/";

    /**
     * 成员列表
     */
    private final String getStatusByArea = "/open/getStatusByArea";
    /**
     *
     * @param taskId
     */
    public void getPicture(int taskId){
        getCache(GET_PICTURE, null, new NewCustomResponseHandler<HashMap<String, List<AddressPicInfo>>>(taskId){});
    }

    /**
     *
     * @param taskId
     * @param villageId
     */
    public void getCarousel(int taskId, String villageId){
        getCache(String.format(GET_CAROUSEL, TextUtils.isEmpty(villageId) ? "" : villageId), null, new NewCustomResponseHandler<ArrayList<Carousel>>(taskId){}.setCallSuperRefreshUI(true));
    }

    /**
     * 获取新闻列表
     * @param taskId
     * @param page
     */
    public void getNews(int taskId, String villageId, int page){
        String url = String.format(GET_NEWS, villageId == null ? "" : villageId, page);
        get(url, null,new NewCustomResponseHandler<NewsListObg>(taskId){});
    }

    /**
     * 成员列表
     * @param taskId
     * @param page
     */
    public void getTownUser(int taskId, String type, String id,int page){
        String url=queryVillagesUser+type+"/"+id+"/"+page;
        get(url, null,new NewCustomResponseHandler<List<TownUser>>(taskId){});
    }

    /**
     * 新闻详情
     * @param taskId
     */
    public void getNewsDetail(int taskId, String newsId){
        get(String.format(GET_NEWS_DETAIL, newsId == null ? "" : newsId), null, new NewCustomResponseHandler<NewsDetailObj>(taskId){});
    }


    /**
     *
     */
    public void sendComment(int taskId, String newsId, String content){
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("newsId", newsId);
        paramMap.put("comment", content);
        post(SENT_COMMENT, paramMap, new NewCustomResponseHandler<String>(taskId){});
    }
    /**
     * 获取新闻评论列表
     */
    public void getNewsComment(int taskId, String newsId, int page){
        get(String.format(GET_NEWS_COMMENT, newsId == null ? "" : newsId, page), null, new NewCustomResponseHandler<CommentResponse>(taskId){});
    }

    /**
     * 获取评论回复
     */
    public void getReply(int taskId, String commentId, int page){
        get(String.format(GET_REPLY, commentId == null ? "" : commentId, page), null, new NewCustomResponseHandler<ReplyResponse>(taskId){});
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
     * 搜索
     * @param taskId
     */
    public void search(int taskId, int page, String keyWord){
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("q", keyWord);
        get(String.format(SEARCH_TOTAL, page), paramMap, new NewCustomResponseHandler<NewsResponse>(taskId){});
    }

    /**
     * 热门搜索
     * @param taskId
     */
    public void hotSearch(int taskId){
        getCache(GET_HOT_SEARCH, null, new NewCustomResponseHandler<SuggestVo>(taskId){}.setCallSuperRefreshUI(false));
    }

    /**
     * 点赞/取消点赞
     * @param taskId
     */
    public void praise(int taskId, String newsId, boolean isPraise){
        post(String.format(NEWS_DETAIL_OPERATE, newsId == null ? "" : newsId, "1", isPraise ? "1" : "0"), null, new NewCustomResponseHandler<String>(taskId){});
    }

    /**
     * 不喜欢/取消不喜欢
     * @param taskId
     */
    public void unLike(int taskId, String newsId, boolean isUnLike){
        post(String.format(NEWS_DETAIL_OPERATE, newsId == null ? "" : newsId, "2", isUnLike ? "1" : "0"), null, new NewCustomResponseHandler<String>(taskId){});
    }

    /**
     * 新闻收藏
     */
    public void newCollection(int taskId, String newsId){
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("newsId", newsId);
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

    /**
     * 乡村简介
     */
    public void getVillageMessage(int taskId, String villageId){
        get(String.format(GET_VILLAGE_MESSAGE, villageId == null ? "" : villageId), null, new NewCustomResponseHandler<List<NewsDetailContent>>(taskId){});
    }

    /**
     * 获取我的新闻
     * @param taskId
     * @param page
     */
    public void getMyLikeNews(int taskId, int page){
        get(String.format(GET_MY_LIKE_NEWS, page), null, new NewCustomResponseHandler<CollectionResponse>(taskId){});
    }

}
