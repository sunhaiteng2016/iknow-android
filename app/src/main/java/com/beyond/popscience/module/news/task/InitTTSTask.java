package com.beyond.popscience.module.news.task;

import com.beyond.library.network.enity.MSG;
import com.beyond.library.network.task.ITask;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.thirdsdk.ThirdSDKManagerV2;

/**
 * 初始化 TTS
 * Created by linjinfa on 2017/6/25.
 * email 331710168@qq.com
 */
public class InitTTSTask extends ITask {

    public InitTTSTask(int taskId) {
        super(taskId);
    }

    @Override
    public MSG doTask() {
        ThirdSDKManagerV2.getInstance().initTTSVoice(BeyondApplication.getInstance());
        return new MSG(true, "");
    }

}
