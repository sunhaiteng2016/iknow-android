package com.beyond.library.util;


import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

/**
 * 单位转换操作类
 * @author linjinfa@126.com
 * @date 2013-4-19 下午5:28:11 
 */
public class DensityUtil {

	/** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     * @param dp
     * @return
     */
    public static int dpTopx(Context context, float dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
	}
    
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dp(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }
    
    /**
     * 根据手机的分辨率从 px 的单位 转成为 sp
     * @param context
     * @param pxValue
     * @return
     */
    public static float px2sp(Context context, float pxValue) {
    	float scale = context.getResources().getDisplayMetrics().density;  
		return pxValue / scale + 0.5f;
	}

    /**
     * 根据手机的分辨率从 sp 的单位 转成为 px
     * @param context
     * @param spValue
     * @return
     */
	public static int sp2px(Context context, float spValue) {
		float scale = context.getResources().getDisplayMetrics().density;  
		return (int) (spValue * scale + 0.5f);
	}

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        int width = 0;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= 13) {
            Point size = new Point();
            display.getSize(size);
            width = size.x;
        } else {
            width = display.getWidth();
        }
        return width;
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        int height = 0;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= 13) {
            Point size = new Point();
            display.getSize(size);
            height = size.y;
        } else {
            height = display.getHeight();
        }
        return height;
    }

    /**
     * 获取系统字体 Scale
     * @return
     */
    public static float getSystemFontScale(){
        try {
            Class<?> activityManagerNative = Class.forName("android.app.ActivityManagerNative");
            Object am = activityManagerNative.getMethod("getDefault").invoke(activityManagerNative);
            Object config = am.getClass().getMethod("getConfiguration").invoke(am);
            Configuration configs = (Configuration)config;
            return configs.fontScale;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return 1f;
    }

    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
