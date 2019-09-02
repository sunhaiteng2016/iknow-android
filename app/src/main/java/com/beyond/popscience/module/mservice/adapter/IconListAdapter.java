package com.beyond.popscience.module.mservice.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.CustomBaseAdapter;
import com.beyond.popscience.frame.pojo.IconInfo;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.ImagePickActivity;

/**
 * Created by linjinfa on 2017/10/13.
 * email 331710168@qq.com
 */
public class IconListAdapter extends CustomBaseAdapter<IconInfo> {

    /**
     *
     */
    private IconInfo selectedIconInfo;

    public IconListAdapter(Activity context) {
        super(context);
    }

    public IconListAdapter(Fragment fragment) {
        super(fragment);
    }

    /**
     *
     * @return
     */
    public IconInfo getSelectedIconInfo() {
        return selectedIconInfo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();

            convertView = inflater.inflate(R.layout.adapter_icon_list_item, parent, false);

            viewHolder.imgReLay = (RelativeLayout) convertView.findViewById(R.id.imgReLay);
            viewHolder.iconImgView = (ImageView) convertView.findViewById(R.id.iconImgView);
            viewHolder.nameTxtView = (TextView) convertView.findViewById(R.id.nameTxtView);
            viewHolder.retryUploadTxtView = (TextView) convertView.findViewById(R.id.retryUploadTxtView);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        IconInfo iconInfo = dataList.get(position);

        viewHolder.nameTxtView.setText(iconInfo.getName());

        if(TextUtils.isEmpty(iconInfo.getIconUrl())){
            viewHolder.iconImgView.setImageResource(R.drawable.icon_add_pic);
            viewHolder.retryUploadTxtView.setVisibility(View.GONE);
        }else{
            viewHolder.retryUploadTxtView.setVisibility(View.VISIBLE);
            ImageLoaderUtil.displayImage(context, iconInfo.getIconUrl(), viewHolder.iconImgView);

        }

        viewHolder.imgReLay.setTag(iconInfo);
        viewHolder.imgReLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedIconInfo = (IconInfo) v.getTag();
                startSelectImageActivity();
            }
        });
        return convertView;
    }

    /**
     * 选择图片
     */
    private void startSelectImageActivity() {
        Intent intent1 = new Intent(context, ImagePickActivity.class);
        intent1.putExtra("IsNeedCamera", true);
        intent1.putExtra(Constant.MAX_NUMBER, 1);
        context.startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);
    }

    class ViewHolder{
        RelativeLayout imgReLay;
        ImageView iconImgView;
        TextView nameTxtView;
        TextView retryUploadTxtView;
    }

}
