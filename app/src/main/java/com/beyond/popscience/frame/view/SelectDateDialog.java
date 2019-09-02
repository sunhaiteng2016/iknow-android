package com.beyond.popscience.frame.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.beyond.library.util.DateUtil;
import com.beyond.library.util.DensityUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.widget.wheelview.WheelView;
import com.beyond.popscience.widget.wheelview.adapter.WheelStringAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择日期
 * Created by linjinfa on 2017/6/12.
 * email 331710168@qq.com
 */
public class SelectDateDialog {

    private WheelView yearWheelView;
    private WheelView monthWheelView;
    private WheelView dayWheelView;
    private Dialog dialog;
    private OnSelectClickListener onSelectClickListener;

    public SelectDateDialog(Context context) {
        init(context);
    }

    /**
     * 初始化
     * @param context
     */
    private void init(Context context){
        dialog = new Dialog(context, R.style.DialogPushUpInAnimStyle);
        dialog.setContentView(R.layout.dialog_birthday);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = DensityUtil.getScreenWidth(context);
        window.setAttributes(layoutParams);

        View bgView = window.findViewById(R.id.bgView);
        final TextView cancelTxtView = (TextView) window.findViewById(R.id.cancelTxtView);
        final TextView okTxtView = (TextView) window.findViewById(R.id.okTxtView);
        yearWheelView = (WheelView) window.findViewById(R.id.yearWheelView);
        monthWheelView = (WheelView) window.findViewById(R.id.monthWheelView);
        dayWheelView = (WheelView) window.findViewById(R.id.dayWheelView);
        yearWheelView.setCyclic(true);
        monthWheelView.setCyclic(true);
        dayWheelView.setCyclic(true);
        yearWheelView.setTextSize(18);
        monthWheelView.setTextSize(18);
        dayWheelView.setTextSize(18);

        yearWheelView.setViewBackgroundColor(Color.parseColor("#50f5f6f7"));
        monthWheelView.setViewBackgroundColor(Color.parseColor("#50f5f6f7"));
        dayWheelView.setViewBackgroundColor(Color.parseColor("#50f5f6f7"));

        //年
        List<String> yearList = new ArrayList<>();
        int yearStart = 1880;
        int yearEnd = 2020;
        int yearDefault = 1990;
        int yearDefaultIndex = yearDefault - 1880;
        for(int i = yearStart; i<=yearEnd; i++){
            yearList.add(String.valueOf(i));
        }

        //月
        List<String> monthList = new ArrayList<>();
        for(int i = 1; i<=12; i++){
            monthList.add(String.valueOf(i));
        }

        //日
        List<String> dayList = new ArrayList<>();
        int maxDay = DateUtil.getMaxDays(yearDefault+"-01-01");
        for(int i=1; i<=maxDay; i++){
            dayList.add(String.valueOf(i));
        }

        yearWheelView.setAdapter(new WheelStringAdapter(yearList));
        yearWheelView.setCurrentItem(yearDefaultIndex);

        monthWheelView.setAdapter(new WheelStringAdapter(monthList));

        dayWheelView.setAdapter(new WheelStringAdapter(dayList));

        yearWheelView.addChangingListener(new WheelView.OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                changeDayAdapter();
            }
        });
        monthWheelView.addChangingListener(new WheelView.OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                changeDayAdapter();
            }
        });

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
                if(onSelectClickListener!=null){
                    String year = yearWheelView.getTextItem(yearWheelView.getCurrentItem());
                    String month = monthWheelView.getTextItem(monthWheelView.getCurrentItem());
                    String day = dayWheelView.getTextItem(dayWheelView.getCurrentItem());

                    onSelectClickListener.onOk(year, month, day, DateUtil.toPattern(year+"-"+month+"-"+day, DateUtil.DATE));
                }
                dismiss();
            }
        });
    }

    /**
     *
     */
    public void setDayVisibility(int visibility){
        dayWheelView.setVisibility(visibility);
    }

    /**
     * 设置年
     * @param year
     */
    public void setYear(String year){
        if(TextUtils.isEmpty(year)){
            return ;
        }
        for(int i=0;i<yearWheelView.getAdapter().getItemsCount();i++){
            String yearItem = yearWheelView.getAdapter().getItem(i);
            if(year.equalsIgnoreCase(yearItem)){
                yearWheelView.setCurrentItem(i);
                break;
            }
        }
    }

    /**
     *
     */
    public String getYear(){
        return yearWheelView.getTextItem(yearWheelView.getCurrentItem());
    }

    /**
     * 设置月
     * @param month
     */
    public void setMonth(String month){
        if(TextUtils.isEmpty(month)){
            return ;
        }
        for(int i=0;i<monthWheelView.getAdapter().getItemsCount();i++){
            String monthItem = monthWheelView.getAdapter().getItem(i);
            if(month.equalsIgnoreCase(monthItem)){
                monthWheelView.setCurrentItem(i);
                break;
            }
        }
    }

    /**
     *
     */
    public String getMonth(){
        return monthWheelView.getTextItem(monthWheelView.getCurrentItem());
    }


    /**
     * 设置日
     * @param day
     */
    public void setDay(String day){
        if(TextUtils.isEmpty(day)){
            return ;
        }
        for(int i=0;i<dayWheelView.getAdapter().getItemsCount();i++){
            String dayItem = dayWheelView.getAdapter().getItem(i);
            if(day.equalsIgnoreCase(dayItem)){
                dayWheelView.setCurrentItem(i);
                break;
            }
        }
    }

    /**
     *
     */
    public String getDay(){
        return dayWheelView.getTextItem(dayWheelView.getCurrentItem());
    }


    /**
     * 修改day
     */
    private void changeDayAdapter(){
        String year = yearWheelView.getTextItem(yearWheelView.getCurrentItem());
        String month = monthWheelView.getTextItem(monthWheelView.getCurrentItem());
        int maxDay = DateUtil.getMaxDays(year+"-"+month+"-01");
        if(maxDay!=0 && maxDay != dayWheelView.getAdapter().getItemsCount()){
            int currIndex = dayWheelView.getCurrentItem();
            List<String> dayList = new ArrayList<>();
            for(int i=1; i<= maxDay; i++){
                dayList.add(String.valueOf(i));
            }
            dayWheelView.setAdapter(new WheelStringAdapter(dayList));

            dayWheelView.setCurrentItem(currIndex < maxDay ? currIndex : maxDay-1);
        }
    }

    /**
     *
     */
    public void show(){
        dialog.show();
    }

    /**
     *
     */
    public void dismiss(){
        dialog.dismiss();
        if(onSelectClickListener!=null){
            onSelectClickListener.onCancel();
        }
    }

    public OnSelectClickListener getOnSelectClickListener() {
        return onSelectClickListener;
    }

    public void setOnSelectClickListener(OnSelectClickListener onSelectClickListener) {
        this.onSelectClickListener = onSelectClickListener;
    }

    public interface OnSelectClickListener{
        void onOk(String year, String month, String day, String format);
        void onCancel();
    }

}
