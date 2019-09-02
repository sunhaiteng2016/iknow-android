package com.beyond.popscience.module.news;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.VideoDownloader;
import com.beyond.popscience.frame.view.VideoPlayerController;
import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.NiceVideoPlayerManager;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 视频播放
 * Created by linjinfa on 2017/6/26.
 * email 331710168@qq.com
 */
public class VideoPlayerActivity extends BaseActivity {

    private final int REQUEST_PERMISSION = 101;

    private static final String KEY_THUMB_URL = "thumb_url";
    private static final String KEY_VIDEO_URL = "video_url";

    @BindView(R.id.videoPlayer)
    protected NiceVideoPlayer videoPlayer;
    @BindView(R.id.rl)
    RelativeLayout rl;
    /**
     *
     */
    private String mVideoUrl;
    /**
     *
     */
    private ProgressDialog progressDialog;

    /**
     * @param context
     */
    public static void startActivity(Context context, String thumbUrl, String videoUrl) {
        Intent intent = new Intent(context, VideoPlayerActivity.class);
        intent.putExtra(KEY_THUMB_URL, thumbUrl);
        intent.putExtra(KEY_VIDEO_URL, videoUrl);
        context.startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION) {
            if ((grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                //允许
            } else {
                //拒绝
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_video_player;
    }

    @Override
    public void initUI() {
        super.initUI();

        iniProgressDialog();

//        mVideoUrl = "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-33-30.mp4";
        mVideoUrl = getIntent().getStringExtra(KEY_VIDEO_URL);
        videoPlayer.setUp(mVideoUrl, null);

        // 默认的控制器，如需自定义，可继承NiceVideoPlayer自己实现
        VideoPlayerController controller = new VideoPlayerController(this);
        controller.setUrl(mVideoUrl);

        controller.setOnDownloadListener(new VideoDownloader.OnDownloadListener() {
            @Override
            public void onDownloadStart() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (progressDialog != null) {
                            if (!progressDialog.isShowing()) {
                                progressDialog.show();
                            }
                        }
                    }
                });
            }

            @Override
            public void onDownloadSuccess(String url, String filePath) {
                dismissDownloadProgressDialog();

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(new File(filePath));
                mediaScanIntent.setData(contentUri);
                BeyondApplication.getInstance().sendBroadcast(mediaScanIntent);
            }

            @Override
            public void onDownloading(final int progress) {
                try {
                    if (!VideoPlayerActivity.this.isFinishing()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (progressDialog != null) {
                                    if (progress == 100) {
                                        dismissDownloadProgressDialog();
                                    } else {
                                        progressDialog.setProgress(progress);
                                    }
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDownloadFailed() {
                dismissDownloadProgressDialog();
            }
        });
        controller.setTitle("");

//        String imgUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498459274060&di=6f3d68a2db35c8f21b5783fd6dee19dd&imgtype=0&src=http%3A%2F%2Fwww.zhlzw.com%2FUploadFiles%2FArticle_UploadFiles%2F201204%2F20120412123929822.jpg";
        String imgUrl = getIntent().getStringExtra(KEY_THUMB_URL);
        controller.imageView().setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageLoaderUtil.displayImage(this, imgUrl, controller.imageView());

        videoPlayer.setController(controller);
        NiceVideoPlayerManager.instance().setCurrentNiceVideoPlayer(videoPlayer);

        videoPlayer.start();
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 初始化 PorgressDialog
     */
    private void iniProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("下载中...");
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }

    /**
     *
     */
    private void dismissDownloadProgressDialog() {
        try {
            if (!isFinishing()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (NiceVideoPlayerManager.instance().onBackPressd()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        NiceVideoPlayerManager.instance().pauseNiceVideoPlayer();
        super.onStop();
    }

    @Override
    protected void onRestart() {
        NiceVideoPlayerManager.instance().restartNiceVideoPlayer();
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        // 很重要，在Activity和Fragment的onStop方法中一定要调用，释放的播放器。
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
        progressDialog.dismiss();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
