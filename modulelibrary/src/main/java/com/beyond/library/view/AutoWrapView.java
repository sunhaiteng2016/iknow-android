package com.beyond.library.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * 自动换行View
 * Created by linjinfa 331710168@qq.com on 2015/4/8.
 */
public class AutoWrapView extends LinearLayout {

    /**
     * 是否强制占满一行的空间 时 最小的 子View个数
     */
    private final int FORCE_FILL_LINE_COUNT = 1;
    /**
     * 每一行的相关数据
     */
    private ArrayList<ViewLineInfo> mLineInfoList = new ArrayList<ViewLineInfo>();
    /**
     * 横向间距
     */
    private int horizontalSpaceMargin = 4;
    /**
     * 垂直间距
     */
    private int verticalSpaceMargin = 4;
    /**
     *
     */
    private OnItemClickListener onItemClickListener;
    /**
     * 是否强制占满一行的空间
     */
    private boolean isForceFillLine = false;

    public AutoWrapView(Context context) {
        super(context);
    }

    public AutoWrapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoWrapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        for (int index = 0; index < getChildCount(); index++) {
            final View child = getChildAt(index);
            // measure
            if (child.getLayoutParams() != null && child.getLayoutParams().width > 0) {
                child.measure(MeasureSpec.makeMeasureSpec(child.getLayoutParams().width, MeasureSpec.EXACTLY), MeasureSpec.UNSPECIFIED);
            } else {
                child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            }
        }
        int height = measureHeight(widthSize);
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize);
            }
        }
        setMeasuredDimension(widthSize, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childIndex = 0;
        int currentLineCenterLineY = 0;
        for (int lineIndex = 0; lineIndex < mLineInfoList.size(); lineIndex++) {
            ViewLineInfo info = mLineInfoList.get(lineIndex);
            int currentWidth = 0;
            if (lineIndex != 0) {
                currentLineCenterLineY += verticalSpaceMargin;
            }
            currentLineCenterLineY += info.height / 2;
            //一行内 所有子View占用一行空间的 剩余大小
            int surplusSpace = getMeasuredWidth() - info.sumWidth;
            //将剩余的空间大小 平分给 一行内的所有 子View
            int viewNeedSpace = 0;
            if (info.viewCount != 0) {
                viewNeedSpace = surplusSpace / info.viewCount;
            }
            for (int currentLineViewIndex = 0; currentLineViewIndex < info.viewCount; currentLineViewIndex++) {
                View childView = getChildAt(childIndex);
                int currentChildHeight = childView.getMeasuredHeight();
                childIndex++;
                if (currentLineViewIndex != 0) {
                    currentWidth += horizontalSpaceMargin;
                }
                int childViewWidth = childView.getMeasuredWidth();
                if (isForceFillLine && info.viewCount > FORCE_FILL_LINE_COUNT) {    //强制占满一行空间
                    childViewWidth += viewNeedSpace;
                    childView.getLayoutParams().width = childViewWidth;
                    childView.measure(MeasureSpec.makeMeasureSpec(childView.getLayoutParams().width, MeasureSpec.EXACTLY), MeasureSpec.UNSPECIFIED);
                }

                childView.layout(currentWidth, currentLineCenterLineY - currentChildHeight / 2, currentWidth + childViewWidth, currentLineCenterLineY + currentChildHeight / 2);
                currentWidth += childViewWidth;
            }
            currentLineCenterLineY += info.height / 2;
        }
    }

    /**
     * 计算高度
     *
     * @param parentWidth
     * @return
     */
    private int measureHeight(int parentWidth) {
        mLineInfoList.clear();
        int totalHeight = 0;
        int currentLineMaxHeight = 0;
        int currentWidth = 0;
        int viewCountInCurrentLine = 0;
        int index = 0;
        for (; index < getChildCount(); index++) {
            View childView = getChildAt(index);
            int currentChildWidth = childView.getMeasuredWidth();
            int currentChildHeight = childView.getMeasuredHeight();
            if (currentChildWidth >= parentWidth) {
                currentChildWidth = parentWidth - childView.getPaddingLeft() - childView.getPaddingRight();
                childView.setLayoutParams(new AutoWrapView.LayoutParams(currentChildWidth, AutoWrapView.LayoutParams.WRAP_CONTENT));
            }
            if (currentWidth == 0) {// line start
                currentWidth = currentChildWidth;
                currentLineMaxHeight = currentChildHeight;
                viewCountInCurrentLine++;
            } else if (currentWidth + horizontalSpaceMargin + currentChildWidth > parentWidth) {
                // need change line
                ViewLineInfo info = new ViewLineInfo(viewCountInCurrentLine, currentWidth, currentLineMaxHeight);
                mLineInfoList.add(info);
                totalHeight += currentLineMaxHeight;

                // next line
                currentWidth = currentChildWidth;
                currentLineMaxHeight = currentChildHeight;
                viewCountInCurrentLine = 1;
            } else {
                // add to this line
                currentWidth += currentChildWidth + horizontalSpaceMargin;
                currentLineMaxHeight = Math.max(currentLineMaxHeight, currentChildHeight);
                viewCountInCurrentLine++;
            }
            // last one
            if (index == getChildCount() - 1) {
                ViewLineInfo info = new ViewLineInfo(viewCountInCurrentLine, currentWidth, currentLineMaxHeight);
                mLineInfoList.add(info);
                totalHeight += currentLineMaxHeight;
            }
        }
        return totalHeight + Math.max(0, mLineInfoList.size() - 1) * verticalSpaceMargin;
    }


    /**
     * 添加默认的TextView
     *
     * @param txt
     * @param txtSize px单位
     */
    public void addDefaultTxtView(Object targetObj, String txt, float txtSize, int txtColor, int bgResId) {
        addDefaultTxtView(targetObj, txt, txtSize, txtColor, getResources().getDrawable(bgResId));
    }

    public void addDefaultTxtView(Object targetObj, String txt, float txtSize, int txtColor, Drawable bgDrawable) {
        addDefaultTxtView(-1, targetObj, txt, txtSize, txtColor, bgDrawable);
    }

    public void addDefaultTxtView(int index, Object targetObj, String txt, float txtSize, int txtColor, int bgResId) {
        addDefaultTxtView(index, targetObj, txt, txtSize, txtColor, getResources().getDrawable(bgResId));
    }

    /**
     * 添加默认的TextView
     *
     * @param index
     * @param targetObj
     * @param txt        内容
     * @param txtSize    px单位
     * @param txtColor   字体颜色
     * @param bgDrawable 背景
     */
    public void addDefaultTxtView(int index, Object targetObj, String txt, float txtSize, int txtColor, Drawable bgDrawable) {
        TextView textView = new TextView(getContext());
        textView.setTag(targetObj);
        textView.setSingleLine(true);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setBackgroundDrawable(bgDrawable);
        textView.setPadding(dp2px(getContext(), 13), dp2px(getContext(), 6), dp2px(getContext(), 13), dp2px(getContext(), 6));
        textView.setTextColor(txtColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, txtSize);
        textView.setGravity(Gravity.CENTER);
        textView.setText(txt);
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, v.getTag());
                }
            }
        });
        addView(textView, index);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public int getVerticalSpaceMargin() {
        return verticalSpaceMargin;
    }

    public void setVerticalSpaceMargin(int verticalSpaceMargin) {
        this.verticalSpaceMargin = verticalSpaceMargin;
    }

    public int getHorizontalSpaceMargin() {
        return horizontalSpaceMargin;
    }

    public void setHorizontalSpaceMargin(int horizontalSpaceMargin) {
        this.horizontalSpaceMargin = horizontalSpaceMargin;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public boolean isForceFillLine() {
        return isForceFillLine;
    }

    /**
     * 是否强制占满一行空间
     */
    public void setIsForceFillLine(boolean isForceFillLine) {
        this.isForceFillLine = isForceFillLine;
    }

    private class ViewLineInfo {
        public int viewCount;// view count in one line
        public int height; // line max height
        public int sumWidth; // line sum width

        public ViewLineInfo(int count, int sumWidth, int height) {
            this.height = height;
            this.sumWidth = sumWidth;
            this.viewCount = count;
        }
    }

    /**
     *
     */
    public interface OnItemClickListener {
        void onItemClick(View view, Object targetObj);
    }

}
