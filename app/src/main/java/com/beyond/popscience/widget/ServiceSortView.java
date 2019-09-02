package com.beyond.popscience.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beyond.popscience.R;

/**
 * Created by yao.cui on 2017/6/26.
 */
public class ServiceSortView extends LinearLayout implements View.OnClickListener{

    private LinearLayout llTogether;
    private LinearLayout llTime;
    private LinearLayout llPrice;
    private LinearLayout llDistance;
    //价格
    private TextView tvPrice;
    private ImageView ivPrice;
    private ImageView distanceImgView;
    private ImageView timeImgView;

    /**
     * 排序
     */
    private ISortListener mListener;

    private Type currentType = Type.TOGETHER;//当前排序类型,默认是综合类型
    private Type lastPrice = Type.PRICE_DOWN;
    private Type lastTime = Type.TIME;
    private Type lastDistance = Type.DISTANCE;


    public ServiceSortView(Context context) {
        super(context);
        init(context);
    }

    public ServiceSortView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ServiceSortView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        setOrientation(HORIZONTAL);
        LayoutInflater.from(context).inflate(R.layout.service_sort,this,true);
        llTogether = (LinearLayout) findViewById(R.id.llTogether);
        llPrice = (LinearLayout) findViewById(R.id.llPrice);
        llTime = (LinearLayout) findViewById(R.id.llTime);
        llDistance = (LinearLayout) findViewById(R.id.llDistance);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        ivPrice = (ImageView) findViewById(R.id.ivPrice);
        distanceImgView = (ImageView) findViewById(R.id.distanceImgView);
        timeImgView = (ImageView) findViewById(R.id.timeImgView);

        llTogether.setOnClickListener(this);
        llPrice.setOnClickListener(this);
        llTime.setOnClickListener(this);
        llDistance.setOnClickListener(this);
    }

    /**
     * 设置排序监听
     * @param listener
     */
    public void setSortListener(ISortListener listener){
        this.mListener = listener;
    }
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        llTogether.setSelected(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llTogether:
                llTogether.setSelected(true);
                llDistance.setSelected(false);
                llTime.setSelected(false);
                llPrice.setSelected(false);

                if (mListener!= null){
                    mListener.sortItemClick(Type.TOGETHER);
                }
                currentType = Type.TOGETHER;
                break;
            case R.id.llDistance:
                llDistance.setSelected(true);
                llTogether.setSelected(false);
                llTime.setSelected(false);
                llPrice.setSelected(false);

                if (currentType== Type.DISTANCE_UP){
                    currentType = Type.DISTANCE;
                    distanceImgView.setImageResource(R.drawable.icon_sort_down);
                    lastDistance = currentType;
                } else if (currentType== Type.DISTANCE){
                    currentType = Type.DISTANCE_UP;
                    distanceImgView.setImageResource(R.drawable.icon_sort_up);
                    lastDistance = currentType;
                } else {
                    currentType = lastDistance;
                }

                if (mListener!= null){
                    mListener.sortItemClick(currentType);
                }
                break;
            case R.id.llPrice:
                llDistance.setSelected(false);
                llTogether.setSelected(false);
                llTime.setSelected(false);
                llPrice.setSelected(true);
                if (currentType== Type.PRICE_UP){
                    currentType = Type.PRICE_DOWN;
                    ivPrice.setImageResource(R.drawable.icon_sort_down);
                    lastPrice = currentType;
                } else if (currentType== Type.PRICE_DOWN){
                    currentType = Type.PRICE_UP;
                    ivPrice.setImageResource(R.drawable.icon_sort_up);
                    lastPrice = currentType;
                } else {
                    currentType = lastPrice;
                }

                if (mListener!= null){
                    mListener.sortItemClick(currentType);
                }

                break;
            case R.id.llTime:
                llDistance.setSelected(false);
                llTogether.setSelected(false);
                llTime.setSelected(true);
                llPrice.setSelected(false);

                if (currentType== Type.TIME_UP){
                    currentType = Type.TIME;
                    timeImgView.setImageResource(R.drawable.icon_sort_down);
                    lastTime = currentType;
                } else if (currentType== Type.TIME){
                    currentType = Type.TIME_UP;
                    timeImgView.setImageResource(R.drawable.icon_sort_up);
                    lastTime = currentType;
                } else {
                    currentType = lastTime;
                }

                if (mListener!= null){
                    mListener.sortItemClick(currentType);
                }
                break;
        }
    }

    /**
     * 排序类型
     */
    public enum Type{
        TOGETHER, DISTANCE, DISTANCE_UP, PRICE_DOWN, PRICE_UP, TIME, TIME_UP
    }
    public interface ISortListener{
        /**
         * 点击排序按钮
         * @param type
         */
        void sortItemClick(Type type);
    }
}
