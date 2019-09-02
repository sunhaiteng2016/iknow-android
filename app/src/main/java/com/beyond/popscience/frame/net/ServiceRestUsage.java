package com.beyond.popscience.frame.net;

import android.text.TextUtils;

import com.beyond.library.network.net.httpclient.NewCustomResponseHandler;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.net.base.AppBaseRestUsageV2;
import com.beyond.popscience.frame.pojo.Carousel;
import com.beyond.popscience.frame.pojo.GoodsDetail;
import com.beyond.popscience.frame.pojo.MoreSeviceResponse;
import com.beyond.popscience.frame.pojo.ServiceCategory;
import com.beyond.popscience.frame.pojo.ServiceGoodsCategory;
import com.beyond.popscience.frame.pojo.ServiceGoodsList;
import com.beyond.popscience.frame.pojo.SuggestVo;
import com.beyond.popscience.module.home.WelcomeActivity;
import com.beyond.popscience.module.home.entity.kepuMenu;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务
 * Created by yao.cui on 2017/6/24.
 */

public class ServiceRestUsage extends AppBaseRestUsageV2 {

    private final String GET_CAROUSEL = "/product/getCarousel";//轮播图
    private final String GET_CATEGORY = "/product/getServiceKind";//获取服务类型
    private final String GET_CATMENU = "/tab/queryServiceClass";//获取菜单
    private final String GET_PRODUCTS = "/product/getHomeProduct/{0}";//获取产品
    private final String GET_PRODUCT_BY_CATEGORY = "/product/getClassfyProduct/{0}/{1}?distance={2}&money={3}&time={4}&all={5}";//根据分类获取商品

    private final String GET_PRODUCT_BY_CATEGORYS = "/product/getClassfyProduct/{0}/{1}";
    private final String GET_GOODS_DETAIL = "/product/getProductDetail/{0}";//商品详情
    private final String SEARCH_GOODS = "/product/searchProduct/{0}?q={1}";//搜索商品
    private final String SEARCH_GOODSS = "/product/searchProduct/{0}";//搜索商品
    private final String GET_GOODS_BY_USER = "/product/getAllPublish/{0}?userId={1}";//userId 为空则表示获取自己发布的商品
    private final String GET_MORE_SERVICE = "/product/getOutChainList";
    /**
     * 上传商品接口
     */
    private final String PUBLISH_PRODUCT = "/product/publishProduct";
    /**
     * 修改商品接口
     */
    private final String EDIT_PRODUCT = "/product/editProduct";
    /**
     * 获取商品分类
     */
    private final String GET_PRODUCT_CLASSFY = "/product/getProductClassfy";
    /**
     * 热门搜索
     */
    private final String GET_HOT_SEARCH = "/product/getHotSearch";
    /**
     * 热门搜索 1:技能 2：任务 3出租出售 4求租求购
     */
    private final String GET_SERVICE_HOT_SEARCH = "/common/getHotSearch/%s";
    /**
     * 我发布的技能
     */
    private final String GET_MY_SKILL = "/skill/getMySkill/%s";
    /**
     * 我发布的任务
     */
    private final String GET_MY_TASK = "/skill/getMyTask/%s";
    /**
     * 我发布的出租出售
     */
    private final String GET_MY_BUILDING = "/building/getMyBuilding/%s";
    /**
     * 我发布的求租求购
     */
    private final String GET_MY_RENT = "/building/getMyRent/%s";
    /**
     * 删除发布
     */
    private final String DELTETE = "/common/deltete/%s/%s";
    /**
     * 求职列表
     */
    private final String GET_APPLY_LIST = "/job/getApplyList/%s";
    /**
     * 招聘列表
     */
    private final String GET_JOB_LIST = "/job/getJobList/%s";

    /**
     * 获取轮播图
     *
     * @param taskId
     */
    public void getCarousel(int taskId, String classfyId) {
        String url = GET_CAROUSEL;
        if (!TextUtils.isEmpty(classfyId)) {
            url += "?classfyId=" + classfyId;
        }
        getCache(url, null, new NewCustomResponseHandler<ArrayList<Carousel>>(taskId) {
        }.setCallSuperRefreshUI(true));

    }

