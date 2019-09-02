package com.beyond.popscience.module.point;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.widget.BasePopupWindow.BasePopupWindow;


/**
 * 从底部滑上来的popup
 */
public class SlideFromBottomPopup extends BasePopupWindow implements View.OnClickListener {

    private View popupView;
    private TextView tvSelectAddress;
    private Button btnConfirm;
    private EditText etName, etPhone, etAddress;


    public SlideFromBottomPopup(Activity context) {
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
        popupView = LayoutInflater.from(mContext).inflate(R.layout.activity_write_post_address, null);
        return popupView;
    }

    @Override
    public View getAnimaView() {
        return popupView.findViewById(R.id.popup_anima);
    }

    private void bindEvent() {
        if (popupView != null) {
            tvSelectAddress = (EditText) popupView.findViewById(R.id.tv_select_address);
            btnConfirm = (Button) popupView.findViewById(R.id.write_address_sure);
            etName = (EditText) popupView.findViewById(R.id.et_name);
            etPhone = (EditText) popupView.findViewById(R.id.et_phone);
            etAddress = (EditText) popupView.findViewById(R.id.et_address);
            tvSelectAddress.setOnClickListener(this);
            btnConfirm.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null)
            onItemClickListener.onItemClick(v, etName.getText().toString(), etPhone.getText().toString(),
                    tvSelectAddress.getText().toString(), etAddress.getText().toString());
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View v, String name, String phone, String dizi, String address);
    }
}
