/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.beyond.library.view.pullrefresh;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.GridView;

import com.beyond.library.view.pullrefresh.internal.EmptyViewMethodAccessor;

public class PullToRefreshHeaderGridView extends PullToRefreshAdapterViewBase<HeaderGridView> {

    /**
     * 默认 170dp
     */
    private final int DEFAULT_ZOOM_HEIGHT_DP = 170;
    private ScalingRunnable mScalingRunnable;
    /**
     * 缩放拉伸View
     */
    protected View mZoomView;
    /**
     * 要缩放的View高度
     */
    private int mZoomViewHeight;

    private static final Interpolator sInterpolator = new Interpolator() {
        public float getInterpolation(float paramAnonymousFloat) {
            float f = paramAnonymousFloat - 1.0F;
            return 1.0F + f * (f * (f * (f * f)));
        }
    };

	public PullToRefreshHeaderGridView(Context context) {
		super(context);
        init();
	}

	public PullToRefreshHeaderGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
        init();
	}

	public PullToRefreshHeaderGridView(Context context, Mode mode) {
		super(context, mode);
        init();
	}

	public PullToRefreshHeaderGridView(Context context, Mode mode, AnimationStyle style) {
		super(context, mode, style);
        init();
	}

	@Override
	public final Orientation getPullToRefreshScrollDirection() {
		return Orientation.VERTICAL;
	}

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        if (mZoomView != null) {
            View headerView = mRefreshableView.getHeaderView(0);
            int top = 0;
            if(headerView != null) {
                if(headerView.getParent() != null && !(headerView.getParent() instanceof GridView)){
                    ViewGroup viewGroup = (ViewGroup) headerView.getParent();
                    top = viewGroup.getTop();
                }else{
                    top = headerView.getTop();
                }
            }
            int height = mZoomViewHeight + top;
            if(height<0){
                height = 0;
            }
            ViewGroup.LayoutParams localLayoutParams = mZoomView.getLayoutParams();
            localLayoutParams.height = height;
            mZoomView.setLayoutParams(localLayoutParams);
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 初始化
     */
    private void init(){
        mZoomViewHeight = dp2px(getContext(), DEFAULT_ZOOM_HEIGHT_DP) + getHeaderSize();
        mScalingRunnable = new ScalingRunnable();
    }

    /**
     * 设置zoom的View
     * @param mZoomView
     */
    public void setZoomView(View mZoomView) {
        this.mZoomView = mZoomView;
    }

    /**
     * 设置ZoomView高度
     * @param mZoomViewHeight
     */
    public void setZoomViewHeight(int mZoomViewHeight) {
        this.mZoomViewHeight = mZoomViewHeight;
    }

    /**
     * 重置动画，自动滑动到顶部
     */
    @Override
    protected void smoothScrollToTop() {
        mScalingRunnable.startAnimation(getPullToRefreshScrollDurationLonger());
    }

    /**
     * zoomView动画逻辑 只在下拉刷新、上拉加载的动画过程中 的时候会调用
     * @param newScrollValue 手指Y轴移动距离值
     */
    @Override
    protected void pullHeaderToZoom(int newScrollValue) {
        if(mZoomView==null){
            return ;
        }
        if (mScalingRunnable != null && !mScalingRunnable.isFinished()) {
            mScalingRunnable.abortAnimation();
        }
        if(newScrollValue<0){   //下滑
            ViewGroup.LayoutParams localLayoutParams = mZoomView.getLayoutParams();
            localLayoutParams.height = Math.abs(newScrollValue) + mZoomViewHeight;
            mZoomView.setLayoutParams(localLayoutParams);
        }else{  //上滑

        }
    }

    @Override
	protected final HeaderGridView createRefreshableView(Context context, AttributeSet attrs) {
		final HeaderGridView gv;
		if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
			gv = new InternalGridViewSDK9(context, attrs);
		} else {
			gv = new InternalGridView(context, attrs);
		}

		// Use Generated ID (from res/values/ids.xml)
		gv.setId(android.R.id.list);
		return gv;
	}

	class InternalGridView extends HeaderGridView implements EmptyViewMethodAccessor {

		public InternalGridView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		public void setEmptyView(View emptyView) {
			PullToRefreshHeaderGridView.this.setEmptyView(emptyView);
		}

		@Override
		public void setEmptyViewInternal(View emptyView) {
			super.setEmptyView(emptyView);
		}
	}

	@TargetApi(9)
	final class InternalGridViewSDK9 extends InternalGridView {

		public InternalGridViewSDK9(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
				int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

			final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
					scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);

			// Does all of the hard work...
			OverscrollHelper.overScrollBy(PullToRefreshHeaderGridView.this, deltaX, scrollX, deltaY, scrollY, isTouchEvent);

			return returnValue;
		}
	}

    class ScalingRunnable implements Runnable {
        protected long mDuration;
        protected boolean mIsFinished = true;
        protected float mScale;
        protected long mStartTime;

        ScalingRunnable() {
        }

        public void abortAnimation() {
            mIsFinished = true;
        }

        public boolean isFinished() {
            return mIsFinished;
        }

        public void run() {
            if (mZoomView != null) {
                float f2;
                ViewGroup.LayoutParams localLayoutParams;
                if ((!mIsFinished) && (mScale > 1.0D)) {
                    float f1 = ((float) SystemClock.currentThreadTimeMillis() - (float) mStartTime) / (float) mDuration;
                    f2 = mScale - (mScale - 1.0F) * sInterpolator.getInterpolation(f1);
                    localLayoutParams = mZoomView.getLayoutParams();
                    if (f2 > 1.0F) {
                        localLayoutParams.height = ((int) (f2 * mZoomViewHeight));
                        mZoomView.setLayoutParams(localLayoutParams);
                        post(this);
                        return;
                    }
                    mIsFinished = true;
                }
            }
        }

        public void startAnimation(long paramLong) {
            if (mZoomView != null) {
                mStartTime = SystemClock.currentThreadTimeMillis();
                mDuration = paramLong;
                mScale = ((float) (mZoomView.getBottom()) / mZoomViewHeight);
                mIsFinished = false;
                post(this);
            }
        }
    }

}
