package com.beyond.popscience.locationgoods.dIalog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
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
import com.beyond.popscience.locationgoods.shopcar.util.ToastUtil;
import com.beyond.popscience.locationgoods.view.TimeTools;
import com.beyond.popscience.module.home.entity.Address;
import com.beyond.popscience.utils.CreatLayoutUtils;
import com.bumptech.glide.Glide;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yao.cui on 2017/6/10.
 */

public class GoodAttrPopGroupWindow extends PopupWindow implements View.OnClickListener {

    private int type;
    private Context mContext;


    private IAddressChangeListener mListener;


    @Request
    private AccountRestUsage mAccountRestUsage;
    private View mview;
    private ImageView ivImg;
    private TextView tvTitle, tvPrice1, tvPrice2, tvkc, tvNum, submit, tv_fl111, tv_djs, cjxxdpt, hsdsme;
    private RecyclerView rlv;
    private ImageView ivJian, ivJia;
    private int buyNum = 1;
    public ProductDetail data;
    private CommonAdapter<ProductDetail.ProductBean.SkuListBean> adapter;
    private int curPosition;
    private LinearLayout llHead;


    private Timer mTimer;
    private TimerTask mTimerTask;

    private long MAX_TIME = 12000;
    private long curTime = 0;
    private List<ProductDetail.ProductBean.SkuListBean> skuLists;
    View rootView;

