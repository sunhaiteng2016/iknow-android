package com.beyond.popscience.locationgoods.dIalog;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
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

public class RefusePopWindow extends PopupWindow implements View.OnClickListener {

    private Context mContext;


    private IAddressChangeListener mListener;


    @Request
    private AccountRestUsage mAccountRestUsage;
    private View mview;
    private ImageView ivback;
    private RadioButton rbReason1, rbReason2, rbReason3, rbReason4;
    private TextView tvSubmit;


    public RefusePopWindow(Context context) {
        super(context);
        init(context);
    }


    private void init(Context context) {
        this.mContext = context;

        View rootView = LayoutInflater.from(context).inflate(R.layout.refuse_pop, null);
        ivback = rootView.findViewById(R.id.iv_back);
        rbReason1 = rootView.findViewById(R.id.rb_reason1);
        rbReason2 = rootView.findViewById(R.id.rb_reason2);
        rbReason3 = rootView.findViewById(R.id.rb_reason3);
        rbReason4 = rootView.findViewById(R.id.rb_reason4);
        tvSubmit = rootView.findViewById(R.id.tv_submit);


        //ivback.setOnClickListener(this);
        rbReason1.setOnClickListener(this);
        rbReason2.setOnClickListener(this);
        rbReason3.setOnClickListener(this);
        rbReason4.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);

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
            case R.id.iv_back:
                dismiss();
                break;
            case R.id.tv_submit:
                //mview.setText(xiangname+ cunname);
                dismiss();
                break;
            case R.id.rb_reason1:
                break;
            case R.id.rb_reason2:
                break;
            case R.id.rb_reason3:
                break;
            case R.id.rb_reason4:
                break;
        }
    }


    public void setAddressChangeListener(IAddressChangeListener listener) {
        this.mListener = listener;
    }


    public interface IAddressChangeListener {

        void onAddressChange(Address city, Address zone);

    }

}
