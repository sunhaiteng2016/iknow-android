package com.beyond.popscience.module.point;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.widget.BasePopupWindow.BasePopupWindow;


/**
 * 从底部滑上来的popup
 */
public class ShowInputPopup extends BasePopupWindow implements View.OnClickListener {

    private View popupView;
    private TextView tvWechat, tvFriendCircle, tvQQ, tvSina, tvQzone;
    private ImageView ivPic;


    public ShowInputPopup(Activity context) {
        super(context);
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
        popupView = LayoutInflater.from(mContext).inflate(R.layout.activity_show_input_layout, null);
        return popupView;
    }

    @Override
    public View getAnimaView() {
        return popupView.findViewById(R.id.popup_anima);
    }

    private void bindEvent() {
        if (popupView != null) {
            tvWechat = (TextView) popupView.findViewById(R.id.show_input_wechat);
            tvFriendCircle = (TextView) popupView.findViewById(R.id.show_input_wechat_friend);
            tvQQ = (TextView) popupView.findViewById(R.id.show_input_qq);
            tvSina = (TextView) popupView.findViewById(R.id.show_input_sina);
            tvQzone = (TextView) popupView.findViewById(R.id.show_input_qzone);
            ivPic = (ImageView) popupView.findViewById(R.id.iv_pic);
            ivPic.setOnClickListener(this);
            tvWechat.setOnClickListener(this);
            tvFriendCircle.setOnClickListener(this);
            tvQQ.setOnClickListener(this);
            tvSina.setOnClickListener(this);
            tvQzone.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null)
            onItemClickListener.onItemClick(v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View v);
    }
}
