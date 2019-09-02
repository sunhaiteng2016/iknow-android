package com.beyond.popscience.frame.net;

import com.beyond.library.network.net.httpclient.NewCustomResponseHandler;
import com.beyond.popscience.frame.net.base.AppBaseRestUsageV2;
import com.beyond.popscience.frame.pojo.NewsDetailAnswerObj;
import com.beyond.popscience.frame.pojo.NewsDetailObj;
import com.beyond.popscience.frame.pojo.NewsDetailQuestionObj;
import com.beyond.popscience.frame.pojo.NewsResponse;
import com.beyond.popscience.frame.pojo.SuggestVo;
import com.beyond.popscience.locationgoods.bean.SearchUserbean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 搜索
 * Created by linjinfa on 2017/6/9.
 * email 331710168@qq.com
 */
public class SearchRestUsage extends AppBaseRestUsageV2 {

    /**
     * 热门搜索
     */
    private final String GET_HOT_SEARCH = "/news/getHotSearch";
    /**
     * 搜索
     */
    private final String SEARCH = "/news/search/%s";
    /**
     * 新闻详情
     */
    private final String GET_NEWS_DETAIL = "/news/getNewsDetail/%s";
    /**
     * 获取教学互动题目
     */
    private final String GET_QUESTION = "/news/getQuestion/%s";
    /**
     * 提交互动答案
     */
    private final String SUBMIT_ANSWER = "/news/submitAnswer";
    /**
     * 新闻操作
     */
    private final String NEWS_DETAIL_OPERATE = "/user/dp/%s/%s/%s";
    /**
     * 新闻操作
     */
    private final String SHARE = "/user/share/%s/%s";
    /**
     * 社团成员搜索
     */
    private final String queryUserByTeam = "/im/queryUserByTeam/%s/%s";
    /**
     * 乡镇成员搜索
     */
    private final String queryUserByVillages = "/im/queryUserByVillages/%s";

    /**
     * 热门搜索
     *
     * @param taskId
     */
    public void hotSearch(int taskId) {
        getCache(GET_HOT_SEARCH, null, new NewCustomResponseHandler<SuggestVo>(taskId) {
        }.setCallSuperRefreshUI(false));
    }

    /**
     * 搜索
     *
     * @param taskId
     */
    public void search(int taskId, int page, String keyWord) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("q", keyWord);
//        if(page == 1){  //缓存
//            getCache(String.format(SEARCH, page), paramMap, new NewCustomResponseHandler<NewsResponse>(taskId){});
//        }else{
//            get(String.format(SEARCH, page), paramMap, new NewCustomResponseHandler<NewsResponse>(taskId){});
//        }
        get(String.format(SEARCH, page), paramMap, new NewCustomResponseHandler<NewsResponse>(taskId) {
        });
    }

    /**
     * 新闻详情
     *
     * @param taskId
     */
    public void getNewsDetail(int taskId, String newsId) {
        get(String.format(GET_NEWS_DETAIL, newsId == null ? "" : newsId), null, new NewCustomResponseHandler<NewsDetailObj>(taskId) {
        });
    }

    /**
     * 获取教学互动题目
     *
     * @param taskId
     */
    public void getQuestion(int taskId, String newsId) {
        get(String.format(GET_QUESTION, newsId == null ? "" : newsId), null, new NewCustomResponseHandler<NewsDetailQuestionObj>(taskId) {
        });
    }

    /**
     * 获取教学互动题目
     *
     * @param taskId
     */
    public void share(int taskId, String type, String typeId) {
        get(String.format(SHARE, type == null ? "" : type, typeId == null ? "" : typeId), null, new NewCustomResponseHandler<String>(taskId) {
        });
    }

    /**
     * 社团成员查询
     */
    public void queryUserByTeam(int taskId, String id, String phoneNum) {
        get(String.format(queryUserByTeam, id == null ? "" : id, phoneNum == null ? "" : phoneNum), null, new NewCustomResponseHandler<List<SearchUserbean>>(taskId) {
        });
    }

    /**
     * 社团成员查询
     */
    public void queryUserByVillages(int taskId, String phoneNum) {
        get(String.format(queryUserByVillages, phoneNum == null ? "" : phoneNum), null, new NewCustomResponseHandler<List<SearchUserbean>>(taskId) {
        });
    }

    /**
     * 点赞/取消点赞
     *
     * @param taskId
     */
    public void submitAnswer(int taskId, List<HashMap<String, String>> answerList) {
        HashMap<String, List<HashMap<String, String>>> paramMap = new HashMap<>();
        paramMap.put("answerList", answerList);
        post(SUBMIT_ANSWER, paramMap, new NewCustomResponseHandler<NewsDetailAnswerObj>(taskId) {
        });
    }

    /**
     * 点赞/取消点赞
     *
     * @param taskId
     */
    public void praise(int taskId, String newsId, boolean isPraise) {
        post(String.format(NEWS_DETAIL_OPERATE, newsId == null ? "" : newsId, "1", isPraise ? "1" : "0"), null, new NewCustomResponseHandler<String>(taskId) {
        });
    }

    /**
     * 不喜欢/取消不喜欢
     *
     * @param taskId
     */
    public void unLike(int taskId, String newsId, boolean isUnLike) {
        post(String.format(NEWS_DETAIL_OPERATE, newsId == null ? "" : newsId, "2", isUnLike ? "1" : "0"), null, new NewCustomResponseHandler<String>(taskId) {
        });
    }

}
