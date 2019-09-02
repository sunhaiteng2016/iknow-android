package com.beyond.popscience.locationgoods.http;


import com.beyond.library.network.net.bean.BaseResponse;
import com.beyond.library.network.net.httpclient.NewCustomResponseHandler;
import com.beyond.popscience.frame.net.base.AppBaseRestUsageV2;
import com.beyond.popscience.frame.pojo.BaseObject;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.locationgoods.bean.BannerBean;
import com.beyond.popscience.locationgoods.bean.DpxqBean;
import com.beyond.popscience.locationgoods.bean.EventionBean;
import com.beyond.popscience.locationgoods.bean.Flbean;
import com.beyond.popscience.locationgoods.bean.MAddressListBean;
import com.beyond.popscience.locationgoods.bean.OrderBean;
import com.beyond.popscience.locationgoods.bean.OrderLsitTwoBean;
import com.beyond.popscience.locationgoods.bean.OrderlsitBean;
import com.beyond.popscience.locationgoods.bean.ProductDetail;
import com.beyond.popscience.locationgoods.bean.ProductList;
import com.beyond.popscience.locationgoods.bean.ProductListTwo;
import com.beyond.popscience.locationgoods.bean.ShopInformationBean;
import com.beyond.popscience.locationgoods.bean.TwoModer;


import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/5/9 0009.
 */

public class AddressRestUsage extends AppBaseRestUsageV2 {

    public  static String baseUrl = "https://shop.appwzd.cn/";//电商地址
    //  public static String baseUrl = "http://19.16.10.201:8004/";//电商地址
    /**
     * 添加收货地址
     */
    private final String ADD_ADDRESS = baseUrl + "address/add";
    /**
     * 支付权限
     */
    private final String PAY_STATUS = baseUrl + "pay/payStatus";
    /**
     * 更新收货地址
     */
    private final String UPDATE_ADDRESS = baseUrl + "address/update";
    /**
     * 获取收货地址
     */
    private final String ADDRESS_LIST = baseUrl + "address/getUserAddress/";
    /**
     * 删除收货地址
     */
    private final String DEL_ADDRESS = baseUrl + "address/delete/";
    private final String RELASE_PRODUCT = baseUrl + "product/add";

    /**
     * 商家入驻
     */
    private final String JSON_US = baseUrl + "shop/apply";
    /**
     * 修改商家信息
     */
    private final String EDIT_SHOP_INFORMATION = baseUrl + "shop/editShop";

    /**
     * 商品列表
     */
    private final String PRODUCT_LIST = baseUrl + "product/tables";

    /**
     * 商品详情
     */
    private final String PRODUCT_DATIELS = baseUrl + "product/getProduct/";
    /**
     * 生成订单
     */
    private final String CREAT_ORDER = baseUrl + "order/createOrder";
    /**
     * 生成拼团订单
     */
    private final String CREAT_ORDER_Group = baseUrl + "order/createPtOrder";

    /**
     * 支付宝信息
     */
    private final String ZFB_ORDER = baseUrl + "pay/alipaySign";

    /**
     * 店铺详情
     */
    private final String Dpxq = baseUrl + "shop/queryShopDetail";
    /**
     * 卖家订单列表
     */
    private final String sjddlb = baseUrl + "order/buy/orderList";
    /**
     * 订单列表
     */
    private final String Ddlb = baseUrl + "order/sell/orderList";

    /**
     * 立即发货
     */
    private final String fahuo = baseUrl + "order/faHuo";
    /**
     *
     */
    private final String evaluation = baseUrl + "order/evaluation";

    /**
     * 查询物流信息
     */
    private final String wlxx = "https://wuliu.market.alicloudapi.com/kdi?no=231580571635";

    /**
     * 确认收货
     */
    private final String qrsh = baseUrl + "order/wanCheng";
    /**
     * 取消订单
     */
    private final String cancleOrder = baseUrl + "order/quXiao";

