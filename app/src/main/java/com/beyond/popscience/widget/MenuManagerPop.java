package com.beyond.popscience.widget;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.pojo.NavObj;
import com.beyond.popscience.module.home.adapter.DragAdapter;
import com.beyond.popscience.module.home.adapter.UnSubAdapter;
import com.beyond.popscience.widget.draggridview.DragGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yao.cui on 2017/6/10.
 */

public class MenuManagerPop extends PopupWindow implements DragAdapter.IChangeListener,UnSubAdapter.IUnSubListener{

    private Context mContext;
    private Fragment mFragment;

    private DragGridView mDgvSub;
    private GridView mGvNoSub;//为订阅

    private DragAdapter mDragAdapter;
    private UnSubAdapter unSubAdapter;

    private IManagerLitener mManagerListener;
    private int canDragIndex;//设置从哪个位置可以拖拽
    private List<NavObj> navSub = new ArrayList<>();//订阅
    private List<NavObj> navUnSub = new ArrayList<>();//为订阅
    private LinearLayout llPopTitle;
    private TextView tvOrderDelete;
    private ImageView ivClose;


    public MenuManagerPop(Fragment fragment,Context context, List<NavObj> navSub, List<NavObj> navUnSub, int canDragIndex){
        init(context);
        this.mFragment = fragment;
        this.navSub = navSub;
        this.navUnSub = navUnSub;
        this.canDragIndex = canDragIndex;
        initMenuManager();
    }

    private void init(Context context) {
        this.mContext = context;

        View rootView = LayoutInflater.from(context).inflate(R.layout.menu_manager_pop_layout, null);

        this.setContentView(rootView);
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        this.setOutsideTouchable(false);
        this.setTouchable(true);

        mDgvSub = (DragGridView) rootView.findViewById(R.id.dgvSubscribe);
        mGvNoSub = (GridView) rootView.findViewById(R.id.gvNoSubscribe);
        llPopTitle=(LinearLayout)rootView.findViewById(R.id.llPopTitle);
        tvOrderDelete=(TextView)rootView.findViewById(R.id.tvOrderDelete);
        tvOrderDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canOrder();
            }
        });
        ivClose=(ImageView)rootView.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    /**
     * 初始化已订阅标签 未订阅标签
     */
    private void initMenuManager(){
        mDragAdapter = new DragAdapter(mFragment,navSub,canDragIndex);
        mDragAdapter.setDeleteListener(this);

        mDgvSub.setAdapter(mDragAdapter);
        mDgvSub.setCanDragIndex(canDragIndex);//从第6个开始可以拖拽 从0 开始

        unSubAdapter = new UnSubAdapter(mFragment,navUnSub);
        mGvNoSub.setAdapter(unSubAdapter);
        unSubAdapter.setUnSubListener(this);
    }
    @Override
    public void onDelete(int position) {
        mDgvSub.removeDragImage();

        unSubAdapter.add(navSub.get(position));
        mDragAdapter.remove(position);
        mDragAdapter.notifyDataSetChanged();
        //在操作数据之后调用
        if (mManagerListener!= null){
            mManagerListener.onChange(navSub);
        }

    }

    @Override
    public void onLongClick() {
        mDragAdapter.setIsShowDelete(true);
        mDragAdapter.notifyDataSetChanged();
        if (mManagerListener != null){
            mManagerListener.canManager(true);
        }
    }

    @Override
    public void onPositionChange(int oldPosition, int newPosition) {
        if (mManagerListener!= null){
            mManagerListener.onChange(navSub);
        }
    }

    @Override
    public void onUnSubItemClick(int position) {
        mDragAdapter.add(unSubAdapter.getItem(position));//添加到已订阅
        unSubAdapter.remove(position);//从为订阅中删除

        //在移动数据之后调用
        if (mManagerListener!= null){
            mManagerListener.onChange(navSub);
        }
    }

    /**
     * 设置可排序删除
     */
    public void canOrder(){
        DragGridView.dragResponseMS = 10;
        mDragAdapter.setIsShowDelete(true);
        mDragAdapter.notifyDataSetChanged();
    }

    /**
     * 完成
     * 不可拖拽排序
     */
    public void confirm(){
        DragGridView.dragResponseMS = 700;
        mDragAdapter.setIsShowDelete(false);
        mDragAdapter.notifyDataSetChanged();
        mDgvSub.removeDragImage();
    }

    public void show(View parent) {
       this.showAsDropDown(parent);
    }

    public void setManagerListener(IManagerLitener listener){
        this.mManagerListener = listener;
    }

    public interface IManagerLitener{
        /**
         * 当前状态是否可编辑
         * @param isEditable
         */
        void canManager(boolean isEditable);

        void onChange(List<NavObj> navs);
    }
}
