package com.beyond.popscience.module.mservice;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beyond.library.sharesdk.PlatformListFakeActivity;
import com.beyond.library.sharesdk.ShareUtil;
import com.beyond.library.sharesdk.WebViewShare;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.util.SPUtils;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.module.home.entity.News;
import com.beyond.popscience.module.news.NewsDetailActivity;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by yao.cui on 2017/6/25.
 */

public class X5WebViewActivity extends BaseActivity {
    private static final String KEY_LINK = "url";
    private static final String KEY_TITLE = "title";


    @BindView(R.id.llWebContainer)
    LinearLayout llWebContainer;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.webview)
    WebView webView;
    @BindView(R.id.leftTxtView)
    TextView tvLeft;
    @BindView(R.id.iv_share)
    ImageView ivShare;

    private String url;
    private String shareUrl;
    private boolean isTouPian;

    public static void startActivity(Context context, String url, String title) {
        Intent intent = new Intent(context, X5WebViewActivity.class);
        intent.putExtra(KEY_LINK, url);
        intent.putExtra(KEY_TITLE, title);
        context.startActivity(intent);
    }

    @Override
    public void initUI() {
        super.initUI();
        url = getIntent().getStringExtra(KEY_LINK);
        tvTitle.setText(getIntent().getStringExtra(KEY_TITLE));

        if (url.contains("http://starry.appwzd.cn/web/views/api/index.html")) {
            String[] newUrl = url.split("id=");
            isTouPian = true;
            shareUrl = "http://starry.appwzd.cn/wxServer/auth?id=" + newUrl[1];
        } else if (url.contains("http://starry.appwzd.cn/web/views/activity-api/index.html")) {
            String[] newUrl = url.split("id=");
            shareUrl = "http://starry.appwzd.cn/wxServer/authActivity?id=" + newUrl[1];
            isTouPian = false;
        } else {
            ivShare.setVisibility(View.GONE);
        }
        initWebView();
    }

    private void initWebView() {
        initSetting();
        //WebView加载web资源
        String mobile = (String) SPUtils.get(this, "Mobile", "");
        if (url.indexOf("keyipu") != -1) {
            String mUrl = url + mobile;
            webView.loadUrl(mUrl);
        } else {
            webView.loadUrl(url);
        }
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                if (url == null) return false;

                if (url.contains("starry.appwzd.cn")) {
                    url =url+"&userid="+UserInfoUtil.getInstance().getUserInfo().getToken();
                }
                try {
                    if (url.startsWith("http:") || url.startsWith("https:")) {
                        view.loadUrl(url);
                        return true;
                    } else {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        return true;
                    }
                } catch (Exception e) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    return false;
                }
            }
        });

    }

    private void initSetting() {
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
        return R.layout.activity_webview_x5;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick(R.id.leftTxtView)
    public void close() {
        finish();
    }

    @Override
    public void backClick(View view) {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.iv_share)
    public void onViewClicked() {
        if (isTouPian) {
            WebViewShare webViewShare = new WebViewShare();
            webViewShare.setTitle(getIntent().getStringExtra(KEY_TITLE));
            webViewShare.setImgUrl("");
            webViewShare.setLink(shareUrl);
            ShareUtil.directShareWeiXin(this, webViewShare, new PlatformListFakeActivity.OnResultListener() {
                @Override
                public void onSuccess(Platform platform) {
                    ToastUtil.showCenter(X5WebViewActivity.this, "恭喜您, + 1 科普绿币!");
                }

                @Override
                public void onError(Platform platform) {
                    Log.e("share", "onError" + platform.getName());
                }

                @Override
                public void onCancel(Platform platform) {
                    Log.e("share", "onError" + platform.getName());
                }
            });
        } else {
            WebViewShare webViewShare = new WebViewShare();
            webViewShare.setTitle(getIntent().getStringExtra(KEY_TITLE));
            webViewShare.setImgUrl("");
            webViewShare.setLink(shareUrl);
            ShareUtil.directShare(this, webViewShare, new PlatformListFakeActivity.OnResultListener() {
                @Override
                public void onSuccess(Platform platform) {
                    ToastUtil.showCenter(X5WebViewActivity.this, "恭喜您, + 1 科普绿币!");
                }

                @Override
                public void onError(Platform platform) {
                    Log.e("share", "onError" + platform.getName());
                }

                @Override
                public void onCancel(Platform platform) {
                    Log.e("share", "onError" + platform.getName());
                }
            });
        }

    }
}
