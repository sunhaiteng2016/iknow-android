package com.beyond.popscience.module.home;

import android.text.TextUtils;

import com.beyond.library.network.enity.MSG;
import com.beyond.library.network.net.httpclient.httputil.HttpUtil;
import com.beyond.library.network.task.ITask;
import com.beyond.popscience.frame.util.FileUtil;

/**
 * 清除缓存
 * Created by linjinfa on 2017/6/16.
 * email 331710168@qq.com
 */
public class ClearCacheTask extends ITask {

    public ClearCacheTask(int taskId) {
        super(taskId);
    }

    @Override
    public MSG doTask() {
        if (HttpUtil.getInstance().getHttpInterceptor() != null) {
            String cacheDir = HttpUtil.getInstance().getHttpInterceptor().getCacheDir();
            if (!TextUtils.isEmpty(cacheDir)) {
                FileUtil.deleteDirectory(cacheDir);
            }
        }
        return new MSG(true, "缓存清理成功");
    }

}
