package com.beyond.popscience.module.home.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.CustomRecyclerBaseAdapter;
import com.beyond.popscience.frame.pojo.MoreService;
import com.beyond.popscience.frame.pojo.MoreServiceContent;
import com.beyond.popscience.frame.pojo.ServiceCategory;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.module.mservice.C2CListActivity;
import com.beyond.popscience.module.mservice.MoreServiceActivity;
import com.beyond.popscience.module.mservice.WebViewActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yao.cui on 2017/6/23.
 */

public class MoreServiceCategoryAdapter extends CustomRecyclerBaseAdapter<MoreServiceContent> {
    public MoreServiceCategoryAdapter(Activity context) {
        super(context);
    }

    public MoreServiceCategoryAdapter(Fragment fragment) {
        super(fragment);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MoreServiceContent category = getDataList().get(position);

        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.llCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.startActivity(context,category.getSite(),category.getTitle());
            }
        });
        ImageLoaderUtil.displayImage(context,category.getPic(),viewHolder.cvHeader,getDisplayImageOptions());
        viewHolder.tvName.setText(category.getTitle());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_category_service,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cvHeader)
        CircleImageView cvHeader;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.llCategory)
        LinearLayout llCategory;

        public ViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }

    }
}
