package com.beyond.popscience.frame.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.beyond.popscience.R;


public class D {

    /**
     * 加载loading  带文案的
     *
     * @param context
     * @param message
     * @return
     */
    public static Dialog getProgressDialog(Activity context, String message, boolean isCancelable) {
        if (context == null || context.isFinishing()) {
            return null;
        }

        Dialog dialog = new Dialog(context, R.style.loadingDialog);
        dialog.setCancelable(isCancelable);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_mxd_loading_layout);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        final ImageView refreshImgView = (ImageView) dialog.findViewById(R.id.refreshImgView);
        TextView messageTxtView = (TextView) dialog.findViewById(R.id.dialogMessageTxtView);
        if (!TextUtils.isEmpty(message)) {
            messageTxtView.setText(message);
            messageTxtView.setVisibility(View.VISIBLE);
        } else {
            messageTxtView.setVisibility(View.GONE);
        }

        RotateAnimation mRotateAnimation = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateAnimation.setInterpolator(new LinearInterpolator());
        mRotateAnimation.setDuration(1200 * 2);
        mRotateAnimation.setRepeatCount(Animation.INFINITE);
        mRotateAnimation.setRepeatMode(Animation.RESTART);
        refreshImgView.startAnimation(mRotateAnimation);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                refreshImgView.clearAnimation();
            }
        });
        return dialog;
    }

    /**
     * @param context
     * @param title
     * @param message
     * @param left
     * @param right
     * @param listener
     */
    public static AlertDialog show(Context context,
                                   String title,
                                   String message,
                                   String left,
                                   String right,
                                   DialogInterface.OnClickListener listener) {
        if (context == null) {
            return null;
        }
        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            return null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }
        if (!TextUtils.isEmpty(left)) {
            builder.setNegativeButton(left, listener);
        }
        if (!TextUtils.isEmpty(right)) {
            builder.setPositiveButton(right, listener);
        }
        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            return null;
        }
        return builder.show();
    }

    /**
     * @param context
     * @param title
     * @param message
     * @param left
     * @param right
     * @param listener
     */
    public static AlertDialog show(Context context, boolean isCancel,
                                   String title,
                                   String message,
                                   String left,
                                   String right,
                                   DialogInterface.OnClickListener listener) {
        if (context == null) {
            return null;
        }
        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            return null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(isCancel);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }
        if (!TextUtils.isEmpty(left)) {
            builder.setNegativeButton(left, listener);
        }
        if (!TextUtils.isEmpty(right)) {
            builder.setPositiveButton(right, listener);
        }
        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            return null;
        }
        AlertDialog alertDialog = builder.show();
        alertDialog.setCanceledOnTouchOutside(isCancel);
        return alertDialog;
    }

    /**
     * @param context
     * @param title
     * @param items
     * @param listener
     */
    public static void show(Context context, String title, String[] items, DialogInterface.OnClickListener listener) {
        show(context, title, items, false, listener);
    }

    /**
     * @param context
     * @param title
     * @param items
     * @param listener
     */
    public static void show(Context context, String title, String[] items, boolean cancelable, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(cancelable);
        builder.setTitle(title);
        builder.setItems(items, listener);
        AlertDialog alertDialog = builder.show();
        alertDialog.setCanceledOnTouchOutside(cancelable);
    }

}