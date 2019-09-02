package com.beyond.popscience.module.mservice.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.CustomBaseAdapter;
import com.beyond.popscience.frame.pojo.GoodsDescImgObj;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.Util;
import com.beyond.popscience.module.news.ShowPhotoActivity;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by linjinfa on 2017/10/11.
 * email 331710168@qq.com
 */
public class GoodsDetailAdapter extends CustomBaseAdapter<GoodsDescImgObj> {

    public GoodsDetailAdapter(Activity context) {
        super(context);
    }

    public GoodsDetailAdapter(Fragment fragment) {
        super(fragment);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();

            convertView = inflater.inflate(R.layout.adapter_goods_detail_item, parent, false);

            viewHolder.descTxtView = (TextView) convertView.findViewById(R.id.descTxtView);
            viewHolder.picImgView = (ImageView) convertView.findViewById(R.id.picImgView);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final GoodsDescImgObj goodsDescImgObj = dataList.get(position);

        viewHolder.descTxtView.setText(goodsDescImgObj.getGoodsDesc());
        if(TextUtils.isEmpty(goodsDescImgObj.getGoodsDescImg())){
            viewHolder.picImgView.setVisibility(View.GONE);
        }else{
            viewHolder.picImgView.setVisibility(View.VISIBLE);
            final ViewHolder finalViewHolder = viewHolder;
            ImageLoaderUtil.loadImage(context, goodsDescImgObj.getGoodsDescImg(), getDisplayImageOptions(), new ImageLoadingListener() {
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
                        ViewGroup.LayoutParams layoutParams = finalViewHolder.picImgView.getLayoutParams();
                        if(layoutParams.height != height){
                            layoutParams.height = height;
                            finalViewHolder.picImgView.setLayoutParams(layoutParams);
                        }
                        finalViewHolder.picImgView.setImageBitmap(loadedImage);
                    }
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
//        ImageLoaderUtil.displayImage(context, goodsDescImgObj.getGoodsDescImg(), viewHolder.picImgView, getDisplayImageOptions());
        }

        viewHolder.picImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPhotoActivity.startActivity(context, goodsDescImgObj.getGoodsDescImg());
            }
        });
        return convertView;
    }

    class ViewHolder{
        ImageView picImgView;
        TextView descTxtView;
    }

}
