package com.beyond.popscience.frame.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.view.WindowManager;

/**
 * FastBlur 模糊 Created by paveld on 3/6/14.
 */
@SuppressWarnings("deprecation")
public class FastBlur {

	public static Bitmap doBlur(Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {

		try {
			Bitmap bitmap;
			if (canReuseInBitmap) {
                bitmap = sentBitmap;
            } else {
                bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
            }

			if (radius < 1) {
                return (null);
            }

			int w = bitmap.getWidth();
			int h = bitmap.getHeight();

			int[] pix = new int[w * h];
			bitmap.getPixels(pix, 0, w, 0, 0, w, h);

			int wm = w - 1;
			int hm = h - 1;
			int wh = w * h;
			int div = radius + radius + 1;

			int r[] = new int[wh];
			int g[] = new int[wh];
			int b[] = new int[wh];
			int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
			int vmin[] = new int[Math.max(w, h)];

			int divsum = (div + 1) >> 1;
			divsum *= divsum;
			int dv[] = new int[256 * divsum];
			for (i = 0; i < 256 * divsum; i++) {
                dv[i] = (i / divsum);
            }

			yw = yi = 0;

			int[][] stack = new int[div][3];
			int stackpointer;
			int stackstart;
			int[] sir;
			int rbs;
			int r1 = radius + 1;
			int routsum, goutsum, boutsum;
			int rinsum, ginsum, binsum;

			for (y = 0; y < h; y++) {
                rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
                for (i = -radius; i <= radius; i++) {
                    p = pix[yi + Math.min(wm, Math.max(i, 0))];
                    sir = stack[i + radius];
                    sir[0] = (p & 0xff0000) >> 16;
                    sir[1] = (p & 0x00ff00) >> 8;
                    sir[2] = (p & 0x0000ff);
                    rbs = r1 - Math.abs(i);
                    rsum += sir[0] * rbs;
                    gsum += sir[1] * rbs;
                    bsum += sir[2] * rbs;
                    if (i > 0) {
                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];
                    } else {
                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];
                    }
                }
                stackpointer = radius;

                for (x = 0; x < w; x++) {

                    r[yi] = dv[rsum];
                    g[yi] = dv[gsum];
                    b[yi] = dv[bsum];

                    rsum -= routsum;
                    gsum -= goutsum;
                    bsum -= boutsum;

                    stackstart = stackpointer - radius + div;
                    sir = stack[stackstart % div];

                    routsum -= sir[0];
                    goutsum -= sir[1];
                    boutsum -= sir[2];

                    if (y == 0) {
                        vmin[x] = Math.min(x + radius + 1, wm);
                    }
                    p = pix[yw + vmin[x]];

                    sir[0] = (p & 0xff0000) >> 16;
                    sir[1] = (p & 0x00ff00) >> 8;
                    sir[2] = (p & 0x0000ff);

                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];

                    rsum += rinsum;
                    gsum += ginsum;
                    bsum += binsum;

                    stackpointer = (stackpointer + 1) % div;
                    sir = stack[(stackpointer) % div];

                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];

                    rinsum -= sir[0];
                    ginsum -= sir[1];
                    binsum -= sir[2];

                    yi++;
                }
                yw += w;
            }
			for (x = 0; x < w; x++) {
                rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
                yp = -radius * w;
                for (i = -radius; i <= radius; i++) {
                    yi = Math.max(0, yp) + x;

                    sir = stack[i + radius];

                    sir[0] = r[yi];
                    sir[1] = g[yi];
                    sir[2] = b[yi];

                    rbs = r1 - Math.abs(i);

                    rsum += r[yi] * rbs;
                    gsum += g[yi] * rbs;
                    bsum += b[yi] * rbs;

                    if (i > 0) {
                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];
                    } else {
                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];
                    }

                    if (i < hm) {
                        yp += w;
                    }
                }
                yi = x;
                stackpointer = radius;
                for (y = 0; y < h; y++) {
                    // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                    pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                    rsum -= routsum;
                    gsum -= goutsum;
                    bsum -= boutsum;

                    stackstart = stackpointer - radius + div;
                    sir = stack[stackstart % div];

                    routsum -= sir[0];
                    goutsum -= sir[1];
                    boutsum -= sir[2];

                    if (x == 0) {
                        vmin[y] = Math.min(y + r1, hm) * w;
                    }
                    p = x + vmin[y];

                    sir[0] = r[p];
                    sir[1] = g[p];
                    sir[2] = b[p];

                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];

                    rsum += rinsum;
                    gsum += ginsum;
                    bsum += binsum;

                    stackpointer = (stackpointer + 1) % div;
                    sir = stack[stackpointer];

                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];

                    rinsum -= sir[0];
                    ginsum -= sir[1];
                    binsum -= sir[2];

                    yi += w;
                }
            }

			bitmap.setPixels(pix, 0, w, 0, 0, w, h);

			return (bitmap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 高斯模糊图片
	 * @param bkg
	 * @param width
	 * @param height
	 * @param isHighquality	质量	true：高质量(速度慢)	false: 低质量(速度快)
	 * @return
	 */
	public static Bitmap blur(Bitmap bkg, int width, int height, boolean isHighquality) {
		float scaleFactor = 1;
		float radius = 20;
		if (isHighquality) {
			scaleFactor = 4;
			radius = 12;
		}

        Bitmap overlay = null;
        try {
            overlay = Bitmap.createBitmap((int)(width/scaleFactor), (int)(height/scaleFactor), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(overlay);
            canvas.scale(1 / scaleFactor, 1 / scaleFactor);
            Paint paint = new Paint();
            paint.setFlags(Paint.FILTER_BITMAP_FLAG);
            canvas.drawBitmap(bkg, 0, 0, paint);

            overlay = doBlur(overlay, (int) radius, true);
            if(isHighquality && overlay!=null) {
                overlay = ImageUtils.getScaleByWidthBitmap(overlay, width);
            }
        }catch (OutOfMemoryError e) {
        }catch (Exception e){

        }

		return overlay;
	}
	
	/**
     * 截取Activity图片
     * @param activity
     * @return
     */
    public static Bitmap takeScreenShotActivity(Activity activity) {
        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        // 获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();
        // 去掉标题栏
        Rect frameRect = new Rect();
        view.getWindowVisibleDisplayFrame(frameRect);
        int statusHeight = frameRect.top;
        Bitmap b = Bitmap.createBitmap(b1, 0, statusHeight, width, height-statusHeight);
        view.destroyDrawingCache();
        return b;
    }

    /**
     * 截取Activity图片
     * @param activity
     * @param fgFrameRect
     * @return
     */
//    public static Bitmap takeScreenShotActivityByFrame(Activity activity,Rect fgFrameRect,Rect bgFrameRect) {
//        // View是你需要截图的View
//        View view = activity.getWindow().getDecorView();
//        view.setDrawingCacheEnabled(true);
//        view.buildDrawingCache();
//        Bitmap b1 = view.getDrawingCache();
//        // 去掉标题栏
//        Rect frameRect = new Rect();
//        view.getWindowVisibleDisplayFrame(frameRect);
//        int statusHeight = frameRect.top;
//        Bitmap foreground = Bitmap.createBitmap(b1, fgFrameRect.left, statusHeight+fgFrameRect.top, fgFrameRect.width(), fgFrameRect.height());
//        view.destroyDrawingCache();
//        b1.recycle();
//
//        //重绘图片
//        int bgWidth = bgFrameRect.right;
//        int bgHeight = bgFrameRect.bottom;
//
//        Bitmap newmap=Bitmap.createBitmap(bgWidth,bgHeight, Bitmap.Config.ARGB_8888);
//        Canvas canvas=new Canvas(newmap);
//        canvas.drawColor(Color.rgb(236,236,236));
//        canvas.drawBitmap(foreground, 0,0, null);
//        canvas.save(Canvas.ALL_SAVE_FLAG);
//        canvas.restore();
//        foreground.recycle();
//        return newmap;
//    }
    public static Bitmap takeScreenShotActivityByFrame(Activity activity,Rect fgFrameRect,Rect bgFrameRect) {
        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        // 去掉标题栏
        Rect frameRect = new Rect();
        view.getWindowVisibleDisplayFrame(frameRect);
        int statusHeight = frameRect.top;
        Bitmap foreground = null;
        try {
            foreground = Bitmap.createBitmap(b1, fgFrameRect.left, statusHeight+fgFrameRect.top, fgFrameRect.width(), fgFrameRect.height());
        } catch (OutOfMemoryError e) {
        }catch (Exception e){}
        view.destroyDrawingCache();
        b1.recycle();

        return foreground;
    }



    /**
	 * 获取当前界面的饿高斯模糊图片
	 * @param activity
	 * @return
	 */
	public static Bitmap takeCurrBlur(Activity activity){
		Bitmap bitmap = takeScreenShotActivity(activity);
		if(bitmap!=null){
			WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
			bitmap = blur(bitmap, wm.getDefaultDisplay().getWidth(), wm.getDefaultDisplay().getHeight(), true);
		}
		return bitmap;
	}

    /**
     * 获取当前界面的饿高斯模糊图片 给定截取区域和背景区域
     * @param activity
     * @param f
     * @param b 暂时不用
     * @return
     */
    public static Bitmap takeCurrBlurByFrame(Activity activity,Rect f,Rect b){
        Bitmap Bitmap = takeScreenShotActivityByFrame(activity,f,b);
        if(Bitmap!=null){

            Bitmap = blur(Bitmap, Bitmap.getWidth(), Bitmap.getHeight(), true);
        }
        return Bitmap;
    }
	
	/**
	 * view转成bitmap
	 * @param view
	 * @return
	 */
	public static Bitmap convertViewToBitmap(View view){
	    view.buildDrawingCache();
	    Bitmap bitmap = view.getDrawingCache();
	    return bitmap;
	}
	
	/**
	 * 获取状态栏高度
	 * @return
	 */
	public static int getStatusBarHeight(Activity activity){
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		return  frame.top;
	}

}
