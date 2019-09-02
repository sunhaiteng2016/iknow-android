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
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.SystemClock;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.beyond.library.R;
import com.beyond.library.view.pullrefresh.internal.EmptyViewMethodAccessor;
import com.beyond.library.view.pullrefresh.internal.LoadingLayout;

import java.util.ArrayList;
import java.util.List;


public class PullToRefreshListView extends PullToRefreshAdapterViewBase<ListView>   {

	/**
	 * 存放headerView
	 */
	private List<View> headerViewList = new ArrayList<>();
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

    public static final int LABEL_MODE_DEFAULT = 0;

    public static final int LABEL_MODE_1 = 1;
    
    private int label_mode;

	/**
	 * 原始的 mode
	 */
	private Mode originMode;
	private NestedScrollingChildHelper mScrollingChildHelper;

	public int getLabel_mode() {
        return label_mode;
    }

    public void setLabel_mode(int label_mode) {
        this.label_mode = label_mode;
    }

    private LoadingLayout mHeaderLoadingView;
	private LoadingLayout mFooterLoadingView;

	private FrameLayout mLvFooterLoadingFrame;

	private boolean mListViewExtrasEnabled;

	public PullToRefreshListView(Context context) {
		super(context);
		init();
	}

	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PullToRefreshListView(Context context, Mode mode) {
		super(context, mode);
		init();
	}

	public PullToRefreshListView(Context context, Mode mode, AnimationStyle style) {
		super(context, mode, style);
		init();
	}

