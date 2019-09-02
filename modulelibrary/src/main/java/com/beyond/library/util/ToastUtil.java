package com.beyond.library.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


public class ToastUtil {

    /**
     * @param context
     * @param message
     */
    public static void show(Context context, String message) {
        show(context, message, Toast.LENGTH_SHORT, Gravity.BOTTOM);
    }

    public static void showCenter(Context context, String message) {
        show(context, message, Toast.LENGTH_SHORT, Gravity.CENTER);
    }

    /**
     * @param context
     * @param message
     */
    public static void show(Context context, String message, int duration, int gravity) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        Toast toast = Toast.makeText(context, message, duration);
        if(toast.getView()!=null && toast.getView() instanceof ViewGroup){
            ViewGroup viewGroup = ((ViewGroup)toast.getView());
            int count = viewGroup.getChildCount();
            for(int i=0;i<count;i++){
                View childView = viewGroup.getChildAt(i);
                if(childView!=null && childView instanceof TextView){
                    ((TextView)childView).setTextSize(TypedValue.COMPLEX_UNIT_PX, DensityUtil.dp2px(context, 19));
                }
            }
        }
        toast.setGravity(gravity, 0, 0);
        toast.show();
    }

}
