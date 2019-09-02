package com.beyond.popscience.frame.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.beyond.popscience.R;

import java.util.List;

/**
 * Created by danxiang.feng on 2017/10/12.
 */

public class PopupMenu extends PopupWindow {

    private Activity activity;
    private View popView;
    private LinearLayout popupLayout;

    private OnItemClickListener onItemClickListener;

    public PopupMenu(Activity activity) {
        super(activity);
        this.activity = activity;
        init();
    }

    private void init() {
        if (popView == null || popupLayout == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            popView = inflater.inflate(R.layout.layout_menu, null); // 加载菜单布局文件
            this.setContentView(popView); // 把布局文件添加到popupwindow中
            this.setWidth(dip2px(110)); // 设置菜单的宽度（需要和菜单于右边距的距离搭配，可以自己调到合适的位置）
            this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            this.setFocusable(true); // 获取焦点
            this.setTouchable(true); // 设置PopupWindow可触摸
            this.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
            ColorDrawable dw = new ColorDrawable(0x00000000);
            this.setBackgroundDrawable(dw);

            popupLayout = (LinearLayout) popView.findViewById(R.id.popupLayout);
        }
    }

    public void setDataList(List<String> list, int selectIndex) {
        init();
        popupLayout.removeAllViews();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String text = list.get(i);
                TextView textView = new TextView(activity);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dip2px(35));
                textView.setLayoutParams(layoutParams);
                textView.setTextSize(14);
                textView.setGravity(Gravity.CENTER);
                textView.setText(text);
                if (selectIndex == i) {
                    textView.setTextColor(activity.getResources().getColor(R.color.blue2));
                } else {
                    textView.setTextColor(activity.getResources().getColor(R.color.grey17));
                }
                textView.setTag(i);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = (int) view.getTag();
                        if (onItemClickListener != null) {
                            onItemClickListener.onClick(position);
                        }
                        dismiss();
                    }
                });
                popupLayout.addView(textView);
            }
        }
    }

    /**
     * 设置显示的位置
     */
    public void showLocation(View view, int xoff, int yoff) {
        showAsDropDown(view, xoff, yoff);
    }

    // dip转换为px
    public int dip2px(float dipValue) {
        final float scale = activity.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    // 点击监听接口
    public interface OnItemClickListener {
        void onClick(int position);
    }

    // 设置监听
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


}
