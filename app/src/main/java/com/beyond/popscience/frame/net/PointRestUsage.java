package com.beyond.popscience.frame.net;

import com.beyond.library.network.net.bean.BaseResponse;
import com.beyond.library.network.net.httpclient.NewCustomResponseHandler;
import com.beyond.popscience.frame.net.base.AppBaseRestUsageV2;
import com.beyond.popscience.frame.pojo.ExchangeBean;
import com.beyond.popscience.frame.pojo.PointDetailResult;
import com.beyond.popscience.frame.pojo.ProductDetailResult;
import com.beyond.popscience.frame.pojo.ProductSucBean;
import com.beyond.popscience.frame.pojo.RankingListResult;
import com.beyond.popscience.frame.pojo.RecordDetail;
import com.beyond.popscience.frame.pojo.RecordListResult;
import com.beyond.popscience.frame.pojo.point.IndexDataBean;
import com.beyond.popscience.frame.pojo.point.SignCheckBaseBean;
import com.beyond.popscience.frame.pojo.point.SignCheckBean;
import com.beyond.popscience.frame.pojo.point.SignCheckBeanss;
import com.beyond.popscience.module.point.SHJL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/9 0009.
 */

public class PointRestUsage extends AppBaseRestUsageV2 {

    //  private String baseUrl = "http://19.16.10.201:8080/";//本地
    // private String baseUrl = "http://www.appwzd.cn/";//本地
    private String baseUrl = "http://kpnew.appwzd.cn/";//正式

    /**
     * 兑换记录接口
     */
    private final String RECORD_LIST = baseUrl + "kepuScore/scoreInterface/recordlist";
    /**
     * 商品详情接口
     */
    private final String PRODUCTDETAILS = baseUrl + "kepuScore/scoreInterface/productdetails";
    /**
     * 绿币排行接口
     */
    private final String RANKING_LIST = baseUrl + "kepuScore/scoreInterface/rankinglist";
    /**
     * 兑换成功接口
     */
    private final String PRODUCT_SUC = baseUrl + "kepuScore/scoreInterface/productsuc";
    /**
     * 绿币明细接口
     */
    private final String DETAIL_LIST = baseUrl + "kepuScore/scoreInterface/detaillist";
    /**
     * 审核记录列表
     */
    private final String SHJL_LIST = baseUrl + "kepuScore/scoreInterface/selectWithdrawalRecord";
    /**
     * 获取绿币记录接口
     */
    private final String EARNWAY_LIST = baseUrl + "kepuScore/scoreInterface/earnwaylist";
    /**
     * 提现接口
     */
    private final String WITHDRAW = baseUrl + "kepuScore/scoreInterface/withdraw";
    /**
     * 提现状态接口
     */
    private final String WITHDRAW_PAY_PAY = baseUrl + "kepuScore/scoreInterface/withdrawPay";

    /**
     * 首页接口
     */
    private final String INDEX = baseUrl + "kepuScore/scoreInterface/index";
    /**
     * 商品兑换接口
     */
    private final String INSERTRANKING_LIST = baseUrl + "kepuScore/scoreInterface/insertrankinglist";
    /**
     * 查询审核状态接口
     */
    private final String WITHDARW_PAY = baseUrl + "kepuScore/scoreInterface/withdrawPay";
    /**
     * 支付宝提现接口
     */
    private final String WITHOK = baseUrl + "kepuScore/scoreInterface/withOk";
    /**
     * 签到
     */
    private final String SIGN = baseUrl + "kepuScore/scoreInterface/signToUpdate";
    /**
     * 签到前的查询接口
     */
   // private final String SIGN_CHECK = baseUrl + "kepuScore/scoreInterface/signCheck";
    private final String SIGN_CHECK = baseUrl + "kepuScore/scoreInterface/querysign/";
    /**
     * 意见反馈
     */
    private final String FEED_BACK = baseUrl + "kepu/user/sentAdvice";
    /**
     * 获取验证码
     */
    private final String REQUEST_PIN = baseUrl + "kepuScore/scoreInterface/requestPIN";

    /**
     * 兑换记录
     *
     * @param taskId
     */
    public void recordList(int taskId, String page) {
        Map<String, String> paramMap = new HashMap<>();
        //paramMap.put("userId", userId);
        paramMap.put("page",page);
        post(RECORD_LIST, paramMap, new NewCustomResponseHandler<List<RecordDetail>>(taskId) {
        });
    }

