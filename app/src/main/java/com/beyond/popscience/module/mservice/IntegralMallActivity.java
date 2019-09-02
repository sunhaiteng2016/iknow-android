package com.beyond.popscience.module.mservice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.util.UserInfoUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by qidan.chen on 2018/4/10.
 */

public class IntegralMallActivity extends BaseActivity {
    private static final String KEY_LINK = "url";
    //需要在URL后面拼接一个UUID 格式：？userid = xxx
    private static final String USER_ID = "userId";


    @BindView(R.id.webview)
    WebView webView;

    private String url;
    private String userId;

    public static void startActivity(Context context,String url,String userId){
        Intent intent = new Intent(context,IntegralMallActivity.class);
        intent.putExtra(KEY_LINK,url);
        intent.putExtra(USER_ID,userId);
        context.startActivity(intent);
    }

    @Override
    public void initUI() {
        super.initUI();
        url = getIntent().getStringExtra(KEY_LINK);
        userId =  getIntent().getStringExtra(USER_ID);

        url = url + "?userId=" + userId;

//        Log.i("test","url" + url);
        initWebView();
    }


    private void initWebView(){

        initSetting();

        //WebView加载web资源
        webView.loadUrl(url);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                if (url.contains("starry.appwzd.cn")) {
                    url = url.replace("null", UserInfoUtil.getInstance().getUserInfo().getToken());
                }
                view.loadUrl(url);
                return true;
            }
        });
    }

    private void initSetting(){
        //@SuppressLint("JavascriptInterface")
        //webView.addJavascriptInterface(new JsInterface(this),"androidWebView");

        WebSettings webSettings = webView.getSettings();

        // JSEngine Enable
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);

        // 默认使用缓存
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

        // 设置可以使用localStorage
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);

        // 应用可以有缓存
        webSettings.setAppCacheEnabled(true);
        String appCaceDir = getApplicationContext()
                .getDir("cache", Context.MODE_PRIVATE)
                .getPath();
        webSettings.setAppCachePath(appCaceDir);

        // 可以读取文件缓存(manifest生效)
        webSettings.setAllowFileAccess(true);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_integral_mall;
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK &&webView.canGoBack() ) {
            webView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void backClick(View view){
        if (webView.canGoBack()){
            webView.goBack();
        } else {
            finish();
        }
    }

//    private class JsInterface {
//        private Context mContext;
//
//        public JsInterface(Context context) {
//            this.mContext = context;
//        }
//
//        @JavascriptInterface
//        public void js2AndroidGoBack() {
//            backClick(null);
//        }
//
////        public
//
////        //在js中调用window.AndroidWebView.chen(name)，便会触发此方法。
////        @JavascriptInterface
////        public void chen(String name) {
////            Log.e("name==" ,name);
////        }
//    }
}