    /**
     * 初始化Timer
     */
    public void initTimer() {
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (curTime == 0) {
                    curTime = MAX_TIME;
                } else {
                    curTime -= 1000;
                }
                Message message = new Message();
                message.what = WHAT;
                message.obj = curTime;
                mHandler.sendMessage(message);
            }
        };
        mTimer = new Timer();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT:
                    long sRecLen = (long) msg.obj;
                    if (null == tv_djs) return;
                    tv_djs.setText("结束拼团倒计时：" + TimeTools.getCountTimeByLong(sRecLen));
                    if (sRecLen <= 0) {
                        mTimer.cancel();
                        curTime = 0;
                    }
                    break;
            }
        }
    };
    private static final int WHAT = 101;

    public GoodAttrPopGroupWindow(Context context, ProductDetail data, int type) {
        super(context);
        this.data = data;
        this.type = type;
        for (ProductDetail.ProductBean.SkuListBean bean : data.getProduct().getSkuList()) {
            bean.setFlag(false);
        }
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;

        if (type == 0) {
            rootView = LayoutInflater.from(context).inflate(R.layout.good_attr, null);
            ivImg = rootView.findViewById(R.id.iv_img);
            tvTitle = rootView.findViewById(R.id.tv_title);
            tvPrice1 = rootView.findViewById(R.id.tv_price1);
            tvPrice2 = rootView.findViewById(R.id.tv_price2);
            tvkc = rootView.findViewById(R.id.tv_kc);
            tv_fl111 = rootView.findViewById(R.id.tv_fl111);
            rlv = rootView.findViewById(R.id.rlv);

            ivJian = rootView.findViewById(R.id.iv_jian);
            ivJia = rootView.findViewById(R.id.iv_jia);
            tvNum = rootView.findViewById(R.id.tv_num);
            submit = rootView.findViewById(R.id.submit);
        }
        if (type == 1) {
            rootView = LayoutInflater.from(context).inflate(R.layout.good_attr_two, null);
            ivImg = rootView.findViewById(R.id.iv_img);
            llHead = rootView.findViewById(R.id.ll_head);
            tvTitle = rootView.findViewById(R.id.tv_title);
            tv_djs = rootView.findViewById(R.id.tv_djs);
            cjxxdpt = rootView.findViewById(R.id.cjxxdpt);
            hsdsme = rootView.findViewById(R.id.hsdsme);
            tvPrice1 = rootView.findViewById(R.id.tv_price1);
            tvPrice2 = rootView.findViewById(R.id.tv_price2);
            tvkc = rootView.findViewById(R.id.tv_kc);
            tv_fl111 = rootView.findViewById(R.id.tv_fl111);
            rlv = rootView.findViewById(R.id.rlv);

            ivJian = rootView.findViewById(R.id.iv_jian);
            ivJia = rootView.findViewById(R.id.iv_jia);
            tvNum = rootView.findViewById(R.id.tv_num);
            submit = rootView.findViewById(R.id.submit);
        }
        ivJian.setOnClickListener(this);
        ivJia.setOnClickListener(this);
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
        //初始化数据源
        CreatLayoutUtils.creatLinearLayout(context, rlv);
        if (type == 0) {
            skuLists = data.getProduct().getSkuList();
        }
        if (type == 1) {
            skuLists = new ArrayList<>();
            for (ProductDetail.ProductBean.SkuListBean skuListBean : data.getProduct().getSkuList()) {
                if (null != skuListBean.getGroupPrice()) {
                    skuLists.add(skuListBean);
                }
            }
        }
        adapter = new CommonAdapter<ProductDetail.ProductBean.SkuListBean>(context, R.layout.item_good_attr_pop, skuLists) {

            @Override
            protected void convert(ViewHolder holder, ProductDetail.ProductBean.SkuListBean skuListBean, final int position) {
                holder.setText(R.id.tv_content, skuListBean.getSp1()).setOnClickListener(R.id.tv_content, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        curPosition = position;
                        //获取当前flag
                        boolean curFlg = skuLists.get(position).isFlag();
                        if (curFlg) {
                            skuLists.get(position).setFlag(false);
                        } else {
                            skuLists.get(position).setFlag(true);
                        }
                        //其他数据全部设置未选择
                        for (ProductDetail.ProductBean.SkuListBean bean : skuLists) {
                            if (bean.getId() != skuLists.get(position).getId()) {
                                bean.setFlag(false);
                            }
                        }
                        tvkc.setText("库存:" + skuLists.get(position).getLockStock());
                        if (type == 0) {
                            tvPrice1.setText("￥" + skuLists.get(position).getPrice() + "");
                        }
                        if (type == 1) {
                            tvPrice1.setText("￥" + skuLists.get(position).getGroupPrice() + "");
                        }
                        tvPrice2.setText("拼团价：￥" + skuLists.get(position).getGroupPrice() + "");
                        tv_fl111.setText("已选属性：" + skuLists.get(position).getSp1());
                        adapter.notifyDataSetChanged();
                    }
                });
                if (skuLists.get(position).isFlag()) {
                    //选中的状态
                    holder.getView(R.id.tv_content).setBackgroundResource(R.drawable.bg_blue_round_concer_3);
                    holder.setTextColor(R.id.tv_content, mContext.getResources().getColor(R.color.white));
                } else {
                    holder.getView(R.id.tv_content).setBackgroundResource(R.drawable.bg_blue_round_concer_4);
                    holder.setTextColor(R.id.tv_content, mContext.getResources().getColor(R.color.f6f6f6f));
                }
            }
        };
        rlv.setAdapter(adapter);
        //默认第一个
        Glide.with(mContext).load(data.getProduct().getPic()).into(ivImg);

        if (type == 1) {
            if (null != data.getPt()) {
                llHead.setVisibility(View.VISIBLE);
                hsdsme.setText("已有" + data.getPt().getShopPtNumber() + "人加入拼团");
                cjxxdpt.setText(data.getPt().getNickName() + "的拼团");
                initTimer();
                //得到创建时间
                String createTime = data.getPt().getCreateTime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    long createTimesss = sdf.parse(createTime).getTime();
                    long currentTime = System.currentTimeMillis();
                    MAX_TIME = createTimesss + (1000 * 60 * 60 * 24) - currentTime;
                    mTimer.schedule(mTimerTask, 0, 1000);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                llHead.setVisibility(View.VISIBLE);
                cjxxdpt.setVisibility(View.GONE);
                tv_djs.setVisibility(View.GONE);
                hsdsme.setText("恭喜你将成为拼团发起人");
                hsdsme.setTextColor(mContext.getResources().getColor(R.color.blue));
                hsdsme.setTextSize(16);
                hsdsme.setPadding(10, 10, 10, 40);
            }
        }
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
            case R.id.iv_jia:
                //现货区数量 默认是1
                buyNum = Integer.parseInt(tvNum.getText().toString());
                //这个数字不能大于库存
                //看看选中的是哪个item
                if (data.getProduct().getSkuList().get(curPosition).getStock() > buyNum) {
                    buyNum++;
                    tvNum.setText(buyNum + "");
                } else {
                    ToastUtil.makeText(mContext, "库存没有这么多了！");
                }

                break;
            case R.id.submit:
                boolean b = false;
                for (ProductDetail.ProductBean.SkuListBean bean : skuLists) {
                    if (bean.isFlag()) {
                        b = true;
                    }
                }
                if (b) {
                    Intent intent1 = new Intent(mContext, SubmitOrderActivity.class);
                    intent1.putExtra("data", (Serializable) data.getProduct());
                    intent1.putExtra("position", curPosition);
                    intent1.putExtra("num", tvNum.getText().toString());
                    intent1.putExtra("type", type + "");
                    mContext.startActivity(intent1);
                    dismiss();
                } else {
                    ToastUtil.makeText(mContext, "请选择商品规格！");
                }
                break;
            case R.id.iv_jian:
                buyNum = Integer.parseInt(tvNum.getText().toString());
                //这个数量不能小于1
                if (buyNum <= 1) {
                    tvNum.setText("1");
                    ToastUtil.makeText(mContext, "购买数量最少1个！");
                } else {
                    buyNum--;
                    tvNum.setText(buyNum + "");
                }
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
