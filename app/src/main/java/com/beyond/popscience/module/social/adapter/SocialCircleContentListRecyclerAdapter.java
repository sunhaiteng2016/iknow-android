package com.beyond.popscience.module.social.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.CustomRecyclerBaseAdapter;
import com.beyond.popscience.frame.pojo.ArticleInfo;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.Util;
import com.beyond.popscience.module.home.MySocialActivity;
import com.beyond.popscience.module.news.ShowPhotoActivity;
import com.beyond.popscience.module.social.fragment.SocialCircleContentFragment;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by linjinfa on 2017/6/22.
 * email 331710168@qq.com
 */
public class SocialCircleContentListRecyclerAdapter extends CustomRecyclerBaseAdapter<ArticleInfo> {

    /**
     * 列数
     */
    private final int COLUMN_NUM = 3;
    /**
     * 总宽度
     */
    private int imageLayoutTotalWidth = 0;
    /**
     * 是否在详情中显示
     */
    private boolean isShowInDetail = false;
    /**
     * 是否在我的圈子显示
     */
    private boolean isShowInMySocial = false;

    public SocialCircleContentListRecyclerAdapter(Activity context) {
        super(context);
    }

    public SocialCircleContentListRecyclerAdapter(Fragment fragment) {
        super(fragment);
    }

    public boolean isShowInDetail() {
        return isShowInDetail;
    }

    public void setShowInDetail(boolean showInDetail) {
        isShowInDetail = showInDetail;
    }

    public boolean isShowInMySocial() {
        return isShowInMySocial;
    }

