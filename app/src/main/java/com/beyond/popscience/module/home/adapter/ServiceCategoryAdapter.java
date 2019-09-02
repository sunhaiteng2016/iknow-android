package com.beyond.popscience.module.home.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.CustomRecyclerBaseAdapter;
import com.beyond.popscience.frame.pojo.ServiceCategory;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.module.building.BuildingActivity;
import com.beyond.popscience.module.job.JobActivity;
import com.beyond.popscience.module.mservice.C2CListActivity;
import com.beyond.popscience.module.mservice.GoodsListActivity;
import com.beyond.popscience.module.mservice.MoreServiceActivity;
import com.beyond.popscience.module.mservice.WebViewActivity;
import com.beyond.popscience.module.square.SquareActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yao.cui on 2017/6/23.
 */

public class ServiceCategoryAdapter extends CustomRecyclerBaseAdapter<ServiceCategory> {
    public ServiceCategoryAdapter(Activity context) {
        super(context);
    }

    public ServiceCategoryAdapter(Fragment fragment) {
        super(fragment);
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ServiceCategory category = getDataList().get(position);

        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.llCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("sht",category.toString());
                if("5".equals(category.getTabId())){    //商品买卖
                    GoodsListActivity.startActivity(context, category);
                    return ;
                }
                if("10".equals(category.getTabId())){   //技能/任务广场
                    SquareActivity.startActivity(context);
                    Log.e("sht",category.toString());
                    return;
                }
                if("6".equals(category.getTabId())){   //房屋租售
                    BuildingActivity.startActivity(context);
                    return;
                }
                if("7".equals(category.getTabId())){  //求职招聘
                    JobActivity.startActivity(context);
                    return;
                }
                if (TextUtils.equals("1",category.getTabType())){//外链
                    WebViewActivity.startActivity(context, category.getTabUrl(), category.getTabName());
                }
                if(TextUtils.equals("2",category.getTabType())){//c2c.
                    C2CListActivity.startActivity(context, category.getTabName(), category.getTabId());
                }
                if (TextUtils.equals("4",category.getTabType())){ //更多
                    //TODO 跳转进页面 "更多"
                    MoreServiceActivity.startActivity(context,"服务");
                }

            }
        });
        ImageLoaderUtil.displayImage(context, category.getTabPic(), viewHolder.cvHeader, getDisplayImageOptions());
        viewHolder.tvName.setText(category.getTabName());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_category_service, parent, false);
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

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
