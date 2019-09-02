package com.beyond.library.sharesdk.shareInfo;

import android.content.Context;
import android.graphics.Bitmap;

import com.beyond.library.sharesdk.ShareSdkConfig;
import com.beyond.library.sharesdk.WebViewShare;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.system.text.ShortMessage;

/**
 * Created by lenovo on 2015/8/19.
 */
public class ShareInfoFactory {

    public static HashMap<String, BaseShareFields> create(Context context, List<Platform> name, Object obj) {
        HashMap<String, BaseShareFields> map = matchType(context, name, obj);
        return map == null ? null : prePostProcess(context, map);
    }

    public static HashMap<String, BaseShareFields> matchType(Context context, List<Platform> name, Object obj) {
        if (obj instanceof Bitmap) {
            return new BitmapShare(name, (Bitmap) obj).getMutiMap();
        } else if (obj instanceof HashMap) {
            return new HtmlShare(name, (HashMap<String, WebViewShare>) obj).getMutiMap();
        }
        return null;
    }

    private static HashMap<String, BaseShareFields> prePostProcess(Context context, HashMap<String, BaseShareFields> map) {
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            BaseShareFields fields = map.get(key);
            HashMap<String, Object> shareParamsMap = fields.getShareParamsMap();

            if (!shareParamsMap.containsKey(BaseShareFields.FIELDS_IMAGE_DATA)
                    && !shareParamsMap.containsKey(BaseShareFields.FIELDS_IMAGE_PATH)
                    && !shareParamsMap.containsKey(BaseShareFields.FIELDS_IMAGE_URL)) {
                String filePath = ShareSdkConfig.getInstance().getLogoPath();
                shareParamsMap.put(BaseShareFields.FIELDS_IMAGE_PATH, filePath);
            }

            if (key.equals(SinaWeibo.NAME)) {
                fields.getShareParamsMap().remove(BaseShareFields.FIELDS_TITLE);
                map.put(key, fields);
            } else if (key.equals(ShortMessage.NAME)) {
                fields.getShareParamsMap().remove(BaseShareFields.FIELDS_TITLE);
                fields.getShareParamsMap().remove(BaseShareFields.FIELDS_IMAGE_PATH);
                fields.getShareParamsMap().remove(BaseShareFields.FIELDS_IMAGE_URL);
                fields.getShareParamsMap().remove(BaseShareFields.FIELDS_IMAGE_DATA);
                fields.getShareParamsMap().remove(BaseShareFields.FIELDS_IMAGE_ARRAY);
                map.put(key, fields);
            }
        }
        return map;
    }
}
