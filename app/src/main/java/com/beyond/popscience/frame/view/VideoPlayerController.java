package com.beyond.popscience.frame.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.util.FileUtil;
import com.beyond.popscience.frame.util.VKConstants;
import com.beyond.popscience.frame.util.VideoDownloader;
import com.xiao.nicevideoplayer.NiceUtil;
import com.xiao.nicevideoplayer.NiceVideoPlayerController;

/**
 * Created by linjinfa on 2017/6/26.
 * email 331710168@qq.com
 */
public class VideoPlayerController extends NiceVideoPlayerController implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private Context mContext;
    private ImageView mImage;
    private ImageView mCenterStart;
    private LinearLayout mTop;
    private ImageView mBack;
    private TextView mTitle;
    private TextView downloadTxtView;
    private LinearLayout mBottom;
    private ImageView mRestartPause;
    private TextView mPosition;
    private TextView mDuration;
    private SeekBar mSeek;
    private ImageView mFullScreen;
    private LinearLayout mLoading;
    private TextView mLoadText;
    private LinearLayout mChangePositon;
    private TextView mChangePositionCurrent;
    private ProgressBar mChangePositionProgress;
    private LinearLayout mChangeBrightness;
    private ProgressBar mChangeBrightnessProgress;
    private LinearLayout mChangeVolume;
    private ProgressBar mChangeVolumeProgress;
    private LinearLayout mError;
    private TextView mRetry;
    private LinearLayout mCompleted;
    private TextView mReplay;
    private TextView mShare;
    private boolean topBottomVisible;
    private CountDownTimer mDismissTopBottomCountDownTimer;
    /**
     *
     */
    private OnPlayStateChangedListener onPlayStateChangedListener;
    /**
     *
     */
    private String url;
    /**
     *
     */
    private VideoDownloader.OnDownloadListener onDownloadListener;

    public VideoPlayerController(@NonNull Context context) {
        super(context);
        this.mContext = context;
        this.init();
    }

    private void init() {
        LayoutInflater.from(this.mContext).inflate(R.layout.video_palyer_controller, this, true);
        this.mCenterStart = (ImageView)this.findViewById(R.id.center_start);
        this.mImage = (ImageView)this.findViewById(R.id.image);
        this.mTop = (LinearLayout)this.findViewById(R.id.top);
        this.mBack = (ImageView)this.findViewById(R.id.back);
        this.mTitle = (TextView)this.findViewById(R.id.title);
        this.downloadTxtView = (TextView)this.findViewById(R.id.downloadTxtView);
        this.mBottom = (LinearLayout)this.findViewById(R.id.bottom);
        this.mRestartPause = (ImageView)this.findViewById(R.id.restart_or_pause);
        this.mPosition = (TextView)this.findViewById(R.id.position);
        this.mDuration = (TextView)this.findViewById(R.id.duration);
        this.mSeek = (SeekBar)this.findViewById(R.id.seek);
        this.mFullScreen = (ImageView)this.findViewById(R.id.full_screen);
        this.mLoading = (LinearLayout)this.findViewById(R.id.loading);
        this.mLoadText = (TextView)this.findViewById(R.id.load_text);
        this.mChangePositon = (LinearLayout)this.findViewById(R.id.change_position);
        this.mChangePositionCurrent = (TextView)this.findViewById(R.id.change_position_current);
        this.mChangePositionProgress = (ProgressBar)this.findViewById(R.id.change_position_progress);
        this.mChangeBrightness = (LinearLayout)this.findViewById(R.id.change_brightness);
        this.mChangeBrightnessProgress = (ProgressBar)this.findViewById(R.id.change_brightness_progress);
        this.mChangeVolume = (LinearLayout)this.findViewById(R.id.change_volume);
        this.mChangeVolumeProgress = (ProgressBar)this.findViewById(R.id.change_volume_progress);
        this.mError = (LinearLayout)this.findViewById(R.id.error);
        this.mRetry = (TextView)this.findViewById(R.id.retry);
        this.mCompleted = (LinearLayout)this.findViewById(R.id.completed);
        this.mReplay = (TextView)this.findViewById(R.id.replay);
        this.mShare = (TextView)this.findViewById(R.id.share);
        this.mShare.setVisibility(View.GONE);
        this.mCenterStart.setOnClickListener(this);
        this.mBack.setOnClickListener(this);
        this.mRestartPause.setOnClickListener(this);
        this.mFullScreen.setOnClickListener(this);
        this.mRetry.setOnClickListener(this);
        this.mReplay.setOnClickListener(this);
        this.downloadTxtView.setOnClickListener(this);
        this.mShare.setOnClickListener(this);
        this.mSeek.setOnSeekBarChangeListener(this);
        this.setOnClickListener(this);
    }

    public void setTitle(String title) {
        this.mTitle.setText(title);
    }

    public ImageView imageView() {
        return this.mImage;
    }

    public void setImage(@DrawableRes int resId) {
        this.mImage.setImageResource(resId);
    }

    protected void onPlayStateChanged(int playState) {
        switch(playState) {
            case -1:
                this.cancelUpdateProgressTimer();
                this.setTopBottomVisible(false);
                this.mTop.setVisibility(View.VISIBLE);
                this.mError.setVisibility(View.VISIBLE);
            case 0:
            default:
                break;
            case 1:
                this.mImage.setVisibility(View.GONE);
                this.mLoading.setVisibility(View.VISIBLE);
                this.mLoadText.setText("正在准备...");
                this.mError.setVisibility(View.GONE);
                this.mCompleted.setVisibility(View.GONE);
                this.mTop.setVisibility(View.GONE);
                this.mCenterStart.setVisibility(View.GONE);
                break;
            case 2:
                this.startUpdateProgressTimer();
                break;
            case 3:
                this.mLoading.setVisibility(View.GONE);
                this.mRestartPause.setImageResource(com.xiao.nicevideoplayer.R.drawable.ic_player_pause);
                this.startDismissTopBottomTimer();
                break;
            case 4:
                this.mLoading.setVisibility(View.GONE);
                this.mRestartPause.setImageResource(com.xiao.nicevideoplayer.R.drawable.ic_player_start);
                this.cancelDismissTopBottomTimer();
                break;
            case 5:
                this.mLoading.setVisibility(View.GONE);
                this.mRestartPause.setImageResource(com.xiao.nicevideoplayer.R.drawable.ic_player_pause);
                this.mLoadText.setText("正在缓冲...");
                this.startDismissTopBottomTimer();
                break;
            case 6:
                this.mLoading.setVisibility(View.VISIBLE);
                this.mRestartPause.setImageResource(com.xiao.nicevideoplayer.R.drawable.ic_player_start);
                this.mLoadText.setText("正在缓冲...");
                this.cancelDismissTopBottomTimer();
                break;
            case 7:
                this.cancelUpdateProgressTimer();
                this.setTopBottomVisible(false);
                this.mImage.setVisibility(View.VISIBLE);
                this.mCompleted.setVisibility(View.VISIBLE);
        }
        //
        if(onPlayStateChangedListener!=null){
            onPlayStateChangedListener.OnPlayStateChanged(playState);
        }
    }

    protected void onPlayerStateChanged(int playerState) {
        if(getContext() instanceof AppCompatActivity){
            ActionBar actionBar = ((AppCompatActivity)getContext()).getSupportActionBar();
            if(actionBar!=null){
                actionBar.hide();
            }
        }
        switch(playerState) {
            case 10:
                this.mBack.setVisibility(View.VISIBLE);
                this.mFullScreen.setImageResource(com.xiao.nicevideoplayer.R.drawable.ic_player_enlarge);
                break;
            case 11:
                this.mBack.setVisibility(View.VISIBLE);
                this.mFullScreen.setImageResource(com.xiao.nicevideoplayer.R.drawable.ic_player_shrink);
                break;
            case 12:
                this.mBack.setVisibility(View.VISIBLE);
        }

    }

    protected void reset() {
        this.topBottomVisible = false;
        this.cancelUpdateProgressTimer();
        this.cancelDismissTopBottomTimer();
        this.mSeek.setProgress(0);
        this.mSeek.setSecondaryProgress(0);
        this.mCenterStart.setVisibility(View.VISIBLE);
        this.mImage.setVisibility(View.VISIBLE);
        this.mBottom.setVisibility(View.GONE);
        this.mFullScreen.setImageResource(com.xiao.nicevideoplayer.R.drawable.ic_player_enlarge);
        this.mTop.setVisibility(View.VISIBLE);
        this.mBack.setVisibility(View.VISIBLE);
        this.mLoading.setVisibility(View.GONE);
        this.mError.setVisibility(View.GONE);
        this.mCompleted.setVisibility(View.GONE);
    }

    /**
     * 下载
     */
    private void downloadClick(){
        if(TextUtils.isEmpty(url) || !checkPermission()){
            return ;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("提示");
        builder.setMessage("本次下载将会消耗流量\n请在Wi-Fi条件下载");
        builder.setCancelable(false);

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                final String fileName = String.valueOf(url.hashCode()) + "." + FileUtil.getExtensionName(url);
                VideoDownloader.get().download(url, fileName, VKConstants.SYSTEM_CAMERA_IMG_VIDEO, onDownloadListener);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    /**
     *
     */
    private boolean checkPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){ //>=6.0检查运行时权限
            int grantCode = PermissionChecker.checkSelfPermission(mContext, "android.permission.WRITE_EXTERNAL_STORAGE");
            if(grantCode != PackageManager.PERMISSION_GRANTED){ //未授权
                if(mContext instanceof Activity){
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 101);
                }
                return false;
            }
        }
        return true;
    }

    public void onClick(View v) {
        if(v == this.downloadTxtView){  //下载
            downloadClick();
        }else if(v == this.mCenterStart) {
            if(this.mNiceVideoPlayer.isIdle()) {
                this.mNiceVideoPlayer.start();
            }
        } else if(v == this.mBack) {
            if(this.mNiceVideoPlayer.isFullScreen()) {
                this.mNiceVideoPlayer.exitFullScreen();
            } else if(this.mNiceVideoPlayer.isTinyWindow()) {
                this.mNiceVideoPlayer.exitTinyWindow();
            }else{
                if(getContext() instanceof AppCompatActivity){
                    ((AppCompatActivity)getContext()).finish();
                }
            }
        } else if(v == this.mRestartPause) {
            if(!this.mNiceVideoPlayer.isPlaying() && !this.mNiceVideoPlayer.isBufferingPlaying()) {
                if(this.mNiceVideoPlayer.isPaused() || this.mNiceVideoPlayer.isBufferingPaused()) {
                    this.mNiceVideoPlayer.restart();
                }
            } else {
                this.mNiceVideoPlayer.pause();
            }
        } else if(v == this.mFullScreen) {
            if(!this.mNiceVideoPlayer.isNormal() && !this.mNiceVideoPlayer.isTinyWindow()) {
                if(this.mNiceVideoPlayer.isFullScreen()) {
                    this.mNiceVideoPlayer.exitFullScreen();
                }
            } else {
                this.mNiceVideoPlayer.enterFullScreen();
            }
        } else if(v == this.mRetry) {
            this.mNiceVideoPlayer.restart();
        } else if(v == this.mReplay) {
            this.mRetry.performClick();
        } else if(v == this.mShare) {

        } else if(v == this && (this.mNiceVideoPlayer.isPlaying() || this.mNiceVideoPlayer.isPaused() || this.mNiceVideoPlayer.isBufferingPlaying() || this.mNiceVideoPlayer.isBufferingPaused())) {
            this.setTopBottomVisible(!this.topBottomVisible);
        }

    }

    private void setTopBottomVisible(boolean visible) {
        this.mTop.setVisibility(visible?View.VISIBLE:View.GONE);
        this.mBottom.setVisibility(visible?View.VISIBLE:View.GONE);
        this.topBottomVisible = visible;
        if(visible) {
            if(!this.mNiceVideoPlayer.isPaused() && !this.mNiceVideoPlayer.isBufferingPaused()) {
                this.startDismissTopBottomTimer();
            }
        } else {
            this.cancelDismissTopBottomTimer();
        }

    }

    private void startDismissTopBottomTimer() {
        this.cancelDismissTopBottomTimer();
        if(this.mDismissTopBottomCountDownTimer == null) {
            this.mDismissTopBottomCountDownTimer = new CountDownTimer(8000L, 1L) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    VideoPlayerController.this.setTopBottomVisible(false);
                }
            };
        }

        this.mDismissTopBottomCountDownTimer.start();
    }

    private void cancelDismissTopBottomTimer() {
        if(this.mDismissTopBottomCountDownTimer != null) {
            this.mDismissTopBottomCountDownTimer.cancel();
        }

    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        if(this.mNiceVideoPlayer.isBufferingPaused() || this.mNiceVideoPlayer.isPaused()) {
            this.mNiceVideoPlayer.restart();
        }

        long position = (long)((float)(this.mNiceVideoPlayer.getDuration() * (long)seekBar.getProgress()) / 100.0F);
        this.mNiceVideoPlayer.seekTo(position);
        this.startDismissTopBottomTimer();
    }

    protected void updateProgress() {
        long position = this.mNiceVideoPlayer.getCurrentPosition();
        long duration = this.mNiceVideoPlayer.getDuration();
        int bufferPercentage = this.mNiceVideoPlayer.getBufferPercentage();
        this.mSeek.setSecondaryProgress(bufferPercentage);
        int progress = (int)(100.0F * (float)position / (float)duration);
        this.mSeek.setProgress(progress);
        this.mPosition.setText(NiceUtil.formatTime(position));
        this.mDuration.setText(NiceUtil.formatTime(duration));
    }

    protected void showChangePosition(long duration, int newPositionProgress) {
        this.mChangePositon.setVisibility(View.VISIBLE);
        long newPosition = (long)((float)(duration * (long)newPositionProgress) / 100.0F);
        this.mChangePositionCurrent.setText(NiceUtil.formatTime(newPosition));
        this.mChangePositionProgress.setProgress(newPositionProgress);
        this.mSeek.setProgress(newPositionProgress);
        this.mPosition.setText(NiceUtil.formatTime(newPosition));
    }

    protected void hideChangePosition() {
        this.mChangePositon.setVisibility(View.GONE);
    }

    protected void showChangeVolume(int newVolumeProgress) {
        this.mChangeVolume.setVisibility(View.VISIBLE);
        this.mChangeVolumeProgress.setProgress(newVolumeProgress);
    }

    protected void hideChangeVolume() {
        this.mChangeVolume.setVisibility(View.GONE);
    }

    protected void showChangeBrightness(int newBrightnessProgress) {
        this.mChangeBrightness.setVisibility(View.VISIBLE);
        this.mChangeBrightnessProgress.setProgress(newBrightnessProgress);
    }

    protected void hideChangeBrightness() {
        this.mChangeBrightness.setVisibility(View.GONE);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public VideoDownloader.OnDownloadListener getOnDownloadListener() {
        return onDownloadListener;
    }

    public void setOnDownloadListener(VideoDownloader.OnDownloadListener onDownloadListener) {
        this.onDownloadListener = onDownloadListener;
    }

    /**
     *
     * @return
     */
    public OnPlayStateChangedListener getOnPlayStateChangedListener() {
        return onPlayStateChangedListener;
    }

    /**
     *
     * @param onPlayStateChangedListener
     */
    public void setOnPlayStateChangedListener(OnPlayStateChangedListener onPlayStateChangedListener) {
        this.onPlayStateChangedListener = onPlayStateChangedListener;
    }

    /**
     *
     */
    public interface OnPlayStateChangedListener{
        void OnPlayStateChanged(int playState);
    }


}
