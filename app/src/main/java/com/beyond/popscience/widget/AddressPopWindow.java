package com.beyond.popscience.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
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
import com.beyond.popscience.frame.net.PointRestUsage;
import com.beyond.popscience.module.home.adapter.MyWheelAdapter;
import com.beyond.popscience.module.home.entity.Address;
import com.beyond.popscience.module.home.entity.CunAddress;
import com.beyond.popscience.module.home.entity.DingDan;
import com.beyond.popscience.module.home.entity.XiangAddresss;
import com.beyond.popscience.widget.wheelview.WheelView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okhttp.CallBackUtil;
import com.okhttp.OkhttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by yao.cui on 2017/6/10.
 */

public class AddressPopWindow extends PopupWindow implements View.OnClickListener {

    private Context mContext;

    protected WheelView cityWheelView;
    protected WheelView zoneWheelView;
    protected TextView mTvCancel;
    protected TextView mTvComplete;

    private IAddressChangeListener mListener;
    private List<Address> mAddress;

    private Address mCity;
    private Address mZone;
    public String city;

    public XiangAddresss xiang;
    public   CunAddress cun;

    public ListView lv1, lv2;

    @Request
    private AccountRestUsage mAccountRestUsage;
    private String xiangname;
    public static  String cunname;
    public TextView mview;
    public static int cunid;

    public AddressPopWindow(Context context, String str) {
        super(context);
        this.city = str;
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;

        View rootView = LayoutInflater.from(context).inflate(R.layout.address_pop_layout, null);
        cityWheelView = (WheelView) rootView.findViewById(R.id.wheelview1);
        zoneWheelView = (WheelView) rootView.findViewById(R.id.wheelview2);

        lv1 = (ListView) rootView.findViewById(R.id.lv1);
        lv2 = (ListView) rootView.findViewById(R.id.lv2);

        mTvCancel = (TextView) rootView.findViewById(R.id.tvCancal);
        mTvComplete = (TextView) rootView.findViewById(R.id.tvComplete);


        cityWheelView.setCyclic(true);
        zoneWheelView.setCyclic(true);

        mTvComplete.setOnClickListener(this);
        mTvCancel.setOnClickListener(this);

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

            initAddress();
    }

    private void initAddress() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("address", city);
            OkhttpUtil.okHttpPostJson(BeyondApplication.BaseUrl + "/address/getTown", obj.toString(), new CallBackUtil.CallBackString() {
                @Override
                public void onFailure(Call call, Exception e) {
                    Log.e("", "");
                }

                @Override
                public void onResponse(String response) {
                    Log.e("", "");
                    Gson gsons = new Gson();
                    xiang = gsons.fromJson(response, XiangAddresss.class);
                    //设置listview
                    if (xiang.getData()!=null&&xiang.getData().size()>0){
                        final MyAdapter lv1Adaper = new MyAdapter(xiang.getData(), mContext);
                        lv1.setAdapter(lv1Adaper);
                        xiangname=xiang.getData().get(0).getName();
                        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                xiangname=xiang.getData().get(i).getName();
                                xiang.getData().get(i).setFlag(1);
                                for (int j =0;j<xiang.getData().size();j++){
                                    if (i!=j){
                                        xiang.getData().get(j).setFlag(0);
                                    }
                                }
                                //同时要把 款选中的
                                lv1Adaper.notifyDataSetChanged();
                                JSONObject objs = new JSONObject();
                                try {
                                    objs.put("address", xiang.getData().get(i).getId());
                                    OkhttpUtil.okHttpPostJson(BeyondApplication.BaseUrl + "/address/getCountry", objs.toString(), new CallBackUtil.CallBackString() {
                                        @Override
                                        public void onFailure(Call call, Exception e) {
                                            Log.e("", "");
                                        }

                                        @Override
                                        public void onResponse(String response) {
                                            Log.e("", "");
                                            Gson gson = new Gson();
                                            cun = gson.fromJson(response, CunAddress.class);
                                            if (cun.getData().size()>0&&null!=cun.getData()){
                                                final MyAdapter2 lv2Adapter = new MyAdapter2(cun.getData(), mContext);
                                                lv2.setAdapter(lv2Adapter);
                                                cunname=cun.getData().get(0).getName();
                                                lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                        cunname=cun.getData().get(i).getName();
                                                        cunid=cun.getData().get(i).getId();
                                                        cun.getData().get(i).setFlag(1);
                                                        for (int j =0;j<cun.getData().size();j++){
                                                            if (i!=j){
                                                                cun.getData().get(j).setFlag(0);
                                                            }
                                                        }
                                                        lv2Adapter.notifyDataSetChanged();
                                                    }
                                                });
                                            }else{
                                                Toast.makeText(mContext,"没有具体的街道（村）",Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        return;
                    }
                    Toast.makeText(mContext,"没有相应的乡镇，请及时联系平台添加！",Toast.LENGTH_SHORT).show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }


//        mAddress = new Gson().fromJson(getJson(mContext,"address.json"),new TypeToken<List<Address>>(){}.getType());
     /*   if (0 != BeyondApplication.getInstance().getCacheAddressList().size()) {
            mAddress = BeyondApplication.getInstance().getCacheAddressList();
        } else {
            mAddress = new Gson().fromJson(getJson(mContext, "address.json"), new TypeToken<List<Address>>() {
            }.getType());
        }
        if (mAddress == null || mAddress.isEmpty()) {
            return;
        }
        MyWheelAdapter mCityAdapter = new MyWheelAdapter(mAddress);
        MyWheelAdapter mZoneAdapter = new MyWheelAdapter(mAddress.get(0).getChild());

        cityWheelView.setTextSize(15);
        cityWheelView.setAdapter(mCityAdapter);

        zoneWheelView.setTextSize(15);
        zoneWheelView.setAdapter(mZoneAdapter);

        cityWheelView.addChangingListener(new CityChangeListener());*/

    }

    public void show(View parent) {
          mview= (TextView) parent;
        this.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = 0.7f;
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tvCancal:
                dismiss();
                break;
            case R.id.tvComplete:
               /* MyWheelAdapter cityAdapter = (MyWheelAdapter) cityWheelView.getAdapter();
                MyWheelAdapter zoneAdapter = (MyWheelAdapter) zoneWheelView.getAdapter();

                mCity = cityAdapter.getItemObject(cityWheelView.getCurrentItem());
                mZone = zoneAdapter.getItemObject(zoneWheelView.getCurrentItem());
                if (mListener != null) {
                    mListener.onAddressChange(mCity, mZone);
                }*/
                mview.setText(xiangname+ cunname);
                dismiss();
                break;
        }
    }

    /**
     * 从asset路径下读取对应文件转String输出
     *
     * @param mContext
     * @return
     */
    public static String getJson(Context mContext, String fileName) {
        StringBuilder sb = new StringBuilder();
        AssetManager am = mContext.getAssets();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    am.open(fileName)));
            String next = "";
            while (null != (next = br.readLine())) {
                sb.append(next);
            }
        } catch (IOException e) {
            e.printStackTrace();
            sb.delete(0, sb.length());
        } catch (OutOfMemoryError error) {

        }
        return sb.toString().trim();
    }

