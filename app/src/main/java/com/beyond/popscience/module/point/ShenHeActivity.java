package com.beyond.popscience.module.point;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.ToastUtil;
import com.beyond.library.view.pullrefresh.PullToRefreshBase;
import com.beyond.library.view.pullrefresh.PullToRefreshListView;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.PointRestUsage;
import com.beyond.popscience.frame.pojo.PointDetailResult;
import com.beyond.popscience.frame.util.D;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.module.point.adapter.DetailAdapter;
import com.google.gson.internal.LinkedTreeMap;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ShenHeActivity extends BaseActivity {


    private static final int TASK_DETAIL_LIST_LIST = 8123456;
    private static final int TASK_WITHDRAW_OK = 800011;
    @BindView(R.id.shjl_lv)
    ListView shjl_lv;
    @BindView(R.id.tvback)
    ImageView tvback;

    @Request
    PointRestUsage restUsage;
    private TextView tv_right;
    private List<LinkedTreeMap> obj;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_shen_he;
    }

    @Override
    public void initUI() {
        super.initUI();
        tvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getPointDetails();
    }

    private void withdrawOk(String id) {
        double mid = Double.parseDouble(id);
        int mmid = (int) mid;
        restUsage.withOk(TASK_WITHDRAW_OK, UserInfoUtil.getInstance().getUserInfo().getUserId(), mmid + "");
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case TASK_DETAIL_LIST_LIST:
                if (msg.getIsSuccess()) {
                    obj = (List<LinkedTreeMap>) msg.getObj();
                    if (obj == null) {
                        // Toast.makeText(ShenHeActivity.this,"没有审核记录,请重试！",Toast.LENGTH_LONG).show();
                        return;
                    }
                    shjl_lv.setAdapter(new MyAdapter(obj, ShenHeActivity.this));
                }
                break;
            case TASK_WITHDRAW_OK:
                String data1 = (String) msg.getObj();
                if ("提现成功".equals(data1)) {
                    startActivity(new Intent(this, DepositMoneySuccessActivity.class).putExtra("flag", data1));
                    finish();
                } else {
                    ToastUtil.showCenter(this, data1);
                }
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void getPointDetails() {
        restUsage.shjlList(TASK_DETAIL_LIST_LIST, UserInfoUtil.getInstance().getUserInfo().getUserId());
    }

    class MyAdapter extends BaseAdapter {
        public List<LinkedTreeMap> mobj;
        public Context mcontent;

        public MyAdapter(List<LinkedTreeMap> mobj, Context mcontent) {
            this.mobj = mobj;
            this.mcontent = mcontent;
        }

        @Override
        public int getCount() {
            return mobj.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            viewholder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(ShenHeActivity.this).inflate(R.layout.item_shjl, null);
                holder = new viewholder();
                holder.tv1 = (TextView) convertView.findViewById(R.id.name_shjl);
                holder.tv2 = (TextView) convertView.findViewById(R.id.mobile_shjl);
                holder.tv3 = (TextView) convertView.findViewById(R.id.createtime_shjl);
                holder.tv4 = (TextView) convertView.findViewById(R.id.state_shjl);
                holder.tv5 = (TextView) convertView.findViewById(R.id.createtime_yuanyin);
                convertView.setTag(holder);
            } else {
                holder = (viewholder) convertView.getTag();
            }
            LinkedTreeMap map = (LinkedTreeMap) mobj.get(position);

            holder.tv1.setText("支付宝账号：" + map.get("account") + "");
            holder.tv2.setText(map.get("mobile") + "");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String hms = formatter.format(map.get("createtime"));
            holder.tv3.setText(hms);
            Object orderout = map.get("orderout");
            Double status = (Double) map.get("status");
            if (1.0 == status) {
                holder.tv5.setText("提现");
                holder.tv5.setBackgroundResource(R.drawable.btn_bg_blue);
                holder.tv5.setTextColor(mcontent.getResources().getColor(R.color.white));
                holder.tv5.setOnClickListener(new MyLentener(map.get("id") + ""));
            }
            if (0.0 == status) {
                holder.tv5.setText("审核中");
            }
            if (2.0 == status) {
                holder.tv5.setText("提现成功");
            }
            if (3.0 == status) {
                holder.tv5.setText(orderout + "");
            }
            return convertView;
        }

        class MyLentener implements View.OnClickListener {
            public String id;

            public MyLentener(String id) {
                this.id = id;
            }

            @Override
            public void onClick(View v) {
                // withdrawOk(id);
            }
        }

        class viewholder {
            TextView tv1;
            TextView tv2;
            TextView tv3;
            TextView tv4;
            TextView tv5;
        }
    }
}
