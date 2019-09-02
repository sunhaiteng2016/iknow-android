package com.beyond.popscience.module.social.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.CustomRecyclerBaseAdapter;
import com.beyond.popscience.frame.pojo.ImageInfo;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.VKConstants;
import com.beyond.popscience.module.social.PublishedActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linjinfa on 2017/7/21.
 * email 331710168@qq.com
 */
public class ImageAdapter extends CustomRecyclerBaseAdapter<ImageInfo> {

    /**
     *
     */
    private ImageInfo EMPTY_ADD_IMAGE_DATA = new ImageInfo(R.drawable.wdspk_icon_2_2);

    public ImageAdapter(Activity context) {
        super(context);
        init();
    }

    public ImageAdapter(Fragment fragment) {
        super(fragment);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        appendAddImgData();
    }

    /**
     * 获取所有有效的ImageInfo
     *
     * @return
     */
    public List<ImageInfo> getImageInfoList() {
        List<ImageInfo> imageInfoList = new ArrayList<>();
        for (ImageInfo imageInfo : dataList) {
            if (!TextUtils.isEmpty(imageInfo.getImgUr())) {
                imageInfoList.add(imageInfo);
            }
        }
        return imageInfoList;
    }

    /**
     * 获取所有有效的 image path url
     *
     * @return
     */
    public List<String> getImageUrlList() {
        List<String> imageUrlList = new ArrayList<>();
        for (ImageInfo imageInfo : dataList) {
            if (!TextUtils.isEmpty(imageInfo.getImgUr()) && (imageInfo.getImgUr().toLowerCase().startsWith(VKConstants.HTTP_PROTOCOL_PREFIX) || imageInfo.getImgUr().toLowerCase().startsWith(VKConstants.HTTPS_PROTOCOL_PREFIX))) {
                imageUrlList.add(imageInfo.getImgUr());
            }
        }
        return imageUrlList;
    }

    /**
     * 获取所有有效的 image path url
     *
     * @return
     */
    public List<String> getImagePathList() {
        List<String> imageUrlList = new ArrayList<>();
        for (ImageInfo imageInfo : dataList) {
            if (!TextUtils.isEmpty(imageInfo.getImgUr()) && !(imageInfo.getImgUr().toLowerCase().startsWith(VKConstants.HTTP_PROTOCOL_PREFIX) || imageInfo.getImgUr().toLowerCase().startsWith(VKConstants.HTTPS_PROTOCOL_PREFIX))) {
                imageUrlList.add(imageInfo.getImgUr());
            }
        }
        return imageUrlList;
    }

    /**
     * 追加图片
     *
     * @param imageInfo
     */
    public void appendImage(ImageInfo imageInfo) {
        if (imageInfo != null) {
            if (dataList.size() > 0) {
                dataList.add(dataList.size() - 1, imageInfo);
            } else {
                dataList.add(imageInfo);
            }
        }
    }

    /**
     * 添加图片类型
     */
    private void appendAddImgData() {
        dataList.add(EMPTY_ADD_IMAGE_DATA);
    }


    /**
     * 删除添加图片类型
     */
    public void delAddImgData() {
        dataList.remove(EMPTY_ADD_IMAGE_DATA);
    }

    /**
     * 获取图片个数
     *
     * @return
     */
    public int getImageCount() {
        int count = 0;
        for (ImageInfo imageInfo : dataList) {
            if (!TextUtils.isEmpty(imageInfo.getImgUr())) {
                count++;
            }
        }
        return count;
    }

    /**
     * 是否包含 添加图片类型 数据
     */
    private boolean isContainAddImgData() {
        int count = dataList.size();
        if (count <= 0) {
            return false;
        }
        for (int i = count - 1; i >= 0; i--) {
            ImageInfo imageInfo = dataList.get(i);
            if (imageInfo == EMPTY_ADD_IMAGE_DATA) {
                return true;
            }
        }
        return false;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(inflater.inflate(R.layout.adapter_image, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ImageInfo imageInfo = dataList.get(position);

        ((ImageViewHolder) holder).setData(imageInfo);
    }

    /**
     *
     */
    class ImageViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgView;
        private ImageView delImgView;

        public ImageViewHolder(View itemView) {
            super(itemView);

            imgView = (ImageView) itemView.findViewById(R.id.imgView);
            delImgView = (ImageView) itemView.findViewById(R.id.delImgView);
        }

        /**
         * @param imageInfo
         */
        public void setData(final ImageInfo imageInfo) {
            delImgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dataList.remove(imageInfo);
                    if (!isContainAddImgData()) {
                        appendAddImgData();
                    }
                    notifyDataSetChanged();
                }
            });
            if (imageInfo.getResId() > 0) {
                imgView.setImageResource(imageInfo.getResId());
                delImgView.setVisibility(View.GONE);
                imgView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (context instanceof PublishedActivity) {
                            ((PublishedActivity) context).startSelectImageActivity();
                        }
                    }
                });
            } else {
                imgView.setOnClickListener(null);
                if (TextUtils.isEmpty(imageInfo.getImgUr())) {
                    imgView.setImageBitmap(null);
                } else {
                    String url = imageInfo.getImgUr();
                    DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
                    if (url.toString().toLowerCase().startsWith(VKConstants.HTTP_PROTOCOL_PREFIX) || url.toString().toLowerCase().startsWith(VKConstants.HTTPS_PROTOCOL_PREFIX)) {
                        builder.cacheOnDisk(true);
                    } else {
                        url = VKConstants.FILE_PROTOCOL_PREFIX + (url.toString().startsWith("/") ? (url.toString().replaceFirst("/", "")) : url.toString());
                    }
                    DisplayImageOptions options = builder.displayer(new FadeInBitmapDisplayer(200, true, true, false)).showImageOnLoading(R.drawable.common_transparent).showImageForEmptyUri(R.drawable.common_transparent).build();
                    ImageLoaderUtil.displayImage(context, url, imgView, options);
                }

                delImgView.setVisibility(View.VISIBLE);
            }
        }

    }

}