    /**
     * 兑换成功接口
     *
     * @param taskId
     */
    public void productSuc(int taskId, String successid) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("successid", successid);
        post(PRODUCT_SUC, paramMap, new NewCustomResponseHandler<ProductSucBean>(taskId) {
        });
    }

    /**
     * 商品详情接口
     *
     * @param taskId
     */
    public void productDetial(int taskId, String userId, String id) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        paramMap.put("id", id);
        post(PRODUCTDETAILS, paramMap, new NewCustomResponseHandler<ProductDetailResult>(taskId) {
        });
    }

    /**
     * 绿币排行接口
     *
     * @param taskId
     */
    public void rankingList(int taskId, String userId) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        post(RANKING_LIST, paramMap, new NewCustomResponseHandler<RankingListResult>(taskId) {
        });
    }

    /**
     * 绿币明细接口
     *
     * @param taskId
     */
    public void detailList(int taskId, String userId, int page) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        paramMap.put("page", page + "");
        postCache(DETAIL_LIST, paramMap, new NewCustomResponseHandler<PointDetailResult>(taskId) {
        });
    }

    /**
     * 审核记录列表
     *
     * @param taskId
     */
    public void shjlList(int taskId, String userId) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        postCache(SHJL_LIST, paramMap, new NewCustomResponseHandler<Object>(taskId) {
        });
    }

    /**
     * 获取绿币记录接口
     *
     * @param taskId
     */
    public void earnwayList(int taskId, String userId) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        post(EARNWAY_LIST, paramMap, new NewCustomResponseHandler<RecordListResult>(taskId) {
        });
    }

    /**
     * 提现接口
     *
     * @param taskId
     */
    public void withdraw(int taskId, String amount) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userId", amount);
        post(WITHDRAW, paramMap, new NewCustomResponseHandler<RecordListResult>(taskId) {
        });
    }

    /**
     * 首页接口
     *
     * @param taskId
     */
    public void index(int taskId, String userId) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        post(INDEX, paramMap, new NewCustomResponseHandler<IndexDataBean>(taskId) {
        });
    }

    /**
     * 商品兑换接口
     *
     * @param taskId
     */
    public void insertrankingList(int taskId, String userId, String proid, String name, String phone, String dizi, String address) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        paramMap.put("proid", proid);
        paramMap.put("name", name);
        paramMap.put("phone", phone);
        paramMap.put("dizi", dizi);
        paramMap.put("address", address);
        post(INSERTRANKING_LIST, paramMap, new NewCustomResponseHandler<ExchangeBean>(taskId) {
        });
    }

    /**
     * 调用支付宝提现接口
     *
     * @param taskId
     */
    public void withdrawPay(int taskId, String username, String account, String stexchang, String mobile, String code, String userId) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        paramMap.put("username", username);
        paramMap.put("account", account);
        paramMap.put("stexchang", stexchang);
        paramMap.put("mobile", mobile);
        paramMap.put("mobileCode", code);

        post(WITHDARW_PAY, paramMap, new NewCustomResponseHandler<String>(taskId) {
        });
    }

    /**
     * 调用支付宝提现状态接口
     *
     * @param taskId
     */
    public void withdrawPayPAY(int taskId, String username, String account, String stexchang, String mobile, String code, String userId) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        paramMap.put("username", username);
        paramMap.put("account", account);
        paramMap.put("stexchang", stexchang);
        paramMap.put("mobile", mobile);
        paramMap.put("mobileCode", code);

        post(WITHDARW_PAY, paramMap, new NewCustomResponseHandler<String>(taskId) {
        });
    }

    /**
     * 支付宝提现接口
     *
     * @param taskId
     */
    public void withOk(int taskId, String userId, String id) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        paramMap.put("id", id);
        post(WITHOK, paramMap, new NewCustomResponseHandler<String>(taskId) {
        });
    }

    /**
     * 获取验证码
     *
     * @param taskId
     */
    public void getCode(int taskId, String phone, String userId) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("mobile", phone);
        paramMap.put("userId", userId);
        paramMap.put("pinflag", "2");
        post(REQUEST_PIN, paramMap, new NewCustomResponseHandler<String>(taskId) {
        });
    }

    /**
     * 签到
     *
     * @param taskId
     */
    public void sign(int taskId, Map<String, Object> paramMap) {
        post(SIGN, paramMap, new NewCustomResponseHandler<String>(taskId) {
        });
    }

    /**
     * 签到前的查询接口
     *
     * @param taskId
     */
    public void signCheck(int taskId, Map<String, String> paramMap) {
        post(SIGN_CHECK, paramMap, new NewCustomResponseHandler<List<SignCheckBean>>(taskId) {
        });
    }
    public void signChecks(int taskId, Map<String, String> paramMap,String  id) {
        String sss = SIGN_CHECK + id;
        post(sss, paramMap, new NewCustomResponseHandler<SignCheckBeanss>(taskId) {
        });
    }
    /**
     * 意见反馈接口
     *
     * @param taskId
     * @param paramMap
     */
    public void feedBack(int taskId, Map<String, Object> paramMap) {
        post(FEED_BACK, paramMap, new NewCustomResponseHandler<String>(taskId) {
        });
    }
}
