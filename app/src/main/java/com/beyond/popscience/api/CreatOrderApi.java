package com.beyond.popscience.api;

import com.beyond.library.network.net.httpclient.NewCustomResponseHandler;
import com.beyond.popscience.frame.net.base.AppBaseRestUsageV2;
import com.beyond.popscience.frame.pojo.AliPayBean;
import com.beyond.popscience.frame.pojo.CreatOrderB;
import com.beyond.popscience.frame.pojo.CreatOrderBean;
import com.beyond.popscience.frame.pojo.CreatOrdersMy;
import com.beyond.popscience.frame.pojo.DataBean;
import com.beyond.popscience.frame.pojo.OrderInfoBean;
import com.beyond.popscience.frame.pojo.ServiceGoodsList;

import java.util.HashMap;
import java.util.Map;

/**
 * 说明: 生成订单
 * Created by Administrator on 2018/3/10. CREAT_ORDER
 */

public class CreatOrderApi extends AppBaseRestUsageV2{

    //生成订单
    public void creatOrder(int taskId,String buy_user_id,String product_id,String bill_num){
        Map<String,String> params = new HashMap<>();
        params.put("buy_user_id",buy_user_id);//下单人
        params.put("product_id",product_id);//商品id
        params.put("bill_num",bill_num);//购买数量
        post(UrlConfig.CREAT_ORDER,params, new NewCustomResponseHandler<CreatOrdersMy>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    //生成订单
    public void creatOrder2(int taskId,String buy_user_id,String product_id,String bill_num,String order_des,String address){
        Map<String,String> params = new HashMap<>();
        params.put("buy_user_id",buy_user_id);//下单人
        params.put("product_id",product_id);//商品id
        params.put("bill_num",bill_num);//购买数量
        params.put("order_des",order_des);//留言
        params.put("address",address);
        post(UrlConfig.CREAT_ORDER,params, new NewCustomResponseHandler<CreatOrdersMy>(taskId) {
        }.setCallSuperRefreshUI(true));
    }
    //支付宝支付
    public void payAli(int taskId,String order_des,String order_code,String address,String num){
        Map<String,String > params = new HashMap<>();
        params.put("order_des",order_des);//下单人
        params.put("order_code",order_code);//商品id
        params.put("address",address);//购买数量
        params.put("count",num);//购买数量
        post(UrlConfig.ALIPAY,params, new NewCustomResponseHandler<String>(taskId) {
        }.setCallSuperRefreshUI(true));
    }
}
