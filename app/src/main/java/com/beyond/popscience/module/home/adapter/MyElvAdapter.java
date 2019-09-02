package com.beyond.popscience.module.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.pojo.NavObj;
import com.beyond.popscience.frame.pojo.ServiceCategory;
import com.beyond.popscience.frame.util.SPUtils;
import com.beyond.popscience.module.building.BuildingActivity;
import com.beyond.popscience.module.home.entity.Menus;
import com.beyond.popscience.module.home.shopcart.MoreMenuActivity;
import com.beyond.popscience.module.home.shopcart.NewFragmentActivity;
import com.beyond.popscience.module.job.JobActivity;
import com.beyond.popscience.module.mservice.GoodsListActivity;
import com.beyond.popscience.module.mservice.WebViewActivity;
import com.beyond.popscience.module.point.PonitShopActivity;
import com.beyond.popscience.module.square.SquareActivity;
import com.beyond.popscience.view.MyGridView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class MyElvAdapter extends BaseExpandableListAdapter {

    private final Context context;
    List<Menus.ResultListBean> resultList;
    public MyElvAdapter(Context context, List<Menus.ResultListBean> resultList) {
        this.resultList = resultList;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return resultList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return i;
    }

    @Override
    public Object getChild(int i, int i1) {
        return i1;
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        GroupViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_name, null);
            holder = new GroupViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (GroupViewHolder) view.getTag();
        }
        holder.name.setText(resultList.get(i).getName());
        return view;

    }

    @Override
    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
        ChildViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_gridview, null);
            holder = new ChildViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ChildViewHolder) view.getTag();
        }
        final MoreMenusTwoAdapter  menusTwoAdapter= new MoreMenusTwoAdapter(resultList.get(i).getTabList(),context);
        holder.name.setAdapter(menusTwoAdapter);
        final List<Menus.ResultListBean.TabListBean> childList = resultList.get(i).getTabList();
        menusTwoAdapter.setOnJianClickLintener(new MoreMenusTwoAdapter.OnJianCliclLintener() {
            @Override
            public void Clicks(View view, int position) {
                //这个要看看当前是什么元素   还要加到上边的数据源里面
                Menus.ResultListBean.TabListBean tab = resultList.get(i).getTabList().get(position);
                Menus.UserAllTabBean bean = new Menus.UserAllTabBean();
                bean.setTabname(tab.getTabname());
                bean.setTabpic(tab.getTabpic());
                bean.setCategory(tab.getCategory());
                bean.setType(tab.getType());
                bean.setId(tab.getId());
                bean.setClassify(tab.getClassify());
                //通知 我的应用 改变
                EventBus.getDefault().post(bean);
                resultList.get(i).getTabList().remove(position);
                menusTwoAdapter.notifyDataSetChanged();

            }
        });

        holder.name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onClicksss(childList,i);
            }
        });
        return view;
    }

    class GroupViewHolder {
        public TextView name;

        public GroupViewHolder(View view) {
            name = (TextView) view.findViewById(R.id.name);
        }
    }

    class ChildViewHolder {
        public MyGridView name;

        public ChildViewHolder(View view) {
            name = (MyGridView) view.findViewById(R.id.myGridView);
        }
    }
    private void onClicksss(List<Menus.ResultListBean.TabListBean> wntjList, int position) {

        final int tabId = wntjList.get(position).getId();
        final String tabName = wntjList.get(position).getTabname();
        //外链
        if (wntjList.get(position).getType() == 1) {
            WebViewActivity.startActivity(context, wntjList.get(position).getTaburl(), wntjList.get(position).getTabname());
        }
        //文章菜单
        if (wntjList.get(position).getType() == 2) {
            int classId = wntjList.get(position).getClassify();
            NavObj obj = new NavObj();
            obj.setClassId(classId + "");
            obj.setClassName(tabName);
            Intent intent = new Intent(context, NewFragmentActivity.class);
            intent.putExtra("name", "文章");
            intent.putExtra("nav", obj);
            context.startActivity(intent);
        }
        //主菜单
        if (wntjList.get(position).getType() == 3) {
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
            if (5 == tabId) {
                JobActivity.startActivity(context);
            }
            if (4 == tabId) {
                BuildingActivity.startActivity(context);
            }
            if (7 == tabId) {
                Intent intent = new Intent(context, PonitShopActivity.class);
                intent.putExtra("score", SPUtils.get(context, "score", "") + "");
                context.startActivity(intent);
            }
            if (22 == tabId) {
                Intent intent = new Intent(context, NewFragmentActivity.class);
                NavObj obj = new NavObj();
                obj.setClassId("1");
                obj.setClassName(tabName);
                intent.putExtra("name", "科普");
                intent.putExtra("nav", obj);
                context.startActivity(intent);
            }
            if (24 == tabId) {
                Intent intent = new Intent(context, NewFragmentActivity.class);
                intent.putExtra("name", "服务");
                context.startActivity(intent);
            }
        }

    }
}
