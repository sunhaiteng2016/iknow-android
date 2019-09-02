package com.beyond.library.sharesdk.theme.classic;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import com.mob.tools.gui.ViewPagerAdapter;

public class ViewPagerClassic extends ViewGroup {
    private static final int SNAP_VELOCITY = 500;
    private int currentScreen;
    private Scroller scroller;
    private VelocityTracker mVelocityTracker;
    private float lastMotionX;
    private float lastMotionY;
    private static final int TOUCH_STATE_REST = 0;
    private static final int TOUCH_STATE_SCROLLING = 1;
    private int touchState;
    private int touchSlop;
    private int mMaximumVelocity;
    private ViewPagerAdapter adapter;

    public ViewPagerClassic(Context context) {
        this(context, null);
    }

    public ViewPagerClassic(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerClassic(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.touchState = 0;
        this.init(context);
    }

    private void init(Context context) {
        this.scroller = new Scroller(this.getContext(), new Interpolator() {
            float[] values = new float[]{0.0F, 0.0157073F, 0.0314108F, 0.0471065F, 0.0627905F, 0.0784591F, 0.0941083F, 0.109734F, 0.125333F, 0.140901F, 0.156434F, 0.171929F, 0.187381F, 0.202787F, 0.218143F, 0.233445F, 0.24869F, 0.263873F, 0.278991F, 0.29404F, 0.309017F, 0.323917F, 0.338738F, 0.353475F, 0.368125F, 0.382683F, 0.397148F, 0.411514F, 0.425779F, 0.439939F, 0.45399F, 0.46793F, 0.481754F, 0.495459F, 0.509041F, 0.522499F, 0.535827F, 0.549023F, 0.562083F, 0.575005F, 0.587785F, 0.60042F, 0.612907F, 0.625243F, 0.637424F, 0.649448F, 0.661312F, 0.673013F, 0.684547F, 0.695913F, 0.707107F, 0.718126F, 0.728969F, 0.739631F, 0.750111F, 0.760406F, 0.770513F, 0.78043F, 0.790155F, 0.799685F, 0.809017F, 0.81815F, 0.827081F, 0.835807F, 0.844328F, 0.85264F, 0.860742F, 0.868632F, 0.876307F, 0.883766F, 0.891007F, 0.898028F, 0.904827F, 0.911403F, 0.917755F, 0.92388F, 0.929776F, 0.935444F, 0.940881F, 0.946085F, 0.951057F, 0.955793F, 0.960294F, 0.964557F, 0.968583F, 0.97237F, 0.975917F, 0.979223F, 0.982287F, 0.985109F, 0.987688F, 0.990024F, 0.992115F, 0.993961F, 0.995562F, 0.996917F, 0.998027F, 0.99889F, 0.999507F, 0.999877F, 1.0F};

            public float getInterpolation(float t) {
                int i = (int)(t * 100.0F);
                return this.values[i];
            }
        });
        ViewConfiguration configuration = ViewConfiguration.get(context);
        this.touchSlop = configuration.getScaledTouchSlop();
        this.mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    public int getCurrentScreen() {
        return this.currentScreen;
    }

    public void setCurrentScreen(int theCurrentScreen) {
        if(this.adapter != null) {
            if(!this.scroller.isFinished()) {
                this.scroller.abortAnimation();
            }

            int lastScreen = this.currentScreen;
            this.currentScreen = Math.max(0, Math.min(theCurrentScreen, this.getChildCount()));
            this.adapter.onScreenChange(this.currentScreen, lastScreen);
            int scrW = com.mob.tools.utils.R.getScreenWidth(this.getContext());
            int newX = this.currentScreen * scrW;
            this.scroller.startScroll(0, 0, newX, 0);
            this.scrollTo(newX, 0);
        }
    }

    public void computeScroll() {
        if(this.adapter != null) {
            if(this.scroller.computeScrollOffset()) {
                this.scrollTo(this.scroller.getCurrX(), this.scroller.getCurrY());
                this.postInvalidate();
            } else {
                int lastScreen = this.currentScreen;
                int scrX = this.scroller.getCurrX();
                int w = this.getWidth();
                int index = scrX / w;
                if(scrX % w > w / 2) {
                    ++index;
                }

                this.currentScreen = Math.max(0, Math.min(index, this.getChildCount() - 1));
                if(lastScreen != this.currentScreen && this.adapter != null) {
                    this.adapter.onScreenChange(this.currentScreen, lastScreen);
                }
            }

        }
    }

    public void setAdapter(ViewPagerAdapter adapter) {
        this.adapter = adapter;
        this.removeAllViews();
        this.currentScreen = 0;
        if(this.adapter != null) {
            int i = 0;

            for(int count = adapter.getCount(); i < count; ++i) {
                this.addView(adapter.getView(i, null, this));
            }

        }
    }

    protected void dispatchDraw(Canvas canvas) {
        if(this.adapter != null && this.getChildCount() > 0) {
            long drawingTime = this.getDrawingTime();
            if(this.currentScreen > 0) {
                this.drawChild(canvas, this.getChildAt(this.currentScreen - 1), drawingTime);
            }

            this.drawChild(canvas, this.getChildAt(this.currentScreen), drawingTime);
            if(this.currentScreen < this.getChildCount() - 1) {
                this.drawChild(canvas, this.getChildAt(this.currentScreen + 1), drawingTime);
            }

        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(this.adapter == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            int count = this.getChildCount();
            int maxHeight = 0;

            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int width = com.mob.tools.utils.R.getScreenWidth(this.getContext());
            if(widthMode == MeasureSpec.EXACTLY){
                width = widthSize;
            }
            int adjustedWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);

            int adjustedHeightMeasureSpec;
            for(adjustedHeightMeasureSpec = 0; adjustedHeightMeasureSpec < count; ++adjustedHeightMeasureSpec) {
                View i = this.getChildAt(adjustedHeightMeasureSpec);
                i.measure(adjustedWidthMeasureSpec, 0);
                int child = i.getMeasuredHeight();
                if(child > maxHeight) {
                    maxHeight = child;
                }
            }

            adjustedHeightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.EXACTLY);
            super.onMeasure(adjustedWidthMeasureSpec, adjustedHeightMeasureSpec);

            for(int var9 = 0; var9 < count; ++var9) {
                View var10 = this.getChildAt(var9);
                var10.measure(adjustedWidthMeasureSpec, adjustedHeightMeasureSpec);
            }

        }
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if(this.adapter != null) {
            int cLeft = 0;
            int cWidth = right - left;
            byte cTop = 0;
            int cHeight = bottom - top;
            int i = 0;

            for(int count = this.getChildCount(); i < count; ++i) {
                View child = this.getChildAt(i);
                if(child.getVisibility() != View.GONE) {
                    child.layout(cLeft, cTop, cLeft + cWidth, cHeight);
                    cLeft += cWidth;
                }
            }

        }
    }

    public boolean dispatchUnhandledMove(View focused, int direction) {
        if(this.adapter == null) {
            return super.dispatchUnhandledMove(focused, direction);
        } else {
            if(direction == View.FOCUS_LEFT) {
                if(this.currentScreen > 0) {
                    this.scrollToScreen(this.currentScreen - 1);
                    return true;
                }
            } else if(direction == View.FOCUS_RIGHT && this.currentScreen < this.getChildCount() - 1) {
                this.scrollToScreen(this.currentScreen + 1);
                return true;
            }

            return super.dispatchUnhandledMove(focused, direction);
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if(action == 2 && this.touchState != 0) {
            return true;
        } else {
            if(this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }

            this.mVelocityTracker.addMovement(ev);
            switch(action) {
                case 0:
                    float x1 = ev.getX();
                    float y1 = ev.getY();
                    this.lastMotionX = x1;
                    this.lastMotionY = y1;
                    this.touchState = this.scroller.isFinished()?0:1;
                    break;
                case 1:
                case 3:
                    if(this.mVelocityTracker != null) {
                        this.mVelocityTracker.recycle();
                        this.mVelocityTracker = null;
                    }

                    this.touchState = 0;
                    break;
                case 2:
                    this.handleInterceptMove(ev);
            }

            return this.touchState != 0;
        }
    }

    private void handleInterceptMove(MotionEvent ev) {
        float x = ev.getX();
        float y = ev.getY();
        int xDiff = (int) Math.abs(x - this.lastMotionX);
        int yDiff = (int) Math.abs(y - this.lastMotionY);
        boolean xMoved = xDiff > this.touchSlop;
        boolean yMoved = yDiff > this.touchSlop;
        if((xMoved || yMoved) && xMoved) {
            this.touchState = 1;
            this.lastMotionX = x;
        }

    }

    public boolean onTouchEvent(MotionEvent ev) {
        if(this.adapter == null) {
            return false;
        } else {
            if(this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }

            this.mVelocityTracker.addMovement(ev);
            int action = ev.getAction();
            float x = ev.getX();
            switch(action) {
                case 0:
                    if(this.touchState != 0) {
                        if(!this.scroller.isFinished()) {
                            this.scroller.abortAnimation();
                        }

                        this.lastMotionX = x;
                    }
                    break;
                case 1:
                    if(this.touchState == 1) {
                        VelocityTracker velocityTracker = this.mVelocityTracker;
                        velocityTracker.computeCurrentVelocity(1000, (float)this.mMaximumVelocity);
                        int velocityX = (int)velocityTracker.getXVelocity();
                        if(velocityX > 500 && this.currentScreen > 0) {
                            this.scrollToScreen(this.currentScreen - 1);
                        } else if(velocityX < -500 && this.currentScreen < this.getChildCount() - 1) {
                            this.scrollToScreen(this.currentScreen + 1);
                        } else {
                            int screenWidth = this.getWidth();
                            int whichScreen = (this.getScrollX() + screenWidth / 2) / screenWidth;
                            this.scrollToScreen(whichScreen);
                        }

                        if(this.mVelocityTracker != null) {
                            this.mVelocityTracker.recycle();
                            this.mVelocityTracker = null;
                        }
                    }

                    this.touchState = 0;
                    break;
                case 2:
                    if(this.touchState == 1) {
                        this.handleScrollMove(ev);
                    } else if(this.onInterceptTouchEvent(ev) && this.touchState == 1) {
                        this.handleScrollMove(ev);
                    }
                    break;
                case 3:
                    this.touchState = 0;
            }

            return true;
        }
    }

    private void handleScrollMove(MotionEvent ev) {
        if(this.adapter != null) {
            float x1 = ev.getX();
            int deltaX = (int)(this.lastMotionX - x1);
            this.lastMotionX = x1;
            if(deltaX < 0) {
                if(this.getScrollX() > 0) {
                    this.scrollBy(Math.max(-this.getScrollX(), deltaX), 0);
                }
            } else if(deltaX > 0 && this.getChildCount() != 0) {
                View lastScr = this.getChildAt(this.getChildCount() - 1);
                int availableToScroll = lastScr.getRight() - this.getScrollX() - this.getWidth();
                if(availableToScroll > 0) {
                    this.scrollBy(Math.min(availableToScroll, deltaX), 0);
                }
            }

        }
    }

    public void scrollToScreen(int whichScreen) {
        this.scrollToScreen(whichScreen, false);
    }

    private void scrollToScreen(int whichScreen, boolean immediate) {
        boolean changingScreens = whichScreen != this.currentScreen;
        View focusedChild = this.getFocusedChild();
        if(focusedChild != null && changingScreens && focusedChild == this.getChildAt(this.currentScreen)) {
            focusedChild.clearFocus();
        }

        int newX = whichScreen * this.getWidth();
        int delta = newX - this.getScrollX();
        this.scroller.startScroll(this.getScrollX(), 0, delta, 0, immediate?0: Math.abs(delta) / 2);
        this.invalidate();
    }

    public void scrollLeft() {
        if(this.adapter != null) {
            if(this.currentScreen > 0 && this.scroller.isFinished()) {
                this.scrollToScreen(this.currentScreen - 1);
            }

        }
    }

    public void scrollRight() {
        if(this.adapter != null) {
            if(this.currentScreen < this.getChildCount() - 1 && this.scroller.isFinished()) {
                this.scrollToScreen(this.currentScreen + 1);
            }

        }
    }
}
