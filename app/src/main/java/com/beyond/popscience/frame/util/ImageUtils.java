package com.beyond.popscience.frame.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.BuildConfig;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {

    public static final String CORPIMG_NAME = "corpimg.jpg";
    public static final String TAKEPHOTO_NAME = "takephoto.jpg";
    public static final int GET_IMAGE_BY_CAMERA = 5001;
    public static final int GET_IMAGE_FROM_PHONE = 5002;
    public static final int CROP_IMAGE = 5003;
    public static Uri imageUriFromCamera;
    public static Uri cropImageUri;
    public static boolean isCamera = false;

    public static String imagepath;

    /**
     * 打开摄像头拍照
     *
     * @param activity
     * @param filename
     */
    public static void startActivityCameraImage(final Activity activity, String filename) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(FileUtil.getDirectory(VKConstants.CACHE_IMG), filename)));
        activity.startActivityForResult(intent, ImageUtils.GET_IMAGE_BY_CAMERA);
    }


    public static void openCameraImage(final Activity activity) {
        isCamera = true;
        ImageUtils.imageUriFromCamera = ImageUtils.createImagePathUri(activity, TAKEPHOTO_NAME);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, ImageUtils.imageUriFromCamera);
        activity.startActivityForResult(intent, ImageUtils.GET_IMAGE_BY_CAMERA);
    }

    public static void openCameraImage(final Activity activity, String filename) {
        ImageUtils.imageUriFromCamera = ImageUtils.createImagePathUri(activity, filename);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, ImageUtils.imageUriFromCamera);
        activity.startActivityForResult(intent, ImageUtils.GET_IMAGE_BY_CAMERA);
    }

    public static void openLocalImage(final Activity activity) {
        isCamera = false;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        activity.startActivityForResult(intent, ImageUtils.GET_IMAGE_FROM_PHONE);
    }

    public static void cropImage(Activity activity, Uri srcUri) {
        cropImage(activity, srcUri, -1, -1, false);
    }

    /**
     * 剪裁图片
     *
     * @param activity
     * @param srcUri
     * @param outputX
     * @param outputY
     * @param isReturnData
     */
    public static void cropImage(Activity activity, Uri srcUri, int outputX, int outputY, boolean isReturnData) {
        ImageUtils.cropImageUri = ImageUtils.createImagePathUri(activity, CORPIMG_NAME);
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat) {
            String path = getPath(activity, srcUri, isKitKat);
            srcUri = Uri.fromFile(new File(path));
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(srcUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX > 0 ? outputX : 100);
        intent.putExtra("outputY", outputY > 0 ? outputY : 100);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, ImageUtils.cropImageUri);
        intent.putExtra("return-data", isReturnData);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        activity.startActivityForResult(intent, CROP_IMAGE);
    }

    private static Uri createImagePathUri(Context context, String filename) {
        Uri imageFilePath = null;
        File photoPath = new File(VKConstants.CACHE_IMG);
        if (!photoPath.exists()) {
            photoPath.mkdirs();
        }
        File photo = new File(photoPath, filename);
        if (!photo.exists()) {
            try {
                photo.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        imageFilePath = Uri.fromFile(photo);
//        imageFilePath = FileProvider.getUriForFile(context,
//                BuildConfig.APPLICATION_ID+".provider",photo);
        return imageFilePath;
    }

    public static String getImagePathByUri(Activity context, Uri uri, boolean isCamera) {
        try {
            final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
            String path = getPath(context,uri,isKitKat);
            return path;
//            if (isKitKat) {
//                String path = getPath(context, uri, isKitKat);
//                return path;
////                srcUri = Uri.fromFile(new File(path));
//            }

//            String[] proj = {MediaStore.Images.Media.DATA};
//            Cursor actualimagecursor = context.managedQuery(uri, proj, null, null, null);
//            int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            actualimagecursor.moveToFirst();
//            return actualimagecursor.getString(actual_image_column_index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String readStream(String path, boolean isCamera) throws Exception {
        File file = new File(path);
        Bitmap scBitmap = BitmapFactory.decodeFile(path);

        if (isCamera) {
            compressBmpToFile(scBitmap, file);
        } else {
            compressBmpToFile(scBitmap, file);
        }

        InputStream inStream = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        String mImage = new String(Base64.encode(data, Base64.DEFAULT));
        outStream.close();
        inStream.close();
        return mImage;
    }

    public static void compressBmpToFile(Bitmap bmp, File file) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;//个人喜欢从80开始,
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > 200) {
            baos.reset();
            options -= 10;
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 压缩图片到3M
     *
     * @param bmp
     * @return
     */
    public static String compressThumpImageTo3M(Bitmap bmp) {
        return compressThumpImageToSizeK(bmp, 1024 * 3);
    }

    /**
     * 压缩图片到4M
     *
     * @param bmp
     * @return
     */
    public static String compressThumpImageTo4M(Bitmap bmp) {
        return compressThumpImageToSizeK(bmp, 1024 * 4);
    }

    /**
     * 压缩图片到300kb
     *
     * @param bmp
     * @return
     */
    public static String compressThumpImageTo300K(Bitmap bmp) {
        return compressThumpImageToSizeK(bmp, 300);
    }

    /**
     * 压缩图片到200kb
     *
     * @param bmp
     * @return
     */
    public static String compressThumpImageTo150K(Bitmap bmp) {
        return compressThumpImageToSizeK(bmp, 150);
    }

    /**
     * 将图片压缩到指定的大小  单位kb
     *
     * @param bmp
     * @return
     */
    public static String compressThumpImageToSizeK(Bitmap bmp, long imgSize) {
        File filepath = FileUtil.getDirectory(VKConstants.CACHE_IMG);
        File file = new File(filepath, System.currentTimeMillis() + ".jpg");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            int options = 100;
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
            while (baos.toByteArray().length / 1024 > imgSize) {
                baos.reset();
                if (options == 10) {
                    options -= 1;
                } else {
                    options -= 10;
                }
                if (options < 2) {
                    break;
                }
                bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
            }
        } catch (Exception e) {
        } catch (OutOfMemoryError error) {
        }
        try {
            if (bmp != null) {
                bmp.recycle();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            return null;
        }
        return file.getAbsolutePath();
    }

    /**
     * 模糊压缩到3M
     *
     * @param sourceSize
     * @param bitmap
     * @return
     */
    public static String compressBlurryThumpImageTo3M(long sourceSize, Bitmap bitmap) {
        return compressBlurryThumpImageToSizeK(sourceSize, 3 * 1024 * 1024, bitmap);
    }

    /**
     * 模糊压缩到300kb
     *
     * @param sourceSize
     * @param bitmap
     * @return
     */
    public static String compressBlurryThumpImageTo300K(long sourceSize, Bitmap bitmap) {
        return compressBlurryThumpImageToSizeK(sourceSize, 300 * 1024, bitmap);
    }

    /**
     * 将图片模糊压缩到指定的大小  单位b
     *
     * @return
     */
    public static String compressBlurryThumpImageToSizeK(long sourceSize, long targetSize, Bitmap bmp) {
        int quality;
        if (sourceSize <= targetSize) {
            quality = 90;
        } else {
            quality = (int) (targetSize * 1f / sourceSize * 100);
            if (quality < 0) {
                quality = 90;
            }
        }
        return compressBlurryImageAndSave(bmp, quality);
    }

    /**
     * 模糊压缩图片并保存
     *
     * @param bitmap
     * @return
     */
    public static String compressBlurryImageAndSave(Bitmap bitmap, int quality) {
        if (bitmap == null) {
            return null;
        }
        File file = new File(FileUtil.getDirectory(VKConstants.CACHE_IMG), System.currentTimeMillis() + ".jpg");
        ByteArrayOutputStream baos = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            if (bitmap != null) {
                bitmap.recycle();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
            return null;
        }
        return file.getAbsolutePath();
    }


    public static Bitmap compressImageFromFile(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;//只读边,不读内容
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 800f;//
        float ww = 480f;//
        int be = 1;
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置采样率

        newOpts.inPreferredConfig = Config.ARGB_8888;//该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收

        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
//		return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
        //其实是无效的,大家尽管尝试
        return bitmap;
    }

    /**
     * 缩放图片
     *
     * @param srcBitmap
     * @param targetWidth
     * @param targetHeight
     * @return
     */
    private static Bitmap getScaleBitmap(Bitmap srcBitmap, int targetWidth, int targetHeight) {
        if (srcBitmap != null && targetWidth > 0 && targetHeight > 0) {
            float widthScale = targetWidth * 1f / srcBitmap.getWidth();
            float heightScale = targetHeight * 1f / srcBitmap.getHeight();
            float scale = Math.max(widthScale, heightScale);
            srcBitmap = bitmapZoomByScale(srcBitmap, scale, scale);
        }
        return srcBitmap;
    }

    /**
     * 根据高度缩放图片
     *
     * @param srcBitmap
     * @param targetHeight
     * @return
     */
    public static Bitmap getScaleByHeightBitmap(Bitmap srcBitmap, int targetHeight) {
        if (srcBitmap != null && targetHeight > 0) {
            float widthScale = targetHeight * 1f / srcBitmap.getHeight();
            srcBitmap = bitmapZoomByScale(srcBitmap, widthScale, widthScale);
        }
        return srcBitmap;
    }

    /**
     * 根据宽度缩放图片
     *
     * @param srcBitmap
     * @param targetWidth
     * @return
     */
    public static Bitmap getScaleByWidthBitmap(Bitmap srcBitmap, int targetWidth) {
        if (srcBitmap != null && targetWidth > 0) {
            float widthScale = targetWidth * 1f / srcBitmap.getWidth();
            srcBitmap = bitmapZoomByScale(srcBitmap, widthScale, widthScale);
        }
        return srcBitmap;
    }

    /**
     * 缩放剪裁图片
     *
     * @param srcBitmap
     * @return
     */
    public static Bitmap getScaleCutBitmap(Bitmap srcBitmap, int targetWidth, int targetHeight, Bitmap defaultBitmap) {
        if (srcBitmap == null) {
            srcBitmap = defaultBitmap;
        }
        srcBitmap = getScaleBitmap(srcBitmap, targetWidth, targetHeight);
        if (srcBitmap != null && srcBitmap.getWidth() >= targetWidth && srcBitmap.getHeight() >= targetHeight) {
            try {
                srcBitmap = Bitmap.createBitmap(srcBitmap, (srcBitmap.getWidth() - targetWidth) / 2, (srcBitmap.getHeight() - targetHeight) / 2, targetWidth, targetHeight, null, false);
            } catch (OutOfMemoryError e) {
            } catch (Exception e) {
            }
        }
        return srcBitmap;
    }

    /**
     * 按照宽度缩放 裁掉顶部部分图片
     *
     * @param srcBitmap
     * @return
     */
    public static Bitmap getScaleWithCutTopBitmap(Bitmap srcBitmap, int targetWidth, int targetHeight, Bitmap defaultBitmap) {
        if (srcBitmap == null) {
            srcBitmap = defaultBitmap;
        }
        srcBitmap = getScaleByWidthBitmap(srcBitmap, targetWidth);
        if (srcBitmap != null) {
            try {
                if (targetHeight <= 0) {
                    targetHeight = srcBitmap.getHeight();
                }
                srcBitmap = Bitmap.createBitmap(srcBitmap, 0, Math.abs(srcBitmap.getHeight() - targetHeight), targetWidth, targetHeight, null, false);
            } catch (OutOfMemoryError e) {
            } catch (Exception e) {
            }
        }
        return srcBitmap;
    }

    /**
     * 按照高度缩放 裁掉右边部分图片
     *
     * @param srcBitmap
     * @return
     */
    public static Bitmap getScaleHeightCutRightBitmap(Bitmap srcBitmap, int targetWidth, int targetHeight, Bitmap defaultBitmap) {
        if (srcBitmap == null) {
            srcBitmap = defaultBitmap;
        }
        srcBitmap = getScaleByHeightBitmap(srcBitmap, targetHeight);
        if (srcBitmap != null) {
            try {
                srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, targetWidth, targetHeight, null, false);
            } catch (OutOfMemoryError e) {
            } catch (Exception e) {
            }
        }
        return srcBitmap;
    }

    /**
     * 按照高度缩放 裁掉右边部分图片
     *
     * @param srcBitmap
     * @return
     */
    public static Bitmap getScaleHeightCutLeftBitmap(Bitmap srcBitmap, int targetWidth, int targetHeight, Bitmap defaultBitmap) {
        if (srcBitmap == null) {
            srcBitmap = defaultBitmap;
        }
        srcBitmap = getScaleByHeightBitmap(srcBitmap, targetHeight);
        if (srcBitmap != null) {
            try {
                srcBitmap = Bitmap.createBitmap(srcBitmap, srcBitmap.getWidth() - targetWidth, 0, targetWidth, targetHeight, null, false);
            } catch (OutOfMemoryError e) {
            } catch (Exception e) {
            }
        }
        return srcBitmap;
    }

    /**
     * 按照宽度缩放 保留中部部分图片
     *
     * @param srcBitmap
     * @return
     */
    public static Bitmap getScaleWithCutCenterBitmap(Bitmap srcBitmap, int targetWidth, int targetHeight, Bitmap defaultBitmap) {
        if (srcBitmap == null) {
            srcBitmap = defaultBitmap;
        }
        srcBitmap = getScaleByWidthBitmap(srcBitmap, targetWidth);
        int top = 0;
        if (srcBitmap != null) {
            try {
                int height = srcBitmap.getHeight();
                if (targetHeight <= 0) {
                    targetHeight = height;
                } else if (targetHeight > 0 && targetHeight <= height) {
                    top = (height - targetHeight) / 2;
                }
                srcBitmap = Bitmap.createBitmap(srcBitmap, 0, top, targetWidth, targetHeight, null, false);
            } catch (OutOfMemoryError e) {
            } catch (Exception e) {
            }
        }
        return srcBitmap;
    }

    /**
     * 按照宽度缩放 剪裁掉底部部分图片
     *
     * @param srcBitmap
     * @return
     */
    public static Bitmap getScaleWithCutBottomBitmap(Bitmap srcBitmap, int targetWidth, int targetHeight, Bitmap defaultBitmap) {
        if (srcBitmap == null) {
            srcBitmap = defaultBitmap;
        }
        srcBitmap = getScaleByWidthBitmap(srcBitmap, targetWidth);
        if (srcBitmap != null) {
            try {
                if (targetHeight <= 0) {
                    targetHeight = srcBitmap.getHeight();
                }
                srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, targetWidth, targetHeight, null, false);
            } catch (OutOfMemoryError e) {
            } catch (Exception e) {
            }
        }
        return srcBitmap;
    }

    /**
     * 图片本身加上圆角
     *
     * @param bitmap
     * @param pixels
     * @return
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        if (bitmap == null) {
            return null;
        }
        return toRoundCorner(bitmap, pixels, bitmap.getWidth(), bitmap.getHeight());
    }

    /**
     * 图片本身加上圆角
     *
     * @param bitmap
     * @param pixels
     * @param width
     * @param height
     * @return
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels, int width, int height) {
        if (bitmap == null) {
            return null;
        }
        Bitmap output = null;
        try {
            output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            int color = 0xff424242;
            Paint paint = new Paint();
            Rect rect = new Rect(0, 0, width, height);
            RectF rectF = new RectF(rect);
            float roundPx = pixels;
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
        } catch (OutOfMemoryError e) {
        } catch (Exception e) {
        }
        return output;
    }

    /**
     * 按照指定长宽压缩
     *
     * @param srcBitmap
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap bitmapZoomBySize(Bitmap srcBitmap, int newWidth, int newHeight) {
        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();
        float scaleWidth = ((float) newWidth) / srcWidth;
        float scaleHeight = ((float) newHeight) / srcHeight;
        return bitmapZoomByScale(srcBitmap, scaleWidth, scaleHeight);
    }

    /**
     * 按正方形裁切图片
     */
    public static Bitmap ImageCrop(Bitmap bitmap) {
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();
        int wh = w > h ? h : w;// 裁切后所取的正方形区域边长

        int retX = w > h ? (w - h) / 2 : 0;//基于原图，取正方形左上角x坐标
        int retY = w > h ? 0 : (h - w) / 2;
        //下面这句是关键
        return Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null, false);
    }

    /**
     * 使用长宽缩放比缩放
     *
     * @param srcBitmap
     * @param scaleWidth
     * @param scaleHeight
     * @return
     */
    public static Bitmap bitmapZoomByScale(Bitmap srcBitmap, float scaleWidth, float scaleHeight) {
        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = null;
        try {
            resizedBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcWidth, srcHeight, matrix, true);

        } catch (OutOfMemoryError e) {
        } catch (Exception e) {
        }
        if (resizedBitmap != null) {
            return resizedBitmap;
        } else {
            return srcBitmap;
        }
    }

    /**
     * 加载本地图片 生成缩略图
     *
     * @param filePath
     * @return
     */
    public static Bitmap getThumbBitmap(String filePath, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, width, height);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap bm = null;
        try {
            bm = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError e) {
        } catch (Exception e) {
        }
        if (bm == null) {
            return null;
        }
        int degree = readPictureDegree(filePath);
        bm = rotateBitmap(bm, degree);
        if (bm == null) {
            return null;
        }
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            Bitmap.CompressFormat format = null;
            if (filePath.toLowerCase().endsWith("jpg") || filePath.toLowerCase().endsWith("jpeg")) {
                format = Bitmap.CompressFormat.JPEG;
            } else if (filePath.toLowerCase().endsWith("png")) {
                format = Bitmap.CompressFormat.PNG;
            } else {
                format = Bitmap.CompressFormat.JPEG;
            }
            bm.compress(format, 100, baos);
        } finally {
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bm;
    }

    /**
     * 计算inSampleSize值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // Calculate ratios of height and width to requested height and
            // width
            int heightRatio = Math.round((float) height / (float) reqHeight);
            int widthRatio = Math.round((float) width / (float) reqWidth);
            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
        }
        return inSampleSize;
    }

    /**
     * 加载图片的degree
     *
     * @param path
     * @return
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     *
     * @param bitmap
     * @param rotate
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
        if (bitmap == null)
            return null;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        // Setting post rotate to 90
        Matrix mtx = new Matrix();
        mtx.postRotate(rotate);
        try {
            return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
        } catch (OutOfMemoryError e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 回收ImageView上的图片
     *
     * @param imageView
     */
    public static void recyleImageViewBitmap(ImageView imageView) {
        if (imageView == null) {
            return;
        }
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        if (drawable != null && drawable.getBitmap() != null) {
            drawable.getBitmap().recycle();
        }
        imageView.setImageBitmap(null);
    }

    /**
     * view 转成 Bitmap
     * @return
     */
    public static Bitmap convertViewToBitmap(View view){
        if(view == null || view.getWidth() == 0 || view.getHeight() == 0){
            return null;
        }
        try {
            Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(Color.WHITE);
            view.draw(canvas);
            return bitmap;
        } catch (Exception e){
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * bitmap转成stream
     *
     * @param bitmap
     * @return
     */
    public static InputStream bitmapToStream(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        InputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        return isBm;
    }

    /**
     * 处理拍照图片 复制生成新图片路径
     */
    public static String copyTakePhoto() {
        try {
            File imgFile = new File(FileUtil.getDirectory(VKConstants.CACHE_IMG), ImageUtils.TAKEPHOTO_NAME);
            if (!imgFile.exists()) {
                return null;
            }
            File newImgFile = new File(FileUtil.getDirectory(VKConstants.CACHE_IMG), System.currentTimeMillis() + ".jpg");
            imgFile.renameTo(newImgFile);
            if (!newImgFile.exists()) {
                return null;
            }
            return newImgFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Uri addImageToGallery(Context context, String filepath, String title) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Audio.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, filepath);

        return context.getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);
    }

    /**
     *
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri, final boolean isKitKat) {
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // MediaStore (and general)
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * 将View转成bitmap保存到系统相册里
     *
     * @param activity
     * @param endCallback 保存结束回调    此回调运行在主线程
     */
    public static void saveBitmapToCameraByViewThread(final Activity activity, final Bitmap bitmap, final Runnable endCallback) {
        if (activity != null && bitmap != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String filepathString = "";
                    if (bitmap != null) {
                        filepathString = saveBitmapToCamera(bitmap);
                    }
                    if (activity != null) {
                        final String finalFilepathString = filepathString;
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                boolean isSuccess = false;
                                if (!TextUtils.isEmpty(finalFilepathString)) {
                                    File file = new File(finalFilepathString);
                                    if (file.exists()) {
                                        isSuccess = true;
                                    }
                                }
                                if (isSuccess) {
                                    if (activity != null) {
                                        activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(finalFilepathString))));
                                        ToastUtil.showCenter(activity, "保存到相册成功");
                                    }
                                } else {
                                    if (activity != null) {
                                        ToastUtil.showCenter(activity, "保存到相册失败");
                                    }
                                }
                                if (endCallback != null) {
                                    endCallback.run();
                                }
                            }
                        });
                    }else{
                        if (endCallback != null) {
                            endCallback.run();
                        }
                    }
                }
            }).start();
        }else{
            if (endCallback != null) {
                endCallback.run();
            }
        }
    }

    /**
     * 将View转成bitmap保存到系统相册里
     *
     * @param activity
     * @param view
     * @param endCallback 保存结束回调    此回调运行在主线程
     */
    public static void saveBitmapToCameraByViewThread(final Activity activity, final View view, final Runnable endCallback) {
        if (activity != null && view != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String filepathString = "";
                    Bitmap bitmap = convertViewToBitmap(view);
                    if (bitmap != null) {
                        filepathString = saveBitmapToCamera(bitmap);
                        bitmap.recycle();
                    }
                    if (activity != null) {
                        final String finalFilepathString = filepathString;
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                boolean isSuccess = false;
                                if (!TextUtils.isEmpty(finalFilepathString)) {
                                    File file = new File(finalFilepathString);
                                    if (file.exists()) {
                                        isSuccess = true;
                                    }
                                }
                                if (isSuccess) {
                                    if (activity != null) {
                                        activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(finalFilepathString))));
                                        ToastUtil.showCenter(activity, "保存到相册成功");
                                    }
                                } else {
                                    if (activity != null) {
                                        ToastUtil.showCenter(activity, "保存到相册失败");
                                    }
                                }
                                if (endCallback != null) {
                                    endCallback.run();
                                }
                            }
                        });
                    }
                }
            }).start();
        }
    }

    /**
     * 保存图片到系统相册
     * @param bitmap
     * @return
     */
    private static String saveBitmapToCamera(Bitmap bitmap) {
        final String SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath();
        final String SYSTEM_CAMERA_IMG = SDCARD+"/DCIM/Camera";
        File file_will = new File(FileUtil.getDirectory(SYSTEM_CAMERA_IMG), System.currentTimeMillis() + ".jpg");
        if(!file_will.exists()){
            try {
                file_will.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file_will);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError error){
            error.printStackTrace();
        }
        return file_will.getAbsolutePath();
    }

}
