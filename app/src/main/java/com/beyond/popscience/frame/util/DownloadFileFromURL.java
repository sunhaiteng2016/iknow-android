package com.beyond.popscience.frame.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by linjinfa on 2017/6/16.
 * email 331710168@qq.com
 */
public class DownloadFileFromURL extends AsyncTask<String, String, String> {

    private ProgressDialog pDialog;
    private Activity activity;

    public DownloadFileFromURL(Activity activity) {
        this.activity = activity;
    }

    /**
     * Before starting background thread Show Progress Bar Dialog
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (pDialog == null) {
            pDialog = new ProgressDialog(activity);
            pDialog.setTitle("更新");
            pDialog.setMessage("下载中...");
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

            pDialog.setCanceledOnTouchOutside(false);
            pDialog.setCancelable(false);
        }
        pDialog.show();
    }

    /**
     * Downloading file in background thread
     */
    @Override
    protected String doInBackground(String... f_url) {
        int count;
        try {
            URL url = new URL(f_url[0]);
            URLConnection conection = url.openConnection();
            conection.connect();

            // this will be useful so that you can show a tipical 0-100%
            // progress bar
            int lenghtOfFile = conection.getContentLength();

            // download the file
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            String filepath = VKConstants.CACHE_DATA;
            File file = new File(filepath);
            if (!file.exists()) {
                file.mkdirs();
            }
            filepath = filepath + "update.apk";
            file = new File(filepath);
            if (!file.exists())
                file.createNewFile();
            // Output stream
            OutputStream output = new FileOutputStream(filepath);
            byte data[] = new byte[1024];
            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();
            // closing streams
            output.close();
            input.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Updating progress bar
     */
    protected void onProgressUpdate(String... progress) {
        // setting progress percentage
        int position = Integer.parseInt(progress[0]);
        if (position == 100) {
            if (pDialog != null) {
                pDialog.dismiss();
            }
            String path = VKConstants.CACHE_DATA + "update.apk";
            Intent intent = new Intent();
            Uri data;
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // 判断版本大于等于7.0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                data = FileProvider.getUriForFile(activity, "com.gymj.apk.xj.fileProvider", new File(path));
                // 给目标应用一个临时授权
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                data = Uri.fromFile(new File(path));
            }
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(data, "application/vnd.android.package-archive");
            activity.startActivity(intent);
            return;
        }
        if (pDialog != null) {
            pDialog.setProgress(position);
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }
}