    /**
     * 获取本地分类
     */
    private final String getFl = baseUrl + "product/cateList";

    /**
     * 商品评价  product/getProductEvaluation
     */
    private final String getProductEvaluation = baseUrl + "product/getProductEvaluation";

    /**
     * 轮播
     */
    private final String getBanner = baseUrl + "product/banner";

    /**
     * 查询店铺详情
     */
    private final String getShopDetail = baseUrl + "shop/queryShopDetail";

    /**
     * 店铺状态
     */

    private final String getShopStatus = baseUrl + "shop/queryShopStatus";

    /**
     * 根据区域
     */
    private final String getTables = baseUrl + "product/tables";
    /**
     * 添加商品
     */
    private final String updateProduct = baseUrl + "product/update";

    /**
     * 商铺的列表
     */
    private final String getShopTables = baseUrl + "shop/shopTables";
    private final String DeleteList = baseUrl + "product/deleteList";

    /**
     * 上架商品  product/open/
     */
    private final String productOpen = baseUrl + "product/open/";
    private final String producDown = baseUrl + "product/down/";
    /**
     * 拒绝接单
     */
    private final String jvJue = baseUrl + "order/jvJue";

    public void jvJue(int taskId, HashMap<String, Object> paramMap) {
        post(jvJue, paramMap, new NewCustomResponseHandler<BaseObject>(taskId) {
        });
    }

    public void deleteList(int taskId, HashMap<String, Object> paramMap) {
        post(DeleteList, paramMap, new NewCustomResponseHandler<BaseObject>(taskId) {
        });
    }

    public void productOpen(int taskId, String id) {
        post(productOpen + id, null, new NewCustomResponseHandler<BaseObject>(taskId) {
        });
    }

    public void producDown(int taskId, String id) {
        post(producDown + id, null, new NewCustomResponseHandler<BaseObject>(taskId) {
        });
    }

    public void getShopTables(int taskId, HashMap<String, Object> paramMap) {
        post(getShopTables, paramMap, new NewCustomResponseHandler<List<ProductListTwo>>(taskId) {
        });
    }

    public void getTables(int taskId, HashMap<String, Object> paramMap) {
        post(getTables, paramMap, new NewCustomResponseHandler<List<ProductList>>(taskId) {
        });
    }

    public void getShopStatus(int taskId) {
        get(getShopStatus, null, new NewCustomResponseHandler<String>(taskId) {
        });
    }

    public void getShopDetail(int taskId) {
        get(getShopDetail, null, new NewCustomResponseHandler<ShopInformationBean>(taskId) {
        });
    }

    public void getBanner(int taskId) {
        get(getBanner, null, new NewCustomResponseHandler<List<BannerBean>>(taskId) {
        });
    }

    public void getProductEvaluation(int taskId, HashMap<String, Object> paramMap) {
        post(getProductEvaluation, paramMap, new NewCustomResponseHandler<List<EventionBean>>(taskId) {
        });
    }

    public void getFl(int taskId, HashMap<String, Object> paramMap) {
        post(getFl, paramMap, new NewCustomResponseHandler<List<Flbean>>(taskId) {
        });
    }

    public void evaluation(int taskId, HashMap<String, Object> paramMap) {
        post(evaluation, paramMap, new NewCustomResponseHandler<BaseResponse>(taskId) {
        });
    }

    public void cancleOrder(int taskId, HashMap<String, Object> paramMap) {
        post(cancleOrder, paramMap, new NewCustomResponseHandler<BaseResponse>(taskId) {
        });
    }

    public void qrsh(int taskId, HashMap<String, Object> paramMap) {
        post(qrsh, paramMap, new NewCustomResponseHandler<BaseResponse>(taskId) {
        });
    }

    public void fahuo(int taskId, HashMap<String, Object> paramMap) {
        post(fahuo, paramMap, new NewCustomResponseHandler<List<OrderlsitBean>>(taskId) {
        });
    }

