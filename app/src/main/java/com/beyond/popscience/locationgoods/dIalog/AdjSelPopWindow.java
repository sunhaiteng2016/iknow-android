package com.beyond.popscience.locationgoods.dIalog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.beyond.popscience.R;
import com.beyond.popscience.module.home.entity.Address;
import com.beyond.popscience.utils.sun.util.OpenFileUtil;

import java.io.File;

/**
 * Created by yao.cui on 2017/6/10.
 */

public class AdjSelPopWindow extends PopupWindow implements View.OnClickListener {

    private final String path;
    private Context mContext;


    private IAddressChangeListener mListener;


    private View mview;
    private LinearLayout tvBdyd, tv_fxzf;

    public AdjSelPopWindow(Context context, String path) {
        super(context);
        this.path = path;
        init(context);
    }


    private void init(Context context) {
        this.mContext = context;
        View rootView = LayoutInflater.from(context).inflate(R.layout.adj_sel, null);
        tvBdyd = rootView.findViewById(R.id.tv_bdyd);
        tv_fxzf = rootView.findViewById(R.id.tv_fxzf);
        tvBdyd.setOnClickListener(this);
        tv_fxzf.setOnClickListener(this);
        this.setContentView(rootView);
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setBackgroundDrawable(new ColorDrawable(0));
        setAnimationStyle(R.style.popwindow_anim_style);

        setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
                lp.alpha = 1f;
                ((Activity) mContext).getWindow().setAttributes(lp);
            }
        });
    }

    public void show(View parent) {
        mview = (View) parent;
        this.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = 0.7f;
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_bdyd:
                Intent intent = OpenFileUtil.openFile(path, mContext.getApplicationContext());
                mContext.startActivity(intent);
                break;
            case R.id.tv_fxzf:
                try {
                    if (fileIsExists(path)) {
                        File file = new File(path);
                        String houziss = getFileNameWithSuffix(path);
                        String[] houzizz = houziss.split("\\.");
                        String houzi = houzizz[1];
                        Intent email = new Intent(Intent.ACTION_SEND);
                        if (houzi.equals("JPG") || houzi.equals("jpg") || houzi.equals("png") || houzi.equals("PNG") || houzi.equals("JPEG") || houzi.equals("jpeg")) {
                            email.setType("image/jpeg");
                        } else {
                            email.setType("application/octet-stream");
                        }
                        email.putExtra(Intent.EXTRA_EMAIL, "");
                        email.putExtra(Intent.EXTRA_SUBJECT, "");
                        email.putExtra(Intent.EXTRA_TEXT, "");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Uri contentUri = FileProvider.getUriForFile(mContext, "com.gymj.apk.xj.fileProvider", file);
                            email.putExtra(Intent.EXTRA_STREAM, contentUri);
                            mContext.startActivity(Intent.createChooser(email, "分享一下"));
                        } else {
                            Uri sss = Uri.fromFile(file);
                            email.putExtra(Intent.EXTRA_STREAM, sss);
                            mContext.startActivity(Intent.createChooser(email, "分享一下"));
                        }
                    }
                } catch (Exception e) {

                }

                break;
        }
    }

    public static boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     * 获取文件名及后缀
     */
    public String getFileNameWithSuffix(String path) {
        if (TextUtils.isEmpty(path)) {
            return "";
        }
        int start = path.lastIndexOf("/");
        if (start != -1) {
            return path.substring(start + 1);
        } else {
            return "";
        }
    }

    public void setAddressChangeListener(IAddressChangeListener listener) {
        this.mListener = listener;
    }


    public interface IAddressChangeListener {

        void onAddressChange(Address city, Address zone);

    }

}
