package com.beyond.popscience.locationgoods.dIalog;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.beyond.popscience.R;


/**
 * Created by yao.cui on 2017/6/10.
 */

public class WlPopWindow extends PopupWindow implements View.OnClickListener {

    private Context mContext;


    private IAddressChangeListener mListener;


    private View mview;
    private EditText edwlgs, edwldh;
    private TextView submit;

    public WlPopWindow(Context context) {
        super(context);
        init(context);

    }


    private void init(Context context) {
        this.mContext = context;
        View rootView = LayoutInflater.from(context).inflate(R.layout.wl_pop, null);
        edwlgs = rootView.findViewById(R.id.ed_wl_gs);
        edwldh = rootView.findViewById(R.id.ed_wl_dh);
        submit = rootView.findViewById(R.id.submit);
        submit.setOnClickListener(this);

        this.setContentView(rootView);
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setBackgroundDrawable(new ColorDrawable(0));
        setAnimationStyle(R.style.popwindow_anim_style);

        setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
                lp.alpha = 1f;
                ((Activity) mContext).getWindow().setAttributes(lp);
            }
        });
    }

    public void show(View parent) {
        mview = (View) parent;
        this.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = 0.7f;
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.submit:
                String wlgs = edwlgs.getText().toString().trim();
                String wldh = edwldh.getText().toString().trim();
                if (TextUtils.isEmpty(wlgs)){
                    com.beyond.library.util.ToastUtil.showCenter(mContext,"请输入物流公司！");
                    return;
                }
                if (TextUtils.isEmpty(wldh)){
                    com.beyond.library.util.ToastUtil.showCenter(mContext,"请输入物流单号！");
                    return;
                }
                mListener.onClick(wlgs, wldh);
                dismiss();
                break;
        }
    }

    public void setAddressChangeListener(IAddressChangeListener listener) {
        this.mListener = listener;
    }


    public interface IAddressChangeListener {
        void onClick(String wlgs, String whdh);
    }

}
