package com.beyond.popscience.api;

/**
 * 新的接口地址
 */

public class UrlConfig {
    /**
     * 地址列表
     */
    public static final String ADDRESS_LIST = "/address/addressList";

    /**
     * buy_user_id	下单人
     2 product_id	商品id
     3 bill_num     购买数量
     */
    public static final String CREAT_ORDER = "/order/creatOrder";//order/creatOrder

    /**
     1 userId          登录人id
     2 contactName     联系人姓名
     3 contactPhone    联系人电话
     4 province        省
     5 city            市
     6 area            区
     7 street          街道
     8 addressDetail   具体地址
     9 status          状态             1:默认地址 2,3,以上常用地址
     */
    public static final String ADDRESS_ADD = "/address/addAddress";

    /**
     *       order_des 	订单留言
     2       order_code      订单号          (订单号和order_id不一样)
     3       address         地址            用户选择的地址
     */
    public static final String ALIPAY = "/alipay/dopay";

    /**
     * addressId       地址id          不可空
     2       userId          登录人id
     3       contactName     联系人姓名
     4       contactPhone    联系人电话
     5       province        省
     6       city            市
     7       area            区
     8       street          街道
     9       addressDetail   具体地址
     10      status          状态             1:默认地址 2,3,以上常用地址
     */
    public static final String UPDATE_ADDRESS = "/address/updateCommit";

    /**
     * addressId       地址id          不可空
     */
    public static final String DETELT_ADDRESS = "/address/deleteCommit";

}