	/**
	 * 初始化
	 */
	private void init(){
		mZoomViewHeight = dp2px(getContext(), DEFAULT_ZOOM_HEIGHT_DP) + getHeaderSize();
		mScalingRunnable = new ScalingRunnable();
		mScrollingChildHelper = new NestedScrollingChildHelper(this);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			setNestedScrollingEnabled(true);
		}

	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	private int dp2px(Context context, float dpValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	@Override
	public final Orientation getPullToRefreshScrollDirection() {
		return Orientation.VERTICAL;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		if (mZoomView != null) {
			View headerView = null;
			if(headerViewList.size()!=0){
				headerView = headerViewList.get(0);
			}
			int top = 0;
			if(headerView != null) {
				if(headerView.getParent() != null && !(headerView.getParent() instanceof ListView)){
					ViewGroup viewGroup = (ViewGroup) headerView.getParent();
					top = viewGroup.getTop();
				}else{
					top = headerView.getTop();
				}
			}
			int height = mZoomViewHeight + top;
			if(height < 0){
				height = 0;
			}
			ViewGroup.LayoutParams localLayoutParams = mZoomView.getLayoutParams();
			if(localLayoutParams!=null){
				if(localLayoutParams.height != height){
					localLayoutParams.height = height;
					mZoomView.setLayoutParams(localLayoutParams);
				}
			}
		}
	}

	@Override
	protected void onRefreshing(final boolean doScroll) {
		/**
		 * If we're not showing the Refreshing view, or the list is empty, the
		 * the header/footer views won't show so we use the normal method.
		 */
		ListAdapter adapter = mRefreshableView.getAdapter();
		if (!mListViewExtrasEnabled || !getShowViewWhileRefreshing() || null == adapter || adapter.isEmpty()) {
			super.onRefreshing(doScroll);
			return;
		}

		super.onRefreshing(false);

		final LoadingLayout origLoadingView, listViewLoadingView, oppositeListViewLoadingView;
		final int selection, scrollToY;

		switch (getCurrentMode()) {
			case MANUAL_REFRESH_ONLY:
			case PULL_FROM_END:
				origLoadingView = getFooterLayout();
				if(origLoadingView.isShowFooterNoDataView()){
					return ;
				}
				listViewLoadingView = mFooterLoadingView;
				oppositeListViewLoadingView = mHeaderLoadingView;
				selection = mRefreshableView.getCount() - 1;
				scrollToY = getScrollY() - getFooterSize();
				break;
			case PULL_FROM_START:
			default:
				origLoadingView = getHeaderLayout();
				listViewLoadingView = mHeaderLoadingView;
				oppositeListViewLoadingView = mFooterLoadingView;
				selection = 0;
				scrollToY = getScrollY() + getHeaderSize();
				break;
		}

		// Hide our original Loading View
		origLoadingView.reset();
		origLoadingView.hideAllViews();

		// Make sure the opposite end is hidden too
		oppositeListViewLoadingView.setVisibility(View.GONE);

		// Show the ListView Loading View and set it to refresh.
		listViewLoadingView.setVisibility(View.VISIBLE);
		listViewLoadingView.refreshing();

		if (doScroll) {
			// We need to disable the automatic visibility changes for now
			disableLoadingLayoutVisibilityChanges();

			// We scroll slightly so that the ListView's header/footer is at the
			// same Y position as our normal header/footer
			setHeaderScroll(scrollToY);

			// Make sure the ListView is scrolled to show the loading
			// header/footer
			mRefreshableView.setSelection(selection);

			// Smooth scroll as normal
			smoothScrollTo(0);
		}
	}

	@Override
	protected void onReset() {
		/**
		 * If the extras are not enabled, just call up to super and return.
		 */
		if (!mListViewExtrasEnabled) {
			super.onReset();
			return;
		}

		final LoadingLayout originalLoadingLayout, listViewLoadingLayout;
		final int scrollToHeight, selection;
		final boolean scrollLvToEdge;

        
		switch (getCurrentMode()) {
			case MANUAL_REFRESH_ONLY:
			case PULL_FROM_END:
				originalLoadingLayout = getFooterLayout();
				listViewLoadingLayout = mFooterLoadingView;
				selection = mRefreshableView.getCount() - 1;
				scrollToHeight = getFooterSize();
				scrollLvToEdge = Math.abs(mRefreshableView.getLastVisiblePosition() - selection) <= 1;
				break;
			case PULL_FROM_START:
			default:
				originalLoadingLayout = getHeaderLayout();
				listViewLoadingLayout = mHeaderLoadingView;
				scrollToHeight = -getHeaderSize();
				selection = 0;
				scrollLvToEdge = Math.abs(mRefreshableView.getFirstVisiblePosition() - selection) <= 1;
				break;
		}

		// If the ListView header loading layout is showing, then we need to
		// flip so that the original one is showing instead
		if (listViewLoadingLayout.getVisibility() == View.VISIBLE) {

			// Set our Original View to Visible
			originalLoadingLayout.showInvisibleViews();

			// Hide the ListView Header/Footer
			listViewLoadingLayout.setVisibility(View.GONE);

			/**
			 * Scroll so the View is at the same Y as the ListView
			 * header/footer, but only scroll if: we've pulled to refresh, it's
			 * positioned correctly
			 */
			if (scrollLvToEdge && getState() != State.MANUAL_REFRESHING) {
				mRefreshableView.setSelection(selection);
				setHeaderScroll(scrollToHeight);
			}
		}
        
        originalLoadingLayout.setLabel_mode(label_mode);
        listViewLoadingLayout.setLabel_mode(label_mode);
        mHeaderLoadingView.setLabel_mode(label_mode);
        mFooterLoadingView.setLabel_mode(label_mode);

		// Finally, call up to super
		super.onReset();
	}

	/**
	 * 添加loading FooterView
	 */
	private void addLoadingFooterView(){
		if (mLvFooterLoadingFrame != null  && mLvFooterLoadingFrame.getParent()==null) {
			getRefreshableView().addFooterView(mLvFooterLoadingFrame, null, false);
		}
	}

	@Override
	public void onLoadMoreComplete() {
		mFooterLoadingView.hiddenFooterNoDataView();
		super.onLoadMoreComplete();
	}

	@Override
	public void onLoadMoreCompleteAndNoData() {
		mFooterLoadingView.showFooterNoDataView();
		super.onLoadMoreCompleteAndNoData();
	}

	@Override
	protected LoadingLayoutProxy createLoadingLayoutProxy(final boolean includeStart, final boolean includeEnd) {
		LoadingLayoutProxy proxy = super.createLoadingLayoutProxy(includeStart, includeEnd);

		if (mListViewExtrasEnabled) {
			final Mode mode = getMode();

			if (includeStart && mode.showHeaderLoadingLayout()) {
				proxy.addLayout(mHeaderLoadingView);
			}
			if (includeEnd && mode.showFooterLoadingLayout()) {
				proxy.addLayout(mFooterLoadingView);
			}
		}

		return proxy;
	}

	protected ListView createListView(Context context, AttributeSet attrs) {
		final ListView lv;
		if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
			lv = new InternalListViewSDK9(context, attrs);
		} else {
			lv = new InternalListView(context, attrs);
		}
		return lv;
	}


	@Override
	protected ListView createRefreshableView(Context context, AttributeSet attrs) {
		ListView lv = createListView(context, attrs);

		// Set it to this so it can be used in ListActivity/ListFragment
		lv.setId(android.R.id.list);
		return lv;
	}

