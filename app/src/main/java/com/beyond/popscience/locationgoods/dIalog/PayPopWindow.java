package com.beyond.popscience.locationgoods.dIalog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.net.AccountRestUsage;
import com.beyond.popscience.locationgoods.SubmitOrderActivity;
import com.beyond.popscience.locationgoods.bean.ProductDetail;
import com.beyond.popscience.locationgoods.http.AddressRestUsage;
import com.beyond.popscience.locationgoods.shopcar.util.ToastUtil;
import com.beyond.popscience.module.home.entity.Address;
import com.beyond.popscience.utils.CreatLayoutUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yao.cui on 2017/6/10.
 */

public class PayPopWindow extends PopupWindow implements View.OnClickListener {

    private String orderSign;
    private Context mContext;
    private String price;
    @Request
    private AddressRestUsage addressRestUsage = new AddressRestUsage();
    private View mview;
    private TextView tvGoodName, tv_price, tv_submit;
    private CommonAdapter<ProductDetail.ProductBean.SkuListBean> adapter;
    private ImageView iv_back;
    private LinearLayout ll_zfb_pay, ll_wx_pay;
    private int payType = 1;

    public PayPopWindow(Context context, String orderSign, String price) {
        super(context);
        this.orderSign = orderSign;
        this.price = price;
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        View rootView = LayoutInflater.from(context).inflate(R.layout.pay_type_pop, null);
        tvGoodName = rootView.findViewById(R.id.tv_good_name);
        ll_zfb_pay = rootView.findViewById(R.id.ll_zfb_pay);
        ll_wx_pay = rootView.findViewById(R.id.ll_wx_pay);
        iv_back = rootView.findViewById(R.id.iv_back);

        tv_price = rootView.findViewById(R.id.tv_price);
        tv_price.setText(price + "元");
        tvGoodName.setText(orderSign);
        tv_submit = rootView.findViewById(R.id.tv_submit);
        ll_zfb_pay.setOnClickListener(this);
        ll_wx_pay.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
        iv_back.setOnClickListener(this);
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
            case R.id.tv_submit:
                linterset.OnClick(payType);
                dismiss();
                break;
            case R.id.ll_zfb_pay:
                payType = 1;
                ll_zfb_pay.setBackgroundResource(R.drawable.bg_gray_round_blue);
                ll_wx_pay.setBackgroundResource(R.drawable.bg_gray_round_w);
                break;
            case R.id.ll_wx_pay:
                payType = 2;
                ll_zfb_pay.setBackgroundResource(R.drawable.bg_gray_round_w);
                ll_wx_pay.setBackgroundResource(R.drawable.bg_gray_round_blue);
                break;
            case R.id.iv_back:
                dismiss();
                break;
        }
    }

    //定义一个确定的接口
    public interface submitPayClickLinster {
        void OnClick(int pay);
    }

    public submitPayClickLinster linterset;

    //对外提供的方法
    public void setSubmitPayClickLinterset(submitPayClickLinster linterset) {
        this.linterset = linterset;
    }

}
