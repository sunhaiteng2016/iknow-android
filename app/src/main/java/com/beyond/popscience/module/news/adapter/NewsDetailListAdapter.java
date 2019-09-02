package com.beyond.popscience.module.news.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.util.DensityUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.CustomBaseAdapter;
import com.beyond.popscience.frame.net.VideoRestUsage;
import com.beyond.popscience.frame.pojo.NewsDetailContent;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.SPUtils;
import com.beyond.popscience.frame.util.Util;
import com.beyond.popscience.frame.util.VKConstants;
import com.beyond.popscience.frame.util.VideoDownloader;
import com.beyond.popscience.module.news.ShowPhotoActivity;
import com.beyond.popscience.module.news.VideoPlayerActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.facebook.drawee.view.SimpleDraweeView;


/**
 * Created by linjinfa on 2017/6/9.
 * email 331710168@qq.com
 */
public class NewsDetailListAdapter extends CustomBaseAdapter<NewsDetailContent> {

    private int txtSize;
    private String videoThumbUrl;
    private String videoName;
    private final int TASK_GET_VIDEO = 9001;
    @Request
    private VideoRestUsage videoRestUsage;

    public NewsDetailListAdapter(Fragment fragment) {
        super(fragment);
        init();
    }

    public NewsDetailListAdapter(Activity context) {
        super(context);
        init();
    }

    private void init() {
        txtSize = (int) SPUtils.get(context, "txtSize", DensityUtil.sp2px(context, 17));
    }

    /**
     * 色绘制视频缩略图url
     *
     * @param url
     */
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

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        ShowImageClick showImageClick = null;
        DownloadVideoClick downloadVideoClick = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            showImageClick = new ShowImageClick();
            downloadVideoClick = new DownloadVideoClick();
            switch (getItemViewType(position)) {
                case 0: //图片
                    convertView = inflater.inflate(R.layout.adapter_news_detail_item, parent, false);
                    viewHolder.picImgView = (SimpleDraweeView) convertView.findViewById(R.id.picImgView);
                    final ViewGroup.LayoutParams layoutParams = viewHolder.picImgView.getLayoutParams();
                    final ViewHolder finalViewHolder = viewHolder;
                    final ShowImageClick finalShowImageClick = showImageClick;
                    final View finalConvertView = convertView;
                    //先压缩了
                    Glide.with(context).load(dataList.get(position).getContentNews()).asBitmap().into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            float width = resource.getWidth();
                            float height = resource.getHeight();
                            float sreenWigth = DensityUtil.getScreenWidth(context);
                            float bl = sreenWigth / width;
                            layoutParams.height = (int) (bl * height);
                            finalViewHolder.picImgView.setLayoutParams(layoutParams);
                            finalViewHolder.picImgView.setOnClickListener(finalShowImageClick);
                            finalConvertView.setTag(finalViewHolder.picImgView.getId(), finalShowImageClick);

                        }
                    });

                    break;
                case 1: //文字
                    convertView = inflater.inflate(R.layout.adapter_news_detail_txt_item, parent, false);
                    viewHolder.contextTxtView = (TextView) convertView.findViewById(R.id.contextTxtView);
                    break;
                case 2:
                    convertView = inflater.inflate(R.layout.adapter_news_detail_item_video, parent, false);
                    viewHolder.videoImgView = (SimpleDraweeView) convertView.findViewById(R.id.vodeoImgView);
                    ViewGroup.LayoutParams params = viewHolder.videoImgView.getLayoutParams();
                    params.height = Util.getImageHeight(DensityUtil.getScreenWidth(context));
                    viewHolder.videoImgView.setLayoutParams(params);
                    viewHolder.videoImgView.setOnClickListener(showImageClick);
                    viewHolder.tvDownloadVideo = (TextView) convertView.findViewById(R.id.tv_video_download);
                    viewHolder.tvDownloadVideo.setOnClickListener(downloadVideoClick);
                    convertView.setTag(viewHolder.videoImgView.getId(), showImageClick);
                    convertView.setTag(viewHolder.tvDownloadVideo.getId(), downloadVideoClick);
                    break;

                default:

                    break;
            }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

            if (viewHolder.picImgView != null) {
                showImageClick = (ShowImageClick) convertView.getTag(viewHolder.picImgView.getId());
            }
            if (viewHolder.videoImgView != null) {
                showImageClick = (ShowImageClick) convertView.getTag(viewHolder.videoImgView.getId());
            }
        }

        NewsDetailContent newsDetailContent = dataList.get(position);
        switch (getItemViewType(position)) {
            case 0: //图片
//                ImageLoaderUtil.displayImage(context, newsDetailContent.getContentNews(), viewHolder.picImgView, getDisplayImageOptions());
                //  ImageLoaderUtil.display(context, newsDetailContent.getContentNews(), viewHolder.picImgView);
                ImageLoaderUtil.displayImageFresco(newsDetailContent.getContentNews(), viewHolder.picImgView);
                if (showImageClick != null) {
                    showImageClick.set(newsDetailContent);
                }
                break;
            case 1: //文字
                viewHolder.contextTxtView.setText(newsDetailContent.getContentNews() != null ? Html.fromHtml(newsDetailContent.getContentNews()) : "");
                viewHolder.contextTxtView.setTextSize(TypedValue.COMPLEX_UNIT_PX, txtSize);
                break;
            case 2:
//                ImageLoaderUtil.displayImage(context, videoThumbUrl, viewHolder.videoImgView, getDisplayImageOptions());
                ImageLoaderUtil.displayImageFresco(videoThumbUrl, viewHolder.videoImgView);
                if (showImageClick != null) {
                    showImageClick.set(newsDetailContent);
                }
                if (downloadVideoClick != null) {
                    downloadVideoClick.set(newsDetailContent);
                }
                break;
            default:

                break;
        }

        return convertView;
    }

    class ViewHolder {
        TextView contextTxtView;
        SimpleDraweeView picImgView;
        SimpleDraweeView videoImgView;
        TextView tvDownloadVideo;
    }

    class ShowImageClick implements View.OnClickListener {

        private NewsDetailContent newsDetailContent;

        /**
         * @param newsDetailContent
         */
        public void set(NewsDetailContent newsDetailContent) {
            this.newsDetailContent = newsDetailContent;
        }

        @Override
        public void onClick(View v) {
            if (newsDetailContent != null && !TextUtils.isEmpty(newsDetailContent.getContentNews())) {
                if (v.getId() == R.id.picImgView) {
                    ShowPhotoActivity.startActivity(context, newsDetailContent.getContentNews());
                } else if (v.getId() == R.id.vodeoImgView) {
                    VideoPlayerActivity.startActivity(context, videoThumbUrl, newsDetailContent.getContentNews());
                }

//
            }
        }

    }

    class DownloadVideoClick implements View.OnClickListener {
        private NewsDetailContent newsDetailContent;

        /**
         * @param newsDetailContent
         */
        public void set(NewsDetailContent newsDetailContent) {
            this.newsDetailContent = newsDetailContent;
        }

        @Override
        public void onClick(View v) {
            final String videoURL = newsDetailContent.getContentNews();

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
