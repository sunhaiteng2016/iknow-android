package com.beyond.popscience.frame.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.library.util.DensityUtil;
import com.beyond.popscience.R;

/**
 * 选择性别
 * Created by linjinfa on 2017/6/12.
 * email 331710168@qq.com
 */
public class SexDialog {

    private Dialog dialog;
    private OnSexClickListener onSexClickListener;
    private CheckBox manCheckBox;
    private CheckBox womanCheckBox;

    public SexDialog(Context context) {
        init(context);
    }

    /**
     * 初始化
     * @param context
     */
    private void init(Context context){
        dialog = new Dialog(context, R.style.DialogPushUpInAnimStyle);
        dialog.setContentView(R.layout.dialog_sex);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = DensityUtil.getScreenWidth(context);
        window.setAttributes(layoutParams);

        View bgView = window.findViewById(R.id.bgView);
        RelativeLayout manReLay = (RelativeLayout) window.findViewById(R.id.manReLay);
        RelativeLayout womanReLay = (RelativeLayout) window.findViewById(R.id.womanReLay);
        manCheckBox = (CheckBox) window.findViewById(R.id.manCheckBox);
        womanCheckBox = (CheckBox) window.findViewById(R.id.womanCheckBox);
        final TextView okTxtView = (TextView) window.findViewById(R.id.okTxtView);

        bgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        manReLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manCheckBox.toggle();
                womanCheckBox.setChecked(!manCheckBox.isChecked());
            }
        });
        womanReLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                womanCheckBox.toggle();
                manCheckBox.setChecked(!womanCheckBox.isChecked());
            }
        });
        okTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onSexClickListener!=null){
                    onSexClickListener.onOk(manCheckBox.isChecked());
                }
                dismiss();
            }
        });
    }

    /**
     * 设置默认选中的 sex
     * @param sex
     */
    public void setSex(String sex){
        if(!TextUtils.isEmpty(sex)){
            if("男".equals(sex)){
                manCheckBox.setChecked(true);
                womanCheckBox.setChecked(false);
            }else{
                manCheckBox.setChecked(false);
                womanCheckBox.setChecked(true);
            }
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
    }

    public OnSexClickListener getOnSexClickListener() {
        return onSexClickListener;
    }

    public void setOnSexClickListener(OnSexClickListener onSexClickListener) {
        this.onSexClickListener = onSexClickListener;
    }

    public interface OnSexClickListener{
        void onOk(boolean isMan);
    }

}
