package com.beyond.library.util;

import android.util.Log;

/**
 * <p>
 * <p>一般工具类,控制调试日志的输出;
 * <p>使用 L.v() L.d() L.i() L.w() and L.e()方法;
 * <li> VERBOSE  啰嗦级别  使用 Log.v();
 * <li> DEBUG    调试级别  使用 Log.d();
 * <li> INFO     信息级别  使用 Log.i();
 * <li> WARN     警告级别  使用 Log.w();
 * <li> ERROR    错误级别  使用 Log.e();
 */
public class L {

    /**
     *
     */
    public static boolean isDebug = false;

    /**
     *
     */
    private static String tag = "weimob";
    private static String tag_sql = "weimob_sql";
    private static String tag_wwq = "wwq";
    private static String tag_psd = "psd";
    private static String tag_msg = "msg";

    /**
     * 打开/关闭 输出日志内容
     * 默认情况下 这个功能是关闭的
     *
     * @param isDebug
     */
    public static void writeDebugLogs(boolean isDebug) {
        L.isDebug = isDebug;
    }

    /**
     * 打开/关闭 输出日志内容
     * 默认情况下 这个功能是关闭的
     * 默认输出日志的标签：{@code weimob_log_tag }
     *
     * @param isDebug
     * @param tag
     */
    public static void writeDebugLogs(boolean isDebug, String tag) {
        L.isDebug = isDebug;
        L.tag = tag;
    }

    /**
     * 获取当前输出日志的标签
     */
    public static String getTag() {
        return tag;
    }

    /**
     * 输出{@code VERBOSE} {@link } 级别的日志信息.
     *
     * @param message 将要被输出的日志消息
     */
    public static void v(String message) {
        if (isDebug) {
            Log.v(tag, message);
        }
    }

    /**
     * 输出{@code VERBOSE} {@link } 级别的日志信息.
     *
     * @param tag     将以这个指定的标签输出日志消息
     * @param message 将要被输出的日志消息
     */
    public static void v(String tag, String message) {
        if (isDebug) {
            Log.v(tag, message);
        }
    }

    /**
     * 输出{@code DEBUG} {@link } 级别的日志信息.
     *
     * @param message 将要被输出的日志消息
     */
    public static void d(String message) {
        if (isDebug) {
            Log.d(tag, message);
        }
    }

    /**
     * 输出{@code DEBUG} {@link } 级别的日志信息.
     *
     * @param tag     将以这个指定的标签输出日志消息
     * @param message 将要被输出的日志消息
     */
    public static void d(String tag, String message) {
        if (isDebug) {
            Log.d(tag, message);
        }
    }

    /**
     * 输出{@code INFO} {@link } 级别的日志信息.
     *
     * @param message 将要被输出的日志消息
     */
    public static void i(String message) {
        if (isDebug) {
            Log.i(tag, message);
        }
    }

    /**
     * 输出{@code INFO} {@link } 级别的日志信息.
     *
     * @param tag     将以这个指定的标签输出日志消息
     * @param message 将要被输出的日志消息
     */
    public static void i(String tag, String message) {
        if (isDebug) {
            Log.i(tag, message);
        }
    }

    /**
     * 输出{@code WARN} {@link } 级别的日志信息.
     *
     * @param message 将要被输出的日志消息
     */
    public static void w(String message) {
        if (isDebug) {
            Log.w(tag, message);
        }
    }

    /**
     * 输出{@code WARN} {@link } 级别的日志信息.
     *
     * @param tag     将以这个指定的标签输出日志消息
     * @param message 将要被输出的日志消息
     */
    public static void w(String tag, String message) {
        if (isDebug) {
            Log.w(tag, message);
        }
    }

    /**
     * 输出{@code ERROR} {@link } 级别的日志信息.
     *
     * @param message 将要被输出的日志消息
     */
    public static void e(String message) {
        if (isDebug) {
            Log.e(tag, message);
        }
    }

    /**
     * 输出{@code ERROR} {@link } 级别的日志信息.
     *
     * @param tag     将以这个指定的标签输出日志消息
     * @param message 将要被输出的日志消息
     */
    public static void e(String tag, String message) {
        if (isDebug) {
            Log.e(tag, message);
        }
    }
    /*****************************************************************/
    /**
     * 输出{@code DEBUG} {@link } 级别的日志信息.
     *
     * @param message 将要被输出的日志消息
     */
    public static void wd(String message) {
        if (isDebug) {
            Log.d(tag_wwq, message);
        }
    }

    public static void psd(String msg) {
        if (isDebug) {
            Log.d(tag_psd, msg);
        }
    }

    /**
     * sql log
     *
     * @param msg
     */
    public static void vSql(String msg) {
        if (isDebug) {
            Log.e(tag_sql, msg);
        }
    }

    public static void msg(String msg) {
        if (isDebug) {
            Log.e(tag_msg, msg);
        }
    }

}
