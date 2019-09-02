/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.beyond.library.view.pullrefresh.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.library.R;
import com.beyond.library.view.pullrefresh.ILoadingLayout;
import com.beyond.library.view.pullrefresh.PullToRefreshBase;
import com.beyond.library.view.pullrefresh.PullToRefreshListView;


@SuppressLint("ViewConstructor")
public abstract class LoadingLayout extends FrameLayout implements ILoadingLayout {

    private int label_mode;
    /**
     * 是否强制隐藏头部ImageView
     */
    private boolean isForceGoneHeaderImageView;

    public int getLabel_mode() {
        return label_mode;
    }

    public void setLabel_mode(int label_mode) {
        this.label_mode = label_mode;
    }

    static final String LOG_TAG = "PullToRefresh-LoadingLayout";

    static final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();

    protected RelativeLayout mInnerLayout;
    protected LinearLayout refTxtLinLay;

    protected final ImageView mHeaderImage;
    protected final ProgressBar mHeaderProgress;

    private boolean mUseIntrinsicAnimation;

    protected final TextView mHeaderText;
    private final TextView mSubHeaderText;
    /**
     * 萌主到底了
     */
    private final RelativeLayout endFooterReLay;

    protected final PullToRefreshBase.Mode mMode;
    protected final PullToRefreshBase.Orientation mScrollDirection;

    private CharSequence mPullLabelFromTop;
    private CharSequence mRefreshingLabelFromTop;
    private CharSequence mReleaseLabelFromTop;

    private CharSequence mPullLabelFromBottom;
    private CharSequence mRefreshingLabelFromBottom;
    private CharSequence mReleaseLabelFromBottom;

