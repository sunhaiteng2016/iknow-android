package com.beyond.popscience.frame.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.beyond.library.util.DensityUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.widget.wheelview.WheelMenuInfo;
import com.beyond.popscience.widget.wheelview.WheelView;
import com.beyond.popscience.widget.wheelview.adapter.WheelMenuAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 选择文化程度/职业
 * Created by linjinfa on 2017/6/12.
 * email 331710168@qq.com
 */
public class SelectCultureDialog {

    /**
     * 文化程度
     */
    private static final WheelMenuInfo WHEEL_MENU_INFOS[] = new WheelMenuInfo[]{
            new WheelMenuInfo("1", "博士"),
            new WheelMenuInfo("2", "硕士"),
            new WheelMenuInfo("3", "本科"),
            new WheelMenuInfo("4", "专科"),
            new WheelMenuInfo("5", "高中"),
            new WheelMenuInfo("6", "高中以下")
    };

    /**
     * 学历选择   比文化程度多一个 全部 选项
     */
    private static final WheelMenuInfo WHEEL_MENU_EDUCATION[] = new WheelMenuInfo[]{
            new WheelMenuInfo(null, "全部"),
            new WheelMenuInfo("1", "博士"),
            new WheelMenuInfo("2", "硕士"),
            new WheelMenuInfo("3", "本科"),
            new WheelMenuInfo("4", "大专"),
            new WheelMenuInfo("5", "高中"),
            new WheelMenuInfo("6", "高中以下")
    };

    /**
     * 职业
     */
    private static final WheelMenuInfo WHEEL_MENU_INFOS_CAREER[] = new WheelMenuInfo[]{
            new WheelMenuInfo("1", "公务员"),
            new WheelMenuInfo("2", "教师"),
            new WheelMenuInfo("3", "医务人员"),
            new WheelMenuInfo("4", "科研人员"),
            new WheelMenuInfo("5", "学生"),
            new WheelMenuInfo("6", "农民"),
            new WheelMenuInfo("7", "工人"),
            new WheelMenuInfo("8", "企业主"),
            new WheelMenuInfo("9", "企业管理人员"),
            new WheelMenuInfo("10", "金融服务业"),
            new WheelMenuInfo("11", "律师"),
            new WheelMenuInfo("12", "技术人员"),
            new WheelMenuInfo("13", "自由职业")
    };
    /**
     * 工作经验
     */
    private static final WheelMenuInfo WHEEL_MENU_INFO_WORKEXPERIENCE[] = new WheelMenuInfo[]{
            new WheelMenuInfo("1", "全部"),
            new WheelMenuInfo("2", "应届生"),
            new WheelMenuInfo("3", "1年以内"),
            new WheelMenuInfo("4", "1-3年"),
            new WheelMenuInfo("5", "3-5年"),
            new WheelMenuInfo("6", "5-10年"),
            new WheelMenuInfo("7", "10年以上")
    };

    /**
     * 薪资
     */
    private static final WheelMenuInfo WHEEL_MENU_INFO_SALARY[] = new WheelMenuInfo[]{
            new WheelMenuInfo(null, "全部"),
            new WheelMenuInfo("1", "1000以下"),
            new WheelMenuInfo("2", "1000-3000"),
            new WheelMenuInfo("3", "3000-5000"),
            new WheelMenuInfo("4", "5000-7000"),
            new WheelMenuInfo("5", "7000-10000"),
            new WheelMenuInfo("6", "10000以上"),
            new WheelMenuInfo("7", "面议")
    };

    /**
     * 薪资
     */
    private static final WheelMenuInfo WHEEL_MENU_INFO_SALARY_x[] = new WheelMenuInfo[]{
            new WheelMenuInfo("1", "1000以下"),
            new WheelMenuInfo("2", "1000-3000"),
            new WheelMenuInfo("3", "3000-5000"),
            new WheelMenuInfo("4", "5000-7000"),
            new WheelMenuInfo("5", "7000-10000"),
            new WheelMenuInfo("6", "10000以上"),
            new WheelMenuInfo("7", "面议")
    };

    /**
     * 文化程度 默认
     */
    public static final int TYPE_DEFAULT = 0;
    /**
     * 学历
     */
    public static final int TYPE_EDUCATION = 1;
    /**
     * 职业
     */
    public static final int TYPE_CAREER = 2;
    /**
     * 工资经验
     */
    public static final int TYPE_WORKEXPERIENCE = 3;
    /**
     * 薪资
     */
    public static final int TYPE_SALARY = 4;
    /**
     * 薪资，，没有全部选项了
     */
    public static final int TYPE_SALARY_X = 5;

    private boolean isCyclic = true;

    private WheelView cultureWheelView;
    private Dialog dialog;
    private OnSelectClickListener onSelectClickListener;
    private WheelMenuAdapter wheelMenuAdapter;

    public SelectCultureDialog(Context context) {
        init(context, TYPE_DEFAULT);
    }

