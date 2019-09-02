package com.beyond.library.sharesdk.links;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.beyond.library.util.ToastUtil;

import java.util.HashMap;

import cn.sharesdk.framework.CustomPlatform;

/**
 * 转发到乡镇 这个是没有界面
 */
public class Link extends CustomPlatform {

    public static final String NAME = Link.class.getSimpleName();
    private Context mContext;

    public Link(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getSortId() {
        return 36;
    }

    @Override
    protected boolean checkAuthorize(int i, Object o) {
        return false;
    }

    @Override
    public void share(ShareParams params) {
        /*HashMap<String, Object> map = params.toMap();
        String text = map.get("text") != null ? (String) map.get("text") : "";
        String tip = "内容为空";
        if (!TextUtils.isEmpty(text)) {
            clioboar(mContext, text);
            tip = "已复制到剪切板";
        }
        ToastUtil.show(mContext, tip);*/
        String titles = params.getTitle();
        String url = params.getUrl();
        String pics = params.getImageUrl();
        Intent intent = new Intent();
        intent.setAction("com.test.TestActivity");
        intent.addCategory("com.test.TestActivity.category");
        intent.putExtra("titles",titles);
        intent.putExtra("pics",pics);
        intent.putExtra("link",url);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        getContext().startActivity(intent);
    }

    public static void clioboar(Context context, String message) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Activity.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", message);
        clipboard.setPrimaryClip(clip);
    }
}
