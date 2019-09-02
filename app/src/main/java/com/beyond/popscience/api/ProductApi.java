package com.beyond.popscience.api;

import com.beyond.library.network.net.httpclient.NewCustomResponseHandler;
import com.beyond.popscience.frame.net.base.AppBaseRestUsageV2;
import com.beyond.popscience.frame.pojo.GoodsDetailNew;
import com.beyond.popscience.frame.pojo.ServiceGoodsList;
import com.beyond.popscience.module.home.WelcomeActivity;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Describe：商品接口
 * Date：2018/3/8
 * Time: 14:09
 * Author: Bin.Peng
 */

public class ProductApi extends AppBaseRestUsageV2 {
    //产品列表  /kepu/product/getHomeProduct  /product/getHomeProduct/{0}
    private final String GET_PRODUCTS = "/product/getHomeProduct/{0}";
    //产品详情getProductDetail2/{productId}
    private final String GET_PRODUCTS_DETAIL = "/product/getProductDetail2/{0}";
    //收藏商品
    private final String GET_PRODUCTS_COLLET = "/product/likeProduct";
    //删除收藏商品
    private final String GET_PRODUCTS_DELETE_COLLET = "/product/deltete/likeProduct";
    /**
     * 商品列表
     */
    public void getProductsList(int taskId, int page) {
        String url = MessageFormat.format(GET_PRODUCTS, page);
        Map<String,String>  map = new HashMap<>();
        map.put("areaName", WelcomeActivity.seletedAdress);
        post(url, map, new NewCustomResponseHandler<ServiceGoodsList>(taskId) {
        }.setCallSuperRefreshUI(true));
    }
    /**
     * 商品详情
     */
    public void getProductsDetail(int taskId, String productId) {
        String url = MessageFormat.format(GET_PRODUCTS_DETAIL, productId);
        get(url, null, new NewCustomResponseHandler<GoodsDetailNew>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    /**
     * 收藏商品
     */
    public void getProductsCollet(int taskId, String productId) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("productId", productId);

//        String url = MessageFormat.format(GET_PRODUCTS_COLLET, productId);
        post(GET_PRODUCTS_COLLET, paramMap, new NewCustomResponseHandler<String>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    /**
     * 删除收藏商品
     */
    public void getProductsDeleteCollet(int taskId, String productId) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("productId", productId);
        get(GET_PRODUCTS_DELETE_COLLET, paramMap, new NewCustomResponseHandler<String>(taskId) {
        }.setCallSuperRefreshUI(true));
    }


}