    public SelectCultureDialog(Context context, int type) {
        init(context, type);
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context, int type) {
        dialog = new Dialog(context, R.style.DialogPushUpInAnimStyle);
        dialog.setContentView(R.layout.dialog_culture);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = DensityUtil.getScreenWidth(context);
        window.setAttributes(layoutParams);

        View bgView = window.findViewById(R.id.bgView);
        final TextView cancelTxtView = (TextView) window.findViewById(R.id.cancelTxtView);
        final TextView okTxtView = (TextView) window.findViewById(R.id.okTxtView);
        cultureWheelView = (WheelView) window.findViewById(R.id.cultureWheelView);
        cultureWheelView.setViewBackgroundColor(Color.parseColor("#50f5f6f7"));
        cultureWheelView.setCyclic(isCyclic);
        cultureWheelView.setTextSize(18);

        List<WheelMenuInfo> list = getWheelMenuInfosByType(type);
        wheelMenuAdapter = new WheelMenuAdapter(list);
        cultureWheelView.setAdapter(wheelMenuAdapter);

        bgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        cancelTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        okTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSelectClickListener != null) {
                    WheelMenuInfo wheelMenuInfo = wheelMenuAdapter.getItemObject(cultureWheelView.getCurrentItem());

                    onSelectClickListener.onOk(wheelMenuInfo);
                }
                dismiss();
            }
        });
    }

    private static List<WheelMenuInfo> getWheelMenuInfosByType(int type) {
        List<WheelMenuInfo> list = null;
        switch (type) {
            case TYPE_SALARY_X:
                list = Arrays.asList(WHEEL_MENU_INFO_SALARY_x);
                break;
            case TYPE_SALARY:
                list = Arrays.asList(WHEEL_MENU_INFO_SALARY);
                break;
            case TYPE_WORKEXPERIENCE:
                list = Arrays.asList(WHEEL_MENU_INFO_WORKEXPERIENCE);
                break;
            case TYPE_CAREER:
                list = Arrays.asList(WHEEL_MENU_INFOS_CAREER);
                break;
            case TYPE_EDUCATION:
                list = Arrays.asList(WHEEL_MENU_EDUCATION);
                break;
            case TYPE_DEFAULT:
                list = Arrays.asList(WHEEL_MENU_INFOS);
                break;
            default:
                list = new ArrayList<>();
                break;
        }
        return list;
    }

    /**
     * 设置选中的数据
     *
     * @param code
     */
    public void setSelectedMenu(String code) {
        if (!TextUtils.isEmpty(code)) {
            for (int i = 0; i < wheelMenuAdapter.getItemsCount(); i++) {
                WheelMenuInfo wheelMenuInfo = wheelMenuAdapter.getItemObject(i);
                if (wheelMenuInfo != null && code.equals(wheelMenuInfo.getCode())) {
                    cultureWheelView.setCurrentItem(i);
                    break;
                }
            }
        }
    }

    /**
     * 设置选中的数据
     *
     * @param name
     */
    public void setSelectedMenuName(String name) {
        if (!TextUtils.isEmpty(name)) {
            for (int i = 0; i < wheelMenuAdapter.getItemsCount(); i++) {
                WheelMenuInfo wheelMenuInfo = wheelMenuAdapter.getItemObject(i);
                if (wheelMenuInfo != null && name.equals(wheelMenuInfo.getName())) {
                    cultureWheelView.setCurrentItem(i);
                    break;
                }
            }
        }
    }

    public static String getMenuCode(String name, int type) {
        if (name != null) {
            List<WheelMenuInfo> list = getWheelMenuInfosByType(type);
            for (int i = 0; i < list.size(); i++) {
                WheelMenuInfo wheelMenuInfo = list.get(i);
                if (name.equals(wheelMenuInfo.getName())) {
                    return wheelMenuInfo.getCode();
                }
            }
        }
        return null;
    }

    /**
     * @param code
     * @return
     */
    public static String getMenuName(String code, int type) {
        if (!TextUtils.isEmpty(code)) {
            List<WheelMenuInfo> list = getWheelMenuInfosByType(type);
            for (int i = 0; i < list.size(); i++) {
                WheelMenuInfo wheelMenuInfo = list.get(i);
                if (code.equals(wheelMenuInfo.getCode())) {
                    return wheelMenuInfo.getName();
                }
            }
        }
        return null;
    }

    /**
     * @param code
     * @return
     */
    public static String getMenuName(String code) {
        return getMenuName(code, TYPE_DEFAULT);
    }

    /**
     * @return
     */
    public WheelMenuInfo getSelectedMenu() {
        WheelMenuInfo wheelMenuInfo = wheelMenuAdapter.getItemObject(cultureWheelView.getCurrentItem());
        return wheelMenuInfo;
    }

    /**
     *
     */
    public void show() {
        cultureWheelView.post(new Runnable() {
            @Override
            public void run() {
                cultureWheelView.invalidateTxt();
            }
        });
        dialog.show();
    }

    public SelectCultureDialog setCyclic(boolean cyclic) {
        this.isCyclic = isCyclic;
        if (cultureWheelView != null) {
            cultureWheelView.setCyclic(cyclic);
        }
        return this;
    }

    /**
     *
     */
    public void dismiss() {
        dialog.dismiss();
    }

    public OnSelectClickListener getOnSelectClickListener() {
        return onSelectClickListener;
    }

    public void setOnSelectClickListener(OnSelectClickListener onSelectClickListener) {
        this.onSelectClickListener = onSelectClickListener;
    }

    public interface OnSelectClickListener {
        void onOk(WheelMenuInfo wheelMenuInfo);
    }

}
