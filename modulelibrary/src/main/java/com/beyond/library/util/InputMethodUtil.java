package com.beyond.library.util;


import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 软键盘工具类
 * @author linjinfa@126.com
 * @date 2013-7-10 下午5:46:02 
 */
public class InputMethodUtil {
	
	/**
	 * 显示软键盘
	 * @param context
	 */
	public static void showSoftInput(Activity context){
		InputMethodManager inputManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.showSoftInput(context.getCurrentFocus(), 0);
	}
	
	/**
	 * 显示软键盘 并绑定到指定View
	 * @param context
	 * @param view
	 */
	public static void showSoftInput(Context context, View view){
        if(view==null){
            return ;
        }
		InputMethodManager inputManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.showSoftInput(view, InputMethodManager.RESULT_SHOWN);
		inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
	}
	
	/**
	 * 通过绑定的View 隐藏软键盘
	 * @param context
	 */
	public static void hiddenSoftInput(Context context, View view){
		if(view.getWindowToken()!=null){
            try {
                InputMethodManager inputManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}
	
	/**
	 * 隐藏软键盘
	 * @param context
	 */
	public static void hiddenSoftInput(Activity context){
		InputMethodManager inputManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if(context.getCurrentFocus()!=null && context.getCurrentFocus().getWindowToken()!=null){
			inputManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	
	/**
	 * 显示或隐藏软键盘
	 * @param context
	 */
	public static void showOrHiddenSoftInput(Activity context){
		InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
}
