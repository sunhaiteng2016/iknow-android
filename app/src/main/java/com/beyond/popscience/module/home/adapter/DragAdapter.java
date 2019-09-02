package com.beyond.popscience.module.home.adapter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.beyond.popscience.R;

import com.beyond.popscience.frame.base.CustomBaseAdapter;
import com.beyond.popscience.frame.pojo.NavObj;
import com.beyond.popscience.widget.draggridview.DragGridBaseAdapter;
public class DragAdapter extends CustomBaseAdapter<NavObj> implements DragGridBaseAdapter {
    private int mHidePosition = -1;
    private boolean isShowDelete;// 根据这个变量来判断是否显示删除图标，true是显示，false是不显示
    private IChangeListener mListener;
    private int fiexedCount;

    public DragAdapter(Fragment context, List<NavObj> list, int fiexedCount) {
        super(context);
        this.dataList = list;
        this.fiexedCount = fiexedCount;
    }

    public void setIsShowDelete(boolean isShowDelete) {
        this.isShowDelete = isShowDelete;
        notifyDataSetChanged();
    }

    public void changeData(List<NavObj> list) {
        this.dataList = list;
        notifyDataSetChanged();
    }

    /**
     * 删除项
     * @param position
     */
    public void remove(int position){
        if (position<dataList.size()){
            dataList.remove(position);
        }
        notifyDataSetChanged();
    }

    /**
     * 添加项
     * @param nav
     */
    public void add(NavObj nav){
        dataList.add(nav);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public NavObj getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 由于复用convertView导致某些item消失了，所以这里不复用item，
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_grid, null);
        TextView mTag = (TextView) convertView.findViewById(R.id.tvItem);
        Button deletebtn = (Button) convertView.findViewById(R.id.imgDelete);
        mTag.setText(dataList.get(position).getClassName());
        if (position == mHidePosition) {
            convertView.setVisibility(View.INVISIBLE);
        }

        if (position<fiexedCount){
            mTag.setBackgroundResource(R.drawable.bg_border_round_blue);
        } else {
            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mListener!= null){
                        mListener.onLongClick();
                    }
                    return true;
                }
            });
        }
        deletebtn.setVisibility(isShowDelete && position>4? View.VISIBLE : View.GONE);// 设置删除按钮是否显示
        deletebtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onDelete(position);
                }
            }
        });
        return convertView;
    }

    public void setDeleteListener(IChangeListener listener){
        this.mListener = listener;

    }

    @Override
    public void reorderItems(int oldPosition, int newPosition) {
        NavObj temp = dataList.get(oldPosition);
        if (oldPosition < newPosition) {
            for (int i = oldPosition; i < newPosition; i++) {
                Collections.swap(dataList, i, i + 1);
            }
        } else if (oldPosition > newPosition) {
            for (int i = oldPosition; i > newPosition; i--) {
                Collections.swap(dataList, i, i - 1);
            }
        }
        if (mListener!= null){
            mListener.onPositionChange(oldPosition,newPosition);
        }
        dataList.set(newPosition, temp);
    }

    @Override
    public void setHideItem(int hidePosition) {
        this.mHidePosition = hidePosition;
        notifyDataSetChanged();
    }

    /**
     * 点击删除监听
     */
    public interface IChangeListener{
        /**
         * 点击删除时触发
         * @param position
         */
        void onDelete(int position);

        /**
         * 长按时触发
         */
        void onLongClick();

        /**
         * 拖拽位置切换
         * @param oldPosition
         * @param newPosition
         */
        void onPositionChange(int oldPosition,int newPosition);
    }

}