    public void setShowInMySocial(boolean showInMySocial) {
        isShowInMySocial = showInMySocial;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SocialCircleViewHolder(inflater.inflate(R.layout.adapter_social_circle_content_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClick(holder, position, getItemId(position));
                }
            }
        });

        ArticleInfo articleInfo = dataList.get(position);
        ((SocialCircleViewHolder)holder).setData(articleInfo);
    }

    class SocialCircleViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView avatarImgView;
        private TextView communityNameTxtView;
        private TextView authorTxtView;
        private TextView timeStampTxtView;
        private TextView praiseNumTxtView;
        private TextView commentContentTxtView;
        private TextView replyNumTxtView;
        private TextView delTxtView;
        private ImageView praiseImgView;
        private LinearLayout praiseLinLay;
        private LinearLayout picsLinLay;
        private LinearLayout normalOperLinLay;

        public SocialCircleViewHolder(View itemView) {
            super(itemView);
            avatarImgView = (CircleImageView) itemView.findViewById(R.id.avatarImgView);
            communityNameTxtView = (TextView) itemView.findViewById(R.id.communityNameTxtView);
            authorTxtView = (TextView) itemView.findViewById(R.id.authorTxtView);
            timeStampTxtView = (TextView) itemView.findViewById(R.id.timeStampTxtView);
            praiseNumTxtView = (TextView) itemView.findViewById(R.id.praiseNumTxtView);
            commentContentTxtView = (TextView) itemView.findViewById(R.id.commentContentTxtView);
            replyNumTxtView = (TextView) itemView.findViewById(R.id.replyNumTxtView);
            delTxtView = (TextView) itemView.findViewById(R.id.delTxtView);
            praiseImgView = (ImageView) itemView.findViewById(R.id.praiseImgView);
            praiseLinLay = (LinearLayout) itemView.findViewById(R.id.praiseLinLay);
            picsLinLay = (LinearLayout) itemView.findViewById(R.id.picsLinLay);
            normalOperLinLay = (LinearLayout) itemView.findViewById(R.id.normalOperLinLay);

            PraiseClick praiseClick = new PraiseClick();
            praiseLinLay.setOnClickListener(praiseClick);
            itemView.setTag(praiseLinLay.getId(), praiseClick);

            if(isShowInDetail){
                normalOperLinLay.setVisibility(View.GONE);
            }else{
                normalOperLinLay.setVisibility(View.VISIBLE);
            }
        }

        /**
         *
         * @param articleInfo
         */
        public void setData(final ArticleInfo articleInfo){
            delTxtView.setVisibility(isShowInMySocial ? View.VISIBLE : View.GONE);
            delTxtView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(context instanceof MySocialActivity){
                        ((MySocialActivity)context).requestDeleteArticle(articleInfo);
                    }
                }
            });


            PraiseClick praiseClick = (PraiseClick) itemView.getTag(praiseLinLay.getId());
            praiseClick.set(articleInfo);

            if(!TextUtils.isEmpty(articleInfo.getCommunityName())){
                communityNameTxtView.setVisibility(View.VISIBLE);
                communityNameTxtView.setText(articleInfo.getCommunityName());
            }else{
                communityNameTxtView.setVisibility(View.GONE);
            }

            ImageLoaderUtil.displayImage(context, articleInfo.getAvatar(), avatarImgView, getAvatarDisplayImageOptions());
            authorTxtView.setText(articleInfo.getNickName());

            praiseImgView.setImageResource(articleInfo.isPraised() ? R.drawable.icon_like_red : R.drawable.icon_like);

            praiseNumTxtView.setText(articleInfo.getPraiseNum());
            replyNumTxtView.setText(articleInfo.getReplyNum());
            timeStampTxtView.setText(Util.getDisplayDateTime(BeyondApplication.getInstance().getCurrSystemTime(), articleInfo.getPublishTime()));

            commentContentTxtView.setText(articleInfo.getContent());

            if(imageLayoutTotalWidth == 0){
                picsLinLay.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        picsLinLay.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        imageLayoutTotalWidth = picsLinLay.getMeasuredWidth();

                        addImageViewLayout(articleInfo);
                    }
                });
            }else{
                addImageViewLayout(articleInfo);
            }
        }

        /**
         * 添加image layout
         * @param articleInfo
         */
        private void addImageViewLayout(ArticleInfo articleInfo){
//            String tests[] = {
//                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498202848605&di=53ff51cb7bfac60595f52ec3534a56f7&imgtype=0&src=http%3A%2F%2Fimg.tupianzj.com%2Fuploads%2Fallimg%2F160309%2F9-16030Z92137.jpg",
//                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498202848605&di=53ff51cb7bfac60595f52ec3534a56f7&imgtype=0&src=http%3A%2F%2Fimg.tupianzj.com%2Fuploads%2Fallimg%2F160309%2F9-16030Z92137.jpg",
//                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498202894397&di=d30aa219a4325fdc5870eaeee14f0902&imgtype=0&src=http%3A%2F%2Fwww.pp3.cn%2Fuploads%2F201504%2F2015041508.jpg",
//                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498202894397&di=d30aa219a4325fdc5870eaeee14f0902&imgtype=0&src=http%3A%2F%2Fwww.pp3.cn%2Fuploads%2F201504%2F2015041508.jpg",
//                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498202848605&di=53ff51cb7bfac60595f52ec3534a56f7&imgtype=0&src=http%3A%2F%2Fimg.tupianzj.com%2Fuploads%2Fallimg%2F160309%2F9-16030Z92137.jpg",
//                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498202848605&di=53ff51cb7bfac60595f52ec3534a56f7&imgtype=0&src=http%3A%2F%2Fimg.tupianzj.com%2Fuploads%2Fallimg%2F160309%2F9-16030Z92137.jpg",
//                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498202894397&di=d30aa219a4325fdc5870eaeee14f0902&imgtype=0&src=http%3A%2F%2Fwww.pp3.cn%2Fuploads%2F201504%2F2015041508.jpg",
//                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498202894397&di=d30aa219a4325fdc5870eaeee14f0902&imgtype=0&src=http%3A%2F%2Fwww.pp3.cn%2Fuploads%2F201504%2F2015041508.jpg"
//            };
//            List<String> picList = new ArrayList<String>(Arrays.asList(tests));

            picsLinLay.setVisibility(View.GONE);

            final List<String> picList = articleInfo.getDetailPicList();
            if(picList!=null && picList.size()!=0){
                //间距
                GradientDrawable dividerDrawable = new GradientDrawable();
                dividerDrawable.setSize(context.getResources().getDimensionPixelSize(R.dimen.dp5), 0);
                dividerDrawable.setStroke(0, Color.TRANSPARENT);

                int count = picList.size();
                int rowCount = (int) Math.ceil(count * 1f / COLUMN_NUM);
                for(int rowIndex = 0; rowIndex<rowCount; rowIndex++){
                    int fromIndex = rowIndex * COLUMN_NUM;
                    int toIndex = fromIndex + COLUMN_NUM;
                    if (fromIndex < count) {
                        if (toIndex >= count) {
                            toIndex = count;
                        }
                        List<String> rowPicList = picList.subList(fromIndex, toIndex);
                        if (rowPicList == null || rowPicList.size() == 0) {
                            continue;
                        }
                        //补全 COLUMN_NUM
                        int rowPicCount = rowPicList.size();
                        if(rowPicCount < COLUMN_NUM){
                            for(int i =0; i<(COLUMN_NUM - rowPicCount); i++){
                                rowPicList.add("");
                            }
                        }

                        LinearLayout linearLayout = (LinearLayout) picsLinLay.getChildAt(rowIndex);
                        if(linearLayout == null){
                            linearLayout = new LinearLayout(context);
                            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.topMargin = context.getResources().getDimensionPixelSize(R.dimen.dp5);
                            picsLinLay.addView(linearLayout, layoutParams);
                        }else{
                            linearLayout.setVisibility(View.VISIBLE);
                        }

                        if(linearLayout.getDividerDrawable() == null){
                            linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
                            linearLayout.setDividerDrawable(dividerDrawable);
                        }

                        int imgWidthHeight = imageLayoutTotalWidth / COLUMN_NUM;
                        rowPicCount = rowPicList.size();
                        for(int picIndex = 0; picIndex<rowPicCount; picIndex++){
                            String picUrl = rowPicList.get(picIndex);
                            ImageView imageView = (ImageView) linearLayout.getChildAt(picIndex);
                            if(imageView == null){
                                imageView = new ImageView(context);
                                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                LinearLayout.LayoutParams imgLp = new LinearLayout.LayoutParams(0, imgWidthHeight, 1);
                                linearLayout.addView(imageView, imgLp);
                            }else{
                                LinearLayout.LayoutParams imgLp = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                                if(imgLp.height != imgWidthHeight){
                                    imgLp.height = imgWidthHeight;
                                    imageView.setLayoutParams(imgLp);
                                }
                            }

                            imageView.setImageBitmap(null);
                            imageView.setOnClickListener(null);
                            imageView.setTag(String.valueOf((rowIndex * COLUMN_NUM + picIndex)));

                            if(!TextUtils.isEmpty(picUrl)){
                                ImageLoaderUtil.displayImage(context, picUrl, imageView, getDisplayImageOptions());

                                imageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ArrayList<String> showPicList = new ArrayList<String>();
                                        for(String str : picList){
                                            if(!TextUtils.isEmpty(str)){
                                                showPicList.add(str);
                                            }
                                        }
                                        ShowPhotoActivity.startActivity(context, showPicList, Integer.parseInt(v.getTag().toString()));
                                    }
                                });
                            }
                        }
                    }
                }
                for(int childIndex = rowCount; childIndex<picsLinLay.getChildCount(); childIndex++){
                    View childView = picsLinLay.getChildAt(childIndex);
                    if(childView!=null){
                        childView.setVisibility(View.GONE);
                    }
                }
                picsLinLay.setVisibility(View.VISIBLE);
            }
        }

        class PraiseClick implements View.OnClickListener{

            private ArticleInfo articleInfo;

            /**
             *
             * @param articleInfo
             */
            public void set(ArticleInfo articleInfo) {
                this.articleInfo = articleInfo;
            }

            @Override
            public void onClick(View v) {
                if(fragment!=null){
                    ((SocialCircleContentFragment)fragment).requestPraise(articleInfo);
                }
            }
        }

    }
}
