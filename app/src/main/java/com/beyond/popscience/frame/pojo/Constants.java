package com.beyond.popscience.frame.pojo;

import android.graphics.Color;

/**
 * Created by wanghongchuang
 * on 2016/8/25.
 * email:844285775@qq.com
 */
public class Constants {

    private static final String PACKAGE_NAME = "com.leiapp.constants";
    //刷新用户头像
    public static final String ACTION_UPDATE_AVATAR = PACKAGE_NAME + ".update_avatar";
    //刷新收货人信息
    public static final String ACTION_UPTATE_CONSIGNEE = PACKAGE_NAME + ".update_consignee";
    //刷新money
    public static final String ACTION_UPTATE_MONEY = PACKAGE_NAME + ".update_Money";

    //刷新申请代理
    public static final String ACTION_UPTATE_DAILI = PACKAGE_NAME + ".update_daili";

    //刷新SKU
    public static final String ACTION_UPTATE_SKU_MONEY = PACKAGE_NAME + ".update_SKU";

    //刷新main
    public static final String ACTION_UPTATE_MAIN_MONEY = PACKAGE_NAME + ".update_MAIN";

    //刷新钱包
    public static final String ACTION_UPTATE_QIANBAOMONEY = PACKAGE_NAME + ".update_QIANBAOMoney";

    //刷新评价订单
    public static final String ACTION_PINGJIA = PACKAGE_NAME + ".update_action_pingjia";


    //刷新所有订单
    public static final String ACTION_ORDER = PACKAGE_NAME + ".update_action_order";

    //刷新所有订单
    public static final String ACTION_ORDERPT = PACKAGE_NAME + ".update_action_orderPT";

    //刷新我的收藏
    public static final String ACTION_COLLOCTION = PACKAGE_NAME + ".update_action_colloction";

    //刷新乐点商城
    public static final String ACTION_HAPPYSTORE = PACKAGE_NAME + ".update_action_happystore";

    //监听粘贴板
    public static final String Listen_Data = PACKAGE_NAME + ".listen_data";

    //用户在其他设备登录
    public static final String ACTION_RONGIM_OFFLINE_BY_OTHER = PACKAGE_NAME + ".action.offline.by.other";

    //读秒
    public static final String ACTION_RUN_TIME = PACKAGE_NAME + ".action.time";

    //刷新购物车
    public static final String ACTION_CART = PACKAGE_NAME + ".action.CART";

    //刷新购物车数量
    public static final String ACTION_NUM = PACKAGE_NAME + ".action.NUM";

    //刷新团队
    public static final String ACTION_TEAM = PACKAGE_NAME + ".action.team";

    //刷新购物车
    public static final String ACTION_CHOOSE_GWC = PACKAGE_NAME + ".action.gwc";

    //刷新是否设置交易密码
    public static final String ACTION_JAIOYIPWD = PACKAGE_NAME + ".action.jiaoyipwd";

    //认证状态
    public static final String ACTION_RENZHENG = PACKAGE_NAME + ".action.renzheng";

    //刷新银行卡列表
    public static final String ACTION_BANKLIST = PACKAGE_NAME + ".action.banklist";

    //刷新我的绿币
    public static final String ACTION_JIFEN = PACKAGE_NAME + ".action.jifen";

    public static final int[] tagColors = new int[]{
            Color.parseColor("#90C5F0"),
            Color.parseColor("#91CED5"),
            Color.parseColor("#F88F55"),
            Color.parseColor("#C0AFD0"),
            Color.parseColor("#E78F8F"),
            Color.parseColor("#67CCB7"),
            Color.parseColor("#F6BC7E")
    };

    //微信支付成功 --- CartMakeSureActivity
    public static final String ACTION_WEIXINPAY_SUCCRESS_CARTMAKESUREACTIVITY =".action.weixinpay.succress.CartMakeSureActivity" ;
    //微信支付成功 --- LDOrderConfirmActivity
    public static final String ACTION_WEIXINPAY_SUCCRESS_LDORDERCONFIRMACTIVITY =".action.weixinpay.succress.LDOrderConfirmActivity" ;
    //微信支付成功 --- PayActivity
    public static final String ACTION_WEIXINPAY_SUCCRESS_PAYACTIVITY =".action.weixinpay.succress.PayActivity" ;
    //微信支付成功 --- TGMakeOrderActivity
    public static final String ACTION_WEIXINPAY_SUCCRESS_TGMAKEORDERACTIVITY =".action.weixinpay.succress.TGMakeOrderActivity" ;

    //微信支付成功
    public static final String ACTION_WEIXINPAY_SUCCRESS =".action.weixinpay.succress" ;
    //刷新首页数据
    public static final String ACTION_UPDATE_HOMEONE = PACKAGE_NAME + ".update_homeone";
    public static final String ACTION_UPDATE_HOMETWO = PACKAGE_NAME + ".update_hometwo";
    public static final String ACTION_UPDATE_HOMEFOUR = PACKAGE_NAME + ".update_homefour";
    /**
     * 刷新钱包
     */
    public static final String ACTION_UPDATE_QIANBAO = PACKAGE_NAME + ".update_qianbao";
    /**
     * 刷新店铺详情
     */
    public static final String ACTION_UPDATE_SHOP= PACKAGE_NAME + ".update_shop";
}
