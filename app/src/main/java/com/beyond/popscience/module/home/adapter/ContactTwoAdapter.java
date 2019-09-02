package com.beyond.popscience.module.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.alibaba.fastjson.JSON;
import com.beyond.library.network.net.bean.BaseResponse;
import com.beyond.library.network.net.httpclient.NewCustomResponseHandler;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.net.base.AppBaseRestUsageV1;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.module.home.UserDetailsActivity;
import com.beyond.popscience.module.home.entity.ShuaXin;
import com.beyond.popscience.module.home.fragment.view.ContactBean;
import com.beyond.popscience.module.home.fragment.view.PinYinStyle;
import com.beyond.popscience.module.home.fragment.view.SwipeLayout;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ContactTwoAdapter extends BaseAdapter implements SwipeLayout.SwipeListener {
    private Context context;
    private ArrayList<ContactBean> list;
    public PinYinStyle sortToken;

    public ContactTwoAdapter(Context context, ArrayList<ContactBean> list) {
        super();
        this.context = context;
        this.list = list;
        sortToken = new PinYinStyle();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_contacts_lv, null);
        }

        ViewHolder holder = ViewHolder.getHolder(convertView);
        //设置数据
        ContactBean contactBean = list.get(position);
        holder.tv_contact_name.setText(contactBean.getName());

        final int mtype = list.get(position).getType();

        int flag1 = list.get(position).getFlag1();
        int flag2 = list.get(position).getFlag2();

        if (mtype == 2) {

            holder.tvRequest.setVisibility(View.VISIBLE);

            if (flag1 == 0) {
                holder.tvRequest.setOnClickListener(new Flag1OnClick(position));
            } else {
                holder.tvRequest.setText("已邀请");
            }
        } else {
            holder.tvRequest.setVisibility(View.GONE);
        }

        if (mtype == 0) {
            holder.tvAddFriends.setVisibility(View.VISIBLE);
            if (flag2 == 0) {
                holder.tvAddFriends.setOnClickListener(new Flag2OnClick(position));
            } else {
                holder.tvAddFriends.setText("已申请");
            }
        } else {
            holder.tvAddFriends.setVisibility(View.GONE);
        }
        if (mtype == 1) {
            holder.hy.setVisibility(View.VISIBLE);
        } else {
            holder.hy.setVisibility(View.GONE);
        }
        Glide.with(context).load(contactBean.getHeadImg()).into(holder.img);
        String currentAlphabet = contactBean.getPinyin().charAt(0) + "";
        if (position > 0) {
            String lastAlphabet = list.get(position - 1).getPinyin().charAt(0) + "";
            //获取上一个item的首字母
            if (currentAlphabet.equals(lastAlphabet)) {
                //首字母相同，需要隐藏当前item的字母的TextView
                holder.tv_first_alphabet.setVisibility(View.GONE);
            } else {
                //不相同就要显示当前的首字母
                holder.tv_first_alphabet.setVisibility(View.VISIBLE);
                holder.tv_first_alphabet.setText(currentAlphabet);
            }
        } else {
            holder.tv_first_alphabet.setVisibility(View.VISIBLE);
            holder.tv_first_alphabet.setText(currentAlphabet);
        }


        return convertView;
    }

    @Override
    public void onOpen(Object obj) {
    }

    @Override
    public void onClose(Object obj) {
    }

    static class ViewHolder {
        TextView tv_contact_name;
        TextView tv_first_alphabet;
        TextView tvCheck;
        TextView tvRequest;
        TextView tvAddFriends;
        TextView hy;
        ImageView img;
        LinearLayout itemAddressList;

        public ViewHolder(View convertView) {
            tv_contact_name = (TextView) convertView.findViewById(R.id.tv_contact_name);
            tv_first_alphabet = (TextView) convertView.findViewById(R.id.tv_first_alphabet);
            img = (ImageView) convertView.findViewById(R.id.img);
            tvCheck = (TextView) convertView.findViewById(R.id.tv_check);
            tvRequest = (TextView) convertView.findViewById(R.id.tv_request);
            tvAddFriends = (TextView) convertView.findViewById(R.id.tv_add_friends);
            hy = (TextView) convertView.findViewById(R.id.hy);
            itemAddressList = (LinearLayout) convertView.findViewById(R.id.item_address_list);
        }

        public static ViewHolder getHolder(View convertView) {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            if (holder == null) {
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            return holder;
        }
    }

    /**
     * 添加好友
     */
    public class Flag2OnClick implements View.OnClickListener {
        private int position;

        public Flag2OnClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            AppBaseRestUsageV1 appBaseRestUsageV1 = new AppBaseRestUsageV1();
            HashMap<String, String> map = new HashMap();
            map.put("userid", UserInfoUtil.getInstance().getUserInfo().getUserId());
            map.put("touserid", list.get(position).getUserid() + "");
            appBaseRestUsageV1.post(BeyondApplication.BaseUrl + "/im/addFriend", map, new NewCustomResponseHandler() {
                @Override
                public void onFailure(int statusCode, Map headerMap, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headerMap, responseString, throwable);
                }

                @Override
                public void onSuccess(int httpStatusCode, Map headerMap, String responseStr) {
                    super.onSuccess(httpStatusCode, headerMap, responseStr);
                    BaseResponse sss = JSON.parseObject(responseStr, BaseResponse.class);
                    ToastUtil.show(context, "添加成功！");
                    EventBus.getDefault().post(new ShuaXin());
                    list.get(position).setFlag2(1);
                    notifyDataSetChanged();
                }
            });
        }
    }

    public class Flag1OnClick implements View.OnClickListener {
        private int position;

        public Flag1OnClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            //邀请好友
            list.get(position).setFlag1(1);
            notifyDataSetChanged();
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + list.get(position).getMobile()));
            intent.putExtra("sms_body", "邀请您点击：http://www.appwzd.cn/app.jsp下载加入“科普中国户户通”");
            context.startActivity(intent);

        }
    }
}
