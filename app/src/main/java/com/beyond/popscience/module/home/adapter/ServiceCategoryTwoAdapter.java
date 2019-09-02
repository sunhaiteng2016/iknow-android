package com.beyond.popscience.module.home.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.CustomRecyclerBaseAdapter;
import com.beyond.popscience.frame.pojo.NavObj;
import com.beyond.popscience.frame.pojo.ServiceCategory;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.SPUtils;
import com.beyond.popscience.locationgoods.ZczxActivity;
import com.beyond.popscience.module.building.BuildingActivity;
import com.beyond.popscience.module.home.entity.kepuMenu;
import com.beyond.popscience.module.home.shopcart.MoreMenuActivity;
import com.beyond.popscience.module.home.shopcart.NewFragmentActivity;
import com.beyond.popscience.module.job.JobActivity;
import com.beyond.popscience.module.mservice.C2CListActivity;
import com.beyond.popscience.module.mservice.GoodsListActivity;
import com.beyond.popscience.module.mservice.MoreServiceActivity;
import com.beyond.popscience.module.mservice.WebViewActivity;
import com.beyond.popscience.module.point.PonitShopActivity;
import com.beyond.popscience.module.square.SquareActivity;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yao.cui on 2017/6/23.
 */

public class ServiceCategoryTwoAdapter extends CustomRecyclerBaseAdapter<kepuMenu> {
    public ServiceCategoryTwoAdapter(Activity context) {
        super(context);
    }

    public ServiceCategoryTwoAdapter(Fragment fragment) {
        super(fragment);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final kepuMenu category = getDataList().get(position);

        ViewHolder viewHolder = (ViewHolder) holder;
        final int tabId = category.getId();

        viewHolder.llCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getDataList().get(position).getType() == 1) {
                    WebViewActivity.startActivity(context, getDataList().get(position).getTaburl(), getDataList().get(position).getTabname());
                }
                if (getDataList().get(position).getType() == 3) {
                    if (category.getTabname().equals("自产自销")){
                        Intent intent = new Intent(context,ZczxActivity.class);
                        context.startActivity(intent);
                    }
                    if (3 == tabId) {
                        Intent intent = new Intent(context, NewFragmentActivity.class);
                        intent.putExtra("name", "乡镇");
                        context.startActivity(intent);
                    }
                    if (2 == tabId) {
                        Intent intent = new Intent(context, NewFragmentActivity.class);
                        intent.putExtra("name", "社团");
                        context.startActivity(intent);
                    }
                    if (1 == tabId) {
                        ServiceCategory category = new ServiceCategory();
                        category.setTabId("5");
                        category.setTabType("2");
                        category.setTabName("商品买卖");
                        category.setTabPic("'http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/7d1c6754e74941fa8d3e45208e8032c1.jpg");
                        GoodsListActivity.startActivity(context, category);
                    }
                    if (6 == tabId) {
                        SquareActivity.startActivity(context);
                    }
                    if (7 == tabId) {
                        Intent intent = new Intent(context, PonitShopActivity.class);
                        intent.putExtra("score", SPUtils.get(context, "score", "") + "");
                        context.startActivity(intent);
                    }
                    if (5 == tabId) {
                        JobActivity.startActivity(context);
                    }
                    if (4 == tabId) {
                        BuildingActivity.startActivity(context);
                    }
                }

            }
        });
        Glide.with(context).load(category.getTabpic()).into(viewHolder.cvHeader);
        viewHolder.tvName.setText(category.getTabname());
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
        ImageView cvHeader;
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
