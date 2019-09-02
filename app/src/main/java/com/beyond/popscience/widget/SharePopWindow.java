package com.beyond.popscience.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.beyond.library.network.Request;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.net.AccountRestUsage;
import com.beyond.popscience.module.home.adapter.MyWheelAdapter;
import com.beyond.popscience.module.home.entity.Address;
import com.beyond.popscience.module.home.entity.CunAddress;
import com.beyond.popscience.module.home.entity.XiangAddresss;
import com.beyond.popscience.widget.wheelview.WheelView;
import com.google.gson.Gson;
import com.okhttp.CallBackUtil;
import com.okhttp.OkhttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import okhttp3.Call;

/**
 * Created by yao.cui on 2017/6/10.
 */

public class SharePopWindow extends PopupWindow implements View.OnClickListener {

    private Context mContext;

    private ShareChangeListener mListener;
    private List<Address> mAddress;


    @Request
    private AccountRestUsage mAccountRestUsage;
    private View mview;
    private LinearLayout ll1,ll2,ll3;

    public SharePopWindow(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;

        View rootView = LayoutInflater.from(context).inflate(R.layout.share_pop_layout, null);

        ll1=rootView.findViewById(R.id.ll1);
        ll2=rootView.findViewById(R.id.ll2);
        ll3=rootView.findViewById(R.id.ll3);
        ll1.setOnClickListener(this);
        ll2.setOnClickListener(this);
        ll3.setOnClickListener(this);
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
          mview= parent;
        this.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = 0.7f;
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll1:
                if (mListener != null) {
                    mListener.onAddressChange(1);
                }
                dismiss();
                break;
            case R.id.ll2:
                if (mListener != null) {
                    mListener.onAddressChange(2);
                }
                dismiss();
                break;
            case R.id.ll3:
                if (mListener != null) {
                    mListener.onAddressChange(3);
                }
                dismiss();
                break;
        }
    }


    public interface ShareChangeListener {

        void onAddressChange(int flag);

    }
    public void  setOnClickListener(ShareChangeListener listener){
        this.mListener=listener;
    }
}
