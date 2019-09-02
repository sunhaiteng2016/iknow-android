package com.beyond.library.view.pullrefresh.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.beyond.library.R;
import com.beyond.library.view.pullrefresh.PullToRefreshBase;


/**
 * 帧动画 loading
 * Created by linjinfa 331710168@qq.com on 2015/8/26.
 */
public class FrameLoadingLayout extends LoadingLayout {

    public FrameLoadingLayout(Context context, PullToRefreshBase.Mode mode, PullToRefreshBase.Orientation scrollDirection, TypedArray attrs) {
        super(context, mode, scrollDirection, attrs);
        mInnerLayout.setPadding(mInnerLayout.getPaddingLeft(), dp2px(context, 5),mInnerLayout.getPaddingRight(), dp2px(context,5));
//        if(mHeaderText!=null){
//            ViewGroup.LayoutParams mHeaderTextLParms = mHeaderText.getLayoutParams();
//            if(mHeaderTextLParms!=null && mHeaderTextLParms instanceof LinearLayout.LayoutParams){
//                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)mHeaderTextLParms;
//                layoutParams.topMargin = DensityUtil.dp2px(context,10);
//                mHeaderText.setLayoutParams(layoutParams);
//            }
//        }
        mHeaderImage.setPadding(0,0,0,0);
        mHeaderImage.setScaleType(ImageView.ScaleType.CENTER);
    }

    @Override
    protected void onLoadingDrawableSet(Drawable imageDrawable) {

    }

    @Override
    protected void onPullImpl(float scaleOfLayout) {

    }

    @Override
    protected void pullToRefreshImpl() {

    }

    @Override
    protected void refreshingImpl() {

    }

    @Override
    protected void releaseToRefreshImpl() {

    }

    @Override
    protected void resetImpl() {

    }

    /**
     * 下拉时是否执行动画
     * @return
     */
    @Override
    public boolean isShowAnimWhenPull() {
        return false;
    }

    @Override
    protected int getDefaultDrawableResId() {
        return R.drawable.mxd_refresh_loading;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