    public void Ddlb(int taskId, HashMap<String, Object> paramMap) {
        post(Ddlb, paramMap, new NewCustomResponseHandler<List<OrderlsitBean>>(taskId) {
        });
    }

    public void sjddlb(int taskId, HashMap<String, Object> paramMap) {
        post(sjddlb, paramMap, new NewCustomResponseHandler<List<OrderLsitTwoBean>>(taskId) {
        });
    }

    public void Dpxq(int taskId, HashMap<String, Object> paramMap) {
        get(Dpxq, null, new NewCustomResponseHandler<DpxqBean>(taskId) {
        });
    }


    public void ZfbOrder(int taskId, HashMap<String, Object> paramMap) {
        post(ZFB_ORDER, paramMap, new NewCustomResponseHandler<String>(taskId) {
        });
    }

    public void creatOrder(int taskId, HashMap<String, Object> paramMap) {
        post(CREAT_ORDER, paramMap, new NewCustomResponseHandler<OrderBean>(taskId) {
        });
    }

    public void creatOrderGroup(int taskId, HashMap<String, Object> paramMap) {
        post(CREAT_ORDER_Group, paramMap, new NewCustomResponseHandler<OrderBean>(taskId) {
        });
    }

    public void getProductDateis(int taskId, String userId) {
        String url = PRODUCT_DATIELS + userId + "/" + UserInfoUtil.getInstance().getUserInfo().getUserId();
        get(url, null, new NewCustomResponseHandler<ProductDetail>(taskId) {
        });
    }

    public void wlxx(int taskId) {
        get(wlxx, null, new NewCustomResponseHandler<BaseObject>(taskId) {
        });
    }

    public void productList(int taskId, HashMap<String, Object> paramMap) {
        post(PRODUCT_LIST, paramMap, new NewCustomResponseHandler<List<ProductList>>(taskId) {
        });
    }

    //发布商品
    public void relaseProduct(int taskId, HashMap<String, Object> paramMap) {
        post(RELASE_PRODUCT, paramMap, new NewCustomResponseHandler<BaseObject>(taskId) {
        });
    }

    //修改商品信息
    public void updateProduct(int taskId, HashMap<String, Object> paramMap) {
        post(updateProduct, paramMap, new NewCustomResponseHandler<BaseObject>(taskId) {
        });
    }

    //商家入驻
    public void JoinUs(int taskId, HashMap<String, String> paramMap) {
        post(JSON_US, paramMap, new NewCustomResponseHandler<TwoModer>(taskId) {
        });
    }

    //商家入驻
    public void editShopInformation(int taskId, HashMap<String, String> paramMap) {
        post(EDIT_SHOP_INFORMATION, paramMap, new NewCustomResponseHandler<TwoModer>(taskId) {
        });
    }

    //添加收货地址
    public void addAddress(int taskId, HashMap<String, String> paramMap) {
        post(ADD_ADDRESS, paramMap, new NewCustomResponseHandler<BaseObject>(taskId) {
        });
    }
    //是否支付权限
    public void payStatus(int taskId, HashMap<String, Object> paramMap) {
        post(PAY_STATUS, paramMap, new NewCustomResponseHandler<String>(taskId) {
        });
    }
    //修改收货地址
    public void updataAddress(int taskId, HashMap<String, String> paramMap) {
        post(UPDATE_ADDRESS, paramMap, new NewCustomResponseHandler<BaseObject>(taskId) {
        });
    }

    //获取收货地址
    public void getAddressList(int taskId, String userId) {
        String url = ADDRESS_LIST + userId;
        get(url, null, new NewCustomResponseHandler<List<MAddressListBean>>(taskId) {
        });
    }

    //删除收货地址
    public void delAddress(int taskId, String id) {
        String url = DEL_ADDRESS + id;
        post(url, null, new NewCustomResponseHandler<List<MAddressListBean>>(taskId) {
        });
    }
}
