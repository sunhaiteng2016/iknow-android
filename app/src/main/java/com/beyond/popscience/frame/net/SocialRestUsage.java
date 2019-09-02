package com.beyond.popscience.frame.net;

import android.text.TextUtils;

import com.beyond.library.network.net.httpclient.NewCustomResponseHandler;
import com.beyond.popscience.frame.net.base.AppBaseRestUsageV2;
import com.beyond.popscience.frame.pojo.ArticleInfo;
import com.beyond.popscience.frame.pojo.ArticleRemindResponse;
import com.beyond.popscience.frame.pojo.ArticleResponse;
import com.beyond.popscience.frame.pojo.Comment;
import com.beyond.popscience.frame.pojo.NoticeDetail;
import com.beyond.popscience.frame.pojo.NoticeListObj;
import com.beyond.popscience.frame.pojo.PublishNoticeResult;
import com.beyond.popscience.frame.pojo.PublishTeachResult;
import com.beyond.popscience.frame.pojo.SocialInfo;
import com.beyond.popscience.frame.pojo.SocialIntro;
import com.beyond.popscience.frame.pojo.SocialResponse;
import com.beyond.popscience.frame.pojo.TeachDetail;
import com.beyond.popscience.frame.pojo.SuggestVo;
import com.beyond.popscience.frame.pojo.TeachItem;
import com.beyond.popscience.frame.pojo.TeachListObj;
import com.beyond.popscience.module.home.WelcomeActivity;
import com.beyond.popscience.module.home.entity.TownUser;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 社团（圈子）
 * Created by yao.cui on 2017/7/17.
 */

public class SocialRestUsage extends AppBaseRestUsageV2 {
    private final String PUBLISH_NOTICE = "/team/publishNotice/";//发布公告
    private final String GET_NOTICE = "/team/noticeDetail/";//公告详情
    private final String GET_NOTICE_LIST = "/team/getNotice/{0}/{1}";
    private final String GET_TEACH_LIST = "/team/getTeach/{0}/{1}";//获取教学列表
    private final String GET_TEACH_DETAIL = "/team/teachDetail/{0}";//获取教学详情
    private final String POST_AUDIT = "/team/applyCommunity/";//社团审核
    private final String PUBLISH_TEACH = "/team/publishTeach/";//发布教学
    private final String POST_AUDIT_NORMAL = "/team/joinCommunity/";//社团申请 非正规社团
    private final String GET_INTRO = "/team/communityDetail/";//获取社团简介
    private final String GET_USER = "/team/queryTeamUser/";//获取社团成员
    /**
     *
     */
    private final String GET_MY_ARTICLE = "/team/getMyArticle/%s";//我发布的社团(乡镇社团)帖子
    /**
     * 删除社团帖子(含乡镇社团)
     */
    private final String DELETE_ARTICLE = "/team/deleteArticle/%s";

    /**
     * 首页推荐社团
     */
    private final String GET_CAROUSEL = "/team/getCarousel";
    /**
     * 我的社团
     */
    private final String MY_COMMUNITY = "/team/MyCommunity";
    /**
     * 退出
     */
    private final String QUIT = "/team/quit/%s";
    /**
     * 获取所有社团
     */
    private final String GET_ALL_COMMUNITY = "/team/getAllCommunity/%s";
    /**
     * 加入普通社团
     */
    private final String JOIN_COMMUNITY = "/team/joinCommunity/%s";
    /**
     * 热门搜索
     */
    private final String GET_HOT_SEARCH = "/team/getHotSearch";
    /**
     * 搜索
     */
    private final String SEARCH = "/team/search/%s";
    /**
     * 社团帖子列表
     */
    private final String GET_ARTICLE = "/team/getArticle/%s/%s";
    /**
     * 帖子点赞/取消
     */
    private final String PRAISE = "/team/dp/%s/%s";
    /**
     * 发布社团帖子
     */
    private final String PUBLISH_ARTICLE = "/team/publishArticle/%s";
    /**
     * 修改社团帖子(含乡镇社团)
     */
    private final String EDIT_ARTICLE = "/team/editArticle/%s";
    /**
     * 帖子详情
     */
    private final String ARTICLE_DETAIL = "/team/articleDetail/%s";
    /**
     * 发表帖子评论
     */
    private final String SENT_ARTICLE_COMMENT = "/team/sentArticleComment";
    /**
     * 帖子评论回复
     */
    private final String SENT_ARTICLE_REPLY = "/team/sentArticleReply";
    /**
     * 消息提醒列表
     */
    private final String GET_MY_REMIND = "/team/getMyRemind/%s";


