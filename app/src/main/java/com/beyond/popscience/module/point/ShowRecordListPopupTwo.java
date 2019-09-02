package com.beyond.popscience.module.point;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.beyond.library.util.DensityUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.pojo.RecordDetail;
import com.beyond.popscience.module.personal.Zzjl;
import com.beyond.popscience.module.point.adapter.RecordListAdapter;
import com.beyond.popscience.module.point.adapter.RecordListAdapterTwo;
import com.beyond.popscience.widget.BasePopupWindow.BasePopupWindow;
import com.beyond.popscience.widget.GridSpacingItemDecoration;

import java.util.List;


/**
 * 从底部滑上来的popup
 */
public class ShowRecordListPopupTwo extends BasePopupWindow implements View.OnClickListener {

    private View popupView;
    private ImageView ivArrow;
    private RecyclerView lvRecord;
    private List<Zzjl.DataBean> recordDetails;


    public ShowRecordListPopupTwo(Activity context, List<Zzjl.DataBean> recordDetails) {
        super(context);
        this.recordDetails = recordDetails;
        bindEvent();
    }

    @Override
    public Animation getShowAnimation() {
        return getTranslateAnimation(250 * 4, 0, 3500);
    }

    @Override
    protected View getClickToDismissView() {
        return popupView.findViewById(R.id.click_to_dismiss);
    }

    @Override
    public View getPopupView() {
        popupView = LayoutInflater.from(mContext).inflate(R.layout.activity_exchange_records_layout, null);
        return popupView;
    }

    @Override
    public View getAnimaView() {
        return popupView.findViewById(R.id.popup_anima);
    }

    private void bindEvent() {
        if (popupView != null) {
            ivArrow = (ImageView) popupView.findViewById(R.id.iv_arrow);
            lvRecord = (RecyclerView) popupView.findViewById(R.id.lv_record);
            lvRecord.setLayoutManager(new LinearLayoutManager(mContext));
            lvRecord.addItemDecoration(new GridSpacingItemDecoration(1,
                    DensityUtil.dp2px(mContext, 1), false));
            RecordListAdapterTwo adapter = new RecordListAdapterTwo(mContext);
            adapter.getDataList().addAll(recordDetails);
            lvRecord.setAdapter(adapter);
            ivArrow.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_arrow)
            dismiss();
        else if (onItemClickListener != null)
            onItemClickListener.onItemClick(v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View v);
    }
}