    public void setAddressChangeListener(IAddressChangeListener listener) {
        this.mListener = listener;
    }

   /* *//**
     * 设置默认滚轮选中地址
     *
     * @param
     *//*
    public String setDetault(String addressId) {
        int cityPos = 0;
        int zonePos = 0;
        String addressStr = "";

        for (int i = 0; i < mAddress.size(); i++) {
            Address cityAddress = mAddress.get(i);
            if (cityAddress != null && cityAddress.getChild() != null) {
                for (int j = 0; j < cityAddress.getChild().size(); j++) {
                    Address zoneAddress = cityAddress.getChild().get(j);
                    if (zoneAddress != null && TextUtils.equals(zoneAddress.getId() + "", addressId)) {
                        cityPos = i;
                        zonePos = j;
                        addressStr = cityAddress.getName() + " " + zoneAddress.getName();
                        break;
                    }
                }
            }
        }
        cityWheelView.setCurrentItem(cityPos);
        Address cityAddress = mAddress.get(cityPos);
        MyWheelAdapter adapter = new MyWheelAdapter(cityAddress.getChild());
        zoneWheelView.setAdapter(adapter);
        zoneWheelView.setCurrentItem(zonePos);

        return addressStr;
    }*/

    public interface IAddressChangeListener {

        void onAddressChange(Address city, Address zone);

    }

    class CityChangeListener implements WheelView.OnWheelChangedListener {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            if (newValue >= 0 && newValue < cityWheelView.getAdapter().getItemsCount()) {
                Address address = ((MyWheelAdapter) cityWheelView.getAdapter())
                        .getItemObject(cityWheelView.getCurrentItem());

                if (address != null && address.getChild() != null) {
                    MyWheelAdapter adapter = new MyWheelAdapter(address.getChild());
                    zoneWheelView.setAdapter(adapter);
                }
            }
        }
    }

    class MyAdapter extends BaseAdapter {
        public Context context;
        List<XiangAddresss.DataBean> mList;

        public MyAdapter(List<XiangAddresss.DataBean> List, Context context) {
            this.mList = List;
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
                view = LayoutInflater.from(context).inflate(R.layout.item_only_textview, null);
                holder.xingming = (TextView) view.findViewById(R.id.tv_only_textview);
                holder.ll_onlytext= (LinearLayout) view.findViewById(R.id.ll_onlytext);
                view.setTag(holder);
            } else {
                holder = (viewHolder) view.getTag();
            }
            holder.xingming.setText(mList.get(i).getName());
            if (mList.get(i).getFlag()==1){
                //代表是选中的
                holder.ll_onlytext.setBackgroundResource(R.color.blue);
                holder.xingming.setTextColor(context.getResources().getColor(R.color.white));
            }else{
                holder.ll_onlytext.setBackgroundResource(R.color.white);
                holder.xingming.setTextColor(context.getResources().getColor(R.color.text51));
            }
            return view;
        }

        class viewHolder {
            TextView xingming;
            LinearLayout ll_onlytext;
        }
    }
    class MyAdapter2 extends BaseAdapter {
        public Context context;
        List<CunAddress.DataBean> mList;

        public MyAdapter2(List<CunAddress.DataBean> List, Context context) {
            this.mList = List;
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
                view = LayoutInflater.from(context).inflate(R.layout.item_only_textview, null);
                holder.xingming = (TextView) view.findViewById(R.id.tv_only_textview);
                holder.ll_onlytext= (LinearLayout) view.findViewById(R.id.ll_onlytext);
                view.setTag(holder);
            } else {
                holder = (viewHolder) view.getTag();
            }
            holder.xingming.setText(mList.get(i).getName());
            if (mList.get(i).getFlag()==1){
                //代表是选中的
                holder.ll_onlytext.setBackgroundResource(R.color.blue);
                holder.xingming.setTextColor(context.getResources().getColor(R.color.white));
            }else{
                holder.ll_onlytext.setBackgroundResource(R.color.white);
                holder.xingming.setTextColor(context.getResources().getColor(R.color.text51));
            }
            return view;
        }

        class viewHolder {
            TextView xingming;
            LinearLayout ll_onlytext;
        }
    }
}
