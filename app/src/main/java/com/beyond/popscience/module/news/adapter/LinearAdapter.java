package com.beyond.popscience.module.news.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beyond.library.util.DensityUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.pojo.NewsDetailContent;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.SPUtils;
import com.beyond.popscience.frame.util.VKConstants;
import com.beyond.popscience.frame.util.VideoDownloader;
import com.beyond.popscience.module.news.ShowPhotoActivity;
import com.beyond.popscience.module.news.VideoPlayerActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class LinearAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<NewsDetailContent> list;
    private Context mContext;
    private String videoThumbUrl;
    private String videoName;
    private int txtSize;

    public LinearAdapter(Context context, List<NewsDetailContent> list) {
        mContext = context;
        this.list = list;
        init();
    }

    public void setThumbUrl(String url) {
        this.videoThumbUrl = url;
    }

    public void setVideoName(String name) {
        this.videoName = name;
    }

    public void setTxtSize(int txtSize) {
        this.txtSize = txtSize;
        notifyDataSetChanged();
    }

    private void init() {
        txtSize = (int) SPUtils.get(mContext, "txtSize", DensityUtil.sp2px(mContext, 17));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_news_detail_item, parent, false);
            return new PhotoHolder(view);
        } else if (viewType == 1) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_news_detail_txt_item, parent, false);
            return new TextHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_news_detail_item_video, parent, false);
            return new VideoHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof PhotoHolder) {
            final PhotoHolder photoHolder = (PhotoHolder) holder;
            Glide.with(mContext).load(list.get(position).getContentNews()).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    float width = resource.getWidth();
                    float height = resource.getHeight();
                    float sreenWigth = DensityUtil.getScreenWidth(mContext);
                    float bl = sreenWigth / width;
                    ViewGroup.LayoutParams layoutParams = photoHolder.picImgage.getLayoutParams();
                    layoutParams.height = (int) (bl * height);
                    photoHolder.picImgage.setLayoutParams(layoutParams);
                    ImageLoaderUtil.displayImageFresco(list.get(position).getContentNews(), photoHolder.picImgage);
                    photoHolder.picImgage.setOnClickListener(new ShowImageClick(list.get(position)));
                }
            });
        }
        if (holder instanceof TextHolder) {
            TextHolder textHolder = (TextHolder) holder;
            textHolder.contextTxtView.setText(list.get(position).getContentNews() != null ? Html.fromHtml("     "+list.get(position).getContentNews()) : "");
            textHolder.contextTxtView.setTextSize(TypedValue.COMPLEX_UNIT_PX, txtSize);
        }
        if (holder instanceof VideoHolder) {
            final VideoHolder videoHolder = (VideoHolder) holder;
            Glide.with(mContext).load(videoThumbUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    float width = resource.getWidth();
                    float height = resource.getHeight();
                    float sreenWigth = DensityUtil.getScreenWidth(mContext);
                    float bl = sreenWigth / width;
                    ViewGroup.LayoutParams layoutParams = videoHolder.vodeoImgView.getLayoutParams();
                    layoutParams.height = (int) (bl * height);
                    videoHolder.vodeoImgView.setLayoutParams(layoutParams);
                    ImageLoaderUtil.displayImageFresco(videoThumbUrl, videoHolder.vodeoImgView);
                    videoHolder.vodeoImgView.setOnClickListener(new ShowImageClick(list.get(position)));
                    ((VideoHolder) videoHolder).tv_video_download.setOnClickListener(new DownloadVideoClick(list.get(position)));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * 图片布局
     */
    static class PhotoHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView picImgage;

        PhotoHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View item) {
            picImgage = (SimpleDraweeView) item.findViewById(R.id.picImgView);
        }
    }

    /**
     * 视频布局
     */
    static class VideoHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView vodeoImgView;
        TextView tv_video_download;

        VideoHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View item) {
            vodeoImgView = (SimpleDraweeView) item.findViewById(R.id.vodeoImgView);
            tv_video_download = (TextView) item.findViewById(R.id.tv_video_download);

        }
    }

    /**
     * 文字布局
     */
    static class TextHolder extends RecyclerView.ViewHolder {
        TextView contextTxtView;

        TextHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View item) {
            contextTxtView = (TextView) item.findViewById(R.id.contextTxtView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        NewsDetailContent newsDetailContent = getItem(position);
        if (newsDetailContent == null) return 0;

        if ("1".equals(newsDetailContent.getContentType())) { //0:图片1:文字2:视频
            return 1;
        } else if ("2".equals(newsDetailContent.getContentType())) {
            return 2;
        }
        return 0;
    }

    public NewsDetailContent getItem(int position) {
        if (position < 0 || position >= list.size()) {
            return null;
        }
        return list.get(position);
    }

    class ShowImageClick implements View.OnClickListener {

        private NewsDetailContent newsDetailContent;


        public ShowImageClick(NewsDetailContent newsDetailContent) {
            this.newsDetailContent = newsDetailContent;
        }

        @Override
        public void onClick(View v) {
            if (newsDetailContent != null && !TextUtils.isEmpty(newsDetailContent.getContentNews())) {
                if (v.getId() == R.id.picImgView) {
                    ShowPhotoActivity.startActivity(mContext, newsDetailContent.getContentNews());
                } else if (v.getId() == R.id.vodeoImgView) {
                    VideoPlayerActivity.startActivity(mContext, videoThumbUrl, newsDetailContent.getContentNews());
                }
            }
        }

    }

    class DownloadVideoClick implements View.OnClickListener {
        private NewsDetailContent newsDetailContent;


        public DownloadVideoClick(NewsDetailContent newsDetailContent) {
            this.newsDetailContent = newsDetailContent;
        }

        @Override
        public void onClick(View v) {
            final String videoURL = newsDetailContent.getContentNews();

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("提示");
            builder.setMessage("本次下载将会消耗流量\n请在Wi-Fi条件下载");
            builder.setCancelable(false);

            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    requestVideo(videoURL, videoName + ".mp4");
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

    }

    private void requestVideo(String videoURL, final String videoName) {

        VideoDownloader.get().download(videoURL, videoName, VKConstants.VIDEO_DOWNLOAD_PATH, new VideoDownloader.OnDownloadListener() {
            @Override
            public void onDownloadStart() {

            }

            @Override
            public void onDownloadSuccess(String url, String filePath) {
                Activity activity = (Activity) mContext;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("提示");
                        builder.setMessage("\"" + videoName + "\"" + "\n已下载完成" +
                                "\n存放地址为\"sd卡根目录\\IKnow\\Videos\\\"" +
                                "\n请使用系统播放器播放");
                        builder.setCancelable(false);
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create();
                        builder.show();

                    }
                });

            }

            @Override
            public void onDownloading(int progress) {

            }

            @Override
            public void onDownloadFailed() {
                Activity activity = (Activity) mContext;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("提示");
                        builder.setMessage(videoName + "视频下载失败");
                        builder.setCancelable(false);
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create();
                        builder.show();
                    }
                });
            }
        });
    }
}
