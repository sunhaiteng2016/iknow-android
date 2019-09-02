package com.beyond.popscience.module.building.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.CustomBaseAdapter;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.Util;
import com.beyond.popscience.module.news.ShowPhotoActivity;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by linjinfa on 2017/10/14.
 * email 331710168@qq.com
 */
public class BuildDetailImgListAdapter extends CustomBaseAdapter<String> {

    public BuildDetailImgListAdapter(Activity context) {
        super(context);
    }

    public BuildDetailImgListAdapter(Fragment fragment) {
        super(fragment);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();

            convertView = inflater.inflate(R.layout.adapter_build_detail_img_list_item, parent, false);

            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imgView);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final String url = dataList.get(position);
        if(TextUtils.isEmpty(url)){
            viewHolder.imageView.setVisibility(View.GONE);
        }else{
            viewHolder.imageView.setVisibility(View.VISIBLE);
            final ViewHolder finalViewHolder = viewHolder;
            ImageLoaderUtil.loadImage(context, url, getDisplayImageOptions(), new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    if(loadedImage!=null && loadedImage.getWidth()!=0 && loadedImage.getHeight()!=0){
                        int screenWidth = Util.getScreenWidth(context);
                        int height = (int) (screenWidth / (loadedImage.getWidth() *1f / loadedImage.getHeight()));
                        ViewGroup.LayoutParams layoutParams = finalViewHolder.imageView.getLayoutParams();
                        if(layoutParams.height != height){
                            layoutParams.height = height;
                            finalViewHolder.imageView.setLayoutParams(layoutParams);
                        }
                        finalViewHolder.imageView.setImageBitmap(loadedImage);
                    }
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowPhotoActivity.startActivity(context, url);
                }
            });
        }

        return convertView;
    }

    class ViewHolder{
        ImageView imageView;
    }

}
