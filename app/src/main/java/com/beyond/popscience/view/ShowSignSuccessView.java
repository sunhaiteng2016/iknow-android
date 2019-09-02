package com.beyond.popscience.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.widget.BasePopupWindow.BasePopupWindow;

public class ShowSignSuccessView  extends BasePopupWindow {
    private View popupView;
    private TextView textView;
    private String signNo;

    public ShowSignSuccessView(Activity context,String signNo) {
        super(context);
        this.signNo = signNo;
        bindEvent();
    }

    @Override
    public Animation getShowAnimation() {
        return getTranslateAnimation(250 * 4, 0, 3500);
    }

    @Override
    protected View getClickToDismissView() {
        return popupView.findViewById(R.id.click_to_dismiss);
    }

    @Override
    public View getPopupView() {
        popupView = LayoutInflater.from(mContext).inflate(R.layout.activity_sign_in_layout, null);
        return popupView;
    }

    @Override
    public View getAnimaView() {
        return popupView.findViewById(R.id.popup_anima);
    }

    private void bindEvent() {
        if (popupView != null) {
            textView = (TextView) popupView.findViewById(R.id.sign_num);
            textView.setText(signNo);
        }
    }
//    public void addTextViewInterface(TextViewInterfce textViewInterfce){
//        textViewInterfce.setTextView(textView);
//    }
//    interface TextViewInterfce{
//        void setTextView(TextView textView);
//    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }
}