	@Override
	protected void handleStyledAttributes(TypedArray a) {
		super.handleStyledAttributes(a);

		mListViewExtrasEnabled = a.getBoolean(R.styleable.PullToRefresh_ptrListViewExtrasEnabled, true);

		if (mListViewExtrasEnabled) {
			final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
					FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);

			// Create Loading Views ready for use later
			FrameLayout frame = new FrameLayout(getContext());
			mHeaderLoadingView = createLoadingLayout(getContext(), Mode.PULL_FROM_START, a);
			mHeaderLoadingView.setVisibility(View.GONE);
			frame.addView(mHeaderLoadingView, lp);
			mRefreshableView.addHeaderView(frame, null, false);

			mLvFooterLoadingFrame = new FrameLayout(getContext());
			mFooterLoadingView = createLoadingLayout(getContext(), Mode.PULL_FROM_END, a);
			mFooterLoadingView.setVisibility(View.GONE);
			mLvFooterLoadingFrame.addView(mFooterLoadingView, lp);

			/**
			 * If the value for Scrolling While Refreshing hasn't been
			 * explicitly set via XML, enable Scrolling While Refreshing.
			 */
			if (!a.hasValue(R.styleable.PullToRefresh_ptrScrollingWhileRefreshingEnabled)) {
				setScrollingWhileRefreshingEnabled(true);
			}
		}
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
	 *
	 * @param view
	 */
	public void addHeaderView(View view){
		if(view!=null){
			headerViewList.add(view);
			getRefreshableView().addHeaderView(view);
		}
	}

	/**
	 *
	 * @param view
	 * @param data
	 * @param isSelectable
	 */
	public void addHeaderView(View view, Object data, boolean isSelectable) {
		if(view!=null){
			headerViewList.add(view);
			getRefreshableView().addHeaderView(view, data, isSelectable);
		}
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
		if(mZoomView == null){
			return ;
		}
		if (mScalingRunnable != null && !mScalingRunnable.isFinished()) {
			mScalingRunnable.abortAnimation();
		}
		if(newScrollValue<0){   //下滑
			ViewGroup.LayoutParams localLayoutParams = mZoomView.getLayoutParams();
			if(localLayoutParams!=null){
				int height = Math.abs(newScrollValue) + mZoomViewHeight;
				if(localLayoutParams.height != height){
					localLayoutParams.height = height;
					mZoomView.setLayoutParams(localLayoutParams);
				}
			}
		}else{  //上滑
			ViewGroup.LayoutParams localLayoutParams = mZoomView.getLayoutParams();
			if(localLayoutParams!=null){
				int height = mZoomViewHeight - Math.abs(newScrollValue);
				if(localLayoutParams.height != height){
					localLayoutParams.height = height;
					mZoomView.setLayoutParams(localLayoutParams);
				}
			}
		}
	}

	@TargetApi(9)
	final class InternalListViewSDK9 extends InternalListView {

		public InternalListViewSDK9(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
				int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

			final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
					scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);

			// Does all of the hard work...
			OverscrollHelper.overScrollBy(PullToRefreshListView.this, deltaX, scrollX, deltaY, scrollY, isTouchEvent);

			return returnValue;
		}
	}

	protected class InternalListView extends ListView implements EmptyViewMethodAccessor {

		private boolean mAddedLvFooter = false;

		public InternalListView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		protected void dispatchDraw(Canvas canvas) {
			/**
			 * This is a bit hacky, but Samsung's ListView has got a bug in it
			 * when using Header/Footer Views and the list is empty. This masks
			 * the issue so that it doesn't cause an FC. See Issue #66.
			 */
			try {
				super.dispatchDraw(canvas);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public boolean dispatchTouchEvent(MotionEvent ev) {
			/**
			 * This is a bit hacky, but Samsung's ListView has got a bug in it
			 * when using Header/Footer Views and the list is empty. This masks
			 * the issue so that it doesn't cause an FC. See Issue #66.
			 */
			try {
				return super.dispatchTouchEvent(ev);
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
				return false;
			}
		}

		@Override
		public void setAdapter(ListAdapter adapter) {
			// Add the Footer View at the last possible moment
			if(!mAddedLvFooter){
				addLoadingFooterView();
				mAddedLvFooter = true;
			}

			super.setAdapter(adapter);
		}

		@Override
		public void setEmptyView(View emptyView) {
			PullToRefreshListView.this.setEmptyView(emptyView);
		}

		@Override
		public void setEmptyViewInternal(View emptyView) {
			super.setEmptyView(emptyView);
		}


    }

    /**
     *
     */

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener){
        this.onLoadMoreListener = onLoadMoreListener;
        this.getRefreshableView().setOnScrollListener(onScrollListener);
    }

    private AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {

        private int pre_last_position;
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//            L.d("onScroll: first:" + firstVisibleItem + " vis:" + visibleItemCount + " total:" + totalItemCount);
            int last_position = firstVisibleItem + visibleItemCount;
            if(last_position == totalItemCount){
                if(onLoadMoreListener != null && pre_last_position != last_position){
                    pre_last_position = last_position;
                    onLoadMoreListener.onLoadMore();
                }
            }
        }
    };

    private OnLoadMoreListener onLoadMoreListener;

    public interface OnLoadMoreListener {
        void onLoadMore();
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
