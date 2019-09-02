package com.beyond.library.network.task;

import android.os.Bundle;

import java.io.Serializable;

/**
 * Task线程
 * @author linjinfa@126.com
 * @date 2013-4-16 上午10:07:18
 */
public class TaskRunnable implements Runnable {

    private static TaskRunnable mTaskRunnable = null;

    private TaskRunnable() {
        super();
    }

    public synchronized static TaskRunnable getInstance() {
        if (mTaskRunnable == null)
            mTaskRunnable = new TaskRunnable();
        return mTaskRunnable;
    }

    @Override
    public void run() {
        ITask task = TaskManager.getInstance().getTask();
        if (task==null)
            return ;
        Object result = task.doTask();
        Bundle bundle = new Bundle();
        bundle.putInt("taskId", task.getTaskId());
        bundle.putString("identification", task.getmIdentification());
        bundle.putSerializable("result", (Serializable) result);

        UIOnMainThread.sendToTarget(bundle);
    }

}
