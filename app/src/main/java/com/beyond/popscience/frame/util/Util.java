package com.beyond.popscience.frame.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.beyond.library.util.DateUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by linjinfa on 2017/6/9.
 * email 331710168@qq.com
 */
public class Util {

    /**
     * @return 1:技能 2:任务 3:出租出售 4求租求购 5商品
     */
    public static String getGoodsTypeByTabId(String tabId) {
        if ("5".equals(tabId)) {  //商品
            return "5";
        }
        return "1";
    }

    /**
     * 根据比例 返回 高度 4:3
     *
     * @return
     */
    public static int getImageHeight(int imgWidth) {
        return 3 * imgWidth / 4;
    }

    /**
     * 根据比例 返回 高度 5:3
     *
     * @return
     */
    public static int getImageHeight5V3(int imgWidth) {
        return 3 * imgWidth / 5;
    }

    /**
     * 返回 1:1 的url
     *
     * @return
     */
    public static String getUrlProportion1V1(String url) {
        if (TextUtils.isEmpty(url)) {
            return url;
        }
        String appendParam = "?x-oss-process=image/resize,m_lfit,w_500,limit_0/auto-orient,1/quality,q_90";
        if (url.endsWith(appendParam)) {
            return url;
        }
        return url + appendParam;
    }

    /**
     * 返回 5:3 的url
     *
     * @return
     */
    public static String getUrlProportion5V3(String url) {
        if (TextUtils.isEmpty(url)) {
            return url;
        }
        String appendParam = "?x-oss-process=image/resize,m_lfit,w_1000,limit_0/auto-orient,1/quality,q_90";
        if (url.endsWith(appendParam)) {
            return url;
        }
        return url + appendParam;
    }

    /**
     * 获取显示的时间
     *
     * @param currDateTime
     * @param compareDateTime
     * @return
     */
    public static String getDisplayDateTime(String currDateTime, String compareDateTime) {
        Date currDate = DateUtil.toDateTime(currDateTime);
        Date compareDate = DateUtil.toDateTime(compareDateTime);
        if (currDate == null || compareDate == null) {
            return compareDateTime;
        }
        String currDateStr = DateUtil.toPattern(currDateTime, DateUtil.DATE);
        String compareDateStr = DateUtil.toPattern(compareDateTime, DateUtil.DATE);
        if (TextUtils.isEmpty(currDateStr) || TextUtils.isEmpty(compareDateStr)) {
            return compareDateTime;
        }
        if (!currDateStr.equalsIgnoreCase(compareDateStr)) {  //不在同一天
            return compareDateTime;
        }
        long diff = currDate.getTime() - compareDate.getTime();
        if (diff < 0) {
            return compareDateTime;
        }
        float minute = (diff / 1000f / 60f);

        if (minute < 60) {
            return (int) Math.ceil(minute < 1 ? 1 : minute) + "分钟前";
        }
        return (int) (minute / 60) + "小时前";
    }

    /**
     * @param currDateTime
     * @param olderTime
     * @return
     */
    public static String getBetweenTime(String currDateTime, String olderTime) {
        if (TextUtils.isEmpty(currDateTime) || TextUtils.isEmpty(olderTime)) {
            return "0";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DATETIME);
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(currDateTime));
            long time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(olderTime));
            long time2 = cal.getTimeInMillis();
            long betweenTimeMin = (time1 - time2) / (1000 * 60);
            if (betweenTimeMin < 60) {
                return (int) Math.ceil(betweenTimeMin < 1 ? 1 : betweenTimeMin) + "分钟";
            }
            long betweenTimeHour = betweenTimeMin / 60;
            if (betweenTimeHour < 24) {
                return (int) Math.ceil(betweenTimeHour) + "小时";
            }
            long betweenDay = betweenTimeHour / 24;
            return (int) Math.ceil(betweenDay) + "天";

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "0";
    }

    /**
     * 获取显示的时间
     *
     * @param currDateTime
     * @param compareDateTime
     * @return
     */

    public static String getDisplayDateTimeV2(String currDateTime, String compareDateTime) {
        Date currDate = DateUtil.toDateTime(currDateTime);
        Date compareDate = DateUtil.toDateTime(compareDateTime);
        if (currDate == null || compareDate == null) {
            return compareDateTime;
        }
        String currDateStr = DateUtil.toPattern(currDateTime, DateUtil.DATE);
        String compareDateStr = DateUtil.toPattern(compareDateTime, DateUtil.DATE);
        if (TextUtils.isEmpty(currDateStr) || TextUtils.isEmpty(compareDateStr)) {
            return compareDateTime;
        }
        if (!currDateStr.equalsIgnoreCase(compareDateStr)) {  //不在同一天
            try {
                long dateDiff = Math.abs(DateUtil.getDaysBetween(DateUtil.toDateTime(compareDateTime), DateUtil.toDateTime(currDateTime)));
                if (dateDiff <= 5) {
                    return String.valueOf(dateDiff) + "天前";
                }
                return compareDateStr;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return compareDateTime;
        }
        long diff = currDate.getTime() - compareDate.getTime();
        if (diff < 0) {
            return compareDateTime;
        }
        float minute = (diff / 1000f / 60f);

        if (minute < 60) {
            return (int) Math.ceil(minute < 1 ? 1 : minute) + "分钟前";
        }
        return (int) (minute / 60) + "小时前";
    }

    /**
     * 获取 APP 的版本名称
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String version = "";
        if (context == null) return version;
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 获取 APP 的版本名称
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int version = 0;
        if (context == null)
            return version;
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.widthPixels;
    }


    /**
     * 格式化数据，1.00=>1   1.10=>1.1   1.001=>1
     *
     * @param money
     * @return
     */
    public static String formatMoney(String money) {
        if (money == null || "".equals(money)) {
            return "";
        }
        try {
            DecimalFormat df = new DecimalFormat("0.00");
            String numStr = df.format(new BigDecimal(money));
            if (numStr != null && numStr.indexOf(".") > 0) {
                numStr = numStr.replaceAll("0+?$", "");//去掉多余的0
                numStr = numStr.replaceAll("[.]$", "");//如最后一位是.则去掉
            }
            return numStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return money;
    }

    public static String parsePrice(String lowPrice, String highPrice) {
        lowPrice = Util.formatMoney(lowPrice);
        highPrice = Util.formatMoney(highPrice);
        if (TextUtils.isEmpty(lowPrice) && TextUtils.isEmpty(highPrice)) {
            return null;
        }
        try {
            double low = Double.parseDouble(lowPrice) / 1000;
            lowPrice = formatMoney(String.valueOf((float) (Math.round(low * 100)) / 100)) + "k";
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        try {
            double high = Double.parseDouble(highPrice) / 1000;
            highPrice = formatMoney(String.valueOf((float) (Math.round(high * 100)) / 100)) + "k";

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        String result = lowPrice + " — " + highPrice;
        return result;
    }

}
