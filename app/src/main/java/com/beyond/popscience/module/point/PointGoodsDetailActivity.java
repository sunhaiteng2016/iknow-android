package com.beyond.popscience.module.point;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.PointRestUsage;
import com.beyond.popscience.frame.pojo.ExchangeBean;
import com.beyond.popscience.frame.pojo.ProductDetailResult;
import com.beyond.popscience.frame.pojo.ProductSucBean;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.UserInfoUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 积分商城 商品详请界面
 */
public class PointGoodsDetailActivity extends BaseActivity {
    private static final int TASK_PRODUCT_DETAILS = 800003;
    private static final int TASK_INSERTRANKING_LIST = 800005;
    private static final int TASK_PRODUCT_SUC = 800004;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.point_goods_bg)
    ImageView ivGoodsBg;
    @BindView(R.id.point_goods_name)
    TextView tvGoodsName;
    @BindView(R.id.point_goods_green_coin)
    TextView tvGoodsGreenCoin;
    @BindView(R.id.point_goods_native_price)
    TextView tvGoodsPrice;
    @BindView(R.id.point_goods_post_type)
    TextView tvGoodsPostType;
    @BindView(R.id.point_goods_exchange_btn)
    Button btnGoodsExchange;
    @BindView(R.id.point_goods_exchange_cancel)
    Button btnGoodsExchangeCancel;
    @BindView(R.id.tv_product_title)
    TextView tvProductTitle;
    @BindView(R.id.tv_details)
    TextView tvDetails;
    @BindView(R.id.webview_sun)
    WebView webview_sun;
    @Request
    PointRestUsage restUsage;
    private SlideFromBottomPopup popup;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_point_goods_detail;
    }
    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);

        if (msg.getIsSuccess() && msg.getObj() != null) {
            switch (taskId) {
                case TASK_PRODUCT_DETAILS:
                    Log.e("product===", msg.getMsg());
                    ProductDetailResult result = (ProductDetailResult) msg.getObj();
                    if (result != null) {
                        ImageLoaderUtil.display(this, result.getSt().getDisplaythepicture(), ivGoodsBg);
                        tvGoodsName.setText(result.getSt().getCommodityname());
                        tvGoodsPrice.setText("￥" + result.getSt().getMoney());
                        tvGoodsGreenCoin.setText(result.getSt().getIntegral() + "绿币");
                        if (result.getSt().getFreight() != 0) {
                            tvGoodsPostType.setText("" + result.getSt().getFreight());
                        }
                        if (result.getSt().getIntegral() > result.getUsercore()) {
                            btnGoodsExchange.setVisibility(View.GONE);
                            btnGoodsExchangeCancel.setVisibility(View.VISIBLE);
                        }
                        if(result.getSt().getTitle()==null){
                            tvProductTitle.setVisibility(View.GONE);
                        } else {
                            tvProductTitle.setText(result.getSt().getTitle());
                        }
                        if(result.getSt().getDetails()==null){
                            tvDetails.setVisibility(View.GONE);
                        } else {
                            webview_sun.getSettings().setJavaScriptEnabled(true);//启用js
                            webview_sun.getSettings().setBlockNetworkImage(false);//解决图片不显示
                            String str="<html><head><title></title></head><body>"+result.getSt().getDetails()+"</body></html>";
                            webview_sun.loadDataWithBaseURL(null, str, "text/html" , "utf-8", null);
                            webview_sun.setWebViewClient(new MyWebViewClient());
                            webview_sun.addJavascriptInterface(new JavaScriptInterface(this), "imagelistner");//这个是给图片设置点击监听的，如果你项目需要webview中图片，点击查看大图功能，可以这么添加
                        }
                    }
                    break;
                case TASK_INSERTRANKING_LIST:
                    Log.e("insertranking", msg.getMsg());
                    popup.dismiss();
                    ExchangeBean successid = (ExchangeBean) msg.getObj();
                    productsuc(successid.getSuccessid());
                    break;
                case TASK_PRODUCT_SUC:
                    ProductSucBean bean = (ProductSucBean) msg.getObj();
                    if (bean.getOrde() != null) {
                        Intent intent = new Intent(this, ExchageSuccessActivity.class);
                        intent.putExtra("productSucBean", bean);
                        startActivity(intent);
                    }
                    break;
            }
        }
    }

    @Override
    public void initUI() {
        super.initUI();
        tvTitle.setText("商品详情");
        tvGoodsPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
        getGoodsDetailInfo(getIntent().getStringExtra("id"));

    }

    @OnClick({R.id.point_goods_exchange_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.point_goods_exchange_btn:
                showPopup();
                break;
        }
    }

    private void showPopup() {
        popup = new SlideFromBottomPopup(this);
        popup.setOnItemClickListener(new SlideFromBottomPopup.OnItemClickListener() {
            @Override
            public void onItemClick(View v, String name, String phone, String dizi, String address) {
                switch (v.getId()) {
                    case R.id.write_address_sure:
                        if (name.isEmpty()) {
                            ToastUtil.show(PointGoodsDetailActivity.this, "请输入收货人姓名");
                        } else if (phone.isEmpty()) {
                            ToastUtil.show(PointGoodsDetailActivity.this, "请输入收货人号码");
                        } else if (dizi.isEmpty()) {
                            ToastUtil.show(PointGoodsDetailActivity.this, "请输入地址");
                        } else if (address.isEmpty()) {
                            ToastUtil.show(PointGoodsDetailActivity.this, "请输入街道门牌等详细地址");
                        } else {
                            insertrankingList(name, phone, dizi, address, getIntent().getStringExtra("id"));
                        }
                        break;
                }
            }
        });
        popup.showPopupWindow();
    }

    /**
     * 获取商品详情
     *
     * @param id 商品id
     */
    private void getGoodsDetailInfo(String id) {
//        restUsage.productDetial(TASK_PRODUCT_DETAILS, "12", id);
        restUsage.productDetial(TASK_PRODUCT_DETAILS, UserInfoUtil.getInstance().getUserInfo().getUserId(), id);
    }

    /**
     * 兑换成功接口
     *
     * @param successid 兑换成功id
     */
    private void productsuc(String successid) {
        restUsage.productSuc(TASK_PRODUCT_SUC, successid);
    }

    /**
     * 兑换商品接口
     *
     * @param name    收货人
     * @param phone   电话
     * @param dizi    省市区
     * @param address 详细地址
     * @param proid   商品id
     */
    private void insertrankingList(String name, String phone, String dizi, String address, String proid) {
        restUsage.insertrankingList(TASK_INSERTRANKING_LIST,
                UserInfoUtil.getInstance().getUserInfo().getUserId(),
                proid,
                name,
                phone,
                dizi,
                address);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            imgReset();//重置webview中img标签的图片大小
            // html加载完成之后，添加监听图片的点击js函数
            addImageClickListner();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
    /**
     * 对图片进行重置大小，宽度就是手机屏幕宽度，高度根据宽度比便自动缩放
     **/
    private void imgReset() {
        webview_sun.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName('img'); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "var img = objs[i];   " +
                "    img.style.maxWidth = '100%'; img.style.height = 'auto';  " +
                "}" +
                "})()");
    }
    private void addImageClickListner() {
        // 这段js函数的功能就是，遍历所有的img节点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        webview_sun.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.imagelistner.openImage(this.src);  " +
                "    }  " +
                "}" +
                "})()");
    }
    public static class JavaScriptInterface {

        private Context context;

        public JavaScriptInterface(Context context) {
            this.context = context;
        }

        //点击图片回调方法
        //必须添加注解,否则无法响应
        @JavascriptInterface
        public void openImage(String img) {
            Log.i("TAG", "响应点击事件!");
            Intent intent = new Intent();
            intent.putExtra("image", img);
            intent.setClass(context, BigImageActivity.class);//BigImageActivity查看大图的类，自己定义就好
            context.startActivity(intent);
        }
    }
}
