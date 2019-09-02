package com.beyond.library.network.task;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.beyond.library.network.enity.MSG;


/**
 */
public final class UIOnMainThread {

    /**
     *
     */
    private static Handler mainHandler = null;

    static {
        if (mainHandler == null) {
            mainHandler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    Bundle bundle = msg.getData();
                    if (bundle == null)
                        return;
                    int id = bundle.getInt("taskId");
                    String identification = bundle.getString("identification");
                    MSG result = (MSG) bundle.get("result");
                    IUIController controller = TaskManager.getInstance().getUIController(identification);
                    if (controller == null)
                        return;
                    controller.refreshUI(id, result);
                }
            };
        }
    }

    /**
     * @param runnable
     */
    public static void runOnUiThread(Runnable runnable) {
        if (runnable == null) {
            return;
        }
        if (!isOnUiThread()) {
            mainHandler.post(runnable);
        } else {
            runnable.run();
        }
    }

    /**
     * @param bundle
     */
    public static void sendToTarget(Bundle bundle) {
        Message msg = mainHandler.obtainMessage();
        msg.setData(bundle);
        msg.sendToTarget();
    }

    /**
     * 是否在主线程
     * @return
     */
    public static boolean isOnUiThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }
}