    /**
     * 获取轮播图
     *
     * @param taskId
     */
    public void getCarouselTwo(int taskId, String classfyId) {
        String url = GET_CAROUSEL;
        Map<String, String> map = new HashMap<>();
        map.put("areaName", WelcomeActivity.seletedAdress);
        if (!TextUtils.isEmpty(classfyId)) {
            //url += "?classfyId=" + classfyId;
            map.put("classfyId", classfyId);
        }
        getCacheTwo(url, map, new NewCustomResponseHandler<ArrayList<Carousel>>(taskId) {
        }.setCallSuperRefreshUI(true));

    }

    /**
     * 获取更多服务
     */
    public void getMoreService(int taskId) {
        String url = GET_MORE_SERVICE;
        get(url, null, new NewCustomResponseHandler<MoreSeviceResponse>(taskId) {
        }.setCallSuperRefreshUI(true));
    }


    /**
     * 获取服务类型
     *
     * @param taskId
     */
    public void getServiceCategory(int taskId, String address) {
        getCache(GET_CATMENU + "?areaName=" + address, null, new NewCustomResponseHandler<ArrayList<kepuMenu>>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    /**
     * 获取商品列表
     *
     * @param taskId
     * @param page
     */
    public void getProducts(int taskId, int page) {
        String url = MessageFormat.format(GET_PRODUCTS, page);
        if (page <= 1) {
            getCache(url, null, new NewCustomResponseHandler<ServiceGoodsList>(taskId) {
            }.setCallSuperRefreshUI(true));

        } else {
            get(url, null, new NewCustomResponseHandler<ServiceGoodsList>(taskId) {
            }.setCallSuperRefreshUI(true));
        }
    }


    /**
     * 根据分类获取商品列表
     * <p>
     * ** 不做缓存
     *
     * @param taskId
     * @param classId
     * @param page
     * @param byDis
     * @param byPrice
     * @param byTime
     * @param all
     */
    public void getProductByCategory(int taskId, String classId, int page, String byDis, String byPrice, String byTime, String all) {
        String url = MessageFormat.format(GET_PRODUCT_BY_CATEGORY, classId, page, byDis, byPrice, byTime, all);
        Map<String, String> paramMap = new HashMap<>();
        if (BeyondApplication.getInstance().getLocation() != null) {
            paramMap.put("lat", String.valueOf(BeyondApplication.getInstance().getLocation().latitude));
            paramMap.put("lon", String.valueOf(BeyondApplication.getInstance().getLocation().longitude));
        }
//        if (page<=1){
//            getCache(url, null,new NewCustomResponseHandler<ServiceGoodsList>(taskId){}.setCallSuperRefreshUI(true));
//        } else {
        get(url, paramMap, new NewCustomResponseHandler<ServiceGoodsList>(taskId) {
        }.setCallSuperRefreshUI(true));
//        }
    }

    /**
     * 根据分类获取商品列表
     * <p>
     * ** 不做缓存
     *
     * @param taskId
     * @param classId
     * @param page
     * @param byDis
     * @param byPrice
     * @param byTime
     * @param all
     */
    public void getProductByCategoryTwo(int taskId, String classId, int page, String byDis, String byPrice, String byTime, String all) {
        //String url = MessageFormat.format(GET_PRODUCT_BY_CATEGORY, classId, page, byDis, byPrice, byTime, all);
        String url = MessageFormat.format(GET_PRODUCT_BY_CATEGORYS, classId, page);
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("distance", byDis);
        paramMap.put("money", byPrice);
        paramMap.put("time", byTime);
        paramMap.put("all", all);
        paramMap.put("areaName", WelcomeActivity.seletedAdress);

        post(url, paramMap, new NewCustomResponseHandler<ServiceGoodsList>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    /**
     * 获取商品详情
     *
     * @param taskId
     * @param productId
     */
    public void getGoodsDetail(int taskId, String productId) {
        String url = MessageFormat.format(GET_GOODS_DETAIL, productId);

        get(url, null, new NewCustomResponseHandler<GoodsDetail>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    /**
     * 搜索获取商品列表
     * <p>
     * ** 不做缓存
     *
     * @param taskId
     * @param page
     * @param key
     * @param byDis
     * @param byPrice
     * @param byTime
     * @param all
     */
    public void searchProduct(int taskId, int page, String key, String byDis, String byPrice, String byTime, String all) {
        String url = MessageFormat.format(SEARCH_GOODSS, page);
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("queryName", key);
        paramMap.put("areaName", WelcomeActivity.seletedAdress);
        if (!TextUtils.isEmpty(byDis)) {
            // url += "&distance=" + byDis;
            paramMap.put("distance", byDis);
        }
        if (!TextUtils.isEmpty(byPrice)) {
            // url += "&money=" + byPrice;
            paramMap.put("money", byPrice);
        }

        if (!TextUtils.isEmpty(byTime)) {
            // url += "&time=" + byTime;
            paramMap.put("time", byTime);
        }

        if (!TextUtils.isEmpty(all)) {
            // url += "&all=" + all;
            paramMap.put("all", all);
        }
/*
        if (BeyondApplication.getInstance().getLocation() != null) {
            paramMap.put("lat", String. valueOf(BeyondApplication.getInstance().getLocation().latitude));
            paramMap.put("lon", String.valueOf(BeyondApplication.getInstance().getLocation().longitude));
        }*/
        post(url, paramMap, new NewCustomResponseHandler<ServiceGoodsList>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    /**
     * 获取该用户发布的商品
     *
     * @param taskId
     * @param page
     * @param userId
     */
    public void getProductByUser(int taskId, int page, String userId) {
        String url = MessageFormat.format(GET_GOODS_BY_USER, page, userId);
        get(url, null, new NewCustomResponseHandler<ServiceGoodsList>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    /**
     * 获取该用户发布的技能
     *
     * @param taskId
     * @param page
     */
    public void getMySkill(int taskId, int page) {
        String url = String.format(GET_MY_SKILL, page);
        get(url, null, new NewCustomResponseHandler<ServiceGoodsList>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    /**
     * 获取该用户发布的任务
     *
     * @param taskId
     * @param page
     */
    public void getMyTask(int taskId, int page) {
        String url = String.format(GET_MY_TASK, page);
        get(url, null, new NewCustomResponseHandler<ServiceGoodsList>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    /**
     * 我发布的出租出售
     *
     * @param taskId
     * @param page
     */
    public void getMyBuilding(int taskId, int page) {
        String url = String.format(GET_MY_BUILDING, page);
        get(url, null, new NewCustomResponseHandler<ServiceGoodsList>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    /**
     * 我发布的求租求购
     *
     * @param taskId
     * @param page
     */
    public void getMyRent(int taskId, int page) {
        String url = String.format(GET_MY_RENT, page);
        get(url, null, new NewCustomResponseHandler<ServiceGoodsList>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    /**
     * 我发布的求职
     *
     * @param taskId
     * @param page
     */
    public void getMyApplyList(int taskId, int page) {
        String url = String.format(GET_APPLY_LIST, page);
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("mine", "1");
        post(url, paramMap, new NewCustomResponseHandler<ServiceGoodsList>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    /**
     * 我发布的招聘
     *
     * @param taskId
     * @param page
     */
    public void getMyJobList(int taskId, int page) {
        String url = String.format(GET_JOB_LIST, page);
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("mine", "1");
        post(url, paramMap, new NewCustomResponseHandler<ServiceGoodsList>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    /**
     * 获取商品分类getMyApplyList
     *
     * @param taskId
     */
    public void getProductClassfy(int taskId) {
        get(GET_PRODUCT_CLASSFY, null, new NewCustomResponseHandler<List<ServiceGoodsCategory>>(taskId) {
        });
    }

    /**
     * 发布商品
     *
     * @param taskId
     * @param title
     * @param introduce
     * @param address
     * @param mobile
     * @param coverPic
     * @param detailPics
     * @param classfyId
     * @param classfyName
     * @param userName
     * @param price
     */
    public void publishProduct(int taskId,
                               String productId,
                               String title,
                               String introduce,
                               String address,
                               String mobile,
                               String coverPic,
                               String detailPics,
                               String classfyId,
                               String classfyName,
                               String userName,
                               String price,
                               String lon,
                               String lat,
                               List<HashMap<String, String>> goodsList) {

        Map<Object, Object> paramMap = new HashMap<>();
        paramMap.put("productId", productId);
        paramMap.put("title", title);
        paramMap.put("introduce", introduce);
        paramMap.put("address", address);
        paramMap.put("mobile", mobile);
        paramMap.put("coverPic", coverPic);
        paramMap.put("detailPics", detailPics);
        paramMap.put("classfyId", classfyId);
        paramMap.put("classfyName", classfyName);
        paramMap.put("userName", userName);
        paramMap.put("price", price);
        paramMap.put("lon", lon);
        paramMap.put("lat", lat);
        paramMap.put("goods", goodsList);

        if (TextUtils.isEmpty(productId)) {   //发布商品
            post(PUBLISH_PRODUCT, paramMap, new NewCustomResponseHandler<GoodsDetail>(taskId) {
            });
        } else {  //修改商品
            post(EDIT_PRODUCT, paramMap, new NewCustomResponseHandler<String>(taskId) {
            });
        }
    }

    /**
     * 发布商品
     *
     * @param taskId
     * @param title
     * @param introduce
     * @param mobile
     * @param coverPic
     * @param detailPics
     * @param classfyId
     * @param classfyName
     * @param userName
     * @param price
     */
    public void publishProductTwo(int taskId,
                                  String productId,
                                  String title,
                                  String introduce,
                                  String areaName,
                                  String detailedAddress,
                                  String mobile,
                                  String coverPic,
                                  String detailPics,
                                  String classfyId,
                                  String classfyName,
                                  String userName,
                                  String price,
                                  String lon,
                                  String lat,
                                  List<HashMap<String, String>> goodsList,
                                  String rzjs) {

        Map<Object, Object> paramMap = new HashMap<>();
        paramMap.put("productId", productId);
        paramMap.put("title", title);
        paramMap.put("introduce", introduce);
        paramMap.put("areaName", areaName);
        paramMap.put("detailedAddress", detailedAddress);
        paramMap.put("mobile", mobile);
        paramMap.put("coverPic", coverPic);
        paramMap.put("detailPics", detailPics);
        paramMap.put("classfyId", classfyId);
        paramMap.put("classfyName", classfyName);
        paramMap.put("userName", userName);
        paramMap.put("price", price);
        paramMap.put("lon", lon);
        paramMap.put("lat", lat);
        paramMap.put("goods", goodsList);
        paramMap.put("authorityType", rzjs);

        if (TextUtils.isEmpty(productId)) {   //发布商品
            post(PUBLISH_PRODUCT, paramMap, new NewCustomResponseHandler<GoodsDetail>(taskId) {
            });
        } else {  //修改商品
            post(EDIT_PRODUCT, paramMap, new NewCustomResponseHandler<String>(taskId) {
            });
        }
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
     * 获取热搜词汇
     *
     * @param taskId
     * @param type   1:技能 2：任务 3出租出售 4求租求购 5招聘 6 求职
     */
    public void hotSearch(int taskId, int type) {
        getCache(String.format(GET_SERVICE_HOT_SEARCH, type), null, new NewCustomResponseHandler<SuggestVo>(taskId) {
        }.setCallSuperRefreshUI(false));
    }

    /**
     * 获取热搜词汇
     *
     * @param taskId
     * @param type   1:技能 2：任务 3出租出售 4求租求购 5招聘 6 求职
     * @param typeId 技能id /任务id /出租出售id/ 求租求购id/商品id
     */
    public void deltete(int taskId, String type, String typeId, Object targetObj) {
        post(String.format(DELTETE, type == null ? "" : type, typeId == null ? "" : typeId), null, new NewCustomResponseHandler<String>(taskId) {
        }.setTargetObj(targetObj));
    }

}
