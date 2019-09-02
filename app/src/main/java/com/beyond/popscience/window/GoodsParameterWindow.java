package com.beyond.popscience.window;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.flyco.dialog.widget.popup.base.BasePopup;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Describe：商品参数
 * Date：2018/3/9
 * Time: 15:00
 * Author: Bin.Peng
 */

public class GoodsParameterWindow extends BasePopup<GoodsParameterWindow> {
    private View view;
    private ListView lv_canshu;
    private TextView tv_finish;

    public GoodsParameterWindow(Context context) {
        super(context);
    }

    //初始化控件
    @Override
    public View onCreatePopupView() {
        view = LayoutInflater.from(getContext()).inflate(R.layout.pop_goods_canshu, null);
        lv_canshu = (ListView) view.findViewById(R.id.lv_canshu);
        tv_finish = (TextView) view.findViewById(R.id.tv_finish);
        return view;
    }

    private List<String> mData;

    //设置数据
    @Override
    public void setUiBeforShow() {
        mData = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mData.add("没接口");
        }

        lv_canshu.setAdapter(new ParameterAdapetr());

        tv_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private class ParameterAdapetr extends BaseAdapter {
        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder hodel = null;
            if (convertView == null) {
                hodel = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_layout_canshu, null);
                hodel.tv_canshu_type = (TextView) convertView.findViewById(R.id.tv_canshu_type);
                hodel.tv_canshu_num = (TextView) convertView.findViewById(R.id.tv_canshu_num);

                convertView.setTag(hodel);
            } else {
                hodel = (ViewHolder) convertView.getTag();
            }

            hodel.tv_canshu_type.setText(mData.get(position));
            hodel.tv_canshu_num.setText(mData.get(position));
            return convertView;
        }

        class ViewHolder {
            TextView tv_canshu_type;
            TextView tv_canshu_num;
        }
    }
}
