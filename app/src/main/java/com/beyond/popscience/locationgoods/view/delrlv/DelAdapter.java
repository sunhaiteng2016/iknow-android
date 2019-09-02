package com.beyond.popscience.locationgoods.view.delrlv;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.locationgoods.bean.NotificationBean;

import java.util.ArrayList;
import java.util.List;

public class DelAdapter extends RecyclerView.Adapter<DelAdapter.MyViewHolder> implements LeftSlideView.IonSlidingButtonListener {
    private Context mContext;

    private List<NotificationBean> mDatas = new ArrayList<NotificationBean>();

    private IonSlidingViewClickListener mIDeleteBtnClickListener;

    private IonSlidingViewClickListener mISetBtnClickListener;

    private LeftSlideView mMenu = null;


    public DelAdapter(Fragment context, List<NotificationBean> list) {

        mContext = context.getContext();
        mIDeleteBtnClickListener = (IonSlidingViewClickListener) context;
        mISetBtnClickListener = (IonSlidingViewClickListener) context;
        mDatas = list;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.textView.setText(mDatas.get(position).getTitle());
        int isRead = mDatas.get(position).getIsRead();
        if (isRead == 0) {
            //未读
            holder.tv_read.setText("未读");
        } else {
            holder.tv_read.setText("");
        }
        //设置内容布局的宽为屏幕宽度
        holder.layout_content.getLayoutParams().width = Utils.getScreenWidth(mContext);
        holder.tv_time.setText(mDatas.get(position).getCreateTime());
        //item正文点击事件
        holder.layout_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否有删除菜单打开
                if (menuIsOpen()) {
                    closeMenu();//关闭菜单
                } else {
                    int n = holder.getLayoutPosition();
                    mIDeleteBtnClickListener.onItemClick(v, n);
                }
            }
        });

        //左滑设置点击事件
        holder.btn_Set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = holder.getLayoutPosition();
                mISetBtnClickListener.onSetBtnCilck(view, n);
            }
        });
        //左滑删除点击事件
        holder.btn_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = holder.getLayoutPosition();
                mIDeleteBtnClickListener.onDeleteBtnCilck(view, n);
            }
        });

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {

        //获取自定义View的布局（加载item布局）
        View view = LayoutInflater.from(mContext).inflate(R.layout.ietm, arg0, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView btn_Set;
        public TextView btn_Delete;
        public TextView textView;
        public TextView tv_read;
        public TextView tv_time;
        public ViewGroup layout_content;

        public MyViewHolder(View itemView) {
            super(itemView);
            btn_Set = (TextView) itemView.findViewById(R.id.tv_set);
            btn_Delete = (TextView) itemView.findViewById(R.id.tv_delete);
            tv_read = (TextView) itemView.findViewById(R.id.tv_read);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            textView = (TextView) itemView.findViewById(R.id.text);
            layout_content = (ViewGroup) itemView.findViewById(R.id.layout_content);
            ((LeftSlideView) itemView).setSlidingButtonListener(DelAdapter.this);
        }
    }


    /**
     * 删除item
     *
     * @param position
     */
    public void removeData(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }


    /**
     * 删除菜单打开信息接收
     */
    @Override
    public void onMenuIsOpen(View view) {
        mMenu = (LeftSlideView) view;
    }


    /**
     * 滑动或者点击了Item监听
     *
     * @param leftSlideView
     */
    @Override
    public void onDownOrMove(LeftSlideView leftSlideView) {
        if (menuIsOpen()) {
            if (mMenu != leftSlideView) {
                closeMenu();
            }
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        mMenu.closeMenu();
        mMenu = null;

    }

    /**
     * 判断菜单是否打开
     *
     * @return
     */
    public Boolean menuIsOpen() {
        if (mMenu != null) {
            return true;
        }
        return false;
    }


    /**
     * 注册接口的方法：点击事件。在Mactivity.java实现这些方法。
     */
    public interface IonSlidingViewClickListener {
        void onItemClick(View view, int position);//点击item正文

        void onDeleteBtnCilck(View view, int position);//点击“删除”

        void onSetBtnCilck(View view, int position);//点击“设置”
    }

}
