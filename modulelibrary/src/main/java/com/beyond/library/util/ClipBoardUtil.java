package com.beyond.library.util;

import android.content.Context;
import android.text.ClipboardManager;
import android.text.TextUtils;

/**
 * 剪切板Util
 * Created by linjinfa 331710168@qq.com on 2015/1/20.
 */
public class ClipBoardUtil {

    /**
     * 实现文本复制功能
     * add by wangqianzhou
     *
     * @param content
     */
    public static void copy(String content, Context context) {
        if(TextUtils.isEmpty(content))
            return ;
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    /**
     * 实现粘贴功能
     * add by wangqianzhou
     *
     * @param context
     * @return
     */
    public static String paste(Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        return cmb.getText().toString().trim();
    }

}
