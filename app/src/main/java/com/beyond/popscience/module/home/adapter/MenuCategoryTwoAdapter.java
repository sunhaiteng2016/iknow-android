package com.beyond.popscience.module.home.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.CustomRecyclerBaseAdapter;
import com.beyond.popscience.frame.pojo.NavObj;
import com.beyond.popscience.frame.pojo.ServiceCategory;
import com.beyond.popscience.frame.util.SPUtils;
import com.beyond.popscience.module.building.BuildingActivity;
import com.beyond.popscience.module.home.entity.MyMenu;
import com.beyond.popscience.module.home.entity.kepuMenu;
import com.beyond.popscience.module.home.shopcart.MoreMenuActivity;
import com.beyond.popscience.module.home.shopcart.NewFragmentActivity;
import com.beyond.popscience.module.job.JobActivity;
import com.beyond.popscience.module.mservice.GoodsListActivity;
import com.beyond.popscience.module.mservice.WebViewActivity;
import com.beyond.popscience.module.point.PonitShopActivity;
import com.beyond.popscience.module.square.SquareActivity;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yao.cui on 2017/6/23.
 */

public class MenuCategoryTwoAdapter extends CustomRecyclerBaseAdapter<kepuMenu> {


    public MenuCategoryTwoAdapter(Activity context) {
        super(context);
    }

    public MenuCategoryTwoAdapter(Fragment fragment) {
        super(fragment);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final int tabId = getDataList().get(position).getId();
        final String tabName = getDataList().get(position).getTabname();
        viewHolder.itemMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("更多".equals(tabName)) {
                    Intent intent = new Intent(context, MoreMenuActivity.class);
                    context.startActivity(intent);
                }
                //外链
                if (getDataList().get(position).getType() == 1) {
                    WebViewActivity.startActivity(context, getDataList().get(position).getTaburl(), getDataList().get(position).getTabname());
                }
                //文章菜单
                if (getDataList().get(position).getType()==2){
                    int classId = getDataList().get(position).getClassify();
                    NavObj obj  = new NavObj();
                    obj.setClassId(classId+"");
                    obj.setClassName(tabName);
                    Intent intent = new Intent(context, NewFragmentActivity.class);
                    intent.putExtra("name", "文章");
                    intent.putExtra("nav",obj);
                    context.startActivity(intent);
                }
                //主菜单
                if (getDataList().get(position).getType() == 3) {
                    if (3==tabId) {
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
                    if (6==tabId) {
                        SquareActivity.startActivity(context);
                    }
                    if (5==tabId) {
                        JobActivity.startActivity(context);
                    }
                    if (4==tabId) {
                        BuildingActivity.startActivity(context);
                    }
                    if (7==tabId) {
                        Intent intent  = new Intent(context,PonitShopActivity.class);
                        intent.putExtra("score", SPUtils.get(context, "score", "")+"");
                        context.startActivity(intent);
                    }
                }

            }
        });
        Glide.with(context).load(getDataList().get(position).getTabpic()).into(viewHolder.img);
        viewHolder.llDz.setVisibility(View.GONE);
        viewHolder.name.setText(getDataList().get(position).getTabname());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_menus, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img)
        ImageView img;
        @BindView(R.id.img_iv)
        ImageView imgIv;
        @BindView(R.id.ll_dz)
        LinearLayout llDz;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.item_menu)
        RelativeLayout itemMenu;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