    public LoadingLayout(Context context,
                         final PullToRefreshBase.Mode mode,
                         final PullToRefreshBase.Orientation scrollDirection,
                         TypedArray attrs) {
        super(context);
        mMode = mode;
        mScrollDirection = scrollDirection;

        switch (scrollDirection) {
            case HORIZONTAL:
                LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_header_horizontal, this);
                break;
            case VERTICAL:
            default:
                LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_header_vertical, this);
                break;
        }

        mInnerLayout = (RelativeLayout) findViewById(R.id.fl_inner);
        refTxtLinLay = (LinearLayout) findViewById(R.id.refTxtLinLay);
        mHeaderText = (TextView) mInnerLayout.findViewById(R.id.pull_to_refresh_text);
        mHeaderProgress = (ProgressBar) mInnerLayout.findViewById(R.id.pull_to_refresh_progress);
        mSubHeaderText = (TextView) mInnerLayout.findViewById(R.id.pull_to_refresh_sub_text);
        mHeaderImage = (ImageView) mInnerLayout.findViewById(R.id.pull_to_refresh_image);
        endFooterReLay = (RelativeLayout) mInnerLayout.findViewById(R.id.endFooterReLay);
        if(endFooterReLay!=null){
            endFooterReLay.setVisibility(View.GONE);
        }

        LayoutParams lp = (LayoutParams) mInnerLayout.getLayoutParams();

        switch (mode) {
            case PULL_FROM_END:
                lp.gravity = scrollDirection == PullToRefreshBase.Orientation.VERTICAL ? Gravity.TOP : Gravity.LEFT;

                switch (label_mode) {
                    case PullToRefreshListView.LABEL_MODE_DEFAULT:
                        // Load in labels
                        mPullLabelFromTop = context.getString(R.string.pull_to_refresh_from_bottom_pull_label_from_top);
                        mRefreshingLabelFromTop = context.getString(R.string.pull_to_refresh_from_bottom_refreshing_label_from_top);
                        mReleaseLabelFromTop = context.getString(R.string.pull_to_refresh_from_bottom_release_label_from_top);

                        mPullLabelFromBottom = context.getString(R.string.pull_to_refresh_from_bottom_pull_label_from_bottom);
                        mRefreshingLabelFromBottom = context.getString(R.string.pull_to_refresh_from_bottom_refreshing_label_from_bottom);
                        mReleaseLabelFromBottom = context.getString(R.string.pull_to_refresh_from_bottom_release_label_from_bottom);
                        break;
                    case PullToRefreshListView.LABEL_MODE_1:
                        // Load in labels
                        mPullLabelFromTop = context.getString(R.string.pull_to_refresh_from_bottom_pull_label_from_top2);
                        mRefreshingLabelFromTop = context.getString(R.string.pull_to_refresh_from_bottom_refreshing_label_from_top2);
                        mReleaseLabelFromTop = context.getString(R.string.pull_to_refresh_from_bottom_release_label_from_top2);

                        mPullLabelFromBottom = context.getString(R.string.pull_to_refresh_from_bottom_pull_label_from_bottom2);
                        mRefreshingLabelFromBottom = context.getString(R.string.pull_to_refresh_from_bottom_refreshing_label_from_bottom2);
                        mReleaseLabelFromBottom = context.getString(R.string.pull_to_refresh_from_bottom_release_label_from_bottom2);
                        break;
                }
                break;

            case PULL_FROM_START:
            default:
                lp.gravity = scrollDirection == PullToRefreshBase.Orientation.VERTICAL ? Gravity.BOTTOM : Gravity.RIGHT;


                switch (label_mode) {
                    case PullToRefreshListView.LABEL_MODE_DEFAULT:
                        // Load in labels
                        // Load in labels
                        mPullLabelFromTop = context.getString(R.string.pull_to_refresh_from_bottom_pull_label_from_top);
                        mRefreshingLabelFromTop = context.getString(R.string.pull_to_refresh_from_bottom_refreshing_label_from_top);
                        mReleaseLabelFromTop = context.getString(R.string.pull_to_refresh_from_bottom_release_label_from_top);

                        mPullLabelFromBottom = context.getString(R.string.pull_to_refresh_from_bottom_pull_label_from_bottom);
                        mRefreshingLabelFromBottom = context.getString(R.string.pull_to_refresh_from_bottom_refreshing_label_from_bottom);
                        mReleaseLabelFromBottom = context.getString(R.string.pull_to_refresh_from_bottom_release_label_from_bottom);
                        break;
                    case PullToRefreshListView.LABEL_MODE_1:
                        // Load in labels
                        mPullLabelFromTop = context.getString(R.string.pull_to_refresh_from_bottom_pull_label_from_top2);
                        mRefreshingLabelFromTop = context.getString(R.string.pull_to_refresh_from_bottom_refreshing_label_from_top2);
                        mReleaseLabelFromTop = context.getString(R.string.pull_to_refresh_from_bottom_release_label_from_top2);

                        mPullLabelFromBottom = context.getString(R.string.pull_to_refresh_from_bottom_pull_label_from_bottom2);
                        mRefreshingLabelFromBottom = context.getString(R.string.pull_to_refresh_from_bottom_refreshing_label_from_bottom2);
                        mReleaseLabelFromBottom = context.getString(R.string.pull_to_refresh_from_bottom_release_label_from_bottom2);
                        break;
                }

                break;
        }

        if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderBackground)) {
            Drawable background = attrs.getDrawable(R.styleable.PullToRefresh_ptrHeaderBackground);
            if (null != background) {
                ViewCompat.setBackground(this, background);
            }
        }

        if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderTextAppearance)) {
            TypedValue styleID = new TypedValue();
            attrs.getValue(R.styleable.PullToRefresh_ptrHeaderTextAppearance, styleID);
            setTextAppearance(styleID.data);
        }
        if (attrs.hasValue(R.styleable.PullToRefresh_ptrSubHeaderTextAppearance)) {
            TypedValue styleID = new TypedValue();
            attrs.getValue(R.styleable.PullToRefresh_ptrSubHeaderTextAppearance, styleID);
            setSubTextAppearance(styleID.data);
        }

        // Text Color attrs need to be set after TextAppearance attrs
        if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderTextColor)) {
            ColorStateList colors = attrs.getColorStateList(R.styleable.PullToRefresh_ptrHeaderTextColor);
            if (null != colors) {
                setTextColor(colors);
            }
        }
        if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderSubTextColor)) {
            ColorStateList colors = attrs.getColorStateList(R.styleable.PullToRefresh_ptrHeaderSubTextColor);
            if (null != colors) {
                setSubTextColor(colors);
            }
        }

        // Try and get defined drawable from Attrs
        Drawable imageDrawable = null;
        if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawable)) {
            imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawable);
        }

        // Check Specific Drawable from Attrs, these overrite the generic
        // drawable attr above
        switch (mode) {
            case PULL_FROM_START:
            default:
                if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableStart)) {
                    imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawableStart);
                } else if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableTop)) {
                    Log.w("PullToRefresh", "You're using the deprecated ptrDrawableTop attr, please switch over to ptrDrawableStart");
                    imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawableTop);
                }
                break;

            case PULL_FROM_END:
                if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableEnd)) {
                    imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawableEnd);
                } else if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableBottom)) {
                    Log.w("PullToRefresh", "You're using the deprecated ptrDrawableBottom attr, please switch over to ptrDrawableEnd");
                    imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawableBottom);
                }
                break;
        }

        // If we don't have a user defined drawable, load the default
        if (null == imageDrawable) {
            imageDrawable = context.getResources().getDrawable(getDefaultDrawableResId());
        }

        // Set Drawable, and save width/height
        setLoadingDrawable(imageDrawable);

        reset();
    }

    /**
     * 回收资源
     */
    private void recycleAnimationDrawable() {
        if (mHeaderImage != null && mHeaderImage.getDrawable() != null && mHeaderImage.getDrawable() instanceof AnimationDrawable) {
            AnimationDrawable animationDrawable = (AnimationDrawable) mHeaderImage.getDrawable();
            animationDrawable.stop();
            for (int i = 0; i < animationDrawable.getNumberOfFrames(); i++) {
                Drawable frame = animationDrawable.getFrame(i);
                if (frame instanceof BitmapDrawable) {
                    ((BitmapDrawable) frame).getBitmap().recycle();
                }
                frame.setCallback(null);
            }
            animationDrawable.setCallback(null);
        }
    }

    public final void setHeight(int height) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.height = height;
        requestLayout();
    }

    public final void setWidth(int width) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.width = width;
        requestLayout();
    }

    public final int getContentSize() {
        switch (mScrollDirection) {
            case HORIZONTAL:
                if (mInnerLayout.getMeasuredWidth() == 0) {
                    measureView(mInnerLayout);
                }
                return mInnerLayout.getMeasuredWidth();
            case VERTICAL:
            default:
                if (mInnerLayout.getMeasuredHeight() == 0) {
                    measureView(mInnerLayout);
                }
                return mInnerLayout.getMeasuredHeight();
        }
    }

    /**
     * 计算View的width以及height
     * @param child
     */
    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    public final void hideAllViews() {
        if (View.VISIBLE == mHeaderText.getVisibility()) {
            mHeaderText.setVisibility(View.INVISIBLE);
        }
        if (View.VISIBLE == mHeaderProgress.getVisibility()) {
            mHeaderProgress.setVisibility(View.INVISIBLE);
        }
        if (View.VISIBLE == mHeaderImage.getVisibility()) {
            mHeaderImage.setVisibility(View.INVISIBLE);
        }
        if (View.VISIBLE == mSubHeaderText.getVisibility()) {
            mSubHeaderText.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 强制隐藏头部的Imageview
     */
    @Override
    public final void forceHiddenHeaderImage() {
        isForceGoneHeaderImageView = true;
        mHeaderImage.setVisibility(View.GONE);
    }

    public final void onPull(float scaleOfLayout) {
        if (!mUseIntrinsicAnimation) {
            onPullImpl(scaleOfLayout);
        }
    }

    public final void pullToRefresh() {
        if (null != mHeaderText) {
            switch (mMode) {
                case PULL_FROM_START:
                    mHeaderText.setText(mPullLabelFromTop);
                    break;
                case PULL_FROM_END:
                    mHeaderText.setText(mPullLabelFromBottom);
                    break;
            }
        }

        // Now call the callback
        pullToRefreshImpl();
    }

    public final void refreshing() {
        if (null != mHeaderText) {
            switch (mMode) {
                case PULL_FROM_START:
                    mHeaderText.setText(mRefreshingLabelFromTop);
                    break;
                case PULL_FROM_END:
                    mHeaderText.setText(mRefreshingLabelFromBottom);
                    break;
            }
        }

        if (mUseIntrinsicAnimation) {
            if (!isUserIntrinsicAnimRunning()) {
                AnimationDrawable animationDrawable = ((AnimationDrawable) mHeaderImage.getDrawable());
                if (animationDrawable != null) {
                    animationDrawable.start();
                }
            }
        } else {
            // Now call the callback
            refreshingImpl();
        }

        if (null != mSubHeaderText) {
            mSubHeaderText.setVisibility(View.GONE);
        }
    }

    /**
     * AnimationDrawable 动画是否正在执行   当动画是 AnimationDrawable 时有效
     * @return
     */
    public boolean isUserIntrinsicAnimRunning() {
        if (mUseIntrinsicAnimation) {
            return ((AnimationDrawable) mHeaderImage.getDrawable()).isRunning();
        }
        return false;
    }

    public final void releaseToRefresh() {
        if (null != mHeaderText) {
            switch (mMode) {
                case PULL_FROM_START:
                    mHeaderText.setText(mReleaseLabelFromTop);
                    break;
                case PULL_FROM_END:
                    mHeaderText.setText(mReleaseLabelFromBottom);
                    break;
            }
        }

        // Now call the callback
        releaseToRefreshImpl();
    }

    public final void reset() {
        if (null != mHeaderText) {
            switch (mMode) {
                case PULL_FROM_START:
                    mHeaderText.setText(mPullLabelFromTop);
                    break;
                case PULL_FROM_END:
                    mHeaderText.setText(mPullLabelFromBottom);
                    break;
            }
        }
        if (isForceGoneHeaderImageView) {    //强制隐藏HeaderView
            forceHiddenHeaderImage();
        } else {
            mHeaderImage.setVisibility(View.VISIBLE);
        }

        if (mUseIntrinsicAnimation) {
            AnimationDrawable animationDrawable = ((AnimationDrawable) mHeaderImage.getDrawable());
            if (animationDrawable != null) {
                animationDrawable.stop();
                animationDrawable.selectDrawable(0);
            }
        } else {
            // Now call the callback
            resetImpl();
        }

        if (null != mSubHeaderText) {
            if (TextUtils.isEmpty(mSubHeaderText.getText())) {
                mSubHeaderText.setVisibility(View.GONE);
            } else {
                mSubHeaderText.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void setLastUpdatedLabel(CharSequence label) {
        setSubHeaderText(label);
    }

    public final void setLoadingDrawable(Drawable imageDrawable) {
        // Set Drawable
        mHeaderImage.setImageDrawable(imageDrawable);
        mUseIntrinsicAnimation = (imageDrawable instanceof AnimationDrawable);

        // Now call the callback
        onLoadingDrawableSet(imageDrawable);
    }

    public void setPullLabel(CharSequence pullLabel) {
        mPullLabelFromTop = pullLabel;
    }

    public void setRefreshingLabel(CharSequence refreshingLabel) {
        mRefreshingLabelFromTop = refreshingLabel;
    }

    public void setReleaseLabel(CharSequence releaseLabel) {
        mReleaseLabelFromTop = releaseLabel;
    }

    @Override
    public void setTextTypeface(Typeface tf) {
        mHeaderText.setTypeface(tf);
    }

    public final void showInvisibleViews() {
        if (View.INVISIBLE == mHeaderText.getVisibility()) {
            mHeaderText.setVisibility(View.VISIBLE);
        }
        if (View.INVISIBLE == mHeaderProgress.getVisibility()) {
            mHeaderProgress.setVisibility(View.VISIBLE);
        }
        if (View.INVISIBLE == mHeaderImage.getVisibility()) {
            if (isForceGoneHeaderImageView) {
                forceHiddenHeaderImage();
            } else {
                mHeaderImage.setVisibility(View.VISIBLE);
            }
        }
        if (View.INVISIBLE == mSubHeaderText.getVisibility()) {
            mSubHeaderText.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Callbacks for derivative Layouts
     */

    protected abstract int getDefaultDrawableResId();

    protected abstract void onLoadingDrawableSet(Drawable imageDrawable);

    protected abstract void onPullImpl(float scaleOfLayout);

    protected abstract void pullToRefreshImpl();

    protected abstract void refreshingImpl();

    protected abstract void releaseToRefreshImpl();

    protected abstract void resetImpl();

    /**
     * 下拉时是否执行动画    默认 false    当动画是 AnimationDrawable 时有效
     * @return
     */
    public boolean isShowAnimWhenPull() {
        return false;
    }

    private void setSubHeaderText(CharSequence label) {
        if (null != mSubHeaderText) {
            if (TextUtils.isEmpty(label)) {
                mSubHeaderText.setVisibility(View.GONE);
            } else {
                mSubHeaderText.setText(label);

                // Only set it to Visible if we're GONE, otherwise VISIBLE will
                // be set soon
                if (View.GONE == mSubHeaderText.getVisibility()) {
                    mSubHeaderText.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void setSubTextAppearance(int value) {
        if (null != mSubHeaderText) {
            mSubHeaderText.setTextAppearance(getContext(), value);
        }
    }

    private void setSubTextColor(ColorStateList color) {
        if (null != mSubHeaderText) {
            mSubHeaderText.setTextColor(color);
        }
    }

    private void setTextAppearance(int value) {
        if (null != mHeaderText) {
            mHeaderText.setTextAppearance(getContext(), value);
        }
        if (null != mSubHeaderText) {
            mSubHeaderText.setTextAppearance(getContext(), value);
        }
    }

    private void setTextColor(ColorStateList color) {
        if (null != mHeaderText) {
            mHeaderText.setTextColor(color);
        }
        if (null != mSubHeaderText) {
            mSubHeaderText.setTextColor(color);
        }
    }

    public void setTextColor(int color) {
        if (null != mHeaderText) {
            mHeaderText.setTextColor(color);
        }
        if (null != mSubHeaderText) {
            mSubHeaderText.setTextColor(color);
        }
    }

    /**
     * 显示 底部 盟主到底了view
     */
    public void showFooterNoDataView() {
        if (endFooterReLay != null) {
            refTxtLinLay.setVisibility(View.GONE);
            endFooterReLay.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏 底部 盟主到底了view
     */
    public void hiddenFooterNoDataView() {
        if (endFooterReLay != null) {
            refTxtLinLay.setVisibility(View.VISIBLE);
            endFooterReLay.setVisibility(View.GONE);
        }
    }

    /**
     * 是否显示了 萌主到底了
     * @return
     */
    public boolean isShowFooterNoDataView() {
        if (endFooterReLay != null) {
            return endFooterReLay.getVisibility() == View.VISIBLE;
        }
        return false;
    }

    public PullToRefreshBase.Mode getmMode() {
        return mMode;
    }
}
