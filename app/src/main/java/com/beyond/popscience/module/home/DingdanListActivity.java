package com.beyond.popscience.module.home;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.pojo.UserInfo;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.module.home.entity.DingDan;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.okhttp.CallBackUtil;
import com.okhttp.OkhttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Call;


public class DingdanListActivity extends BaseActivity {
    public ListView dingdan_lv;
    private List<DingDan.DataBean.ListBean> mList;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private ImageView iv_back_dingdan;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_dingdan_list;
    }

    @Override
    public void initUI() {
        super.initUI();
        dingdan_lv = (ListView) findViewById(R.id.dingdan_lv);
        iv_back_dingdan = (ImageView) findViewById(R.id.iv_back_dingdan);
        iv_back_dingdan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        try {
            getdingdanlist();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getdingdanlist() throws JSONException {

       /* HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("buyUserId","11112");*/
        JSONObject json = new JSONObject();
        //json.put("buyUserId", UserInfoUtil.getInstance().getUserInfo().getUserId());
        json.put("buyUserId", "11112");
        OkhttpUtil.okHttpPostJson(BeyondApplication.BaseUrl + "/order/orderList", json.toString(), new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Log.e("", "");
            }

            @Override
            public void onResponse(String response) {
                Log.e("", "");
                Gson gson = new Gson();
                DingDan dingdan = gson.fromJson(response, DingDan.class);
                mList = dingdan.getData().getList();
                dingdan_lv.setAdapter(new MyAdapter(mList,DingdanListActivity.this));
                dingdan_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent  intent  = new Intent(DingdanListActivity.this,DingDanDeltesActivity.class);

                    }
                });
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("DingdanList Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    class MyAdapter extends BaseAdapter {
        public List<DingDan.DataBean.ListBean> mList;
        public Context context;

        public MyAdapter(List<DingDan.DataBean.ListBean> mList, Context context) {
            this.mList = mList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            viewHolder holder;
            if (view == null) {
                holder = new viewHolder();
                view = LayoutInflater.from(context).inflate(R.layout.item_dingdan, null);
                holder.xingming = (TextView) view.findViewById(R.id.tvxingming);
                holder.dizhi = (TextView) view.findViewById(R.id.tvdizhi);
                holder.diangdanhao = (TextView) view.findViewById(R.id.tvdingdanhao);
                holder.dingdanzhuangtai = (TextView) view.findViewById(R.id.tvdingdanzhuangtai);
                view.setTag(holder);
            } else {
                holder = (viewHolder) view.getTag();
            }
            holder.xingming.setText(mList.get(i).getOrderUser());
            holder.dizhi.setText(mList.get(i).getOrderAddress());
            holder.diangdanhao.setText("订单号:"+mList.get(i).getOrderId()+"");
            int type = mList.get(i).getOrderType();
            switch (type) {
                case 0:
                    holder.dingdanzhuangtai.setText("待付款");
                    break;
                case 1:
                    holder.dingdanzhuangtai.setText("待发货");
                    break;
                case 2:
                    holder.dingdanzhuangtai.setText("已发货");
                    break;
                case 3:
                    holder.dingdanzhuangtai.setText("交易成功");
                    break;
                case 4:
                    holder.dingdanzhuangtai.setText("交易关闭");
                    break;
                case 5:
                    holder.dingdanzhuangtai.setText("已评价");
                    break;
                case 6:
                    holder.dingdanzhuangtai.setText("支付中");
                    break;
                case 7:
                    holder.dingdanzhuangtai.setText("充值");
                    break;
                case 8:
                    holder.dingdanzhuangtai.setText("订单");
                    break;
            }
            return view;
        }

        class viewHolder {
            TextView xingming, dizhi, diangdanhao, dingdanzhuangtai;
        }
    }
}
