package com.beyond.popscience.frame.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by linjinfa on 2017/6/10.
 * email 331710168@qq.com
 */
public class TriangleView extends View {

    private Paint paint;
    private Path trianglePath;

    public TriangleView(Context context) {
        super(context);
        init();
    }

    public TriangleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TriangleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     *
     */
    private void init(){
        paint = new Paint();
        paint.setColor(Color.parseColor("#262728"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(trianglePath == null){
            trianglePath = new Path();
            trianglePath.moveTo(getMeasuredWidth()/2, 0);
            trianglePath.lineTo(0, getMeasuredHeight());
            trianglePath.lineTo(getMeasuredWidth(), getMeasuredHeight());
            trianglePath.close();
        }
        canvas.drawPath(trianglePath, paint);
    }

}