    /**
     * 删除我的收藏
     */
    private final String DELETE_NEWS_COLLECTION = "/notice/delete/likeNews";
    /**
     * 新闻收藏
     */
    private final String NEWS_COLLECTION = "/notice/likeNews";

    /**
     * 新闻收藏
     */
    public void newCollection(int taskId, String newsId, String type) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("newsId", newsId);
        paramMap.put("type", type);
        post(NEWS_COLLECTION, paramMap, new NewCustomResponseHandler<String>(taskId) {
        }.setCallSuperRefreshUI(false));
    }

    /**
     * 删除新闻收藏
     */
    public void deleteNewCollection(int taskId, String newsId, String type, Object targetObj) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("newsId", newsId);
        paramMap.put("type", type);
        post(DELETE_NEWS_COLLECTION, paramMap, new NewCustomResponseHandler<String>(taskId) {
        }.setTargetObj(targetObj));
    }

    /**
     * 发布公告
     *
     * @param taskId
     */
    public void publishNotice(int taskId, String communityId, String title, String name, String content) {
        String url = PUBLISH_NOTICE + communityId;
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("title", title);
        paramMap.put("author", name);
        paramMap.put("content", content);
        post(url, paramMap, new NewCustomResponseHandler<PublishNoticeResult>(taskId) {
        });
    }

    /**
     * 获取公告详情
     *
     * @param taskId
     * @param id     {id}/{page}
     */
    public void getNoticeDetail(int taskId, String id) {
        String url = GET_NOTICE + id;
        get(url, null, new NewCustomResponseHandler<NoticeDetail>(taskId) {
        }.setCallSuperRefreshUI(false));
    }

    /**
     * 获取社团成员
     *
     * @param taskId
     * @param id
     */
    public void getUser(int taskId, String id, int page) {
        String url = GET_USER + id + "/" + page;
        get(url, null, new NewCustomResponseHandler<List<TownUser>>(taskId) {
        }.setCallSuperRefreshUI(false));

    }

    /**
     * 获取圈子 公告列表
     *
     * @param taskId
     * @param id
     * @param page
     */
    public void getNoticeList(int taskId, String id, int page) {
        String url = MessageFormat.format(GET_NOTICE_LIST, id, page);
        if (page == 1) {
            getCache(url, null, new NewCustomResponseHandler<NoticeListObj>(taskId) {
            }.setCallSuperRefreshUI(true));
        } else {
            get(url, null, new NewCustomResponseHandler<NoticeListObj>(taskId) {
            }.setCallSuperRefreshUI(true));
        }
    }

    /**
     * 获取圈子 教学列表
     *
     * @param taskId
     * @param id
     * @param page
     */
    public void getTeachList(int taskId, String id, int page) {
        String url = MessageFormat.format(GET_TEACH_LIST, id, page);
        if (page == 1) {
            getCache(url, null, new NewCustomResponseHandler<TeachListObj>(taskId) {
            }.setCallSuperRefreshUI(true));
        } else {
            get(url, null, new NewCustomResponseHandler<TeachListObj>(taskId) {
            }.setCallSuperRefreshUI(true));
        }
    }

    /**
     * 获取教学详情
     *
     * @param taskId
     * @param id
     */
    public void getTeachDetail(int taskId, String id) {
        String url = MessageFormat.format(GET_TEACH_DETAIL, id);
        get(url, null, new NewCustomResponseHandler<TeachDetail>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    /**
     * 提交审核信息
     * 提交之后需要人为审核
     *
     * @param taskId
     * @param communityId 社团id
     * @param userName
     * @param mobile
     * @param position    职位
     * @param credential  毕业证图片
     * @param IDcardZ     身份证正面
     * @param IDcardF     身份证反面
     */
    public void postAudit(int taskId, String communityId, String userName, String mobile, String position,
                          String credential, String IDcardZ, String IDcardF) {
        String url = POST_AUDIT + communityId;
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userName", userName);
        paramMap.put("mobile", mobile);
        paramMap.put("position", position);
        paramMap.put("credential", credential);
        paramMap.put("IDcardZ", IDcardZ);
        paramMap.put("IDcardF", IDcardF);
        post(url, paramMap, new NewCustomResponseHandler<String>(taskId) {
        });
    }

    /**
     * 提交审核信息 非正规社团
     * <p>
     * 申请之后即可得到审核结果
     *
     * @param taskId
     * @param communityId
     */
    public void postAuditNormal(int taskId, String communityId) {
        String url = POST_AUDIT_NORMAL + communityId;
        post(url, null, new NewCustomResponseHandler<String>(taskId) {
        });
    }

    /**
     * 发布教学
     *
     * @param taskId
     * @param communityId
     * @param title
     * @param contentList
     */
    public void publishTeach(int taskId, String communityId, String title, List<TeachItem> contentList) {
        String url = PUBLISH_TEACH + communityId;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("title", title);
        paramMap.put("contentList", contentList);
        post(url, paramMap, new NewCustomResponseHandler<PublishTeachResult>(taskId) {
        });
    }

    /**
     * 查看社团介绍
     *
     * @param taskId
     * @param communityId
     */
    public void getIntro(int taskId, String communityId, String timeStamp) {
        Map<String, String> paramMap = new HashMap<>();
        if (!TextUtils.isEmpty(timeStamp)) {
            paramMap.put("timestamp", timeStamp);
        }

        String url = GET_INTRO + communityId;
        get(url, paramMap, new NewCustomResponseHandler<SocialIntro>(taskId) {
        }.setCallSuperRefreshUI(true));

    }

    /**
     * 首页推荐社团
     *
     * @param taskId
     */
    public void getCarousel(int taskId) {
        // get(GET_CAROUSEL, null,new NewCustomResponseHandler<List<SocialInfo>>(taskId){});
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("address", WelcomeActivity.seletedAdress);
        post(GET_CAROUSEL, paramMap, new NewCustomResponseHandler<List<SocialInfo>>(taskId) {
        });
    }

    /**
     * 我的社团
     *
     * @param taskId
     */
    public void myCommunity(int taskId, String timeStamp) {
        Map<String, String> paramMap = new HashMap<>();
        if (!TextUtils.isEmpty(timeStamp)) {
            paramMap.put("timestamp", timeStamp);
        }
        get(MY_COMMUNITY, paramMap, new NewCustomResponseHandler<List<SocialInfo>>(taskId) {
        });
    }

    /**
     * 所有社区
     *
     * @param taskId
     */
    public void getAllCommunity(int taskId, int page) {
        // get(String.format(GET_ALL_COMMUNITY, page), null,new NewCustomResponseHandler<SocialResponse>(taskId){});
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("address", WelcomeActivity.seletedAdress);
        post(String.format(GET_ALL_COMMUNITY, page), paramMap, new NewCustomResponseHandler<SocialResponse>(taskId) {
        });
    }

    /**
     * 退出社团
     *
     * @param taskId
     */
    public void quit(int taskId, String communityId, Object targetObj) {
        post(String.format(QUIT, communityId == null ? "" : communityId), null, new NewCustomResponseHandler<String>(taskId) {
        }.setTargetObj(targetObj));
    }

    /**
     * 加入普通社团
     *
     * @param taskId
     */
    public void joinCommunity(int taskId, String communityId, Object targetObj) {
        post(String.format(JOIN_COMMUNITY, communityId == null ? "" : communityId), null, new NewCustomResponseHandler<String>(taskId) {
        }.setTargetObj(targetObj));
    }

    /**
     * 发布社团帖子
     *
     * @param taskId
     */
    public void publishArticle(int taskId, String communityId, String content, String detailPics) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("content", content);
        paramMap.put("detailPics", detailPics);
        post(String.format(PUBLISH_ARTICLE, communityId == null ? "" : communityId), paramMap, new NewCustomResponseHandler<ArticleInfo>(taskId) {
        });
    }
    /**
     * 发布社团帖子
     *
     * @param taskId
     */
    public void publishArticleTwo(int taskId, String communityId, String content, String detailPics,String url) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("content", content);
        paramMap.put("detailPics", detailPics);
        paramMap.put("url", url);
        paramMap.put("type", "1");
        post(String.format(PUBLISH_ARTICLE, communityId == null ? "" : communityId), paramMap, new NewCustomResponseHandler<ArticleInfo>(taskId) {
        });
    }
    /**
     * 修改社团帖子(含乡镇社团)
     *
     * @param taskId
     */
    public void editArticle(int taskId, String articleId, String content, String detailPics) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("content", content);
        paramMap.put("detailPics", detailPics);
        post(String.format(EDIT_ARTICLE, articleId == null ? "" : articleId), paramMap, new NewCustomResponseHandler<ArticleInfo>(taskId) {
        });
    }

    /**
     * 发布社团帖子
     *
     * @param taskId
     */
    public void sentArticleComment(int taskId, String articleId, String comment) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("articleId", articleId);
        paramMap.put("comment", comment);
        post(SENT_ARTICLE_COMMENT, paramMap, new NewCustomResponseHandler<Comment>(taskId) {
        });
    }

    /**
     * 帖子评论回复
     *
     * @param taskId
     */
    public void sentArticleReply(int taskId, String commentId, String comment) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("commentId", commentId);
        paramMap.put("comment", comment);
        post(SENT_ARTICLE_REPLY, paramMap, new NewCustomResponseHandler<Comment>(taskId) {
        });
    }

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
    public void search(int taskId, int page, String keyword) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("q", keyword);
        get(String.format(SEARCH, page), paramMap, new NewCustomResponseHandler<SocialResponse>(taskId) {
        });
    }

    /**
     * 社团帖子列表
     *
     * @param taskId
     */
    public void getArticle(int taskId, String communityId, int page) {
        get(String.format(GET_ARTICLE, communityId == null ? "" : communityId, page), null, new NewCustomResponseHandler<ArticleResponse>(taskId) {
        }.setCallSuperRefreshUI(false));
    }

    /**
     * 点赞
     *
     * @param isPraise 是否点赞
     */
    public void praise(int taskId, String articleId, boolean isPraise, Object targetObj) {
        int operate = isPraise ? 1 : 0; //	1:点赞 0:取消
        get(String.format(PRAISE, articleId == null ? "" : articleId, operate), null, new NewCustomResponseHandler<String>(taskId) {
        }.setTargetObj(targetObj).setCallSuperRefreshUI(false));
    }

    /**
     * 帖子详情
     */
    public void articleDetail(int taskId, String articleId) {
        get(String.format(ARTICLE_DETAIL, articleId == null ? "" : articleId), null, new NewCustomResponseHandler<ArticleInfo>(taskId) {
        });
    }

    /**
     * 消息提醒列表
     */
    public void getMyRemind(int taskId, String communityId, String timeStamp) {
        Map<String, String> paramMap = new HashMap<>();
        if (!TextUtils.isEmpty(timeStamp)) {
            paramMap.put("timestamp", timeStamp);
        }
        get(String.format(GET_MY_REMIND, communityId == null ? "" : communityId), paramMap, new NewCustomResponseHandler<ArticleRemindResponse>(taskId) {
        });
    }

    /**
     * 我发布的社团(乡镇社团)帖子
     */
    public void getMyArticle(int taskId, int type, int page) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("type", String.valueOf(type));
        get(String.format(GET_MY_ARTICLE, page), paramMap, new NewCustomResponseHandler<ArticleResponse>(taskId) {
        });
    }

    /**
     * 删除社团帖子(含乡镇社团)
     */
    public void deleteArticle(int taskId, String articleId, Object targetObj) {
        get(String.format(DELETE_ARTICLE, articleId == null ? "" : articleId), null, new NewCustomResponseHandler<String>(taskId) {
        }.setTargetObj(targetObj));
    }

}