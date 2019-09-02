package com.beyond.library.view.pullrefresh;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by linjinfa on 2017/6/7.
 * email 331710168@qq.com
 */
public class PullToRefreshRecycleView extends PullToRefreshBase<RecyclerView> {

    private OnReadyForPullListener onReadyForPullListener;
    private RecyclerView mRefreshableView;

    public PullToRefreshRecycleView(Context context) {
        super(context);
    }

    public PullToRefreshRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshRecycleView(Context context, Mode mode) {
        super(context, mode);
    }

    public PullToRefreshRecycleView(Context context, Mode mode, AnimationStyle animStyle) {
        super(context, mode, animStyle);
    }

    //重写4个方法
    //1 滑动方向
    @Override
    public Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    //滑动的View
    @Override
    protected RecyclerView createRefreshableView(Context context, AttributeSet attrs) {
        mRefreshableView = new RecyclerView(context, attrs);
        return mRefreshableView;
    }

    //是否滑动到底部
    @Override
    protected boolean isReadyForPullEnd() {
        return isLastItemVisible();
    }

    //是否滑动到顶部
    @Override
    protected boolean isReadyForPullStart() {
        if(onReadyForPullListener!=null){
            if(onReadyForPullListener.isDisablePullStart()){
                return false;
            }
        }

        View view = mRefreshableView.getChildAt(0);

        if (view != null) {
            return view.getTop() >= mRefreshableView.getTop();
        }
        return false;
    }

    private boolean isLastItemVisible() {
        if(onReadyForPullListener!=null){
            if(onReadyForPullListener.isDisablePullEnd()){
                return false;
            }
        }
        final RecyclerView.Adapter adapter = mRefreshableView.getAdapter();

        if (null == adapter) {
            return true;
        } else {
            LinearLayoutManager layoutManager = (LinearLayoutManager) mRefreshableView.getLayoutManager();
            final int lastItemPosition = mRefreshableView.getAdapter().getItemCount() - 1;
            final int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();

            /**
             * This check should really just be: lastVisiblePosition ==
             * lastItemPosition, but PtRListView internally uses a FooterView
             * which messes the positions up. For me we'll just subtract one to
             * account for it and rely on the inner condition which checks
             * getBottom().
             */
            if (lastVisiblePosition >= lastItemPosition) {
                final int childIndex = lastVisiblePosition - layoutManager.findFirstVisibleItemPosition();
                final View lastVisibleChild = mRefreshableView.getChildAt(childIndex);
                if (lastVisibleChild != null) {
                    return lastVisibleChild.getBottom() <= mRefreshableView.getBottom();
                }
            }
        }

        return false;
    }

    public OnReadyForPullListener getOnReadyForPullListener() {
        return onReadyForPullListener;
    }

    public void setOnReadyForPullListener(OnReadyForPullListener onReadyForPullListener) {
        this.onReadyForPullListener = onReadyForPullListener;
    }

    /**
     *
     */
    public interface OnReadyForPullListener{
        /**
         * 是否禁用下拉
         * @return
         */
        boolean isDisablePullStart();
        /**
         * 是否禁用上拉
         * @return
         */
        boolean isDisablePullEnd();
    }

}
