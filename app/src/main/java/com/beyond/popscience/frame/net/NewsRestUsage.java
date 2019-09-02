package com.beyond.popscience.frame.net;

import com.beyond.library.network.net.bean.BaseResponse;
import com.beyond.library.network.net.httpclient.NewCustomResponseHandler;
import com.beyond.popscience.frame.net.base.AppBaseRestUsageV2;
import com.beyond.popscience.frame.pojo.BaseObject;
import com.beyond.popscience.frame.pojo.Carousel;
import com.beyond.popscience.frame.pojo.NavObj;
import com.beyond.popscience.frame.pojo.NewsListObg;
import com.beyond.popscience.frame.pojo.NewsRecommendObj;
import com.beyond.popscience.frame.pojo.NewsRecommendResponse;
import com.beyond.popscience.frame.util.SPUtils;
import com.beyond.popscience.module.home.entity.BaseEntity;
import com.beyond.popscience.module.home.entity.Menus;
import com.beyond.popscience.module.home.entity.MyMenu;
import com.beyond.popscience.module.home.entity.kepuMenu;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 新闻
 * Created by yao.cui on 2017/6/14.
 */

public class NewsRestUsage extends AppBaseRestUsageV2 {
    private final String GET_CAROUSEL = "/news/getCarousel?type={0}";//轮播图
    private final String GET_CAROUSEL_TWO = "/news/getCarousel";//轮播图
    private final String GET_NEWS = "/news/getNews/{0}/{1}";//getNews/{type}/{page} 获取新闻列表
    private final String GET_NEWS_two = "/news/getNewsTwo/{0}/{1}";//getNews/{type}/{page} 获取新闻列表
    private final String GET_TOP_NAV = "/news/getTopnav";//获取订阅标签
    private final String GET_RECOMMEND_NEWS = "/news/getCommend";//获取推荐新闻
    private final String GET_MENU = "/tab/queryAllTab";//获取所有的Tab
    private final String GET_MENU_MY = "/tab/queryUserTab";//获取我的Tab
    private final String UP_MENU = "/tab/insertTab";//获取我的Tab
    private final String queryArticleClass = "/tab/queryArticleClass";//获取我的Tab
    private final String queryUserByMobile = "/im/queryUserByMobile/{0}/{1}";//获取我的Tab
    private final String upDateCid = "user/updateCid";//获取我的Tab


    /**
     * 获取轮播图
     *
     * @param taskId
     */
    public void getCarousel(int taskId, String type) {
        String url = MessageFormat.format(GET_CAROUSEL, type);
        getCache(url, null, new NewCustomResponseHandler<ArrayList<Carousel>>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    /**
     * 获取轮播图
     *
     * @param taskId
     */
    public void getCarouseltwo(int taskId, String type, String address) {
        String url = MessageFormat.format(GET_CAROUSEL_TWO, type, address);
        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("address", address);
        getCacheTwo(GET_CAROUSEL_TWO, map, new NewCustomResponseHandler<ArrayList<Carousel>>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    /**
     * 获取新闻列表 使用缓存
     *
     * @param taskId
     * @param type
     * @param page
     */
    public void getNewsCache(int taskId, String type, int page, Map<String, String> paramMap) {
        String url = MessageFormat.format(GET_NEWS_two, type, page);
        getCacheTwo(url, paramMap, new NewCustomResponseHandler<NewsListObg>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    /**
     * 获取新闻列表 不使用缓存
     * @param taskId
     * @param type
     * @param page
     */
    public void getNewsNoCache(int taskId, String type, int page) {
        String url = MessageFormat.format(GET_NEWS, type, page);
        get(url, null, new NewCustomResponseHandler<NewsListObg>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    public void getRecommendNewsNoCache(int taskId) {
        get(GET_RECOMMEND_NEWS, null, new NewCustomResponseHandler<NewsRecommendResponse>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    //获取所有菜单
    public void getMenu(int taskId,String address) {
        get(GET_MENU+"?areaName="+address, null, new NewCustomResponseHandler<Menus>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    //获取所有菜单
    public void getMyMenu(int taskId,String address) {
        get(GET_MENU_MY+"?areaName="+address, null, new NewCustomResponseHandler<List<MyMenu>>(taskId) {
        }.setCallSuperRefreshUI(true));
    }
    //获取科普菜单
    public void getKePuMenu(int taskId) {
        get(queryArticleClass, null, new NewCustomResponseHandler<List<kepuMenu>>(taskId) {
        }.setCallSuperRefreshUI(true));
    }
    //用户定制的菜单
    public void getCustomMenu(int taskId, Map<String,String> list) {
        post(UP_MENU, list, new NewCustomResponseHandler<BaseResponse>(taskId) {
        }.setCallSuperRefreshUI(true));
    }
    /**
     * 获取新闻列表 顶部订阅标签
     *
     * @param taskId
     */
    public void getNavs(int taskId) {
        getCache(GET_TOP_NAV, null, new NewCustomResponseHandler<List<NavObj>>(taskId) {
        }.setCallSuperRefreshUI(false));
    }
}
